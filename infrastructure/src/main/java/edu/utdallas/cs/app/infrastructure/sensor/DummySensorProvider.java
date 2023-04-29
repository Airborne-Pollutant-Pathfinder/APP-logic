package edu.utdallas.cs.app.infrastructure.sensor;

import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.Collections;
import java.util.List;

@Deprecated(since = "database sensor provider is now used")
public class DummySensorProvider implements SensorProvider {
    @Override
    public List<Sensor> findRelevantSensors(BoundingBox box) {
        double latitude = 33.1338284;
        double longitude = -96.7684674;
        if (latitude >= box.getMinimumLatitude() && latitude <= box.getMaximumLatitude() && longitude >= box.getMinimumLongitude() && longitude <= box.getMaximumLongitude()) {
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
