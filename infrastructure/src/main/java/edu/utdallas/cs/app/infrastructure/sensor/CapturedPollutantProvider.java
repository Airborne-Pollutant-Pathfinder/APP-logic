package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.List;
import java.util.Map;

// Aggregrating sensor data from given sensors
public interface CapturedPollutantProvider {
//    List<List<CapturedPollutant>> findLatestDataFor(List<Sensor> sensorsInRange);
    List<CapturedPollutant> findLatestDataFor(Sensor thisSensor);
}
