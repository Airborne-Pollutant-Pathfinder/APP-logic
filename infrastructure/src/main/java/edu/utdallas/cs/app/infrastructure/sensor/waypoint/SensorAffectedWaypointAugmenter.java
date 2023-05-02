package edu.utdallas.cs.app.infrastructure.sensor.waypoint;

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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

        Map<Integer, Double> thresholds = new HashMap<>();
        thresholds.put(PollutantTable.CO, preferences.getCoThreshold());
        thresholds.put(PollutantTable.NO2, preferences.getNO2Threshold());
        thresholds.put(PollutantTable.O3, preferences.getO3Threshold());
        thresholds.put(PollutantTable.PM2_5, preferences.getPM2_5Threshold());
        thresholds.put(PollutantTable.PM10, preferences.getPM10Threshold());
        thresholds.put(PollutantTable.SO2, preferences.getSO2Threshold());

        return sensorsToAvoid.stream()
                .flatMap(sensor -> capturedPollutantProvider.findLatestDataFor(sensor).stream())
                .filter(capturedPollutant -> thresholds.containsKey(capturedPollutant.getPollutantId()) && capturedPollutant.getValue() >= thresholds.get(capturedPollutant.getPollutantId()))
                .findAny()
                .isEmpty();
    }
}
