package edu.utdallas.cs.app.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BoundingBox {
    private final double minimumLatitude;
    private final double minimumLongitude;
    private final double maximumLatitude;
    private final double maximumLongitude;
}
