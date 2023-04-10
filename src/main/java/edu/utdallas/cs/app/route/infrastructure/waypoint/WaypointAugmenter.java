package edu.utdallas.cs.app.route.infrastructure.waypoint;

import edu.utdallas.cs.app.route.domain.GeoLocation;

import java.util.List;

public interface WaypointAugmenter {
    List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints);
}
