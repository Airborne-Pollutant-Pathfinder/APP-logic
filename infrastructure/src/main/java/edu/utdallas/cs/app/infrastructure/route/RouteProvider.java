package edu.utdallas.cs.app.infrastructure.route;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;

import java.util.List;

public interface RouteProvider {
    Route getRoute(List<GeoLocation> waypoints);
}
