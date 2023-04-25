package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.table.SensorTable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
public class DatabaseSensorProvider implements SensorProvider {
    private final SensorsRepository repository;

    public DatabaseSensorProvider(SensorsRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<Sensor> findRelevantSensors(GeoLocation location) {
        Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(location.getLongitude(), location.getLatitude()));
        Collection<SensorTable> sensors = repository.findAllByAreaContainsPoint(point);
        return sensors.stream().map(s -> Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(s.getLocation().getY())
                        .longitude(s.getLocation().getX())
                        .build())
                .radiusInMeters(s.getRadiusMeters())
                .build()).toList();
    }
}
