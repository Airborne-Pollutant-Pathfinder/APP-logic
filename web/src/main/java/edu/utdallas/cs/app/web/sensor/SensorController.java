package edu.utdallas.cs.app.web.sensor;

import edu.utdallas.cs.app.application.sensor.SensorService;
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.sensor.CapturedPollutant;

import java.util.List;
import java.util.Map;

public class SensorController {

    private final SensorService sensorService;

    public SensorController(SensorService sensorService) {
        this.sensorService = sensorService;
    }

    public Map<String,List<CapturedPollutant>> getSensorsWithData(GeoLocation location){
        return sensorService.getSensorsWithData(location);
    }

    public boolean isUserNearHazardousArea(GeoLocation location){
        return sensorService.isUserNearHazardousArea(location);
    }
}
