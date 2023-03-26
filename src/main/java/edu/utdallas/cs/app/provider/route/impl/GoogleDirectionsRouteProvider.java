package edu.utdallas.cs.app.provider.route.impl;

import com.azure.maps.route.MapsRouteClientBuilder;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.errors.ApiException;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.GeocodedWaypoint;
import com.google.maps.model.TravelMode;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.mapper.AzureMapsMapper;
import edu.utdallas.cs.app.mapper.GoogleDirectionsMapper;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleDirectionsRouteProvider implements RouteProvider {
    private final GoogleDirectionsMapper mapper;
    private final GeoApiContext context;

    public GoogleDirectionsRouteProvider(@Value("${GOOGLE_API_KEY}") String apiKey,
                                         GoogleDirectionsMapper mapper) {
        this.mapper = mapper;
        context = new GeoApiContext.Builder()
                .apiKey(apiKey)
                .build();
    }

    @Override
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination, List<Sensor> sensorsToAvoid) {
        DirectionsApiRequest request = DirectionsApi.newRequest(context)
                .origin(mapper.mapToLatLng(origin))
                .destination(mapper.mapToLatLng(destination))
                .mode(TravelMode.DRIVING)
                .alternatives(true);
        try {
            DirectionsResult result = request.await();

            return mapper.mapToRoutes(result.routes);
        } catch (ApiException | InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
