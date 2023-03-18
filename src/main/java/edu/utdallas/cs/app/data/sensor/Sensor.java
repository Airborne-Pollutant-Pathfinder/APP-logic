package edu.utdallas.cs.app.data.sensor;

import edu.utdallas.cs.app.data.GeoLocation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public record Sensor(GeoLocation location, int radius) {
    private static final double METERS_PER_DEGREE_LATITUDE = 111000.0;
    private static final double RADIUS_OF_EARTH_METERS = 6371000.0;

    public List<GeoLocation> getSquareVertices() {
        double centerLatitude = location.latitude();
        double centerLongitude = location.longitude();

        double deltaLatitude = radius / METERS_PER_DEGREE_LATITUDE;
        double deltaLongitude = deltaLatitude / Math.cos(centerLatitude * Math.PI / 180.0);

        GeoLocation upperLeft = new GeoLocation(centerLongitude - deltaLongitude, centerLatitude + deltaLatitude);
        GeoLocation upperRight = new GeoLocation(centerLongitude + deltaLongitude, centerLatitude + deltaLatitude);
        GeoLocation lowerRight = new GeoLocation(centerLongitude + deltaLongitude, centerLatitude - deltaLatitude);
        GeoLocation lowerLeft = new GeoLocation(centerLongitude - deltaLongitude, centerLatitude - deltaLatitude);

        return Arrays.asList(upperLeft, upperRight, lowerRight, lowerLeft, upperLeft);
    }

    @Deprecated(since = "Azure Maps doesn't support circular shapes")
    public List<GeoLocation> getCircleVertices(int numVertices) {
        double centerLon = location.longitude();
        double centerLat = location.latitude();

        List<GeoLocation> vertices = new ArrayList<>();

        double distRadians = radius / RADIUS_OF_EARTH_METERS;
        for (int i = 0; i < numVertices; i++) {
            double rad = Math.toRadians(i * 360.0 / numVertices);
            double lat = Math.asin(Math.sin(centerLat) * Math.cos(distRadians) + Math.cos(centerLat) * Math.sin(distRadians) * Math.cos(rad));
            double lon = centerLon + Math.atan2(Math.sin(rad) * Math.sin(distRadians) * Math.cos(centerLat), Math.cos(distRadians) - Math.sin(centerLat) * Math.sin(lat));
            vertices.add(new GeoLocation(lon, lat));
        }
        vertices.add(vertices.get(0));
        return vertices;
    }
}
