package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.provider.route.RouteProvider;
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
    void Should_ReturnSafestThenFastest_When_GetRoutes() {
        RouteService routeService = new RouteService(routeProviderMock, sensorAvoidingRouteProviderMock, waypointAugmenterMock, waypointReducerMock);

        GeoLocation origin = GeoLocation.builder()
                .latitude(-96.7501)
                .longitude(32.9858)
                .build();
        GeoLocation destination = GeoLocation.builder()
                .latitude(-96.8602)
                .longitude(32.8975)
                .build();

        Route fastestRoute = Route.builder()
                .lengthInMeters(1000)
                .travelTimeInSeconds(Duration.ofMinutes(10))
                .waypoint(origin)
                .waypoint(destination)
                .build();
        Route safestRoute = Route.builder()
                .lengthInMeters(2000)
                .travelTimeInSeconds(Duration.ofMinutes(20))
                .waypoint(origin)
                .waypoint(destination)
                .build();
        List<Route> expectedRoutes = List.of(safestRoute, fastestRoute);

        when(routeProviderMock.getRoute(any(List.class))).thenReturn(fastestRoute);
        when(sensorAvoidingRouteProviderMock.getRoute(any(List.class))).thenReturn(safestRoute);
        when(waypointAugmenterMock.augmentWaypoints(any(List.class))).thenReturn(List.of(origin, destination));
        when(waypointReducerMock.augmentWaypoints(any(List.class))).thenReturn(List.of(origin, destination));

        List<Route> actualRoutes = routeService.getRoutes(origin, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }
}
