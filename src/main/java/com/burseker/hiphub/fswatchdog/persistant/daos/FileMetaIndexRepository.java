package com.burseker.hiphub.fswatchdog.persistant.daos;

import com.burseker.hiphub.fswatchdog.persistant.models.FileMetaIndex;
import org.springframework.data.repository.CrudRepository;

public interface FileMetaIndexRepository extends CrudRepository<FileMetaIndex, Long> {
}
