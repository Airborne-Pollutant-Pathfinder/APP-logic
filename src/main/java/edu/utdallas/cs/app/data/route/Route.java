package edu.utdallas.cs.app.data.route;

import edu.utdallas.cs.app.data.GeoLocation;

import java.time.Duration;
import java.util.List;

public record Route(long lengthInMeters, Duration travelTimeInSeconds, List<GeoLocation> waypoints) {
}
