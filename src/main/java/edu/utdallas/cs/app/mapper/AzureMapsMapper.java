package edu.utdallas.cs.app.mapper;

import com.azure.core.models.GeoLinearRing;
import com.azure.core.models.GeoPolygon;
import com.azure.core.models.GeoPosition;
import com.azure.maps.route.models.MapsSearchRoute;
import com.azure.maps.route.models.RouteLeg;
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.SquareBox;
import edu.utdallas.cs.app.data.route.Route;
import edu.utdallas.cs.app.data.sensor.Sensor;
import org.mapstruct.Mapper;

import java.util.ArrayList;
import java.util.List;

@Mapper(componentModel = "spring")
@Deprecated(since = "Dr. Wenkstern requires us to use Google Directions API now.")
public interface AzureMapsMapper {
    default GeoPosition mapToGeoPosition(GeoLocation location) {
        return new GeoPosition(location.getLongitude(), location.getLatitude());
    }

    List<GeoPosition> mapToGeoPositions(List<GeoLocation> locations);

    GeoLocation mapToGeoLocation(GeoPosition position);

    List<GeoLocation> mapToGeoLocations(List<GeoPosition> locations);

    default Route mapToRoute(MapsSearchRoute route) {
        List<GeoLocation> locations = new ArrayList<>();
        for (RouteLeg leg : route.getLegs()) {
            locations.addAll(mapToGeoLocations(leg.getPoints()));
        }
        return Route.builder()
                .lengthInMeters(route.getSummary().getLengthInMeters())
                .travelTimeInSeconds(route.getSummary().getTravelTimeInSeconds())
                .waypoints(locations)
                .build();
    }

    List<Route> mapToRoutes(List<MapsSearchRoute> routes);

    default List<GeoPolygon> mapToGeoPolygons(List<Sensor> sensors) {
        List<GeoPolygon> polygons = new ArrayList<>();
        for (Sensor sensor : sensors) {
            SquareBox square = sensor.getSquare();
            List<GeoLocation> boxCoords = List.of(square.getUpperLeft(), square.getUpperRight(), square.getLowerRight(), square.getLowerLeft(), square.getUpperLeft());
            polygons.add(new GeoPolygon(new GeoLinearRing(mapToGeoPositions(boxCoords))));
        }
        return polygons;
    }
}
