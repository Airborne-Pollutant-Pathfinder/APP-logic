package edu.utdallas.cs.app.application.sensor;


import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;
import edu.utdallas.cs.app.infrastructure.sensor.SensorProvider;

import java.util.ArrayList;
import java.util.List;

public class SensorService {

    private final SensorProvider sensorProvider;

    public SensorService(SensorProvider sensorProvider) {
        this.sensorProvider = sensorProvider;
    }


    public List<Sensor> getSensorsWithData(GeoLocation location){
        List<Sensor> sensorsLst = new ArrayList<>();

        List<Sensor> sensorsInRange = sensorProvider.findRelevantSensors(location);



        return sensorsLst;
    }

    public Boolean isValidWaypoint(GeoLocation location){



        return Boolean.TRUE;
    }



}
