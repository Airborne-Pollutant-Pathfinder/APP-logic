package edu.utdallas.cs.app.provider.route.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.TravelMode;
import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Parameters;
import com.graphhopper.util.shapes.GHPoint;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.mapper.GoogleDirectionsMapper;
import edu.utdallas.cs.app.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.provider.graphhopper.impl.AvoidSensorsGraphHopperProvider;
import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import edu.utdallas.cs.app.provider.osm.impl.StaticOSMFileProvider;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import edu.utdallas.cs.app.util.BoundingBoxUtil;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class GoogleDirectionsRouteProvider implements RouteProvider {
    /**
     * A constant for increasing the range of a bounding box to be able to provide better possible routes.
     */
    private static final double BOUNDING_BOX_MULTIPLIER = 1.5;
    /**
     * A constant for managing the Ramer–Douglas–Peucker algorithm used to simplify the polyline returned by Google
     */
    private static final double POLYLINE_SIMPLIFICATION_TOLERANCE = 0.01;

    private final GoogleDirectionsMapper googleDirectionsMapper;
    private final GraphHopperMapper graphHopperMapper;
    private final GeoApiContext context;

    public GoogleDirectionsRouteProvider(@Value("${GOOGLE_API_KEY}") String apiKey,
                                         GoogleDirectionsMapper googleDirectionsMapper,
                                         GraphHopperMapper graphHopperMapper) {
        this.googleDirectionsMapper = googleDirectionsMapper;
        this.graphHopperMapper = graphHopperMapper;
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    @Override
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination, List<Sensor> sensorsToAvoid) {
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(googleDirectionsMapper.mapToLatLng(origin))
                .destination(googleDirectionsMapper.mapToLatLng(destination))
                .mode(TravelMode.DRIVING);
        try {
            DirectionsResult result = request.await();
            List<Route> routes = new ArrayList<>();

            routes.add(createSafestRoute(result.routes[0], sensorsToAvoid));
            routes.add(googleDirectionsMapper.mapToRoute(result.routes[0]));

            return routes;
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Route createSafestRoute(DirectionsRoute fastestRoute, List<Sensor> sensorsToAvoid) {
        LineString simplifiedPolyline = createSimplifiedPolyline(fastestRoute);

        double[] boundingBox = createFastestRouteBoundingBox(simplifiedPolyline);
        // OSMFileProvider osmFileProvider = new BoundingBoxOSMFileProvider(boundingBox[0], boundingBox[1], boundingBox[2], boundingBox[3]);
        OSMFileProvider osmFileProvider = new StaticOSMFileProvider(); // todo optimize above osm file provider

        GraphHopperProvider graphHopperProvider = new AvoidSensorsGraphHopperProvider(sensorsToAvoid);
        try {
            GraphHopper hopper = graphHopperProvider.createGraphHopper(osmFileProvider.createOSMFile());

            GHRequest request = new GHRequest()
                    .setProfile("car")
                    .setAlgorithm(Parameters.Algorithms.ASTAR_BI)
                    .putHint(Parameters.CH.DISABLE, true);
            for (Coordinate coordinate : simplifiedPolyline.getCoordinates()) {
                request.addPoint(new GHPoint(getLatitude(coordinate), getLongitude(coordinate)));
            }

            GHResponse response = hopper.route(request);

            if (response.hasErrors()) {
                System.out.println("Error occurred: " + response.getErrors());
                // todo handle error
                return null;
            }

            ResponsePath bestPath = response.getBest();
            double lengthInMeters = bestPath.getDistance();
            Duration travelTimeInSeconds = Duration.ofSeconds(bestPath.getTime() / 1000);
            List<GeoLocation> waypoints = graphHopperMapper.mapToGeoLocations(bestPath.getPoints());

            hopper.close();

            return new Route(lengthInMeters, travelTimeInSeconds, waypoints);
        } catch (IOException | URISyntaxException e) {
            // todo handle exception
            return null;
        }
    }

    private LineString createSimplifiedPolyline(DirectionsRoute fastestRoute) {
        Coordinate[] coordinates = fastestRoute.overviewPolyline.decodePath().stream()
                .map(latLng -> new Coordinate(latLng.lng, latLng.lat))
                .toArray(Coordinate[]::new);

        GeometryFactory geometryFactory = new GeometryFactory();
        LineString polyline = geometryFactory.createLineString(coordinates);
        return (LineString) DouglasPeuckerSimplifier.simplify(polyline, POLYLINE_SIMPLIFICATION_TOLERANCE);
    }

    private double[] createFastestRouteBoundingBox(LineString polyline) {
        double minLat = Arrays.stream(polyline.getCoordinates()).mapToDouble(this::getLatitude).min().orElse(0);
        double minLon = Arrays.stream(polyline.getCoordinates()).mapToDouble(this::getLongitude).min().orElse(0);
        double maxLat = Arrays.stream(polyline.getCoordinates()).mapToDouble(this::getLatitude).max().orElse(0);
        double maxLon = Arrays.stream(polyline.getCoordinates()).mapToDouble(this::getLongitude).max().orElse(0);

        double[] boundingBox = new double[] {minLat, minLon, maxLat, maxLon};
        return BoundingBoxUtil.increaseBoundingBoxByMultiplier(boundingBox, BOUNDING_BOX_MULTIPLIER);
    }

    private double getLatitude(Coordinate coordinate) {
        return coordinate.y;
    }

    private double getLongitude(Coordinate coordinate) {
        return coordinate.x;
    }
}
