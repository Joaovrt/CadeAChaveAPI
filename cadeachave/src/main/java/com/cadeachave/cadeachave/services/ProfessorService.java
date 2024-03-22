package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.dtos.ProfessorRecordDto;
import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.repositories.ProfessorRepository;
import com.cadeachave.cadeachave.repositories.SalaRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Service
public class ProfessorService {
    private Logger logger = Logger.getLogger(ProfessorService.class.getName());

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    SalaRepository salaRepository;

    public ResponseEntity<ProfessorModel> findById(Long id){

        logger.info("Buscando professor...");

        ProfessorModel professor = professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id."));
        return ResponseEntity.status(HttpStatus.OK).body(professor);
    }

    public ResponseEntity<ProfessorModel> findByCpf(String cpf){

        logger.info("Buscando professor...");

        ProfessorModel professor = professorRepository.findByCpf(cpf);
        if(professor==null)
            throw new ResourceNotFoundException("Nenhum professor encontrado com esse CPF.");
        return ResponseEntity.status(HttpStatus.OK).body(professor);
    }

    public ResponseEntity<List<ProfessorModel>> findByNome(String name){

        logger.info("Buscando professor...");

        List<ProfessorModel> professorList = professorRepository.findByNome(name);
        if(professorList.isEmpty())
            throw new ResourceNotFoundException("Nenhum professor encontrado com esse Nome.");
        return ResponseEntity.status(HttpStatus.OK).body(professorList);
    }

    public ResponseEntity<List<ProfessorModel>> findByCpfOrNomeContaining(String termo) {
        logger.info("Buscando professores por CPF ou nome...");

        List<ProfessorModel> professorList = professorRepository.findByCpfContainingOrNomeContaining(termo, termo);
        if (professorList.isEmpty()) {
            throw new ResourceNotFoundException("Nenhum professor encontrado com o CPF ou nome correspondente.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(professorList);
    }

    public ResponseEntity<List<ProfessorModel>> findAll(){

        logger.info("Buscando professores...");

        List<ProfessorModel> professorList = professorRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(professorList);
    }
    
    public ResponseEntity<ProfessorModel> create(ProfessorRecordDto professorDto) {
    logger.info("Cadastrando professor.");
    ProfessorModel professor = new ProfessorModel();
    professor.setNome(professorDto.nome());
    professor.setCpf(professorDto.cpf());
    if (!professorDto.salas().isEmpty()) {
        List<SalaModel> salaList = new ArrayList<>();
        for (Long i : professorDto.salas()) {
            SalaModel sala = salaRepository.findById(i)
                    .orElseThrow(() -> new RuntimeException("Sala não encontrada para o ID: " + i));
            salaList.add(sala);
        }
        professor.setSalas(salaList);
    }
    return ResponseEntity.status(HttpStatus.CREATED).body(professorRepository.save(professor));
}


    public ResponseEntity<ProfessorModel> update (ProfessorRecordDto professorDto, Long id){
        var entity = professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id."));
        entity.setNome(professorDto.nome());
        entity.setCpf(professorDto.cpf());
        if (!professorDto.salas().isEmpty()) {
            List<SalaModel> salaList = new ArrayList<>();
            for (Long i : professorDto.salas()) {
                SalaModel sala = salaRepository.findById(i)
                        .orElseThrow(() -> new RuntimeException("Sala não encontrada para o ID: " + i));
                salaList.add(sala);
            }
            entity.setSalas(salaList);
        }
        logger.info("Atualizando professor.");
        return ResponseEntity.status(HttpStatus.OK).body(professorRepository.save(entity));
    }

    public ResponseEntity<Object> delete (Long id){
        var entity = professorRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id."));
        professorRepository.delete(entity);
        logger.info("Deletando professor.");
        return ResponseEntity.status(HttpStatus.OK).body("Professor deletado.");
    }

}