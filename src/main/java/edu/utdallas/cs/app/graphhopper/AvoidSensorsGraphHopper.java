package edu.utdallas.cs.app.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.WeightingFactory;
import edu.utdallas.cs.app.data.sensor.Sensor;

import java.util.List;

public class AvoidSensorsGraphHopper extends GraphHopper {
    private final List<Sensor> sensorsToAvoid;

    public AvoidSensorsGraphHopper(List<Sensor> sensorsToAvoid) {
        this.sensorsToAvoid = sensorsToAvoid;
    }

    @Override
    protected WeightingFactory createWeightingFactory() {
        return new AvoidSensorsWeightingFactory(getBaseGraph(), getEncodingManager(), sensorsToAvoid);
    }
}
