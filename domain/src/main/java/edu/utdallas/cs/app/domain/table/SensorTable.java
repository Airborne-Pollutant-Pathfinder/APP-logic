package edu.utdallas.cs.app.domain.table;

import jakarta.persistence.*;
import lombok.Data;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;

@Entity
@Table(name = "sensor")
@Data
public class SensorTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sensor_id")
    private int id;

    @Column(name = "location", columnDefinition = "Point")
    private Point location;

    @Column(name = "radius_meters")
    private double radiusMeters;

    @Column(name = "area", columnDefinition = "Geometry")
    private Geometry area;
}
