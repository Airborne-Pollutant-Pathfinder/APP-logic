package edu.utdallas.cs.app.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.WeightingFactory;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import edu.utdallas.cs.app.provider.waypoint.WaypointValidator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SensorAvoidingGraphHopper extends GraphHopper {
    private final WaypointValidator waypointValidator;

    public SensorAvoidingGraphHopper(WaypointValidator waypointValidator) {
        this.waypointValidator = waypointValidator;
    }

    @Override
    protected WeightingFactory createWeightingFactory() {
        return new SensorAvoidingWeightingFactory(getBaseGraph(), getEncodingManager(), waypointValidator);
    }
}
