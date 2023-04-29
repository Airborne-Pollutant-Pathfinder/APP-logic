package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.List;

public interface CapturedPollutantProvider {
    List<CapturedPollutant> findLatestDataFor(Sensor thisSensor);
}
