package com.cadeachave.cadeachave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.ProfessorModel;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long>{

}
