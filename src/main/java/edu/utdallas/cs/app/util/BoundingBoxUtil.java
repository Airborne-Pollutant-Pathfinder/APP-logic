package edu.utdallas.cs.app.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class BoundingBoxUtil {
    public boolean isPointInBoundingBox(double[] boundingBox, double latitude, double longitude) {
        return boundingBox[0] <= latitude && latitude <= boundingBox[2] && boundingBox[1] <= longitude && longitude <= boundingBox[3];
    }

    public double[] increaseBoundingBoxByMultiplier(double[] boundingBox, double multiplier) {
        double centerLat = (boundingBox[0] + boundingBox[2]) / 2;
        double centerLon = (boundingBox[1] + boundingBox[3]) / 2;
        double latDelta = (boundingBox[2] - boundingBox[0]) * multiplier / 2;
        double lonDelta = (boundingBox[3] - boundingBox[1]) * multiplier / 2;
        double minLat = centerLat - latDelta;
        double maxLat = centerLat + latDelta;
        double minLon = centerLon - lonDelta;
        double maxLon = centerLon + lonDelta;
        return new double[]{minLat, minLon, maxLat, maxLon};
    }
}
