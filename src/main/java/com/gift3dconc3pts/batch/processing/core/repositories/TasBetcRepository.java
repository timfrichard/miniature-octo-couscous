package com.gift3dconc3pts.batch.processing.core.repositories;

import com.gift3dconc3pts.batch.processing.core.model.entities.TasBetc;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasBetcRepository extends CrudRepository<TasBetc, Long> {
}
