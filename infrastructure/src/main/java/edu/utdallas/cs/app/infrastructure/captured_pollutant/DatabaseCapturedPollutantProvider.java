package edu.utdallas.cs.app.infrastructure.captured_pollutant;

import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.database.repository.CapturedPollutantRepository;
import edu.utdallas.cs.app.domain.database.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.database.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

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
    public List<CapturedPollutant> findLatestDataFor(Sensor thisSensor) {
        Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(thisSensor.getLocation().getLongitude(), thisSensor.getLocation().getLatitude()));
        SensorTable sensorTable = sensorsRepository.findSensorTableByLocationAndRadiusMeters(point, thisSensor.getRadiusInMeters());
        Collection<CapturedPollutantTable> capturedPollutants = capturedPollutantRepository.findTop100BySensorOrderByDatetimeDesc(sensorTable);

        return capturedPollutants.stream()
                .collect(Collectors.toMap(
                        s -> s.getPollutant().getId(),
                        Function.identity(),
                        (s1, s2) -> s1.getDatetime().after(s2.getDatetime()) ? s1 : s2
                ))
                .values()
                .stream()
                .map(s -> CapturedPollutant.builder()
                        .withSensorId(s.getSensor().getId())
                        .withPollutantId(s.getPollutant().getId())
                        .withValue(s.getValue())
                        .build())
                .toList();
    }
}
