package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.repositories.ProfessorRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ProfessorService {
    private Logger logger = Logger.getLogger(ProfessorService.class.getName());

    @Autowired
    ProfessorRepository professorRepository;

    public ResponseEntity<ProfessorModel> findById(Long id){

        logger.info("Buscando professor...");
        return ResponseEntity.status(HttpStatus.OK).body(professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id.")));
    }

    public ResponseEntity<List<ProfessorModel>> findAll(){

        logger.info("Buscando professores...");

       return ResponseEntity.status(HttpStatus.OK).body(professorRepository.findAll());
    }
    
    public ResponseEntity<ProfessorModel> create(ProfessorModel professor){

        logger.info("Cadastrando professor.");
        return ResponseEntity.status(HttpStatus.CREATED).body(professorRepository.save(professor));
    }

    public ResponseEntity<ProfessorModel> update (ProfessorModel professor, Long id){
        var entity = professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id."));
        entity.setNome(professor.getNome());
        entity.setCpf(professor.getCpf());
        logger.info("Atualizando professor.");
        return ResponseEntity.status(HttpStatus.OK).body(professorRepository.save(entity));
    }

    public void delete (Long id){
        var entity = professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id."));
        professorRepository.delete(entity);
        logger.info("Deletando professor.");
    }
}
