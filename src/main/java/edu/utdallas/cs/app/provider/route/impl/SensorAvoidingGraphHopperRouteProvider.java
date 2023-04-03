package edu.utdallas.cs.app.provider.route.impl;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Parameters;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.provider.graphhopper.impl.SensorAvoidingGraphHopperProvider;
import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import edu.utdallas.cs.app.provider.route.SensorAvoidingRouteProvider;
import edu.utdallas.cs.app.util.BoundingBoxUtil;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

@Service
public class SensorAvoidingGraphHopperRouteProvider implements SensorAvoidingRouteProvider {
    /**
     * A constant for increasing the range of a bounding box to be able to provide better possible routes.
     */
    private static final double BOUNDING_BOX_MULTIPLIER = 1.5;

    private final OSMFileProvider osmFileProvider;
    private final GraphHopperMapper mapper;

    public SensorAvoidingGraphHopperRouteProvider(OSMFileProvider osmFileProvider, GraphHopperMapper mapper) {
        this.osmFileProvider = osmFileProvider;
        this.mapper = mapper;
    }

    @Override
    public Route getRoute(List<GeoLocation> waypoints, List<Sensor> sensorsToAvoid) {
        double[] boundingBox = createFastestRouteBoundingBox(waypoints);
        GraphHopperProvider graphHopperProvider = new SensorAvoidingGraphHopperProvider(sensorsToAvoid);
        try {
            GraphHopper hopper = graphHopperProvider.createGraphHopper(osmFileProvider.createOSMFile(boundingBox));

            GHRequest request = new GHRequest()
                    .setProfile("car")
                    .setAlgorithm(Parameters.Algorithms.ASTAR_BI)
                    .putHint(Parameters.CH.DISABLE, true);
            for (GeoLocation waypoint : waypoints) {
                if (BoundingBoxUtil.isPointInSensors(sensorsToAvoid, waypoint.getLatitude(), waypoint.getLongitude())) {
                    continue;
                }
                request.addPoint(mapper.mapToGHPoint(waypoint));
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
            List<GeoLocation> resultWaypoints = mapper.mapToGeoLocations(bestPath.getPoints());

            hopper.close();

            return new Route(lengthInMeters, travelTimeInSeconds, resultWaypoints);
        } catch (IOException | URISyntaxException e) {
            // todo handle exception
            return null;
        }
    }

    private double[] createFastestRouteBoundingBox(List<GeoLocation> waypoints) {
        double minLat = waypoints.stream().mapToDouble(GeoLocation::getLatitude).min().orElse(0);
        double minLon = waypoints.stream().mapToDouble(GeoLocation::getLongitude).min().orElse(0);
        double maxLat = waypoints.stream().mapToDouble(GeoLocation::getLatitude).max().orElse(0);
        double maxLon = waypoints.stream().mapToDouble(GeoLocation::getLongitude).max().orElse(0);

        double[] boundingBox = new double[] {minLat, minLon, maxLat, maxLon};
        return BoundingBoxUtil.increaseBoundingBoxByMultiplier(boundingBox, BOUNDING_BOX_MULTIPLIER);
    }
}
