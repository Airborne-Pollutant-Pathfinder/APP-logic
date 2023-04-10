package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class DummySensorProvider implements SensorProvider {
    @Override
    public List<Sensor> findRelevantSensors(GeoLocation location) {
        if (location.getLatitude() == 33.1338284 && location.getLongitude() == -96.7684674) {
            return Collections.singletonList(Sensor.builder()
                    .location(GeoLocation.builder()
                            .latitude(33.133839)
                            .longitude(-96.768111)
                            .build())
                    .radiusInMeters(100)
                    .build());
        }
        return Collections.emptyList();
    }
}
