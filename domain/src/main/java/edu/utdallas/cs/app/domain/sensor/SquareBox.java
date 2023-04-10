package edu.utdallas.cs.app.domain.sensor;

import edu.utdallas.cs.app.domain.route.GeoLocation;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(setterPrefix = "with")
public class SquareBox {
    private final GeoLocation upperLeft;
    private final GeoLocation upperRight;
    private final GeoLocation lowerRight;
    private final GeoLocation lowerLeft;
}
