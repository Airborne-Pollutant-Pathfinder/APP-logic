package edu.utdallas.cs.app.provider.graphhopper.impl;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.graphhopper.SensorAvoidingGraphHopper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

/**
 * Provides a custom GraphHopper instance that avoids sensors.
 */
@Component
public class SensorAvoidingGraphHopperProvider implements GraphHopperProvider {
    private final WaypointAugmenter waypointReducer;

    public SensorAvoidingGraphHopperProvider(@Qualifier("sensorWaypointReducer") WaypointAugmenter waypointReducer) {
        this.waypointReducer = waypointReducer;
    }

    @Override
    public GraphHopper createGraphHopper(File osmFile) {
        GraphHopper graphHopper = new SensorAvoidingGraphHopper(waypointReducer);
        graphHopper.setOSMFile(osmFile.getAbsolutePath());
        graphHopper.setGraphHopperLocation("graph_folder");
        graphHopper.setProfiles(new Profile("car").setVehicle("car").setTurnCosts(false));
        graphHopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
        graphHopper.importOrLoad();
        return graphHopper;
    }
}
