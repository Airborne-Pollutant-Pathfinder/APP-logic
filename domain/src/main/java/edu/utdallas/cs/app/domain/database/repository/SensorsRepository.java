package edu.utdallas.cs.app.domain.database.repository;

import edu.utdallas.cs.app.domain.database.table.SensorTable;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface SensorsRepository extends JpaRepository<SensorTable, Integer> {

    @Query("SELECT s FROM SensorTable s WHERE ST_INTERSECTS(?1, s.area)")
    Collection<SensorTable> findAllByAreaIntersectsGeometry(Geometry geometry);

    SensorTable findSensorTableByLocationAndRadiusMeters(Point point, double radiusMeters);
}
