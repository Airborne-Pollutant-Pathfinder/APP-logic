package edu.utdallas.cs.app.domain.table;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pollutant")
@Data
public class PollutantTable {
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
