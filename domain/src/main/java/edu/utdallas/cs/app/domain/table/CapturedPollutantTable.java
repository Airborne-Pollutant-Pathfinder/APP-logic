package edu.utdallas.cs.app.domain.table;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "captured_pollutant")
@Data
public class CapturedPollutantTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "captured_pollutant_id")
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sensor_id")
    private SensorTable sensor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pollutant_id")
    private PollutantTable pollutant;

    @Column(name = "datetime")
    @Temporal(TemporalType.TIMESTAMP)
    private Date datetime;

    @Column(name = "value")
    private double value;
}
