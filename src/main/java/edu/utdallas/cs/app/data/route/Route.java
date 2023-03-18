package edu.utdallas.cs.app.data.route;

import edu.utdallas.cs.app.data.GeoLocation;

import java.util.List;

public record Route(long lengthInMeters, java.time.Duration travelTimeInSeconds, List<GeoLocation> waypoints) {
}
