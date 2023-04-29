package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.database.repository.CapturedPollutantRepository;
import edu.utdallas.cs.app.domain.database.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.database.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import jakarta.transaction.Transactional;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

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
    public List<List<CapturedPollutant>> findLatestDataFor(List<Sensor> sensorsInRange) {

        List<List<CapturedPollutant>> sensorData = new ArrayList<List<CapturedPollutant>>();

        if (!sensorsInRange.isEmpty()) {
            System.out.println(sensorsInRange);
        }

//        for (Sensor sensor : sensorsInRange) {
//            Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(sensor.getLocation().getLongitude(), sensor.getLocation().getLatitude()));
//            SensorTable sensorTable = sensorsRepository.findSensorTableByLocationAndRadiusMeters(point, sensor.getRadiusInMeters());
//            Collection<CapturedPollutantTable> capturedPollutants = capturedPollutantRepository.findTop100BySensor(sensorTable);
//            // use data from this to aggregate values for PM2.5, PM10, etc.
//            // note there are duplicates, can choose to either average or take the most recent
//        }

        ListIterator<Sensor> iter = sensorsInRange.listIterator();  // was intending to use this for index
        while (iter.hasNext()) {
            Sensor sensor = iter.next();
            Point point = new GeometryFactory(new PrecisionModel(), 4326).createPoint(new Coordinate(sensor.getLocation().getLongitude(), sensor.getLocation().getLatitude()));
            SensorTable sensorTable = sensorsRepository.findSensorTableByLocationAndRadiusMeters(point, sensor.getRadiusInMeters());
            Collection<CapturedPollutantTable> capturedPollutants = capturedPollutantRepository.findTop100BySensor(sensorTable);

            // use data from this to aggregate values for PM2.5, PM10, etc.
            // note there are duplicates, can choose to either average or take the most recent


            List<CapturedPollutant> data = capturedPollutants.stream().map(s -> CapturedPollutant.builder()
//                    .id(s.getId())
                    //.sensor(s.getSensor())
//                    .sensorLocation(sensor.getLocation())
                    //.pollutant(s.getPollutant())
                    // .pollutantID( s.getPollutant().getId() )    // -> switch from next for int ID
                    .withPollutant( s.getPollutant().getFullName() ) // CapturedPollutantTable - PollutantTable - PollutantID
                    // .datetime(s.getDatetime())   // DB has 5-min avg values stored. Could still use for frontend to be able to know if a sensor seems to have stopped updating?
                    .withValue(s.getValue())
                    .build()).toList();

            sensorData.add(data);
        }

        return sensorData;

    }


}
