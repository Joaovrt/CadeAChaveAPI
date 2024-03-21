package com.cadeachave.cadeachave.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cadeachave.cadeachave.models.SalaModel;

@Repository
public interface SalaRepository extends JpaRepository<SalaModel,Long>{
    SalaModel findByNome(String nome);
    List<SalaModel> findByNomeContaining(String nome);
    List<SalaModel> findByAberta(boolean aberta);
    List<SalaModel> findByNomeContainingAndAberta(String nome, boolean aberta);
}
