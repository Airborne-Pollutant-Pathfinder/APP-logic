package edu.utdallas.cs.app.data.route;

import edu.utdallas.cs.app.data.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Duration;
import java.util.List;

@Data
@AllArgsConstructor
public final class Route {
    private final double lengthInMeters;
    private final Duration travelTimeInSeconds;
    private final List<GeoLocation> waypoints;
}
