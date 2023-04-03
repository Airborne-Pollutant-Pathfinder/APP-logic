package edu.utdallas.cs.app.provider.route;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;

import java.util.List;

public interface RouteProvider {
    Route getRoute(List<GeoLocation> waypoints);
}
