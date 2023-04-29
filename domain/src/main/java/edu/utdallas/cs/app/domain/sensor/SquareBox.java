package edu.utdallas.cs.app.domain.sensor;

import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class SquareBox {

    public static SquareBox fromBoundingBox(BoundingBox boundingBox) {
        double centerLatitude = (boundingBox.getMinimumLatitude() + boundingBox.getMaximumLatitude()) / 2;
        double centerLongitude = (boundingBox.getMinimumLongitude() + boundingBox.getMaximumLongitude()) / 2;
        double sideLength = Math.max(boundingBox.getMaximumLatitude() - boundingBox.getMinimumLatitude(),
                boundingBox.getMaximumLongitude() - boundingBox.getMinimumLongitude());

        double halfSideLength = sideLength / 2;
        GeoLocation upperLeft = GeoLocation.at(centerLatitude + halfSideLength, centerLongitude - halfSideLength);
        GeoLocation upperRight = GeoLocation.at(centerLatitude + halfSideLength, centerLongitude + halfSideLength);
        GeoLocation lowerRight = GeoLocation.at(centerLatitude - halfSideLength, centerLongitude + halfSideLength);
        GeoLocation lowerLeft = GeoLocation.at(centerLatitude - halfSideLength, centerLongitude - halfSideLength);

        return new SquareBox(upperLeft, upperRight, lowerRight, lowerLeft);
    }

    private final GeoLocation upperLeft;
    private final GeoLocation upperRight;
    private final GeoLocation lowerRight;
    private final GeoLocation lowerLeft;
}
