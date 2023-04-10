package edu.utdallas.cs.app.infrastructure.route;

import com.azure.core.credential.AzureKeyCredential;
import com.azure.core.http.policy.HttpLogDetailLevel;
import com.azure.core.http.policy.HttpLogOptions;
import com.azure.maps.route.MapsRouteAsyncClient;
import com.azure.maps.route.MapsRouteClientBuilder;
import com.azure.maps.route.models.RouteDirections;
import com.azure.maps.route.models.RouteDirectionsOptions;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.infrastructure.route.mapper.AzureMapsMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Optional;

@Deprecated(since = "Dr. Wenkstern requires us to use Google Directions API now.")
@Qualifier("fastest")
public class AzureRouteProvider implements RouteProvider {
    private final MapsRouteAsyncClient client;
    private final AzureMapsMapper mapper;

    public AzureRouteProvider(@Value("${AZURE_MAPS_SHARED_KEY_ID}") String mapsSharedKeyId,
                              @Value("${AZURE_MAPS_CLIENT_ID}") String mapsClientId,
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
    public Route getRoute(List<GeoLocation> waypoints) {
        RouteDirectionsOptions routeOptions = new RouteDirectionsOptions(mapper.mapToGeoPositions(waypoints));
        Optional<RouteDirections> routeDirectionsOpt = client.getRouteDirections(routeOptions).blockOptional();

        if (routeDirectionsOpt.isEmpty()) {
            return null;
        }

        RouteDirections routeDirections = routeDirectionsOpt.get();
        return mapper.mapToRoute(routeDirections.getRoutes().get(0));
    }
}
