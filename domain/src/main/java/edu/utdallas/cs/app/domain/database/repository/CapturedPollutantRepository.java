package edu.utdallas.cs.app.domain.database.repository;

import edu.utdallas.cs.app.domain.database.table.SensorTable;
import edu.utdallas.cs.app.domain.table.CapturedPollutantTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CapturedPollutantRepository extends JpaRepository<CapturedPollutantTable, Integer> {

    Collection<CapturedPollutantTable> findTop100BySensor(SensorTable sensorTable);

}
