package edu.utdallas.cs.app.web.sensor;

import edu.utdallas.cs.app.application.sensor.SensorService;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.domain.sensor.SensorData;
import graphql.kickstart.tools.GraphQLQueryResolver;

import java.util.List;

public class SensorController implements GraphQLQueryResolver {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public List<SensorData> getSensorsWithData(double Latitude, double Longitude){
        GeoLocation location = GeoLocation.at(Latitude, Longitude);
        return sensorService.getSensorsWithData(location);
    }

    public boolean isUserNearHazardousArea(GeoLocation location, RoutingPreferences preferences) {
        return sensorService.isUserNearHazardousArea(location, preferences);
    }
}
