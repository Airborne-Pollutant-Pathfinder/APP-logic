package edu.utdallas.cs.app.domain.database.table;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pollutant")
@Data
public class PollutantTable {
    public static final int CO = 1;
    public static final int NO2 = 2;
    public static final int O3 = 3;
    public static final int PM2_5 = 4;
    public static final int PM10 = 5;
    public static final int SO2 = 6;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pollutant_id")
    private int id;

    @Column(name = "abbreviation")
    private String abbreviation;

    @Column(name = "units")
    private String units;

    @Column(name = "full_name")
    private String fullName;
}
