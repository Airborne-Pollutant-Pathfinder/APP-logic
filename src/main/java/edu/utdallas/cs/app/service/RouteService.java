package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {
    private final RouteProvider routeProvider;

    public RouteService(RouteProvider routeProvider) {
        this.routeProvider = routeProvider;
    }

    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination) {
        return routeProvider.getRoutes(origin, destination, createMockSensorsToAvoid());
    }

    private List<Sensor> createMockSensorsToAvoid() {
        return List.of(
                new Sensor(new GeoLocation(-96.768111, 33.133839), 100)
        );
    }
}