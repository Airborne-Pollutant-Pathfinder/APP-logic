package edu.utdallas.cs.app.infrastructure.route.graphhopper;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.routing.weighting.custom.CustomProfile;
import com.graphhopper.util.CustomModel;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.graphhopper.json.Statement.If;
import static com.graphhopper.json.Statement.Op.MULTIPLY;

/**
 * Provides a custom GraphHopper instance that avoids sensors.
 */
@Component
public class SensorAvoidingGraphHopperProvider implements GraphHopperProvider {
    private static final String GRAPH_HOPPER_LOCATION = "graph_folder";
    private final WaypointValidator waypointReducer;

    public SensorAvoidingGraphHopperProvider(WaypointValidator waypointReducer) {
        this.waypointReducer = waypointReducer;
    }

    @Override
    public GraphHopper createGraphHopper(File osmFile) {
        GraphHopper graphHopper = new SensorAvoidingGraphHopper(waypointReducer);
        List<Profile> profiles = createProfiles();

        graphHopper.setOSMFile(osmFile.getAbsolutePath());
        graphHopper.setGraphHopperLocation(GRAPH_HOPPER_LOCATION);
        graphHopper.setProfiles(profiles);

        graphHopper.importOrLoad();
        return graphHopper;
    }

    private List<Profile> createProfiles() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(createBasicCarProfile());
        profiles.add(createCarWithoutTollsProfile());
        profiles.add(createCarWithoutHighwaysProfile());
        profiles.add(createCarWithoutTollsAndHighwaysProfile());
        return profiles;
    }

    private Profile createBasicCarProfile() {
        return new Profile("car").setVehicle("car");
    }

    private Profile createCarWithoutTollsProfile() {
        return new CustomProfile("car_no_toll")
                .setCustomModel(withTollAvoidance(new CustomModel()))
                .setVehicle("car");
    }

    private Profile createCarWithoutHighwaysProfile() {
        return new CustomProfile("car_no_highways")
                .setCustomModel(withHighwayAvoidance(new CustomModel()))
                .setVehicle("car");
    }

    private Profile createCarWithoutTollsAndHighwaysProfile() {
        return new CustomProfile("car_no_toll_no_highways")
                .setCustomModel(withTollAvoidance(withHighwayAvoidance(new CustomModel())))
                .setVehicle("car");
    }

    private CustomModel withTollAvoidance(CustomModel model) {
        return model.addToPriority(If("toll != NO", MULTIPLY, "0"))
                .addToPriority(If("road_environment == FERRY", MULTIPLY, "0"));
    }

    private CustomModel withHighwayAvoidance(CustomModel model) {
        return model.addToPriority(If("road_class == MOTORWAY", MULTIPLY, "0"));
    }
}
