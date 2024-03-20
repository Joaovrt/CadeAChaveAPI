package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.ProfessorSalaModel;
import com.cadeachave.cadeachave.repositories.ProfessorSalaRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class ProfessorSalaService {
    private Logger logger = Logger.getLogger(ProfessorSalaService.class.getName());

    @Autowired
    ProfessorSalaRepository professorSalaRepository;

    public ProfessorSalaModel findById(Long id){

        logger.info("Buscando professor e sala...");
        return professorSalaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor e sala encontrados com esse id."));
    }

    public List<ProfessorSalaModel> findAll(){

        logger.info("Buscando professores e salas...");

       return professorSalaRepository.findAll();
    }
    
    public ProfessorSalaModel create (ProfessorSalaModel professorSala){

        logger.info("Cadastrando professor e sala.");
        return professorSalaRepository.save(professorSala);
    }

    public ProfessorSalaModel update (ProfessorSalaModel professorSala){
        var entity = professorSalaRepository.findById(professorSala.getId()).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor e sala encontrados com esse id."));
        entity.setProfessor(professorSala.getProfessor());
        entity.setSala(professorSala.getSala());
        logger.info("Atualizando professor e sala.");
        return professorSalaRepository.save(professorSala);
    }

    public void delete (Long id){
        var entity = professorSalaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor e sala encontrados com esse id."));
        professorSalaRepository.delete(entity);
        logger.info("Deletando professor e sala.");
    }
}
