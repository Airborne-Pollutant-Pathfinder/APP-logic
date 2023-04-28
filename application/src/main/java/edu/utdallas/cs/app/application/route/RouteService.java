package edu.utdallas.cs.app.application.route;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.infrastructure.route.RouteProvider;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointAugmenter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RouteService {
    private final RouteProvider mainRouteProvider;
    private final RouteProvider sensorAvoidingRouteProvider;
    private final WaypointAugmenter waypointAugmenter;
    private final WaypointAugmenter sensorWaypointReducer;

    public RouteService(@Qualifier("fastest") RouteProvider mainRouteProvider,
                        @Qualifier("sensorAvoiding") RouteProvider sensorAvoidingRouteProvider,
                        @Qualifier("simplifier") WaypointAugmenter waypointAugmenter,
                        @Qualifier("sensorAvoidingReducer") WaypointAugmenter sensorWaypointReducer) {
        this.mainRouteProvider = mainRouteProvider;
        this.sensorAvoidingRouteProvider = sensorAvoidingRouteProvider;
        this.waypointAugmenter = waypointAugmenter;
        this.sensorWaypointReducer = sensorWaypointReducer;
    }

    /**
     * @param origin      where the route starts
     * @param destination where the route ends
     * @param preferences
     * @param pedestrian
     * @return two routes: the first one being the route that avoids sensors, and the second one being the fastest route
     */
    public List<Route> getRoutes(GeoLocation origin, GeoLocation destination, RoutingPreferences preferences, boolean pedestrian) {
        List<Route> routes = new ArrayList<>();
        // First, let's get the fastest route
        Route fastestRoute = mainRouteProvider.getRoute(List.of(origin, destination), preferences, pedestrian);
        // To avoid removing the origin and destination, we'll make a sublist for the intermediary waypoints between them,
        // do calculations, then add them back just before sending to the sensor avoiding route provider
        List<GeoLocation> intermediaryWaypoints = fastestRoute.getWaypoints().subList(1, fastestRoute.getWaypoints().size() - 1);

        // Then, let's take the individual points of the route and get a reduced version of it to be able to perform
        // recalculations between those points without the risk of going backwards in the route
        List<GeoLocation> simplifiedWaypoints = waypointAugmenter.augmentWaypoints(intermediaryWaypoints);
        // Then, let's remove the waypoints that are inside a sensor's radius
        List<GeoLocation> reducedWaypoints = sensorWaypointReducer.augmentWaypoints(simplifiedWaypoints);

        // Then, let's add back the origin and destination
        reducedWaypoints.add(0, origin);
        reducedWaypoints.add(destination);
        // Then, let's get the route that avoids sensors, giving the reduced version of the fastest route as input to
        // maintain the overall shape of the fastest route
        routes.add(sensorAvoidingRouteProvider.getRoute(reducedWaypoints, preferences, pedestrian));
        routes.add(fastestRoute);
        return routes;
    }
}