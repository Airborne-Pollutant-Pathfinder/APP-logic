package edu.utdallas.cs.app.provider.route.impl;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.core.models.GeoLinearRing;
import com.azure.core.models.GeoPolygon;
import com.azure.core.models.GeoPolygonCollection;
import com.azure.core.models.GeoPosition;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.MapsRouteClientBuilder;
import com.azure.maps.route.models.RouteDirections;
import com.azure.maps.route.models.RouteDirectionsOptions;
import com.azure.maps.route.models.RouteDirectionsParameters;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.mapper.AzureMapsMapper;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Deprecated(since = "Dr. Wenkstern requires us to use Google Directions API now.")
public class AzureRouteProvider implements RouteProvider {
    private final MapsRouteAsyncClient client;
    private final AzureMapsMapper mapper;

    public AzureRouteProvider(@Value("${MAPS_SHARED_KEY_ID}") String mapsSharedKeyId,
                              @Value("${MAPS_CLIENT_ID}") String mapsClientId,
                              AzureMapsMapper mapper) {
        this.mapper = mapper;

        MapsRouteClientBuilder builder = createMapsRouteBuilder(mapsSharedKeyId, mapsClientId);
        client = builder.buildAsyncClient();
    }

    private MapsRouteClientBuilder createMapsRouteBuilder(String mapsSharedKeyId, String mapsClientId) {
        AzureKeyCredential tokenCredential = new AzureKeyCredential(mapsSharedKeyId);

        MapsRouteClientBuilder builder = new MapsRouteClientBuilder();
        builder.credential(tokenCredential);
        builder.mapsClientId(mapsClientId);
        builder.httpLogOptions(new HttpLogOptions().setLogLevel(HttpLogDetailLevel.BODY_AND_HEADERS));
        return builder;
    }

    @Override
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination, List<Sensor> sensorsToAvoid) {
        List<GeoPosition> routePoints = Arrays.asList(
                mapper.mapToGeoPosition(origin),
                mapper.mapToGeoPosition(destination)
        );
        List<GeoPolygon> polygons = mapper.mapToGeoPolygons(sensorsToAvoid);

        RouteDirectionsOptions routeOptions = new RouteDirectionsOptions(routePoints)
                .setMaxAlternatives(2);
        RouteDirectionsParameters routeParameters = new RouteDirectionsParameters()
                .setAvoidAreas(new GeoPolygonCollection(polygons));
        Optional<RouteDirections> routeDirectionsOpt = client.getRouteDirections(routeOptions, routeParameters).blockOptional();

        if (routeDirectionsOpt.isEmpty()) {
            return null;
        }

        RouteDirections routeDirections = routeDirectionsOpt.get();
        return mapper.mapToRoutes(routeDirections.getRoutes());
    }
}
