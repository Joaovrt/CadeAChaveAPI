package com.cadeachave.cadeachave.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.ProfessorModel;

@Repository
public interface ProfessorRepository extends JpaRepository<ProfessorModel, Long>{
    ProfessorModel findByCpf(String cpf);
    List<ProfessorModel> findByNome(String nome);
    @Query("SELECT p FROM ProfessorModel p " +
           "WHERE (:cpf IS NULL OR p.cpf ILIKE %:cpf%) " +
           "OR (:nome IS NULL OR p.nome ILIKE %:nome%) " +
           "AND (:ativo IS NULL OR p.ativo = :ativo)")
    Page<ProfessorModel> findByCpfContainingIgnoreCaseOrNomeContainingIgnoreCaseAndAtivo(String cpf, String nome, Boolean ativo, Pageable pageable);
}
