package edu.utdallas.cs.app.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.routing.WeightingFactory;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class SensorAvoidingGraphHopper extends GraphHopper {
    private final WaypointAugmenter waypointReducer;

    public SensorAvoidingGraphHopper(@Qualifier("sensorWaypointReducer") WaypointAugmenter waypointReducer) {
        this.waypointReducer = waypointReducer;
    }

    @Override
    protected WeightingFactory createWeightingFactory() {
        return new SensorAvoidingWeightingFactory(getBaseGraph(), getEncodingManager(), waypointReducer);
    }
}
