package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.HistoricoModel;
import com.cadeachave.cadeachave.repositories.HistoricoRepository;

import java.util.List;
import java.util.logging.Logger;

@Service
public class HistoricoService {
    private Logger logger = Logger.getLogger(HistoricoService.class.getName());

    @Autowired
    HistoricoRepository historicoRepository;

    public HistoricoModel findById(Long id){

        logger.info("Buscando historico...");
        return historicoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma historico encontrado com esse id."));
    }

    public List<HistoricoModel> findAll(){

        logger.info("Buscando historico...");

       return historicoRepository.findAll();
    }
    
    public HistoricoModel create (HistoricoModel historico){

        logger.info("Cadastrando historico.");
        return historicoRepository.save(historico);
    }

    public HistoricoModel update (HistoricoModel historico){
        var entity = historicoRepository.findById(historico.getId()).orElseThrow(()-> new ResourceNotFoundException("Nenhum historico encontrado com esse id."));
        entity.setAcao(historico.getAcao());
        entity.setHorario(historico.getHorario());
        entity.setProfessorSala(historico.getProfessorSala());
        logger.info("Atualizando historico.");
        return historicoRepository.save(historico);
    }

    public void delete (Long id){
        var entity = historicoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum historico encontrado com esse id."));
        historicoRepository.delete(entity);
        logger.info("Deletando historico.");
    }
}
