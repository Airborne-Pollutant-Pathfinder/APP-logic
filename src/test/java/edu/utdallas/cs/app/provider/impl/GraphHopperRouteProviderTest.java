package edu.utdallas.cs.app.provider.impl;

import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.util.shapes.GHPoint;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.mapper.GraphHopperMapper;
import edu.utdallas.cs.app.provider.graphhopper.GraphHopperProvider;
import edu.utdallas.cs.app.provider.osm.OSMFileProvider;
import edu.utdallas.cs.app.provider.route.impl.GraphHopperRouteProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertNull;

public class GraphHopperRouteProviderTest {

    private GraphHopperProvider graphHopperProviderMock;
    private GraphHopper graphHopperMock;
    private GHResponse ghResponseMock;
    private OSMFileProvider osmFileProviderMock;
    private GraphHopperMapper graphHopperMapperMock;

    @BeforeEach
    void setUp() throws IOException, URISyntaxException {
        graphHopperProviderMock = mock(GraphHopperProvider.class);
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

        assertNull(null, actualRoute);
    }

    private GHPoint createMockGHPoint() {
        return new GHPoint(32.9858, -96.7501);
    }
}
