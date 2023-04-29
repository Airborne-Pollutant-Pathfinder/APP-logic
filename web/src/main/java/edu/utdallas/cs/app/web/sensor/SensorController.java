package edu.utdallas.cs.app.web.sensor;

import edu.utdallas.cs.app.application.sensor.SensorService;
import edu.utdallas.cs.app.domain.route.BoundingBox;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;
import edu.utdallas.cs.app.domain.sensor.Sensor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public HashMap<String,List<CapturedPollutant>> getSensorsWithData(GeoLocation location){

//        List<Sensor> sensorsLst = new ArrayList<>();
        HashMap<String,List<CapturedPollutant>> SensorData = new HashMap<>();

        SensorData = sensorService.getSensorsWithData(location);

        return SensorData;

    }

    public Boolean isUserNearHazardousArea(GeoLocation location){
        return sensorService.isUserNearHazardousArea(location);
    }
}
