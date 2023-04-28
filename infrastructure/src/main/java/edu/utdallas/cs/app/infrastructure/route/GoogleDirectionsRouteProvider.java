package edu.utdallas.cs.app.infrastructure.route;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.infrastructure.route.mapper.GoogleDirectionsMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Qualifier("fastest")
public class GoogleDirectionsRouteProvider implements RouteProvider {
    private final GoogleDirectionsMapper googleDirectionsMapper;
    private final GeoApiContext context;

    public GoogleDirectionsRouteProvider(@Value("${GOOGLE_API_KEY}") String apiKey,
                                         GoogleDirectionsMapper googleDirectionsMapper) {
        this.googleDirectionsMapper = googleDirectionsMapper;
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    @Override
    public Route getRoute(List<GeoLocation> waypoints, RoutingPreferences preferences, boolean pedestrian) {
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(googleDirectionsMapper.mapToLatLng(waypoints.get(0)))
                .destination(googleDirectionsMapper.mapToLatLng(waypoints.get(waypoints.size() - 1)))
                .mode(TravelMode.DRIVING);
        if (preferences.isAvoidHighways()) {
            request = request.avoid(DirectionsApi.RouteRestriction.HIGHWAYS);
        }
        if (preferences.isAvoidTolls()) {
            request = request.avoid(DirectionsApi.RouteRestriction.TOLLS);
        }
        if (pedestrian) {
            request = request.mode(TravelMode.WALKING);
        }
        if (waypoints.size() > 2) {
            request = request.waypoints(googleDirectionsMapper.mapToLatLngs(waypoints.subList(1, waypoints.size() - 1)));
        }
        try {
            DirectionsResult result = request.await();
            return googleDirectionsMapper.mapToRoute(result.routes[0]);
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
