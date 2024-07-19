package com.gift3dconc3pts.batch.processing.core.repositories;


import com.gift3dconc3pts.batch.processing.core.model.entities.ProcessingError;
import org.springframework.data.repository.CrudRepository;

public interface ProcessingErrorRepository extends CrudRepository<ProcessingError, Long> {
}
