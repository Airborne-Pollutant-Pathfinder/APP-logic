package edu.utdallas.cs.app.controller;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getRoute(@RequestParam("originLongitude") double originLongitude,
                                                @RequestParam("originLatitude") double originLatitude,
                                                @RequestParam("destinationLongitude") double destinationLongitude,
                                                @RequestParam("destinationLatitude") double destinationLatitude) {
        GeoLocation origin = new GeoLocation(originLongitude, originLatitude);
        GeoLocation destination = new GeoLocation(destinationLongitude, destinationLatitude);
        List<Route> route = routeService.getRoutes(origin, destination);
        return ResponseEntity.ok(route);
    }
}
