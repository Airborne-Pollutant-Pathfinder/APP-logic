package edu.utdallas.cs.app.route.infrastructure.mapper;

import edu.utdallas.cs.app.route.domain.GeoLocation;
import org.locationtech.jts.geom.Coordinate;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JTSMapper {
    default Coordinate mapToCoordinate(GeoLocation location) {
        return new Coordinate(location.getLongitude(), location.getLatitude());
    }

    Coordinate[] mapToCoordinates(List<GeoLocation> locations);

    @Mapping(target = "latitude", source = "y")
    @Mapping(target = "longitude", source = "x")
    GeoLocation mapToGeoLocation(Coordinate coordinate);

    List<GeoLocation> mapToGeoLocations(Coordinate[] coordinates);
}
