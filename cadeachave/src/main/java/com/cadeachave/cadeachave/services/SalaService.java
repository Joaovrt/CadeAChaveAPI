package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.repositories.SalaRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class SalaService {
    private Logger logger = Logger.getLogger(SalaService.class.getName());

    @Autowired
    SalaRepository salaRepository;

    public SalaModel findById(Long id){

        logger.info("Buscando sala...");
        return salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrado com esse id."));
    }

    public List<SalaModel> findAll(){

        logger.info("Buscando salas...");

       return salaRepository.findAll();
    }
    
    public SalaModel create (SalaModel sala){

        logger.info("Cadastrando sala.");
        return salaRepository.save(sala);
    }

    public SalaModel update (SalaModel sala){
        var entity = salaRepository.findById(sala.getId()).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        entity.setNome(sala.getNome());
        entity.setCodigo(sala.getCodigo());
        entity.setStatus(sala.isStatus());
        logger.info("Atualizando sala.");
        return salaRepository.save(sala);
    }

    public void delete (Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        salaRepository.delete(entity);
        logger.info("Deletando sala.");
    }
}
