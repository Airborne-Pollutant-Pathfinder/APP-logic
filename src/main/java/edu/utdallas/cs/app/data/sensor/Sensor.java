package edu.utdallas.cs.app.data.sensor;

import edu.utdallas.cs.app.data.GeoLocation;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
@AllArgsConstructor
public final class Sensor {
    private static final double METERS_PER_DEGREE_LATITUDE = 111000.0;
    private static final double RADIUS_OF_EARTH_METERS = 6371000.0;

    private final GeoLocation location;
    private final int radius;

    public List<GeoLocation> getSquareVertices() {
        double centerLatitude = location.getLatitude();
        double centerLongitude = location.getLongitude();

        double deltaLatitude = radius / METERS_PER_DEGREE_LATITUDE;
        double deltaLongitude = deltaLatitude / Math.cos(centerLatitude * Math.PI / 180.0);

        GeoLocation upperLeft = new GeoLocation(centerLatitude + deltaLatitude, centerLongitude - deltaLongitude);
        GeoLocation upperRight = new GeoLocation(centerLatitude + deltaLatitude, centerLongitude + deltaLongitude);
        GeoLocation lowerRight = new GeoLocation(centerLatitude - deltaLatitude, centerLongitude + deltaLongitude);
        GeoLocation lowerLeft = new GeoLocation(centerLatitude - deltaLatitude, centerLongitude - deltaLongitude);

        return Arrays.asList(upperLeft, upperRight, lowerRight, lowerLeft, upperLeft);
    }

    @Deprecated(since = "Azure Maps doesn't support circular shapes")
    public List<GeoLocation> getCircleVertices(int numVertices) {
        double centerLon = location.getLongitude();
        double centerLat = location.getLatitude();

        List<GeoLocation> vertices = new ArrayList<>();

        double distRadians = radius / RADIUS_OF_EARTH_METERS;
        for (int i = 0; i < numVertices; i++) {
            double rad = Math.toRadians(i * 360.0 / numVertices);
            double lat = Math.asin(Math.sin(centerLat) * Math.cos(distRadians) + Math.cos(centerLat) * Math.sin(distRadians) * Math.cos(rad));
            double lon = centerLon + Math.atan2(Math.sin(rad) * Math.sin(distRadians) * Math.cos(centerLat), Math.cos(distRadians) - Math.sin(centerLat) * Math.sin(lat));
            vertices.add(new GeoLocation(lat, lon));
        }
        vertices.add(vertices.get(0));
        return vertices;
    }
}
