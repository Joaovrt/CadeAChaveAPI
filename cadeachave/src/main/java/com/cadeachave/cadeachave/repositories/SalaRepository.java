package com.cadeachave.cadeachave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.SalaModel;

@Repository
public interface SalaRepository extends JpaRepository<SalaModel,Long>{

}
