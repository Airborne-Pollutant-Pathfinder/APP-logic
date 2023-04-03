package edu.utdallas.cs.app.controller;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.service.RouteService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/route")
public class RouteController {
    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<Route>> getRoute(@RequestParam("originLatitude") double originLatitude,
                                                @RequestParam("originLongitude") double originLongitude,
                                                @RequestParam("destinationLatitude") double destinationLatitude,
                                                @RequestParam("destinationLongitude") double destinationLongitude) {
        GeoLocation origin = new GeoLocation(originLatitude, originLongitude);
        GeoLocation destination = new GeoLocation(destinationLatitude, destinationLongitude);
        List<Route> route = routeService.getRoutes(origin, destination);
        return ResponseEntity.ok(route);
    }
}
