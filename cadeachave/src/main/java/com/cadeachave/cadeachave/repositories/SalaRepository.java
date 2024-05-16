package com.cadeachave.cadeachave.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.SalaModel;

@Repository
public interface SalaRepository extends JpaRepository<SalaModel,Long>{
    SalaModel findByNome(String nome);
    Page<SalaModel> findByNomeContainingIgnoreCase(String nome, Pageable pageable);
    Page<SalaModel> findByAberta(boolean aberta, Pageable pageable);
     @Query("SELECT s FROM SalaModel s " +
           "WHERE (:nome IS NULL OR s.nome ILIKE %:nome%)" +
           "AND (:aberta IS NULL OR s.aberta = :aberta)" +
           "AND (:ativo IS NULL OR s.ativo = :ativo)" 
           )
    Page<SalaModel> findByNomeIgnoreCaseContainingAndAbertaAndAtivo(String nome, Boolean aberta, Boolean ativo, Pageable pageable);
}
