package edu.utdallas.cs.app.infrastructure.captured_pollutant;

import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.List;

public interface CapturedPollutantProvider {
    List<CapturedPollutant> findLatestDataFor(Sensor thisSensor);
}
