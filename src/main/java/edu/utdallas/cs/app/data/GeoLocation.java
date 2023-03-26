package edu.utdallas.cs.app.data;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public final class GeoLocation {
    private final double longitude;
    private final double latitude;
}
