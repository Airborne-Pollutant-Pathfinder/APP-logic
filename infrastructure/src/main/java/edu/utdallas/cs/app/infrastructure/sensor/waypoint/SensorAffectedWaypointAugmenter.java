package edu.utdallas.cs.app.infrastructure.sensor.waypoint;

import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.util.BoundingBoxUtil;
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

    public SensorAffectedWaypointAugmenter(SensorProvider sensorProvider) {
        this.sensorProvider = sensorProvider;
    }

    @Override
    public List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints) {
        return waypoints.stream().filter(this::isValidWaypoint).collect(Collectors.toList());
    }

    @Override
    public boolean isValidWaypoint(GeoLocation waypoint) {
        BoundingBox boxWithBuffer = BoundingBoxUtil.generateBoundingBox(waypoint.getLatitude(), waypoint.getLongitude(), BUFFER_METERS);
        List<Sensor> sensorsToAvoid = sensorProvider.findRelevantSensors(waypoint);
        // todo analyze each sensor and see if the data it is saying is hazardous to specific user
        return !BoundingBoxUtil.boxIntersectsAnySensor(sensorsToAvoid, boxWithBuffer);
    }
}
