package edu.utdallas.cs.app.domain.database.repository;

import edu.utdallas.cs.app.domain.database.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.database.table.SensorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CapturedPollutantRepository extends JpaRepository<CapturedPollutantTable, Integer> {

    Collection<CapturedPollutantTable> findTop100BySensorOrderByDatetimeDesc(SensorTable sensorTable);

}
