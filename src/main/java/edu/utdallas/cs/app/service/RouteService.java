package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.Route;
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
        return routeProvider.getRoutes(origin, destination);
    }
}