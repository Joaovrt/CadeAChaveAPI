package com.cadeachave.cadeachave.repositories;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.HistoricoModel;
import com.cadeachave.cadeachave.models.SalaModel;


@Repository
public interface HistoricoRepository extends JpaRepository<HistoricoModel, Long>{
    List<HistoricoModel> findBySalaAndAbriu(SalaModel sala, boolean abriu);
    List<HistoricoModel> findByHorarioBetweenAndProfessorIdAndSalaIdAndAbriu(
        Timestamp dataInicial, 
        Timestamp dataFinal, 
        Long professorId, 
        Long salaId, 
        Boolean abriu
    );
}