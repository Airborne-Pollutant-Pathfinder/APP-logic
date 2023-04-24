package edu.utdallas.cs.app.domain.route;

import lombok.*;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
@Builder
public class RoutingPreferences {
    private boolean avoidHighways;
    private boolean avoidTolls;
    private double coThreshold;
    private double no2Threshold;
    private double o3Threshold;
    private double pm2_5Threshold;
    private double pm10Threshold;
    private double so2Threshold;
}
