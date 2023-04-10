package edu.utdallas.cs.app.infrastructure.route.mapper;

import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint;
import com.graphhopper.util.shapes.GHPoint3D;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GraphHopperMapper {
    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "longitude", source = "lon")
    GeoLocation mapToGeoLocation(GHPoint3D point);

    default List<GeoLocation> mapToGeoLocations(PointList pointList) {
        List<GeoLocation> locations = new ArrayList<>();
        for (GHPoint3D ghPoint3D : pointList) {
            locations.add(mapToGeoLocation(ghPoint3D));
        }
        return locations;
    }

    @Mapping(target = "lat", source = "latitude")
    @Mapping(target = "lon", source = "longitude")
    GHPoint mapToGHPoint(GeoLocation location);

    List<GHPoint> mapToGHPoints(List<GeoLocation> waypoints);
}
