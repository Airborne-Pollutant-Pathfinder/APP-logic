package edu.utdallas.cs.app.infrastructure.route.graphhopper;

import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EnumEncodedValue;
import com.graphhopper.routing.ev.RoadAccess;
import com.graphhopper.routing.weighting.TurnCostProvider;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PMap;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SensorAvoidingWeightingTest {
    private BaseGraph graphMock;
    private TurnCostProvider turnCostProviderMock;
    private NodeAccess nodeAccessMock;
    private BooleanEncodedValue accessEncMock;
    private DecimalEncodedValue speedEncMock;
    private EnumEncodedValue<RoadAccess> roadAccessEncMock;
    private PMap map;
    private EdgeIteratorState edgeMock;
    private WaypointValidator waypointValidatorMock;

    @BeforeEach
    void setUp() {
        graphMock = mock(BaseGraph.class);
        nodeAccessMock = mock(NodeAccess.class);
        accessEncMock = mock(BooleanEncodedValue.class);
        speedEncMock = mock(DecimalEncodedValue.class);
        roadAccessEncMock = mock(EnumEncodedValue.class);
        map = new PMap();
        turnCostProviderMock = mock(TurnCostProvider.class);
        edgeMock = mock(EdgeIteratorState.class);
        waypointValidatorMock = mock(WaypointValidator.class);
    }

    @Test
    public void Should_ReturnMaxWeight_When_ValidatorIsTrue() {
        Weighting weighting = new SensorAvoidingWeighting(graphMock, accessEncMock, speedEncMock, roadAccessEncMock, map, turnCostProviderMock, waypointValidatorMock);

        int baseNode = 1;
        int adjNode = 2;

        when(graphMock.getNodeAccess()).thenReturn(nodeAccessMock);

        when(edgeMock.getBaseNode()).thenReturn(baseNode);
        when(edgeMock.getAdjNode()).thenReturn(adjNode);

        when(nodeAccessMock.getLat(baseNode)).thenReturn(32.9858);
        when(nodeAccessMock.getLon(baseNode)).thenReturn(-96.7501);
        when(nodeAccessMock.getLat(adjNode)).thenReturn(32.8975);
        when(nodeAccessMock.getLon(adjNode)).thenReturn(-96.8602);

        when(waypointValidatorMock.isValidWaypoint(any(GeoLocation.class), any())).thenReturn(false);

        double actualWeight = weighting.calcEdgeWeight(edgeMock, false);

        assertEquals(SensorAvoidingWeighting.MAX_WEIGHT, actualWeight);
    }

    @Test
    public void Should_ReturnNotMaxWeight_When_ValidatorIsTrue() {
        Weighting weighting = new SensorAvoidingWeighting(graphMock, accessEncMock, speedEncMock, roadAccessEncMock, map, turnCostProviderMock, waypointValidatorMock);

        int baseNode = 1;
        int adjNode = 2;

        when(graphMock.getNodeAccess()).thenReturn(nodeAccessMock);

        when(edgeMock.getBaseNode()).thenReturn(baseNode);
        when(edgeMock.getAdjNode()).thenReturn(adjNode);

        when(nodeAccessMock.getLat(baseNode)).thenReturn(32.9858);
        when(nodeAccessMock.getLon(baseNode)).thenReturn(-96.7501);
        when(nodeAccessMock.getLat(adjNode)).thenReturn(32.8975);
        when(nodeAccessMock.getLon(adjNode)).thenReturn(-96.8602);

        when(waypointValidatorMock.isValidWaypoint(any(GeoLocation.class), any())).thenReturn(true);

        when(edgeMock.get(speedEncMock)).thenReturn(45.0); // this is only so the result isn't infinity

        double actualWeight = weighting.calcEdgeWeight(edgeMock, false);

        assertNotEquals(SensorAvoidingWeighting.MAX_WEIGHT, actualWeight);
    }
}

