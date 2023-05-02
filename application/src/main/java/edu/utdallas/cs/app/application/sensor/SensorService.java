package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.sensor.SensorData;
import edu.utdallas.cs.app.infrastructure.captured_pollutant.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SensorService {

    private final SensorProvider sensorProvider;

    private final WaypointValidator waypointValidator;

    private final CapturedPollutantProvider capturedPollutantProvider;


    public SensorService(SensorProvider sensorProvider, WaypointValidator waypointValidator, CapturedPollutantProvider capturedPollutantProvider) {
        this.sensorProvider = sensorProvider;
        this.waypointValidator = waypointValidator;
        this.capturedPollutantProvider = capturedPollutantProvider;
    }

    public List<SensorData> getAllSensorsWithData() {
        List<Sensor> sensorsInRange = sensorProvider.findRelevantSensors(BoundingBox.ENTIRE_WORLD);
        List<SensorData> sensorsData = new ArrayList<>();

        for (Sensor sensor : sensorsInRange) {
            List<CapturedPollutant> data = capturedPollutantProvider.findLatestDataFor(sensor);
            sensorsData.add(SensorData.at(sensor, data));
        }
        return sensorsData;
    }

    public boolean isUserNearHazardousArea(GeoLocation location, RoutingPreferences preferences) {
        return !waypointValidator.isValidWaypoint(location, preferences);
    }
}
