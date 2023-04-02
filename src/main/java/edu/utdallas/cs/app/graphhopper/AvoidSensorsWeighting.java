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
import edu.utdallas.cs.app.data.GeoLocation;
import edu.utdallas.cs.app.data.sensor.Sensor;
import edu.utdallas.cs.app.util.BoundingBoxUtil;

import java.util.List;

public class AvoidSensorsWeighting extends FastestWeighting {

    private final BaseGraph graph;
    private final List<Sensor> sensorsToAvoid;

    public AvoidSensorsWeighting(BaseGraph graph, BooleanEncodedValue accessEnc, DecimalEncodedValue speedEnc, EnumEncodedValue<RoadAccess> roadAccessEnc, PMap map, TurnCostProvider turnCostProvider, List<Sensor> sensorsToAvoid) {
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
            if (isPointInSensors(lat, lon)) {
                return Integer.MAX_VALUE;
            }
        } catch (IllegalArgumentException e) {
            // ignore for regular edge weight calc
        }
        return super.calcEdgeWeight(edge, reverse);
    }

    private boolean isPointInSensors(double lat, double lon) {
        for (Sensor sensor : sensorsToAvoid) {
            List<GeoLocation> vertices = sensor.getSquareVertices();
            double[] boundingBox = {
                    vertices.get(0).getLatitude(),
                    vertices.get(0).getLongitude(),
                    vertices.get(2).getLatitude(),
                    vertices.get(2).getLongitude()
            };
            if (BoundingBoxUtil.isPointInBoundingBox(boundingBox, lat, lon)) {
                return true;
            }
        }
        return false;
    }
}
