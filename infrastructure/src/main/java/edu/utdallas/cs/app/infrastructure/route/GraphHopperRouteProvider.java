package edu.utdallas.cs.app.infrastructure.route;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Parameters;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.infrastructure.route.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.infrastructure.route.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.infrastructure.route.osm.OSMFileProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.List;

@Component
@Qualifier("sensorAvoiding")
public class GraphHopperRouteProvider implements RouteProvider {
    private final GraphHopper hopper;
    private final GraphHopperMapper mapper;

    public GraphHopperRouteProvider(GraphHopperProvider graphHopperProvider,
                                    OSMFileProvider osmFileProvider,
                                    GraphHopperMapper mapper) {
        hopper = graphHopperProvider.createGraphHopper(osmFileProvider.getOSMFile());
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
        return Route.builder()
                .lengthInMeters(bestPath.getDistance())
                .travelTimeInSeconds(Duration.ofSeconds(bestPath.getTime() / 1000))
                .waypoints(mapper.mapToGeoLocations(bestPath.getPoints()))
                .build();
    }
}
