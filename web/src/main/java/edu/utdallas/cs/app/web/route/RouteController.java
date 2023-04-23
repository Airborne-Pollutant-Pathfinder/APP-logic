package edu.utdallas.cs.app.web.route;

import edu.utdallas.cs.app.application.route.RouteService;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.Route;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RouteController implements GraphQLQueryResolver {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    public List<Route> route(double originLatitude, double originLongitude,
                             double destinationLatitude, double destinationLongitude,
                             RoutingPreferences preferences) {
        GeoLocation origin = GeoLocation.at(originLatitude, originLongitude);
        GeoLocation destination = GeoLocation.at(destinationLatitude, destinationLongitude);
        return routeService.getRoutes(origin, destination, preferences);
    }
}
