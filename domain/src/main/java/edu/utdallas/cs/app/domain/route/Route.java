package edu.utdallas.cs.app.domain.route;

import lombok.*;

import java.time.Duration;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public final class Route {
    private final double lengthInMeters;
    private final Duration travelTimeInSeconds;
    @Singular private final List<GeoLocation> waypoints;
}
