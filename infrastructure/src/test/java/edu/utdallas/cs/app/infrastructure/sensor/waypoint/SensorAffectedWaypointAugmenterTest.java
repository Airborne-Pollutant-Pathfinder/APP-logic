package edu.utdallas.cs.app.infrastructure.sensor.waypoint;

import edu.utdallas.cs.app.domain.database.table.PollutantTable;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.infrastructure.sensor.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;
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
    private CapturedPollutantProvider capturedPollutantMock;

    @BeforeEach
    public void setUp() {
        sensorProviderMock = mock(SensorProvider.class);
        capturedPollutantMock = mock(CapturedPollutantProvider.class);
    }

    @Test
    public void Should_ReturnFalse_When_ValidateWaypoint_With_PollutantThresholdBeingExceeded() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock, capturedPollutantMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(createMockSensors());
        when(capturedPollutantMock.findLatestDataFor(any(Sensor.class))).thenReturn(List.of(CapturedPollutant.builder()
                .withPollutant(PollutantTable.CO)
                .withValue(100)
                .build()));

        GeoLocation waypoint = GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build();
        RoutingPreferences preferences = RoutingPreferences.builder()
                .avoidHighways(false)
                .avoidTolls(false)
                .coThreshold(10)
                .no2Threshold(Double.MAX_VALUE)
                .o3Threshold(Double.MAX_VALUE)
                .pm2_5Threshold(Double.MAX_VALUE)
                .pm10Threshold(Double.MAX_VALUE)
                .so2Threshold(Double.MAX_VALUE)
                .build();

        boolean result = augmenter.isValidWaypoint(waypoint, preferences);

        assertFalse(result);
    }

    @Test
    public void Should_ReturnTrue_When_ValidateWaypoint_With_NoThresholdsBeingExceeded() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock, capturedPollutantMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(createMockSensors());
        when(capturedPollutantMock.findLatestDataFor(any(Sensor.class))).thenReturn(List.of(CapturedPollutant.builder()
                .withPollutant(PollutantTable.CO)
                .withValue(100)
                .build()));

        GeoLocation waypoint = GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build();
        RoutingPreferences preferences = RoutingPreferences.builder()
                .avoidHighways(false)
                .avoidTolls(false)
                .coThreshold(Double.MAX_VALUE)
                .no2Threshold(Double.MAX_VALUE)
                .o3Threshold(Double.MAX_VALUE)
                .pm2_5Threshold(Double.MAX_VALUE)
                .pm10Threshold(Double.MAX_VALUE)
                .so2Threshold(Double.MAX_VALUE)
                .build();

        boolean result = augmenter.isValidWaypoint(waypoint, preferences);

        assertTrue(result);
    }

    @Test
    public void Should_RemoveWaypoint_When_AugmentWaypoints_With_PollutantThresholdBeingExceeded() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock, capturedPollutantMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(createMockSensors());
        when(capturedPollutantMock.findLatestDataFor(any(Sensor.class))).thenReturn(List.of(CapturedPollutant.builder()
                .withPollutant(PollutantTable.CO)
                .withValue(100)
                .build()));

        List<GeoLocation> waypoints = List.of(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());
        RoutingPreferences preferences = RoutingPreferences.builder()
                .avoidHighways(false)
                .avoidTolls(false)
                .coThreshold(10)
                .no2Threshold(Double.MAX_VALUE)
                .o3Threshold(Double.MAX_VALUE)
                .pm2_5Threshold(Double.MAX_VALUE)
                .pm10Threshold(Double.MAX_VALUE)
                .so2Threshold(Double.MAX_VALUE)
                .build();

        List<GeoLocation> result = augmenter.augmentWaypoints(waypoints, preferences);

        assertEquals(0, result.size());
    }

    @Test
    public void Should_KeepWaypoint_When_AugmentWaypoints_With_NoThresholdsBeingExceeded() {
        SensorAffectedWaypointAugmenter augmenter = new SensorAffectedWaypointAugmenter(sensorProviderMock, capturedPollutantMock);

        when(sensorProviderMock.findRelevantSensors(any(GeoLocation.class))).thenReturn(Collections.emptyList());
        when(capturedPollutantMock.findLatestDataFor(any(Sensor.class))).thenReturn(List.of(CapturedPollutant.builder()
                .withPollutant(PollutantTable.CO)
                .withValue(100)
                .build()));

        List<GeoLocation> waypoints = List.of(GeoLocation.builder()
                .latitude(32.9858)
                .longitude(-96.7501)
                .build());
        RoutingPreferences preferences = RoutingPreferences.builder()
                .avoidHighways(false)
                .avoidTolls(false)
                .coThreshold(Double.MAX_VALUE)
                .no2Threshold(Double.MAX_VALUE)
                .o3Threshold(Double.MAX_VALUE)
                .pm2_5Threshold(Double.MAX_VALUE)
                .pm10Threshold(Double.MAX_VALUE)
                .so2Threshold(Double.MAX_VALUE)
                .build();

        List<GeoLocation> result = augmenter.augmentWaypoints(waypoints, preferences);

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