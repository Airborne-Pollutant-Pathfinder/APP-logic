package edu.utdallas.cs.app.data.sensor;

import edu.utdallas.cs.app.data.GeoLocation;

public record Sensor(GeoLocation location, int radius) {
}
