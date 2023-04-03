package edu.utdallas.cs.app.provider.route;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;

import java.util.List;

public interface SensorAvoidingRouteProvider {
    Route getRoute(List<GeoLocation> waypoints, List<Sensor> sensorsToAvoid);
}
