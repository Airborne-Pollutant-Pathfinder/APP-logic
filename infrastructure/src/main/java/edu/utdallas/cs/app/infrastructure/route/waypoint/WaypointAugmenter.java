package edu.utdallas.cs.app.infrastructure.route.waypoint;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;

import java.util.List;

public interface WaypointAugmenter {
    List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints, RoutingPreferences preferences);
}
