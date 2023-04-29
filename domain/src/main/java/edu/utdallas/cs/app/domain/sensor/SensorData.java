package edu.utdallas.cs.app.domain.sensor;

import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SensorData {

    public static SensorData at(Sensor sensor, List<CapturedPollutant> data) {
        return new SensorData(sensor, data);
    }

    private Sensor sensor;
    private List<CapturedPollutant> data;
}
