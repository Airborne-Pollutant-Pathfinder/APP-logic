package edu.utdallas.cs.app.provider.sensor.impl;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class DummySensorProvider implements SensorProvider {
    @Override
    public List<Sensor> findRelevantSensors(double latitude, double longitude) {
        if (latitude == 33.1338284 && longitude == -96.7684674) {
            return Collections.singletonList(new Sensor(new GeoLocation(33.133839, -96.768111), 100));
        }
        return Collections.emptyList();
    }
}
