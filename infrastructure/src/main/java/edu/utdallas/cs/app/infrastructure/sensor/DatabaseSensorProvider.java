package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.database.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.sensor.SquareBox;
import edu.utdallas.cs.app.infrastructure.route.mapper.JTSMapper;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class DatabaseSensorProvider implements SensorProvider {
    private final SensorsRepository repository;
    private final JTSMapper mapper;

    public DatabaseSensorProvider(SensorsRepository repository, JTSMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public List<Sensor> findRelevantSensors(BoundingBox box) {
        GeometryFactory factory = new GeometryFactory(new PrecisionModel(), 4326);
        Collection<SensorTable> sensors = findRelevantSensors(box, factory);
        return sensors.stream().map(s -> Sensor.builder()
                .id(s.getId())
                .location(GeoLocation.builder()
                        .latitude(s.getLocation().getY())
                        .longitude(s.getLocation().getX())
                        .build())
                .radiusInMeters(s.getRadiusMeters())
                .build()).toList();
    }

    private Collection<SensorTable> findRelevantSensors(BoundingBox box, GeometryFactory factory) {
        if (box.equals(BoundingBox.ENTIRE_WORLD)) {
            return repository.findAll();
        }
        SquareBox square = SquareBox.fromBoundingBox(box);
        Polygon polygon = factory.createPolygon(new Coordinate[]{
                mapper.mapToCoordinate(square.getLowerLeft()),
                mapper.mapToCoordinate(square.getLowerRight()),
                mapper.mapToCoordinate(square.getUpperRight()),
                mapper.mapToCoordinate(square.getUpperLeft()),
                mapper.mapToCoordinate(square.getLowerLeft()),
        });
        Collection<SensorTable> sensors = repository.findAllByAreaIntersectsPolygon(polygon);
        return sensors;
    }
}
