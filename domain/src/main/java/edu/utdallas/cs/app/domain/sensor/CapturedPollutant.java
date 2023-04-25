package edu.utdallas.cs.app.domain.sensor;


import edu.utdallas.cs.app.domain.table.PollutantTable;
import edu.utdallas.cs.app.domain.table.SensorTable;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class CapturedPollutant {

    private int id;
    private SensorTable sensor;
    private PollutantTable pollutant;
    private Date datetime;
    private double value;



}
