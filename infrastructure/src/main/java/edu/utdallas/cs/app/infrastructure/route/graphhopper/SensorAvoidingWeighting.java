package edu.utdallas.cs.app.infrastructure.route.graphhopper;

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
import edu.utdallas.cs.app.domain.route.GeoLocation;
import edu.utdallas.cs.app.domain.route.RoutingPreferences;
import edu.utdallas.cs.app.infrastructure.route.waypoint.WaypointValidator;

/**
 * A weighting that is based off GraphHopper's FastestWeighting, but avoids sensors by assigning it the max weight
 * possible.
 */
public class SensorAvoidingWeighting extends FastestWeighting {
    public static final int MAX_WEIGHT = Integer.MAX_VALUE;
    public static final String ROUTING_PREFERENCES_KEY = "app.routing_preferences";

    private final BaseGraph graph;
    private final PMap map;
    private final WaypointValidator waypointValidator;

    public SensorAvoidingWeighting(BaseGraph graph, BooleanEncodedValue accessEnc, DecimalEncodedValue speedEnc,
                                   EnumEncodedValue<RoadAccess> roadAccessEnc, PMap map,
                                   TurnCostProvider turnCostProvider,
                                   WaypointValidator waypointValidator) {
        super(accessEnc, speedEnc, roadAccessEnc, map, turnCostProvider);
        this.graph = graph;
        this.map = map;
        this.waypointValidator = waypointValidator;
    }

    @Override
    public double calcEdgeWeight(EdgeIteratorState edge, boolean reverse) {
        int base = reverse ? edge.getAdjNode() : edge.getBaseNode();
        try {
            NodeAccess na = graph.getNodeAccess();
            double latitude = na.getLat(base);
            double longitude = na.getLon(base);
            GeoLocation geoLocation = GeoLocation.at(latitude, longitude);
            RoutingPreferences preferences = map.getObject(ROUTING_PREFERENCES_KEY, null);
            if (!waypointValidator.isValidWaypoint(geoLocation, preferences)) {
                return MAX_WEIGHT;
            }
        } catch (IllegalArgumentException e) {
            // ignore for regular edge weight calc
        }
        return super.calcEdgeWeight(edge, reverse);
    }
}
