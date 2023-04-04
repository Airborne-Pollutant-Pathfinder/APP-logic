package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteServiceTest {
    private RouteProvider routeProviderMock;
    private RouteProvider sensorAvoidingRouteProviderMock;
    private WaypointAugmenter waypointAugmenterMock;
    private WaypointAugmenter waypointReducerMock;

    @BeforeEach
    void setUp() {
        routeProviderMock = mock(RouteProvider.class);
        sensorAvoidingRouteProviderMock = mock(RouteProvider.class);
        waypointAugmenterMock = mock(WaypointAugmenter.class);
        waypointReducerMock = mock(WaypointAugmenter.class);
    }

    @Test
    void Should_ReturnSafestThenFastest_When_GettingRoutes() {
        RouteService routeService = new RouteService(routeProviderMock, sensorAvoidingRouteProviderMock, waypointAugmenterMock, waypointReducerMock);

        GeoLocation origin = new GeoLocation(-96.7501, 32.9858);
        GeoLocation destination = new GeoLocation(-96.8602, 32.8975);

        Route fastestRoute = new Route(1000L, Duration.ofMinutes(10), List.of(origin, destination));
        Route safestRoute = new Route(2000L, Duration.ofMinutes(20), List.of(origin, destination));
        List<Route> expectedRoutes = List.of(safestRoute, fastestRoute);

        when(routeProviderMock.getRoute(any(List.class))).thenReturn(fastestRoute);
        when(sensorAvoidingRouteProviderMock.getRoute(any(List.class))).thenReturn(safestRoute);
        when(waypointAugmenterMock.augmentWaypoints(any(List.class))).thenReturn(List.of(origin, destination));
        when(waypointReducerMock.augmentWaypoints(any(List.class))).thenReturn(List.of(origin, destination));

        List<Route> actualRoutes = routeService.getRoutes(origin, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    private List<Sensor> createMockSensors() {
        return List.of(
                new Sensor(new GeoLocation(33.133839, -96.768111), 100)
        );
    }
}
