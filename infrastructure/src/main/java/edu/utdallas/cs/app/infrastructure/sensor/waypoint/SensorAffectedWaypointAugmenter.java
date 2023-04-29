package edu.utdallas.cs.app.infrastructure.sensor.waypoint;

import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.database.table.PollutantTable;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.util.BoundingBoxUtil;
import edu.utdallas.cs.app.infrastructure.captured_pollutant.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointAugmenter;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@Qualifier("sensorAvoidingReducer")
public class SensorAffectedWaypointAugmenter implements WaypointAugmenter, WaypointValidator {
    /**
     * The amount of "buffer area" between a point and a sensor to be marked as a relevant sensor even if not
     * necessarily in the sensor's radius.
     */
    public static final int BUFFER_METERS = 500;

    private final SensorProvider sensorProvider;
    private final CapturedPollutantProvider capturedPollutantProvider;

    public SensorAffectedWaypointAugmenter(SensorProvider sensorProvider, CapturedPollutantProvider capturedPollutantProvider) {
        this.sensorProvider = sensorProvider;
        this.capturedPollutantProvider = capturedPollutantProvider;
    }

    @Override
    public List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints, RoutingPreferences preferences) {
        return waypoints.stream().filter(waypoint -> isValidWaypoint(waypoint, preferences)).collect(Collectors.toList());
    }

    @Override
    public boolean isValidWaypoint(GeoLocation waypoint, RoutingPreferences preferences) {
        BoundingBox boxWithBuffer = BoundingBoxUtil.generateBoundingBox(waypoint.getLatitude(), waypoint.getLongitude(), BUFFER_METERS);
        List<Sensor> sensorsToAvoid = sensorProvider.findRelevantSensors(boxWithBuffer);
        // todo analyze each sensor and see if the data it is saying is hazardous to specific user
        for (Sensor sensor : sensorsToAvoid) {
            List<CapturedPollutant> capturedPollutants = capturedPollutantProvider.findLatestDataFor(sensor);

            // todo is there a better way to do this?
            boolean coLevelNotChecked = true;
            boolean no2LevelNotChecked = true;
            boolean o3LevelNotChecked = true;
            boolean pm2_5LevelNotChecked = true;
            boolean pm10LevelNotChecked = true;
            boolean so2LevelNotChecked = true;

            for (int i = capturedPollutants.size() - 1; i >= 0; i--) {
                CapturedPollutant capturedPollutant = capturedPollutants.get(i);

                if (coLevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.CO)) {
                    if (capturedPollutant.getValue() >= preferences.getCOThreshold()) {
                        return false;
                    }
                    coLevelNotChecked = false;
                }

                if (no2LevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.NO2)) {
                    if (capturedPollutant.getValue() >= preferences.getNO2Threshold()) {
                        return false;
                    }
                    no2LevelNotChecked = false;
                }

                if (o3LevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.O3)) {
                    if (capturedPollutant.getValue() >= preferences.getO3Threshold()) {
                        return false;
                    }
                    o3LevelNotChecked = false;
                }

                if (pm2_5LevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.PM2_5)) {
                    if (capturedPollutant.getValue() >= preferences.getPM2_5Threshold()) {
                        return false;
                    }
                    pm2_5LevelNotChecked = false;
                }

                if (pm10LevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.PM10)) {
                    if (capturedPollutant.getValue() >= preferences.getPM10Threshold()) {
                        return false;
                    }
                    pm10LevelNotChecked = false;
                }

                if (so2LevelNotChecked && capturedPollutant.getPollutant().equals(PollutantTable.SO2)) {
                    if (capturedPollutant.getValue() >= preferences.getSO2Threshold()) {
                        return false;
                    }
                    so2LevelNotChecked = false;
                }

                if (!coLevelNotChecked && !no2LevelNotChecked && !o3LevelNotChecked && !pm2_5LevelNotChecked && !pm10LevelNotChecked && !so2LevelNotChecked) {
                    break;
                }
            }
        }
        return true;
    }
}
