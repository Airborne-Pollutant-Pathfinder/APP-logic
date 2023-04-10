package edu.utdallas.cs.app.route.infrastructure;

import edu.utdallas.cs.app.route.domain.GeoLocation;
import edu.utdallas.cs.app.route.domain.Route;

import java.util.List;

public interface RouteProvider {
    Route getRoute(List<GeoLocation> waypoints);
}
