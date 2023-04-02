package edu.utdallas.cs.app.provider.route;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;

import java.util.List;

public interface RouteProvider {
    /**
     * @param origin where the route starts
     * @param destination where the route ends
     * @param sensorsToAvoid the list of relevant sensors that have already been determined to be avoided
     * @return two routes between the origin and destination. The first route is the fastest route that avoids sensors,
     *         and the second route is the fastest route that does not avoid sensors.
     */
    List<Route> getRoutes(GeoLocation origin, GeoLocation destination, List<Sensor> sensorsToAvoid);
}
