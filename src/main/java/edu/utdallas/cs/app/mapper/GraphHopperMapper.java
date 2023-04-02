package edu.utdallas.cs.app.mapper;

import com.graphhopper.util.PointList;
import com.graphhopper.util.shapes.GHPoint3D;
import edu.utdallas.cs.app.data.GeoLocation;
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
}