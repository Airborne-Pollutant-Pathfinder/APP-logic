package edu.utdallas.cs.app.infrastructure.route.graphhopper;

import com.graphhopper.GraphHopper;

import java.io.File;

public interface GraphHopperProvider {

    GraphHopper createGraphHopper(File osmFile);

}
