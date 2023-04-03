package edu.utdallas.cs.app.provider.graphhopper.impl;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.graphhopper.SensorAvoidingGraphHopper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;

import java.io.File;
import java.util.List;

/**
 * Provides a custom GraphHopper instance that avoids sensors.
 */
public class SensorAvoidingGraphHopperProvider implements GraphHopperProvider {
    private final List<Sensor> sensorsToAvoid;

    public SensorAvoidingGraphHopperProvider(List<Sensor> sensorsToAvoid) {
        this.sensorsToAvoid = sensorsToAvoid;
    }
    
    @Override
    public GraphHopper createGraphHopper(File osmFile) {
        GraphHopper graphHopper = new SensorAvoidingGraphHopper(sensorsToAvoid);
        graphHopper.setOSMFile(osmFile.getAbsolutePath());
        graphHopper.setGraphHopperLocation("graph_folder");
        graphHopper.setProfiles(new Profile("car").setVehicle("car").setTurnCosts(false));
        graphHopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
        graphHopper.importOrLoad();
        return graphHopper;
    }
}
