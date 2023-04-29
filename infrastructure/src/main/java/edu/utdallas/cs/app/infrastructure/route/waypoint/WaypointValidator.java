package edu.utdallas.cs.app.infrastructure.route.waypoint;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;

public interface WaypointValidator {
    boolean isValidWaypoint(GeoLocation waypoint, RoutingPreferences preferences);
}
