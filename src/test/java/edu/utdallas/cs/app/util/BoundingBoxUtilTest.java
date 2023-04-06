package edu.utdallas.cs.app.util;

import edu.utdallas.cs.app.data.BoundingBox;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
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
        BoundingBox box1 = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        BoundingBox box2 = new BoundingBox(41.0, -74.0, 43.0, -72.0);
        assertTrue(BoundingBoxUtil.intersects(box1, box2));
        assertTrue(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnFalse_When_BoundingBoxesNotIntersect() {
        BoundingBox box1 = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        BoundingBox box2 = new BoundingBox(43.0, -74.0, 45.0, -72.0);
        assertFalse(BoundingBoxUtil.intersects(box1, box2));
        assertFalse(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnTrue_When_BoundingBoxesIntersectOnInternationalDateline() {
        BoundingBox box1 = new BoundingBox(40.0, -175.0, 42.0, 175.0);
        BoundingBox box2 = new BoundingBox(41.0, 170.0, 43.0, -170.0);
        assertTrue(BoundingBoxUtil.intersects(box1, box2));
        assertTrue(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnFalse_When_BoundingBoxesNotIntersectOnInternationalDateline() {
        BoundingBox box1 = new BoundingBox(40.0, -175.0, 42.0, -170.0);
        BoundingBox box2 = new BoundingBox(41.0, 170.0, 43.0, 175.0);
        assertFalse(BoundingBoxUtil.intersects(box1, box2));
        assertFalse(BoundingBoxUtil.intersects(box2, box1));
    }

    @Test
    public void Should_ReturnTrue_When_IsBoxInSensors_With_EmptySensorsList() {
        BoundingBox box = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        List<Sensor> sensors = new ArrayList<>();
        assertFalse(BoundingBoxUtil.isBoxInSensors(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_IsBoxInSensors_With_NoSensorIntersectingBoundingBox() {
        BoundingBox box = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor(new GeoLocation(43.0, -74.0), 100));
        assertFalse(BoundingBoxUtil.isBoxInSensors(sensors, box));
    }

    @Test
    public void Should_ReturnTrue_When_IsBoxInSensors_With_SensorIntersectingBoundingBox() {
        BoundingBox box = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor(new GeoLocation(41.0, -74.0), 100));
        assertTrue(BoundingBoxUtil.isBoxInSensors(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_IsBoxInSensors_With_MultipleSensorsAroundBoundingBox() {
        BoundingBox box = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor(new GeoLocation(43.0, -74.0), 100));
        sensors.add(new Sensor(new GeoLocation(41.0, -76.0), 100));
        sensors.add(new Sensor(new GeoLocation(43.0, -76.0), 100));
        assertFalse(BoundingBoxUtil.isBoxInSensors(sensors, box));
    }

    @Test
    public void Should_ReturnFalse_When_IsBoxInSensors_With_MultipleSensorsInBoundingBox() {
        BoundingBox box = new BoundingBox(40.0, -75.0, 42.0, -73.0);
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor(new GeoLocation(43.0, -74.0), 100));
        sensors.add(new Sensor(new GeoLocation(41.0, -76.0), 100));
        sensors.add(new Sensor(new GeoLocation(41.0, -74.0), 100));
        sensors.add(new Sensor(new GeoLocation(39.0, -74.0), 100));
        assertTrue(BoundingBoxUtil.isBoxInSensors(sensors, box));
    }
}
