package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import edu.utdallas.cs.app.provider.route.SensorAvoidingRouteProvider;
import edu.utdallas.cs.app.provider.sensor.SensorAggregator;
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
    private SensorAvoidingRouteProvider sensorAvoidingRouteProviderMock;
    private WaypointAugmenter waypointAugmenterMock;
    private SensorAggregator sensorAggregatorMock;

    @BeforeEach
    void setUp() {
        routeProviderMock = mock(RouteProvider.class);
        sensorAvoidingRouteProviderMock = mock(SensorAvoidingRouteProvider.class);
        waypointAugmenterMock = mock(WaypointAugmenter.class);
        sensorAggregatorMock = mock(SensorAggregator.class);
    }

    @Test
    void Should_ReturnSafestThenFastest_When_GetRoutesWithMockProvider() {
        RouteService routeService = new RouteService(routeProviderMock, sensorAvoidingRouteProviderMock, waypointAugmenterMock, sensorAggregatorMock);

        GeoLocation origin = new GeoLocation(32.9858, -96.7501);
        GeoLocation destination = new GeoLocation(32.8975, -96.8602);

        Route fastestRoute = new Route(1000L, Duration.ofMinutes(10), List.of(origin, destination));
        Route safestRoute = new Route(2000L, Duration.ofMinutes(20), List.of(origin, destination));
        List<Route> expectedRoutes = List.of(safestRoute, fastestRoute);

        when(routeProviderMock.getRoute(any(List.class))).thenReturn(fastestRoute);
        when(sensorAvoidingRouteProviderMock.getRoute(any(List.class), any(List.class))).thenReturn(safestRoute);
        when(waypointAugmenterMock.augmentWaypoints(any(List.class))).thenReturn(List.of(origin, destination));
        when(sensorAggregatorMock.findRelevantSensors(any(Route.class))).thenReturn(createMockSensors());

        List<Route> actualRoutes = routeService.getRoutes(origin, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }

    private List<Sensor> createMockSensors() {
        return List.of(
                new Sensor(new GeoLocation(-96.768111, 33.133839), 100)
        );
    }
}
