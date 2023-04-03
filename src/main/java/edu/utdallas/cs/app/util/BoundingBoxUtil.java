package edu.utdallas.cs.app.util;

import edu.utdallas.cs.app.data.BoundingBox;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BoundingBoxUtil {
    public boolean isPointInBoundingBox(BoundingBox box, double latitude, double longitude) {
        return box.getMinimumLatitude() <= latitude && latitude <= box.getMaximumLatitude()
                && box.getMinimumLongitude() <= longitude && longitude <= box.getMaximumLongitude();
    }

    public BoundingBox increaseBoundingBoxByMultiplier(BoundingBox box, double multiplier) {
        double centerLatitude = (box.getMinimumLatitude() + box.getMaximumLatitude()) / 2;
        double centerLongitude = (box.getMinimumLongitude() + box.getMaximumLongitude()) / 2;
        double latitudeDelta = (box.getMaximumLatitude() - box.getMinimumLatitude()) * multiplier / 2;
        double longitudeDelta = (box.getMaximumLongitude() - box.getMinimumLongitude()) * multiplier / 2;
        double newMinimumLatitude = centerLatitude - latitudeDelta;
        double newMaximumLatitude = centerLatitude + latitudeDelta;
        double newMinimumLongitude = centerLongitude - longitudeDelta;
        double newMaximumLongitude = centerLongitude + longitudeDelta;
        return new BoundingBox(newMinimumLatitude, newMinimumLongitude, newMaximumLatitude, newMaximumLongitude);
    }

    public boolean isPointInSensors(List<Sensor> sensorsToAvoid, double latitude, double longitude) {
        for (Sensor sensor : sensorsToAvoid) {
            List<GeoLocation> vertices = sensor.getSquareVertices();

            double smallestLatitude = Math.min(vertices.get(0).getLatitude(), vertices.get(2).getLatitude());
            double smallestLongitude = Math.min(vertices.get(0).getLongitude(), vertices.get(2).getLongitude());
            double largestLatitude = Math.max(vertices.get(0).getLatitude(), vertices.get(2).getLatitude());
            double largestLongitude = Math.max(vertices.get(0).getLongitude(), vertices.get(2).getLongitude());

            BoundingBox box = new BoundingBox(smallestLatitude, smallestLongitude, largestLatitude, largestLongitude);
            if (isPointInBoundingBox(box, latitude, longitude)) {
                return true;
            }
        }
        return false;
    }
}
