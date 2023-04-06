package edu.utdallas.cs.app.provider.waypoint;

import edu.utdallas.cs.app.data.GeoLocation;

public interface WaypointValidator {
    boolean validateWaypoint(GeoLocation waypoint);
}
