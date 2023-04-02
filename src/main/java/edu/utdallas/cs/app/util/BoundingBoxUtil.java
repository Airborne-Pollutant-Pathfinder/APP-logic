package edu.utdallas.cs.app.util;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import lombok.experimental.UtilityClass;

import java.util.List;

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

    public boolean isPointInSensors(List<Sensor> sensorsToAvoid, double lat, double lon) {
        for (Sensor sensor : sensorsToAvoid) {
            List<GeoLocation> vertices = sensor.getSquareVertices();

            double smallestLatitude = Math.min(vertices.get(0).getLatitude(), vertices.get(2).getLatitude());
            double smallestLongitude = Math.min(vertices.get(0).getLongitude(), vertices.get(2).getLongitude());
            double largestLatitude = Math.max(vertices.get(0).getLatitude(), vertices.get(2).getLatitude());
            double largestLongitude = Math.max(vertices.get(0).getLongitude(), vertices.get(2).getLongitude());

            double[] boundingBox = {
                    smallestLatitude,
                    smallestLongitude,
                    largestLatitude,
                    largestLongitude
            };
            if (isPointInBoundingBox(boundingBox, lat, lon)) {
                return true;
            }
        }
        return false;
    }
}
