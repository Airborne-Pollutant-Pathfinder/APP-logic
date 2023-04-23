package edu.utdallas.cs.app.domain.route;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class RoutingPreferences {
    private final boolean avoidHighways;
    private final boolean avoidTolls;
    private final double coThreshold;
    private final double no2Threshold;
    private final double o3Threshold;
    private final double pm2_5Threshold;
    private final double pm10Threshold;
    private final double so2Threshold;
}
