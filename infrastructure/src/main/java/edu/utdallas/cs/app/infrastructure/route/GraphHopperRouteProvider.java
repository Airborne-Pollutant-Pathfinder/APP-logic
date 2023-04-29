package edu.utdallas.cs.app.infrastructure.route;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.Parameters;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.infrastructure.route.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.infrastructure.route.graphhopper.SensorAvoidingWeighting;
import edu.utdallas.cs.app.infrastructure.route.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.infrastructure.route.osm.OSMFileProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        try {
            hopper = graphHopperProvider.createGraphHopper(osmFileProvider.getOSMFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        this.mapper = mapper;
    }

    @Override
    public Route getRoute(List<GeoLocation> waypoints, RoutingPreferences preferences, boolean pedestrian) {
        GHRequest request = new GHRequest()
                .setProfile(createProfileString(preferences, pedestrian))
                .setAlgorithm(Parameters.Algorithms.ASTAR_BI)
                .setPoints(mapper.mapToGHPoints(waypoints))
                .putHint(Parameters.CH.DISABLE, true)
                .putHint(SensorAvoidingWeighting.ROUTING_PREFERENCES_KEY, preferences);

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

    private String createProfileString(RoutingPreferences preferences, boolean pedestrian) {
        if (pedestrian) {
            return "pedestrian";
        }

        String profile = "car";
        if (preferences.isAvoidTolls()) {
            profile += "_no_toll";
        }
        if (preferences.isAvoidHighways()) {
            profile += "_no_highways";
        }
        return profile;
    }
}
