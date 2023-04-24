package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.repository.CapturedPollutantRepository;
import edu.utdallas.cs.app.domain.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.table.SensorTable;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;

@Component
@Transactional
public class DatabaseCapturedPollutantProvider implements CapturedPollutantProvider {

    private final SensorsRepository sensorsRepository;
    private final CapturedPollutantRepository capturedPollutantRepository;

    public DatabaseCapturedPollutantProvider(SensorsRepository sensorsRepository, CapturedPollutantRepository capturedPollutantRepository) {
        this.sensorsRepository = sensorsRepository;
        this.capturedPollutantRepository = capturedPollutantRepository;
    }

    @Override
    public List<Sensor> findLatestDataFor(List<Sensor> sensorsInRange) {

        if (!sensorsInRange.isEmpty()) {
            System.out.println(sensorsInRange);
        }

        for (Sensor sensor : sensorsInRange) {
            Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(sensor.getLocation().getLongitude(), sensor.getLocation().getLatitude()));
            SensorTable sensorTable = sensorsRepository.findSensorTableByLocationAndRadiusMeters(point, sensor.getRadiusInMeters());
            Collection<CapturedPollutantTable> capturedPollutants = capturedPollutantRepository.findTop100BySensor(sensorTable);
            // use data from this to aggregate values for PM2.5, PM10, etc.
            // note there are duplicates, can choose to either average or take the most recent
        }

        // need to call CapturedPollutant (create query) and get data, rather than just sensors like SensorProvider




        return sensorsInRange;
//        return sensors.stream().map(s -> Sensor.builder()
//                .location(GeoLocation.builder()
//                        .latitude(s.getLocation().getY())
//                        .longitude(s.getLocation().getX())
//                        .build())
//                .radiusInMeters(s.getRadiusMeters())
//                .build()).toList();
    }


}
