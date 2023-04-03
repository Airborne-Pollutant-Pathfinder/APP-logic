package edu.utdallas.cs.app.provider.waypoint;

import edu.utdallas.cs.app.data.GeoLocation;

import java.util.List;

public interface WaypointAugmenter {
    List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints);
}
