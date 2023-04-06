package edu.utdallas.cs.app.graphhopper;

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
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import edu.utdallas.cs.app.provider.waypoint.impl.SensorAffectedWaypointAugmenter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

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
    private SensorProvider sensorProviderMock;
    private WaypointAugmenter waypointReducer;

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
        sensorProviderMock = mock(SensorProvider.class);
        waypointReducer = new SensorAffectedWaypointAugmenter(sensorProviderMock);
    }

    @Test
    public void Should_ReturnMaxWeight_When_SensorOnRoad() {
        Weighting weighting = new SensorAvoidingWeighting(graphMock, accessEncMock, speedEncMock, roadAccessEncMock, map, turnCostProviderMock, waypointReducer);

        int baseNode = 1;
        int adjNode = 2;

        when(graphMock.getNodeAccess()).thenReturn(nodeAccessMock);

        when(edgeMock.getBaseNode()).thenReturn(baseNode);
        when(edgeMock.getAdjNode()).thenReturn(adjNode);

        when(nodeAccessMock.getLat(baseNode)).thenReturn(32.9858);
        when(nodeAccessMock.getLon(baseNode)).thenReturn(-96.7501);
        when(nodeAccessMock.getLat(adjNode)).thenReturn(32.8975);
        when(nodeAccessMock.getLon(adjNode)).thenReturn(-96.8602);

        when(sensorProviderMock.findRelevantSensors(any(double.class), any(double.class))).thenReturn(createMockSensors());

        double actualWeight = weighting.calcEdgeWeight(edgeMock, false);

        assertEquals(SensorAvoidingWeighting.MAX_WEIGHT, actualWeight);
    }

    @Test
    public void Should_ReturnNotMaxWeight_When_SensorNotOnRoad() {
        Weighting weighting = new SensorAvoidingWeighting(graphMock, accessEncMock, speedEncMock, roadAccessEncMock, map, turnCostProviderMock, waypointReducer);

        int baseNode = 1;
        int adjNode = 2;

        when(graphMock.getNodeAccess()).thenReturn(nodeAccessMock);

        when(edgeMock.getBaseNode()).thenReturn(baseNode);
        when(edgeMock.getAdjNode()).thenReturn(adjNode);

        when(nodeAccessMock.getLat(baseNode)).thenReturn(32.9858);
        when(nodeAccessMock.getLon(baseNode)).thenReturn(-96.7501);
        when(nodeAccessMock.getLat(adjNode)).thenReturn(32.8975);
        when(nodeAccessMock.getLon(adjNode)).thenReturn(-96.8602);

        when(edgeMock.get(speedEncMock)).thenReturn(45.0);

        double actualWeight = weighting.calcEdgeWeight(edgeMock, false);

        assertNotEquals(SensorAvoidingWeighting.MAX_WEIGHT, actualWeight);
    }

    private List<Sensor> createMockSensors() {
        return Collections.singletonList(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(32.9858)
                        .longitude(-96.7500)
                        .build())
                .radiusInMeters(100)
                .build());
    }
}

