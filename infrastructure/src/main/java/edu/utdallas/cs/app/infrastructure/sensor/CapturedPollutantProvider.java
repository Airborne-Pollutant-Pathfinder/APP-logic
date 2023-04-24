package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.List;

// Aggregrating sensor data from given sensors
public interface CapturedPollutantProvider {
    List<Sensor> findLatestDataFor(List<Sensor> sensorsInRange);
}
