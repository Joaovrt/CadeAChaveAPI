package com.cadeachave.cadeachave.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.HistoricoModel;

@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoModel, Long>{

}