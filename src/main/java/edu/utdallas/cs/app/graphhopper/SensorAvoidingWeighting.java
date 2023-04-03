package edu.utdallas.cs.app.graphhopper;

import com.graphhopper.routing.ev.BooleanEncodedValue;
import com.graphhopper.routing.ev.DecimalEncodedValue;
import com.graphhopper.routing.ev.EnumEncodedValue;
import com.graphhopper.routing.ev.RoadAccess;
import com.graphhopper.routing.weighting.FastestWeighting;
import com.graphhopper.routing.weighting.TurnCostProvider;
import com.graphhopper.storage.BaseGraph;
import com.graphhopper.storage.NodeAccess;
import com.graphhopper.util.EdgeIteratorState;
import com.graphhopper.util.PMap;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.util.BoundingBoxUtil;

import java.util.List;

/**
 * A weighting that is based off GraphHopper's FastestWeighting, but avoids sensors by assigning it the max weight
 * possible.
 */
public class SensorAvoidingWeighting extends FastestWeighting {
    public static final int MAX_WEIGHT = Integer.MAX_VALUE;

    private final BaseGraph graph;
    private final List<Sensor> sensorsToAvoid;

    public SensorAvoidingWeighting(BaseGraph graph, BooleanEncodedValue accessEnc, DecimalEncodedValue speedEnc, EnumEncodedValue<RoadAccess> roadAccessEnc, PMap map, TurnCostProvider turnCostProvider, List<Sensor> sensorsToAvoid) {
        super(accessEnc, speedEnc, roadAccessEnc, map, turnCostProvider);
        this.graph = graph;
        this.sensorsToAvoid = sensorsToAvoid;
    }

    @Override
    public double calcEdgeWeight(EdgeIteratorState edge, boolean reverse) {
        int adj = edge.getAdjNode();
        int base = edge.getBaseNode();
        if (reverse) {
            int tmp = base;
            base = adj;
            adj = tmp;
        }

        try {
            NodeAccess na = graph.getNodeAccess();
            double lat = na.getLat(base);
            double lon = na.getLon(base);
            if (BoundingBoxUtil.isPointInSensors(sensorsToAvoid, lat, lon)) {
                return MAX_WEIGHT;
            }
        } catch (IllegalArgumentException e) {
            // ignore for regular edge weight calc
        }
        return super.calcEdgeWeight(edge, reverse);
    }
}
