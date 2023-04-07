package edu.utdallas.cs.app.provider.waypoint.impl;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.provider.sensor.SensorProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SensorAffectedWaypointAugmenterTest {
    private SensorProvider sensorProviderMock;

    @BeforeEach
    public void setUp() {
        sensorProviderMock = mock(SensorProvider.class);
    }

    @Test
    public void Should_ReturnFalse_When_ValidateWaypoint_With_SensorInVicinity() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(createMockSensors());

        boolean result = augmenter.isValidWaypoint(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());

        assertFalse(result);
    }

    @Test
    public void Should_ReturnTrue_When_ValidateWaypoint_With_NoSensorInVicinity() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(Collections.emptyList());

        boolean result = augmenter.isValidWaypoint(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());

        assertTrue(result);
    }

    @Test
    public void Should_RemoveWaypoint_When_AugmentWaypoints_With_SensorInVicinity() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(createMockSensors());

        List<GeoLocation> waypoints = List.of(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());

        List<GeoLocation> result = augmenter.augmentWaypoints(waypoints);

        assertEquals(0, result.size());
    }

    @Test
    public void Should_KeepWaypoint_When_AugmentWaypoints_With_NoSensorInVicinity() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(Collections.emptyList());

        List<GeoLocation> waypoints = List.of(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());

        List<GeoLocation> result = augmenter.augmentWaypoints(waypoints);

        assertEquals(waypoints.size(), result.size());
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