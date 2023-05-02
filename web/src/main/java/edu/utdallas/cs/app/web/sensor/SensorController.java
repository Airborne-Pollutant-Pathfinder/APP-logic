package edu.utdallas.cs.app.web.sensor;

import edu.utdallas.cs.app.application.sensor.SensorService;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.domain.sensor.SensorData;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SensorController implements GraphQLQueryResolver {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public List<SensorData> allSensorsWithData() {
        return sensorService.getAllSensorsWithData();
    }

    public boolean userNearHazardousArea(double latitude, double longitude, RoutingPreferences preferences) {
        GeoLocation location = GeoLocation.at(latitude, longitude);
        return sensorService.isUserNearHazardousArea(location, preferences);
    }
}
