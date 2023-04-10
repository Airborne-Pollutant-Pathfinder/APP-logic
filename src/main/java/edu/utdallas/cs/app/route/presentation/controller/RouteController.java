package edu.utdallas.cs.app.route.presentation.controller;

import edu.utdallas.cs.app.route.domain.GeoLocation;
import edu.utdallas.cs.app.route.domain.Route;
import edu.utdallas.cs.app.route.presentation.service.RouteService;
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
        // todo add a verifier for longitude and latitude
        GeoLocation origin = GeoLocation.at(originLatitude, originLongitude);
        GeoLocation destination = GeoLocation.at(destinationLatitude, destinationLongitude);
        List<Route> route = routeService.getRoutes(origin, destination);
        return ResponseEntity.ok(route);
    }
}
