package edu.utdallas.cs.app.domain.route;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

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

    public boolean isAvoidHighways() {
        return avoidHighways;
    }

    public boolean isAvoidTolls() {
        return avoidTolls;
    }

    public double getCoThreshold() {
        return coThreshold;
    }

    public double getNO2Threshold() {
        return no2Threshold;
    }

    public double getO3Threshold() {
        return o3Threshold;
    }

    public double getPM2_5Threshold() {
        return pm2_5Threshold;
    }

    public double getPM10Threshold() {
        return pm10Threshold;
    }

    public double getSO2Threshold() {
        return so2Threshold;
    }
}
