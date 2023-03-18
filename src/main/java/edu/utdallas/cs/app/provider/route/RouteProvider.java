package edu.utdallas.cs.app.provider.route;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.Route;

import java.util.List;

public interface RouteProvider {
    List<Route> getRoutes(GeoLocation origin, GeoLocation destination);
}
