package edu.utdallas.cs.app.provider.waypoint.impl;

import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.mapper.JTSMapper;
import edu.utdallas.cs.app.provider.waypoint.WaypointAugmenter;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * A waypoint augmenter that uses the Ramer–Douglas–Peucker algorithm to simplify a polyline.
 */
@Service
public class RamerDouglasPeuckerWaypointAugmenter implements WaypointAugmenter {
    /**
     * A constant for managing the Ramer–Douglas–Peucker algorithm. The higher the tolerance, the fewer points
     * that will be returned.
     */
    private static final double POLYLINE_SIMPLIFICATION_TOLERANCE = 0.01;

    private final JTSMapper mapper;

    public RamerDouglasPeuckerWaypointAugmenter(JTSMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<GeoLocation> augmentWaypoints(List<GeoLocation> waypoints) {
        Coordinate[] coordinates = mapper.mapToCoordinates(waypoints);
        GeometryFactory geometryFactory = new GeometryFactory();
        LineString polyline = geometryFactory.createLineString(coordinates);
        LineString simplified = (LineString) DouglasPeuckerSimplifier.simplify(polyline, POLYLINE_SIMPLIFICATION_TOLERANCE);
        return mapper.mapToGeoLocations(simplified.getCoordinates());
    }
}
