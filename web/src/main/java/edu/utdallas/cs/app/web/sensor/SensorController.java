package edu.utdallas.cs.app.web.sensor;

import edu.utdallas.cs.app.application.sensor.SensorService;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.ArrayList;
import java.util.List;

public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public List<Sensor> getSensorsWithData(GeoLocation location){

        List<Sensor> sensorsLst = new ArrayList<>();

        // will need to adapt to SensorService when changed to return sensors & data
        sensorsLst = sensorService.getSensorsWithData(location);

        return sensorsLst;

    }

    public Boolean isUserNearHazardousArea(GeoLocation location){
        return sensorService.isUserNearHazardousArea(location);
    }
}