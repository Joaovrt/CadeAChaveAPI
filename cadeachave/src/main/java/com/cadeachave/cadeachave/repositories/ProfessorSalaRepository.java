package com.cadeachave.cadeachave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.ProfessorSalaModel;

@Repository
public interface ProfessorSalaRepository extends JpaRepository<ProfessorSalaModel,Long>{

}
