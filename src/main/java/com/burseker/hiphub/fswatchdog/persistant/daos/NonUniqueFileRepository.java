package com.burseker.hiphub.fswatchdog.persistant.daos;

import com.burseker.hiphub.fswatchdog.persistant.models.NonUniqueFile;
import org.springframework.data.repository.CrudRepository;

public interface NonUniqueFileRepository extends CrudRepository<NonUniqueFile, Long> {
}
