package edu.utdallas.cs.app.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SquareBox {
    private final GeoLocation upperLeft;
    private final GeoLocation upperRight;
    private final GeoLocation lowerRight;
    private final GeoLocation lowerLeft;
}
