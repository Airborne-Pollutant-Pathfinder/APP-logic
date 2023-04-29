package edu.utdallas.cs.app.domain.database.table;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pollutant")
@Data
public class PollutantTable {
    public static final String CO = "CO";
    public static final String NO2 = "NO2";
    public static final String O3 = "O3";
    public static final String PM2_5 = "PM2_5";
    public static final String PM10 = "PM10";
    public static final String SO2 = "SO2";

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
