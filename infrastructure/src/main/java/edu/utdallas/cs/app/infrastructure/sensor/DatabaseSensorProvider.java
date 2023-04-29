package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.database.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.sensor.SquareBox;
import edu.utdallas.cs.app.infrastructure.route.mapper.JTSMapper;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
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
        SquareBox square = SquareBox.fromBoundingBox(box);
        Geometry geometry = new GeometryFactory(new PrecisionModel(), 4326).buildGeometry(List.of(
                mapper.mapToCoordinate(square.getUpperLeft()),
                mapper.mapToCoordinate(square.getUpperRight()),
                mapper.mapToCoordinate(square.getLowerRight()),
                mapper.mapToCoordinate(square.getLowerLeft()),
                mapper.mapToCoordinate(square.getUpperLeft())));
        Collection<SensorTable> sensors = repository.findAllByAreaIntersectsGeometry(geometry);
        return sensors.stream().map(s -> Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(s.getLocation().getY())
                        .longitude(s.getLocation().getX())
                        .build())
                .radiusInMeters(s.getRadiusMeters())
                .build()).toList();
    }
}
