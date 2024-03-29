package edu.utdallas.cs.app.domain.captured_pollutant;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class CapturedPollutant {
    private int sensorId;
    private int pollutantId;
    private double value;
}
