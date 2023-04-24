package edu.utdallas.cs.app.domain.repository;

import edu.utdallas.cs.app.domain.table.CapturedPollutantTable;
import edu.utdallas.cs.app.domain.table.SensorTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface CapturedPollutantRepository extends JpaRepository<CapturedPollutantTable, Integer> {

    Collection<CapturedPollutantTable> findTop100BySensor(SensorTable sensorTable);

}
