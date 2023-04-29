package edu.utdallas.cs.app.domain.sensor;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class CapturedPollutant {
    private int sensorId;
    private String pollutant;
    private double value;
}
