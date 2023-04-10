package edu.utdallas.cs.app.domain.route;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class BoundingBox {
    private final double minimumLatitude;
    private final double minimumLongitude;
    private final double maximumLatitude;
    private final double maximumLongitude;
}
