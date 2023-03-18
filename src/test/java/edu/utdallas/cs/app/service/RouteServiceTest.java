package edu.utdallas.cs.app.service;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.provider.route.RouteProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RouteServiceTest {
    private RouteProvider routeProviderMock;
    private RouteService routeService;

    @BeforeEach
    void setUp() {
        routeProviderMock = mock(RouteProvider.class);
        routeService = new RouteService(routeProviderMock);
    }

    @Test
    void Should_ReturnExpectedRoutes_When_GetRoutesWithMockProvider() {
        GeoLocation origin = new GeoLocation(32.9858, -96.7501);
        GeoLocation destination = new GeoLocation(32.8975, -96.8602);
        List<Route> expectedRoutes = List.of(
                new Route(1000L, java.time.Duration.ofMinutes(10), List.of(origin, destination)),
                new Route(2000L, java.time.Duration.ofMinutes(20), List.of(origin, destination, origin))
        );
        when(routeProviderMock.getRoutes(any(GeoLocation.class), any(GeoLocation.class), any(List.class)))
                .thenReturn(expectedRoutes);

        List<Route> actualRoutes = routeService.getRoutes(origin, destination);

        assertEquals(expectedRoutes, actualRoutes);
    }
}
