package edu.utdallas.cs.app.data;

import java.util.List;

public record Route(long lengthInMeters, java.time.Duration travelTimeInSeconds, List<GeoLocation> waypoints) {
}
