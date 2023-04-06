package edu.utdallas.cs.app.util;

import edu.utdallas.cs.app.data.BoundingBox;
import edu.utdallas.cs.app.data.SquareBox;
import edu.utdallas.cs.app.data.sensor.Sensor;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class BoundingBoxUtil {
    public BoundingBox generateBoundingBox(double latitude, double longitude, double radiusInMeters) {
        // Convert radius from meters to radians
        double radiusInRadians = radiusInMeters / Sensor.RADIUS_OF_EARTH_METERS;

        // Convert latitude and longitude to radians
        double latitudeInRadians = Math.toRadians(latitude);
        double longitudeInRadians = Math.toRadians(longitude);

        // Calculate minimum and maximum latitude and longitude
        double minLatitude = latitudeInRadians - radiusInRadians;
        double maxLatitude = latitudeInRadians + radiusInRadians;
        double minLongitude = longitudeInRadians - radiusInRadians / Math.cos(latitudeInRadians);
        double maxLongitude = longitudeInRadians + radiusInRadians / Math.cos(latitudeInRadians);

        // Convert back to degrees
        double minLatitudeInDegrees = Math.toDegrees(minLatitude);
        double maxLatitudeInDegrees = Math.toDegrees(maxLatitude);
        double minLongitudeInDegrees = Math.toDegrees(minLongitude);
        double maxLongitudeInDegrees = Math.toDegrees(maxLongitude);

        return BoundingBox.builder()
                .withMinimumLatitude(minLatitudeInDegrees)
                .withMaximumLatitude(maxLatitudeInDegrees)
                .withMinimumLongitude(minLongitudeInDegrees)
                .withMaximumLongitude(maxLongitudeInDegrees)
                .build();
    }

    public static boolean intersects(BoundingBox one, BoundingBox two) {
        // Check if the bounding boxes do not overlap in latitude
        if (one.getMaximumLatitude() < two.getMinimumLatitude() || one.getMinimumLatitude() > two.getMaximumLatitude()) {
            return false;
        }

        // Check if the bounding boxes do not overlap in longitude
        double oneMaxLon = one.getMaximumLongitude() < one.getMinimumLongitude() ? one.getMaximumLongitude() + 360 : one.getMaximumLongitude();
        double twoMaxLon = two.getMaximumLongitude() < two.getMinimumLongitude() ? two.getMaximumLongitude() + 360 : two.getMaximumLongitude();

        if (oneMaxLon < two.getMinimumLongitude() || one.getMinimumLongitude() > twoMaxLon) {
            return false;
        }

        // The bounding boxes intersect
        return true;
    }

    public boolean boxIntersectsAnySensor(List<Sensor> sensorsToAvoid, BoundingBox box) {
        for (Sensor sensor : sensorsToAvoid) {
            SquareBox square = sensor.getSquare();

            double smallestLatitude = Math.min(square.getUpperLeft().getLatitude(), square.getLowerRight().getLatitude());
            double smallestLongitude = Math.min(square.getUpperLeft().getLongitude(), square.getLowerRight().getLongitude());
            double largestLatitude = Math.max(square.getUpperLeft().getLatitude(), square.getLowerRight().getLatitude());
            double largestLongitude = Math.max(square.getUpperLeft().getLongitude(), square.getLowerRight().getLongitude());

            BoundingBox sensorBox = BoundingBox.builder()
                    .withMinimumLatitude(smallestLatitude)
                    .withMaximumLatitude(largestLatitude)
                    .withMinimumLongitude(smallestLongitude)
                    .withMaximumLongitude(largestLongitude)
                    .build();
            if (intersects(sensorBox, box)) {
                return true;
            }
        }
        return false;
    }
}
