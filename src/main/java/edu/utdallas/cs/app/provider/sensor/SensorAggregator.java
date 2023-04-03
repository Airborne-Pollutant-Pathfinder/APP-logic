package edu.utdallas.cs.app.provider.sensor;

import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;

import java.util.List;

/**
 * Aggregates relevant sensors from a given route.
 */
public interface SensorAggregator {
    List<Sensor> findRelevantSensors(Route route);
}