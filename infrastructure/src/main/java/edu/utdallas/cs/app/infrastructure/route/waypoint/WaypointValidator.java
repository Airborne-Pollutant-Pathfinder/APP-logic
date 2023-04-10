package edu.utdallas.cs.app.infrastructure.route.waypoint;

import edu.utdallas.cs.app.domain.route.GeoLocation;

public interface WaypointValidator {
    boolean isValidWaypoint(GeoLocation waypoint);
}
