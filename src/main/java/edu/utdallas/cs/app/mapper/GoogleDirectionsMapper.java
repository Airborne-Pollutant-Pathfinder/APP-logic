package edu.utdallas.cs.app.mapper;

import com.google.maps.internal.PolylineEncoding;
import com.google.maps.model.*;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.route.Route;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
public interface GoogleDirectionsMapper {
    @Mapping(target = "lat", source = "latitude")
    @Mapping(target = "lng", source = "longitude")
    LatLng mapToLatLng(GeoLocation location);

    @Mapping(target = "latitude", source = "lat")
    @Mapping(target = "longitude", source = "lng")
    GeoLocation mapToGeoLocation(LatLng position);

    default List<GeoLocation> mapToGeoLocations(DirectionsLeg leg) {
        List<GeoLocation> locations = new ArrayList<>();
        for (DirectionsStep step : leg.steps) {
            EncodedPolyline polyline = step.polyline;
            List<LatLng> latLngs = PolylineEncoding.decode(polyline.getEncodedPath());
            for (LatLng latLng : latLngs) {
                locations.add(mapToGeoLocation(latLng));
            }
        }
        return locations;
    }

    default Route mapToRoute(DirectionsRoute route) {
        DirectionsLeg leg = route.legs[0]; // assumption: no intermediate destinations, only a start and end

        long lengthInMeters = leg.distance.inMeters;
        Duration travelTimeInSeconds = Duration.ofSeconds(leg.duration.inSeconds);
        List<GeoLocation> locations = mapToGeoLocations(leg);

        return new Route(lengthInMeters, travelTimeInSeconds, locations);
    }

    List<Route> mapToRoutes(DirectionsRoute[] routes);
}
