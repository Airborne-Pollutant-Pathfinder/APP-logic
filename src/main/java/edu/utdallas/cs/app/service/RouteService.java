package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import edu.utdallas.cs.app.provider.route.SensorAvoidingRouteProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    private final RouteProvider mainRouteProvider;
    private final SensorAvoidingRouteProvider sensorAvoidingRouteProvider;
    private final WaypointAugmenter waypointAugmenter;

    public RouteService(RouteProvider mainRouteProvider, SensorAvoidingRouteProvider sensorAvoidingRouteProvider,
                        WaypointAugmenter waypointAugmenter) {
        this.mainRouteProvider = mainRouteProvider;
        this.sensorAvoidingRouteProvider = sensorAvoidingRouteProvider;
        this.waypointAugmenter = waypointAugmenter;
    }

    /**
     * @param origin where the route starts
     * @param destination where the route ends
     * @return two routes: the first one being the route that avoids sensors, and the second one being the fastest route
     */
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination) {
        List<Route> routes = new ArrayList<>();
        Route fastestRoute = mainRouteProvider.getRoute(List.of(origin, destination));
        List<GeoLocation> simplifiedWaypoints = waypointAugmenter.augmentWaypoints(fastestRoute.getWaypoints());
        routes.add(sensorAvoidingRouteProvider.getRoute(simplifiedWaypoints, createMockSensorsToAvoid()));
        routes.add(fastestRoute);
        return routes;
    }

    private List<Sensor> createMockSensorsToAvoid() {
        return List.of(
                new Sensor(new GeoLocation(-96.768111, 33.133839), 100)
        );
    }
}