package edu.utdallas.cs.app.domain.sensor;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public final class Sensor {
    public static final double RADIUS_OF_EARTH_METERS = 6371000.0;
    private static final double METERS_PER_DEGREE_LATITUDE = 111000.0;

    private final int id;
    private final GeoLocation location;
    private final double radiusInMeters;

    public SquareBox getSquare() {
        double centerLatitude = location.getLatitude();
        double centerLongitude = location.getLongitude();

        double deltaLatitude = radiusInMeters / METERS_PER_DEGREE_LATITUDE;
        double deltaLongitude = deltaLatitude / Math.cos(centerLatitude * Math.PI / 180.0);

        GeoLocation upperLeft = GeoLocation.at(centerLatitude + deltaLatitude, centerLongitude - deltaLongitude);
        GeoLocation upperRight = GeoLocation.at(centerLatitude + deltaLatitude, centerLongitude + deltaLongitude);
        GeoLocation lowerRight = GeoLocation.at(centerLatitude - deltaLatitude, centerLongitude + deltaLongitude);
        GeoLocation lowerLeft = GeoLocation.at(centerLatitude - deltaLatitude, centerLongitude - deltaLongitude);

        return SquareBox.builder()
                .withUpperLeft(upperLeft)
                .withUpperRight(upperRight)
                .withLowerRight(lowerRight)
                .withLowerLeft(lowerLeft)
                .build();
    }

    @Deprecated(since = "Azure Maps doesn't support circular shapes")
    public List<GeoLocation> getCircleVertices(int numVertices) {
        double centerLatitude = location.getLatitude();
        double centerLongitude = location.getLongitude();

        List<GeoLocation> vertices = new ArrayList<>();

        double distRadians = radiusInMeters / RADIUS_OF_EARTH_METERS;
        for (int i = 0; i < numVertices; i++) {
            double rad = Math.toRadians(i * 360.0 / numVertices);
            double latitude = Math.asin(Math.sin(centerLatitude) * Math.cos(distRadians) + Math.cos(centerLatitude) * Math.sin(distRadians) * Math.cos(rad));
            double longitude = centerLongitude + Math.atan2(Math.sin(rad) * Math.sin(distRadians) * Math.cos(centerLatitude), Math.cos(distRadians) - Math.sin(centerLatitude) * Math.sin(latitude));
            vertices.add(GeoLocation.at(latitude, longitude));
        }
        vertices.add(vertices.get(0));
        return vertices;
    }
}
