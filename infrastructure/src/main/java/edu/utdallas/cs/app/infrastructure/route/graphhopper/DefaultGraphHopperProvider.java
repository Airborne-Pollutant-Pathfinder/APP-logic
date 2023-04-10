package edu.utdallas.cs.app.infrastructure.route.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.CHProfile;
import com.graphhopper.config.Profile;

import java.io.File;

public class DefaultGraphHopperProvider implements GraphHopperProvider {
    @Override
    public GraphHopper createGraphHopper(File osmFile) {
        GraphHopper graphHopper = new GraphHopper();
        graphHopper.setOSMFile(osmFile.getAbsolutePath());
        graphHopper.setGraphHopperLocation("graph_folder");
        graphHopper.setProfiles(new Profile("car").setVehicle("car").setWeighting("fastest").setTurnCosts(false));
        graphHopper.getCHPreparationHandler().setCHProfiles(new CHProfile("car"));
        graphHopper.importOrLoad();
        return graphHopper;
    }
}
