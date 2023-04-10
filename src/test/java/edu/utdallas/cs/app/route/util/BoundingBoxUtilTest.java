package edu.utdallas.cs.app.route.util;

import edu.utdallas.cs.app.route.domain.BoundingBox;
import edu.utdallas.cs.app.route.domain.GeoLocation;
import edu.utdallas.cs.app.sensor.domain.Sensor;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BoundingBoxUtilTest {

    private static final double DELTA = 0.002;

    @Test
    public void Should_Succeed_When_GenerateBoundingBoxOnPointFor100Meters() {
        BoundingBox boundingBox = BoundingBoxUtil.generateBoundingBox(40.7128, -74.0060, 100.0);
        assertEquals(40.711984, boundingBox.getMinimumLatitude(), DELTA);
        assertEquals(-74.007554, boundingBox.getMinimumLongitude(), DELTA);
        assertEquals(40.713616, boundingBox.getMaximumLatitude(), DELTA);
        assertEquals(-74.004446, boundingBox.getMaximumLongitude(), DELTA);
    }

    @Test
    public void Should_ReturnTrue_When_BoundingBoxesIntersect() {
        BoundingBox box1 = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        BoundingBox box2 = BoundingBox.builder()
                .withMinimumLatitude(41.0)
                .withMinimumLongitude(-74.0)
                .withMaximumLatitude(43.0)
                .withMaximumLongitude(-72.0)
                .build();
        assertTrue(BoundingBoxUtil.intersects(box1, box2));
        assertTrue(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnFalse_When_BoundingBoxesNotIntersect() {
        BoundingBox box1 = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        BoundingBox box2 = BoundingBox.builder()
                .withMinimumLatitude(43.0)
                .withMinimumLongitude(-74.0)
                .withMaximumLatitude(45.0)
                .withMaximumLongitude(-72.0)
                .build();
        assertFalse(BoundingBoxUtil.intersects(box1, box2));
        assertFalse(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnTrue_When_BoundingBoxesIntersectOnInternationalDateline() {
        BoundingBox box1 = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-175.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(175.0)
                .build();
        BoundingBox box2 = BoundingBox.builder()
                .withMinimumLatitude(41.0)
                .withMinimumLongitude(170.0)
                .withMaximumLatitude(43.0)
                .withMaximumLongitude(-170.0)
                .build();
        assertTrue(BoundingBoxUtil.intersects(box1, box2));
        assertTrue(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnFalse_When_BoundingBoxesNotIntersectOnInternationalDateline() {
        BoundingBox box1 = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-175.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-170.0)
                .build();
        BoundingBox box2 = BoundingBox.builder()
                .withMinimumLatitude(41.0)
                .withMinimumLongitude(170.0)
                .withMaximumLatitude(43.0)
                .withMaximumLongitude(175.0)
                .build();
        assertFalse(BoundingBoxUtil.intersects(box1, box2));
        assertFalse(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnTrue_When_BoxIntersectsAnySensor_With_EmptySensorsList() {
        BoundingBox box = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        List<Sensor> sensors = new ArrayList<>();
        assertFalse(BoundingBoxUtil.boxIntersectsAnySensor(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_BoxIntersectsAnySensor_With_NoSensorIntersectingBoundingBox() {
        BoundingBox box = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(43.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        assertFalse(BoundingBoxUtil.boxIntersectsAnySensor(sensors, box));
    }

    @Test
    public void Should_ReturnTrue_When_BoxIntersectsAnySensor_With_SensorIntersectingBoundingBox() {
        BoundingBox box = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(41.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        assertTrue(BoundingBoxUtil.boxIntersectsAnySensor(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_BoxIntersectsAnySensor_With_MultipleSensorsAroundBoundingBox() {
        BoundingBox box = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(43.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(41.0)
                        .longitude(-76.0)
                        .build())
                .radiusInMeters(100)
                .build());
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(43.0)
                        .longitude(-76.0)
                        .build())
                .radiusInMeters(100)
                .build());
        assertFalse(BoundingBoxUtil.boxIntersectsAnySensor(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_BoxIntersectsAnySensor_With_MultipleSensorsInBoundingBox() {
        BoundingBox box = BoundingBox.builder()
                .withMinimumLatitude(40.0)
                .withMinimumLongitude(-75.0)
                .withMaximumLatitude(42.0)
                .withMaximumLongitude(-73.0)
                .build();
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(43.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(41.0)
                        .longitude(-76.0)
                        .build())
                .radiusInMeters(100)
                .build());
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(41.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        sensors.add(Sensor.builder()
                .location(GeoLocation.builder()
                        .latitude(39.0)
                        .longitude(-74.0)
                        .build())
                .radiusInMeters(100)
                .build());
        assertTrue(BoundingBoxUtil.boxIntersectsAnySensor(sensors, box));
    }
}
