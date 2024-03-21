package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.controllers.ProfessorController;
import com.cadeachave.cadeachave.controllers.SalaController;
import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.repositories.SalaRepository;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;
import java.util.logging.Logger;

@Service
public class SalaService {
    private Logger logger = Logger.getLogger(SalaService.class.getName());

    @Autowired
    SalaRepository salaRepository;

    public ResponseEntity<SalaModel> findById(Long id){

        logger.info("Buscando sala...");

        SalaModel sala = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        sala.add(linkTo(methodOn(SalaController.class).findAll()).withRel("Lista de salas"));
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<SalaModel> findByNome(String nome){

        logger.info("Buscando sala...");

        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse nome.");
        sala.add(linkTo(methodOn(SalaController.class).findAll()).withRel("Lista de salas"));
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContaining(String termo){

        logger.info("Buscando salas...");

       List<SalaModel> salaList = salaRepository.findByNomeContaining(termo);
        if(!salaList.isEmpty()){
            for(SalaModel sala : salaList){
                Long id = sala.getId();
                sala.add(linkTo(methodOn(SalaController.class).findById(id)).withSelfRel());
            }
        }
        else
            throw new ResourceNotFoundException("Nenhum professor encontrado com o CPF ou nome correspondente.");
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByStatus(boolean status){

        logger.info("Buscando salas...");

        List<SalaModel> salaList = salaRepository.findByStatus(status);
        if(!salaList.isEmpty()){
            for(SalaModel sala : salaList){
                Long id = sala.getId();
                sala.add(linkTo(methodOn(ProfessorController.class).findById(id)).withSelfRel());
            }
        }
        else
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse status.");
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContainingAndStatus(String nome, boolean status) {
        logger.info("Buscando sala por nome e status...");

        List<SalaModel> salaList = salaRepository.findByNomeContainingAndStatus(nome, status);
        if (salaList.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma sala encontrada com o nome e status correspondente.");
        }
        for (SalaModel sala : salaList) {
            Long id = sala.getId();
            sala.add(linkTo(methodOn(ProfessorController.class).findById(id)).withSelfRel());
        }
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findAll(){

        logger.info("Buscando salas...");

       List<SalaModel> salaList = salaRepository.findAll();
        if(!salaList.isEmpty()){
            for(SalaModel sala : salaList){
                Long id = sala.getId();
                sala.add(linkTo(methodOn(SalaController.class).findById(id)).withSelfRel());
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }
    
    public ResponseEntity<SalaModel> create (SalaModel sala){

        logger.info("Cadastrando sala.");

        return ResponseEntity.status(HttpStatus.CREATED).body(salaRepository.save(sala));
    }

    public ResponseEntity<SalaModel> update (SalaModel sala, Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        entity.setNome(sala.getNome());
        entity.setCodigo(sala.getCodigo());
        entity.setStatus(sala.isStatus());
        logger.info("Atualizando sala.");
        return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(entity));
    }

    public ResponseEntity<Object> delete (Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        salaRepository.delete(entity);
        logger.info("Deletando sala.");
        return ResponseEntity.status(HttpStatus.OK).body("Sala deletada.");
    }
}
