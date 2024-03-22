package com.cadeachave.cadeachave.services;

import java.util.List;
import java.util.logging.Logger;
import java.util.Comparator;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.HistoricoModel;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.repositories.HistoricoRepository;

@Service
public class HistoricoService {

    private Logger logger = Logger.getLogger(HistoricoService.class.getName());

    @Autowired
    HistoricoRepository historicoRepository;

    public ResponseEntity<HistoricoModel> findById(Long id){

        logger.info("Buscando historico...");

        HistoricoModel historico = historicoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma historico encontrado com esse id."));
        return ResponseEntity.status(HttpStatus.OK).body(historico);
    }

    public ResponseEntity<List<HistoricoModel>> findAll(){

        logger.info("Buscando historico...");

        List<HistoricoModel> historicoList = historicoRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(historicoList);
    }
    
    public ResponseEntity<HistoricoModel> create (ProfessorModel professor, SalaModel sala, boolean abriu){

        logger.info("Cadastrando historico.");
        HistoricoModel historico = new HistoricoModel();
        historico.setProfessor(professor);
        historico.setSala(sala);
        historico.setAbriu(abriu);
        historico.setHorario(new Timestamp(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.CREATED).body(historicoRepository.save(historico));
    }

    public List<HistoricoModel> buscarRegistrosPorIntervaloDeDatas(Timestamp dataInicial, Timestamp dataFinal) {
        return historicoRepository.findByHorarioBetween(dataInicial, dataFinal);
    }

    public boolean validaUltimoAAbrir(ProfessorModel professor, SalaModel sala){
        List<HistoricoModel> historicoList = historicoRepository.findBySalaAndAbriu(sala, true);
        if(!historicoList.isEmpty()){
            historicoList.sort(Comparator.comparing(HistoricoModel::getHorario).reversed());
            HistoricoModel historicoMaisRecente = historicoList.get(0);
            if(historicoMaisRecente.getProfessor()==professor)
                return true;
        }
        return false;
    }
}