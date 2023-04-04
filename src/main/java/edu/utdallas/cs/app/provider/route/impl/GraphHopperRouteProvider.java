package edu.utdallas.cs.app.provider.route.impl;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Parameters;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

@Component
@Qualifier("selfHosted")
public class GraphHopperRouteProvider implements RouteProvider {
    private final GraphHopper hopper;
    private final GraphHopperMapper mapper;

    public GraphHopperRouteProvider(GraphHopperProvider graphHopperProvider,
                                    OSMFileProvider osmFileProvider,
                                    GraphHopperMapper mapper) {
        try {
            hopper = graphHopperProvider.createGraphHopper(osmFileProvider.getOSMFile());
        } catch (IOException | URISyntaxException e) {
            // todo handle exception
            throw new RuntimeException(e);
        }
        this.mapper = mapper;
    }

    @Override
    public Route getRoute(List<GeoLocation> waypoints) {
        GHRequest request = new GHRequest()
                .setProfile("car")
                .setAlgorithm(Parameters.Algorithms.ASTAR_BI)
                .setPoints(mapper.mapToGHPoints(waypoints))
                .putHint(Parameters.CH.DISABLE, true);

        GHResponse response = hopper.route(request);

        if (response.hasErrors()) {
            System.out.println("Error occurred: " + response.getErrors());
            // todo handle error
            return null;
        }

        ResponsePath bestPath = response.getBest();
        double lengthInMeters = bestPath.getDistance();
        Duration travelTimeInSeconds = Duration.ofSeconds(bestPath.getTime() / 1000);
        List<GeoLocation> resultWaypoints = mapper.mapToGeoLocations(bestPath.getPoints());

        hopper.close();

        return new Route(lengthInMeters, travelTimeInSeconds, resultWaypoints);
    }
}
