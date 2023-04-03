package edu.utdallas.cs.app.graphhopper;

import com.graphhopper.config.Profile;
import com.graphhopper.routing.WeightingFactory;
import com.graphhopper.routing.ev.*;
import com.graphhopper.routing.util.EncodingManager;
import com.graphhopper.routing.util.VehicleEncodedValues;
import com.graphhopper.routing.weighting.DefaultTurnCostProvider;
import com.graphhopper.routing.weighting.TurnCostProvider;
import com.graphhopper.routing.weighting.Weighting;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.util.PMap;
import com.graphhopper.util.Parameters;
import edu.utdallas.cs.app.data.sensor.Sensor;

import java.util.List;

import static com.graphhopper.routing.weighting.FastestWeighting.DESTINATION_FACTOR;
import static com.graphhopper.routing.weighting.FastestWeighting.PRIVATE_FACTOR;
import static com.graphhopper.routing.weighting.TurnCostProvider.NO_TURN_COST_PROVIDER;
import static com.graphhopper.routing.weighting.Weighting.INFINITE_U_TURN_COSTS;
import static com.graphhopper.util.Helper.toLowerCase;

public class SensorAvoidingWeightingFactory implements WeightingFactory {
    private final BaseGraph graph;
    private final EncodingManager encodingManager;
    private final List<Sensor> sensorsToAvoid;

    public SensorAvoidingWeightingFactory(BaseGraph graph, EncodingManager encodingManager, List<Sensor> sensorsToAvoid) {
        this.graph = graph;
        this.encodingManager = encodingManager;
        this.sensorsToAvoid = sensorsToAvoid;
    }

    @Override
    public Weighting createWeighting(Profile profile, PMap requestHints, boolean disableTurnCosts) {
        // Merge profile hints with request hints, the request hints take precedence.
        // Note that so far we do not check if overwriting the profile hints actually works with the preparation
        // for LM/CH. Later we should also limit the number of parameters that can be used to modify the profile.
        PMap hints = new PMap();
        hints.putAll(profile.getHints());
        hints.putAll(requestHints);

        final String vehicle = profile.getVehicle();
        if (isOutdoorVehicle(vehicle)) {
            hints.putObject(PRIVATE_FACTOR, hints.getDouble(PRIVATE_FACTOR, 1.2));
        } else {
            hints.putObject(DESTINATION_FACTOR, hints.getDouble(DESTINATION_FACTOR, 10));
            hints.putObject(PRIVATE_FACTOR, hints.getDouble(PRIVATE_FACTOR, 10));
        }
        TurnCostProvider turnCostProvider;
        if (profile.isTurnCosts() && !disableTurnCosts) {
            DecimalEncodedValue turnCostEnc = encodingManager.getDecimalEncodedValue(TurnCost.key(vehicle));
            if (turnCostEnc == null)
                throw new IllegalArgumentException("Vehicle " + vehicle + " does not support turn costs");
            int uTurnCosts = hints.getInt(Parameters.Routing.U_TURN_COSTS, INFINITE_U_TURN_COSTS);
            turnCostProvider = new DefaultTurnCostProvider(turnCostEnc, graph.getTurnCostStorage(), uTurnCosts);
        } else {
            turnCostProvider = NO_TURN_COST_PROVIDER;
        }

        String weightingStr = toLowerCase(profile.getWeighting());
        if (weightingStr.isEmpty())
            throw new IllegalArgumentException("You have to specify a weighting");

        Weighting weighting = null;
        BooleanEncodedValue accessEnc = encodingManager.getBooleanEncodedValue(VehicleAccess.key(vehicle));
        DecimalEncodedValue speedEnc = encodingManager.getDecimalEncodedValue(VehicleSpeed.key(vehicle));
        DecimalEncodedValue priorityEnc = encodingManager.hasEncodedValue(VehiclePriority.key(vehicle))
                ? encodingManager.getDecimalEncodedValue(VehiclePriority.key(vehicle))
                : null;
        if (!encodingManager.hasEncodedValue(RoadAccess.KEY))
            throw new IllegalArgumentException("The fastest weighting requires road_access");
        EnumEncodedValue<RoadAccess> roadAccessEnc = encodingManager.getEnumEncodedValue(RoadAccess.KEY, RoadAccess.class);
        weighting = new SensorAvoidingWeighting(graph, accessEnc, speedEnc, roadAccessEnc, hints, turnCostProvider, sensorsToAvoid);
        return weighting;
    }

    public boolean isOutdoorVehicle(String name) {
        return VehicleEncodedValues.OUTDOOR_VEHICLES.contains(name);
    }
}
