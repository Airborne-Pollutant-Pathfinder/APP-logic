package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.database.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;
import edu.utdallas.cs.app.infrastructure.sensor.CapturedPollutantProvider;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;

import java.awt.*;
import java.util.*;
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


    public HashMap<String,List<CapturedPollutant>> getSensorsWithData(GeoLocation location){

        List<Sensor> sensorsInRange = sensorProvider.findRelevantSensors(location);
        HashMap<String,List<CapturedPollutant>> SensorData = new HashMap<>();

        if (!sensorsInRange.isEmpty()) {
            System.out.println(sensorsInRange);
        }

        ListIterator<Sensor> iter = sensorsInRange.listIterator();  // was intending to use this for index
        while (iter.hasNext()) {
            Sensor sensor = iter.next();

            List<CapturedPollutant> data = capturedPollutantProvider.findLatestDataFor(sensor);
            String sensorID = Integer.toString(data.get(0).getSensorId());

            SensorData.put(sensorID, data );

        }

        return SensorData;



/*        List<List<CapturedPollutant>> sensorData = capturedPollutantProvider.findLatestDataFor(sensorsLst);

         combine above two and return
        return sensorsLst;*/
    }

    public Boolean isUserNearHazardousArea(GeoLocation location){
        // WaypointValidator will have logic for if a sensor is red and if user is within 500m
        Boolean check = WaypointValidator.isValidWaypoint(location);


        return check;
    }



}
