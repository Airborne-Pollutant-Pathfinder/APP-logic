package edu.utdallas.cs.app.provider.route.impl;

import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.TravelMode;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.mapper.GoogleDirectionsMapper;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
@Qualifier("thirdParty")
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
    public Route getRoute(List<GeoLocation> waypoints) {
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(googleDirectionsMapper.mapToLatLng(waypoints.get(0)))
                .destination(googleDirectionsMapper.mapToLatLng(waypoints.get(waypoints.size() - 1)))
                .mode(TravelMode.DRIVING);
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
