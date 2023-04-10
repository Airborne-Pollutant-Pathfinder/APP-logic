package edu.utdallas.cs.app.route.infrastructure.waypoint;

import edu.utdallas.cs.app.route.domain.GeoLocation;

public interface WaypointValidator {
    boolean isValidWaypoint(GeoLocation waypoint);
}
