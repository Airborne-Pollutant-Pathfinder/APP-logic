package edu.utdallas.cs.app.route.infrastructure;

import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;
import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import edu.utdallas.cs.app.route.domain.GeoLocation;
import edu.utdallas.cs.app.route.domain.Route;
import edu.utdallas.cs.app.route.infrastructure.graphhopper.SensorAvoidingWeighting;
import edu.utdallas.cs.app.route.infrastructure.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.route.infrastructure.osm.OSMFileProvider;
import edu.utdallas.cs.app.route.infrastructure.GraphHopperRouteProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.Duration;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class GraphHopperRouteProviderTest {

    private SensorAvoidingWeighting.GraphHopperProvider graphHopperProviderMock;
    private GraphHopper graphHopperMock;
    private GHResponse ghResponseMock;
    private OSMFileProvider osmFileProviderMock;
    private GraphHopperMapper graphHopperMapperMock;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        graphHopperProviderMock = mock(SensorAvoidingWeighting.GraphHopperProvider.class);
        graphHopperMock = mock(GraphHopper.class);
        ghResponseMock = mock(GHResponse.class);
        osmFileProviderMock = mock(OSMFileProvider.class);
        graphHopperMapperMock = mock(GraphHopperMapper.class);

        when(osmFileProviderMock.getOSMFile()).thenReturn(null);
        when(graphHopperProviderMock.createGraphHopper(any())).thenReturn(graphHopperMock);
    }

    @Test
    public void Should_ReturnNull_When_GetRouteWithErrors() {
        GraphHopperRouteProvider routeProvider = new GraphHopperRouteProvider(graphHopperProviderMock, osmFileProviderMock, graphHopperMapperMock);
        List<GeoLocation> waypoints = List.of();

        when(graphHopperMapperMock.mapToGHPoints(waypoints)).thenReturn(List.of(createMockGHPoint()));

        when(graphHopperMock.route(any())).thenReturn(ghResponseMock);
        when(ghResponseMock.hasErrors()).thenReturn(true);
        when(ghResponseMock.getErrors()).thenReturn(List.of());

        Route actualRoute = routeProvider.getRoute(waypoints);

        assertNull(actualRoute);
    }

    @Test
    public void Should_ReturnRoute_When_GetRoute() {
        GraphHopperRouteProvider routeProvider = new GraphHopperRouteProvider(graphHopperProviderMock, osmFileProviderMock, graphHopperMapperMock);
        List<GeoLocation> waypoints = List.of();

        when(graphHopperMapperMock.mapToGHPoints(waypoints)).thenReturn(List.of(createMockGHPoint()));
        when(graphHopperMapperMock.mapToGeoLocations(any(PointList.class))).thenReturn(List.of(createMockGeoLocation()));

        when(graphHopperMock.route(any())).thenReturn(ghResponseMock);
        when(ghResponseMock.hasErrors()).thenReturn(false);
        when(ghResponseMock.getBest()).thenReturn(createMockResponsePath());

        Route expectedRoute = Route.builder()
                .lengthInMeters(1)
                .travelTimeInSeconds(Duration.ofSeconds(1))
                .waypoint(createMockGeoLocation())
                .build();
        Route actualRoute = routeProvider.getRoute(waypoints);

        assertEquals(expectedRoute, actualRoute);
    }

    private ResponsePath createMockResponsePath() {
        ResponsePath path = new ResponsePath();
        path.setDistance(1);
        path.setTime(1000);

        PointList list = new PointList();
        list.add(createMockGHPoint());
        path.setPoints(list);
        return path;
    }

    private GHPoint createMockGHPoint() {
        return new GHPoint(32.9858, -96.7501);
    }

    private GeoLocation createMockGeoLocation() {
        return GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build();
    }
}
