package edu.utdallas.cs.app.provider.graphhopper;

import com.graphhopper.GraphHopper;

import java.io.File;

public interface GraphHopperProvider {

    GraphHopper createGraphHopper(File osmFile);

}
