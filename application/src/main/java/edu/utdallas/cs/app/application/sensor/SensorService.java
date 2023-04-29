package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.captured_pollutant.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensorService {

    private final SensorProvider sensorProvider;

    private final WaypointValidator waypointValidator;

    private final CapturedPollutantProvider capturedPollutantProvider;

    public SensorService(SensorProvider sensorProvider, WaypointValidator waypointValidator, CapturedPollutantProvider capturedPollutantProvider) {
        this.sensorProvider = sensorProvider;
        this.waypointValidator = waypointValidator;
        this.capturedPollutantProvider = capturedPollutantProvider;
    }


    public Map<String, List<CapturedPollutant>> getSensorsWithData(GeoLocation location) {
        List<Sensor> sensorsInRange = sensorProvider.findRelevantSensors(location);
        Map<String, List<CapturedPollutant>> sensorsWithData = new HashMap<>();

        for (Sensor sensor : sensorsInRange) {
            List<CapturedPollutant> data = capturedPollutantProvider.findLatestDataFor(sensor);
            String sensorId = Integer.toString(data.get(0).getSensorId());

            sensorsWithData.put(sensorId, data);
        }
        return sensorsWithData;
    }

    public boolean isUserNearHazardousArea(GeoLocation location){
        return waypointValidator.isValidWaypoint(location);
    }
}
