package edu.utdallas.cs.app.provider.sensor.impl;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.sensor.SensorAggregator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DummySensorAggregator implements SensorAggregator {
    @Override
    public List<Sensor> findRelevantSensors(Route route) {
        return List.of(
                new Sensor(new GeoLocation(33.133839, -96.768111), 100)
        );
    }
}
