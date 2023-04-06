package edu.utdallas.cs.app.data;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public final class GeoLocation {
    public static GeoLocation at(double latitude, double longitude) {
        return new GeoLocation(latitude, longitude);
    }

    private final double latitude;
    private final double longitude;
}
