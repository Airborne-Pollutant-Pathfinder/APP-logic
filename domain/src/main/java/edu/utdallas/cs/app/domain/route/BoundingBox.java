package edu.utdallas.cs.app.domain.route;

import edu.utdallas.cs.app.domain.sensor.SquareBox;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class BoundingBox {

    public static BoundingBox ENTIRE_WORLD = BoundingBox.builder()
            .withMaximumLatitude(-90.0)
            .withMinimumLongitude(-180.0)
            .withMinimumLatitude(90.0)
            .withMaximumLongitude(180.0)
            .build();

    public static BoundingBox fromSquareBox(SquareBox squreBox) {
        double smallestLatitude = Math.min(squreBox.getUpperLeft().getLatitude(), squreBox.getLowerRight().getLatitude());
        double smallestLongitude = Math.min(squreBox.getUpperLeft().getLongitude(), squreBox.getLowerRight().getLongitude());
        double largestLatitude = Math.max(squreBox.getUpperLeft().getLatitude(), squreBox.getLowerRight().getLatitude());
        double largestLongitude = Math.max(squreBox.getUpperLeft().getLongitude(), squreBox.getLowerRight().getLongitude());

        return new BoundingBox(smallestLatitude, smallestLongitude, largestLatitude, largestLongitude);
    }

    private final double minimumLatitude;
    private final double minimumLongitude;
    private final double maximumLatitude;
    private final double maximumLongitude;
}
