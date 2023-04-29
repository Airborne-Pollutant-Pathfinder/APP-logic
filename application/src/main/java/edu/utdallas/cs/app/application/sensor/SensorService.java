package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.captured_pollutant.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.domain.sensor.SensorData;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.captured_pollutant.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;

import java.util.ArrayList;
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


    public List<SensorData> getSensorsWithData(GeoLocation location) {
        List<Sensor> sensorsInRange = sensorProvider.findRelevantSensors(location);
        List<SensorData> sensorsData = new ArrayList<>();

        for (Sensor sensor : sensorsInRange) {
            List<CapturedPollutant> data = capturedPollutantProvider.findLatestDataFor(sensor);
            String sensorId = Integer.toString(data.get(0).getSensorId());

            sensorsData.add(SensorData.at(sensorId,data));
        }
        return sensorsData;
    }

    public boolean isUserNearHazardousArea(GeoLocation location){
        return waypointValidator.isValidWaypoint(location, );
    }
}
