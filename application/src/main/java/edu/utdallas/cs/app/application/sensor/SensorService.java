package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.sensor.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;

import java.util.ArrayList;
import java.util.List;

public class SensorService {

    private final SensorProvider sensorProvider;

    private final WaypointValidator WaypointValidator;

    private final CapturedPollutantProvider capturedPollutantProvider;

    public SensorService(SensorProvider sensorProvider, WaypointValidator WaypointValidator, CapturedPollutantProvider capturedPollutantProvider) {
        this.sensorProvider = sensorProvider;
        this.WaypointValidator = WaypointValidator;
        this.capturedPollutantProvider = capturedPollutantProvider;
    }


    public List<Sensor> getSensorsWithData(GeoLocation location){
        List<Sensor> sensorsLst = new ArrayList<>();

        sensorsLst = sensorProvider.findRelevantSensors(location);

        capturedPollutantProvider.findLatestDataFor(sensorsLst);

        // combine above two and return
        return sensorsLst;
    }

    public Boolean isUserNearHazardousArea(GeoLocation location){
        // WaypointValidator will have logic for if a sensor is red and if user is within 500m
        Boolean check = WaypointValidator.isValidWaypoint(location);


        return check;
    }



}
