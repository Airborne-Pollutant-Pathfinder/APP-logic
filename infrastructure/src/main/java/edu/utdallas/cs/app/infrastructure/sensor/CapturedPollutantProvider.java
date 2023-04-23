package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.repository.SensorsRepository;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.table.SensorTable;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;

import java.util.Collection;
import java.util.List;

public class CapturedPollutantProvider {

    private final SensorsRepository repository;

    public CapturedPollutantProvider(SensorsRepository repository) {
        this.repository = repository;
    }

    public List<Sensor> findLatestDataFor(List<Sensor> sensorsInRange) {

        if (!sensorsInRange.isEmpty()) {
            System.out.println(sensorsInRange);
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
