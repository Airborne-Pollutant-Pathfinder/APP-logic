package edu.utdallas.cs.app.provider.waypoint.impl;

import edu.utdallas.cs.app.data.BoundingBox;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import edu.utdallas.cs.app.provider.waypoint.WaypointValidator;
import edu.utdallas.cs.app.util.BoundingBoxUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Qualifier("sensorWaypointReducer")
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
        return waypoints.stream().filter(this::isValidWaypoint).toList();
    }

    @Override
    public boolean isValidWaypoint(GeoLocation waypoint) {
        BoundingBox boxWithBuffer = BoundingBoxUtil.generateBoundingBox(waypoint.getLatitude(), waypoint.getLongitude(), BUFFER_METERS);
        List<Sensor> sensorsToAvoid = sensorProvider.findRelevantSensors(waypoint.getLatitude(), waypoint.getLongitude());
        // todo analyze each sensor and see if the data it is saying is hazardous to specific user
        return !BoundingBoxUtil.boxIntersectsAnySensor(sensorsToAvoid, boxWithBuffer);
    }
}
