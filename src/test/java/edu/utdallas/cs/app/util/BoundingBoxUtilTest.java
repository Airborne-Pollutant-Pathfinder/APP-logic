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
}
