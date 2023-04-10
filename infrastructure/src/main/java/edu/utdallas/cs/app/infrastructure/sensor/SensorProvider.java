package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.List;

/**
 * Aggregates relevant sensors from a given route.
 */
public interface SensorProvider {
    // todo this should take in a bounding box, not a point, in order to fully account for the buffer zone
    List<Sensor> findRelevantSensors(GeoLocation location);
}
