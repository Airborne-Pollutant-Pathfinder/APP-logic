package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import edu.utdallas.cs.app.provider.route.SensorAvoidingRouteProvider;
import edu.utdallas.cs.app.provider.sensor.SensorAggregator;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    private final RouteProvider mainRouteProvider;
    private final SensorAvoidingRouteProvider sensorAvoidingRouteProvider;
    private final WaypointAugmenter waypointAugmenter;
    private final SensorAggregator sensorAggregator;

    public RouteService(RouteProvider mainRouteProvider, SensorAvoidingRouteProvider sensorAvoidingRouteProvider,
                        WaypointAugmenter waypointAugmenter, SensorAggregator sensorAggregator) {
        this.mainRouteProvider = mainRouteProvider;
        this.sensorAvoidingRouteProvider = sensorAvoidingRouteProvider;
        this.waypointAugmenter = waypointAugmenter;
        this.sensorAggregator = sensorAggregator;
    }

    /**
     * @param origin where the route starts
     * @param destination where the route ends
     * @return two routes: the first one being the route that avoids sensors, and the second one being the fastest route
     */
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination) {
        List<Route> routes = new ArrayList<>();
        // First, let's get the fastest route
        Route fastestRoute = mainRouteProvider.getRoute(List.of(origin, destination));
        // Then, let's take the individual points of the route and get a reduced version of it to be able to perform
        // recalculations between those points without the risk of going backwards in the route
        List<GeoLocation> simplifiedWaypoints = waypointAugmenter.augmentWaypoints(fastestRoute.getWaypoints());
        // Then, let's get the route that avoids sensors, giving the reduced version of the route as input to maintain
        // the overall shape of the fastest route
        routes.add(sensorAvoidingRouteProvider.getRoute(simplifiedWaypoints, sensorAggregator.findRelevantSensors(fastestRoute)));
        routes.add(fastestRoute);
        return routes;
    }
}