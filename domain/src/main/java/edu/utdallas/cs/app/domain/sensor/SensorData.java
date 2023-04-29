package edu.utdallas.cs.app.domain.sensor;

import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SensorData {

    public static SensorData at(String sensorID, List<CapturedPollutant> data) {
        return new SensorData(sensorID,data);
    }

    private String sensorId;
    private List<CapturedPollutant> data;
}
