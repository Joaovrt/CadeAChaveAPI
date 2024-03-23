package com.cadeachave.cadeachave.services;

import java.util.List;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.Comparator;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.dtos.HistoricoResponseRecordDto;
import com.cadeachave.cadeachave.dtos.ProfessorWithoutSalasRecordDto;
import com.cadeachave.cadeachave.exceptions.ResourceBadRequestException;
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

    public ResponseEntity<HistoricoResponseRecordDto> findById(Long id){
        HistoricoModel historico = historicoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma historico encontrado com o id: "+id));
        HistoricoResponseRecordDto historicoResponse = convertToHistoricoResponseRecordDto(historico);
        
        return ResponseEntity.status(HttpStatus.OK).body(historicoResponse);
    }

    public ResponseEntity<List<HistoricoResponseRecordDto>> findAll(){
        List<HistoricoModel> historicoList = historicoRepository.findAll();
        List<HistoricoResponseRecordDto> historicoResponseList = new ArrayList<HistoricoResponseRecordDto>();
        for (HistoricoModel historico : historicoList) {
            historicoResponseList.add(convertToHistoricoResponseRecordDto(historico));
        }

        return ResponseEntity.status(HttpStatus.OK).body(historicoResponseList);
    }
    
    public ResponseEntity<HistoricoModel> create (ProfessorModel professor, SalaModel sala, boolean abriu){
        HistoricoModel historico = new HistoricoModel();
        historico.setProfessor(professor);
        historico.setSala(sala);
        historico.setAbriu(abriu);
        historico.setHorario(new Timestamp(System.currentTimeMillis()));

        return ResponseEntity.status(HttpStatus.CREATED).body(historicoRepository.save(historico));
    }

    public ResponseEntity<List<HistoricoResponseRecordDto>> buscarHistoricoComFiltro(String dataInicial, String dataFinal, Long professorId, Long salaId, Boolean abriu) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Timestamp dataHoraInicial = new Timestamp(dateFormat.parse(dataInicial).getTime());
            Timestamp dataHoraFinal = new Timestamp(dateFormat.parse(dataFinal).getTime());
    
            List<HistoricoModel> historicoList = historicoRepository.findByHorarioBetweenAndProfessorIdAndSalaIdAndAbriu(dataHoraInicial, dataHoraFinal, professorId, salaId, abriu);
            List<HistoricoResponseRecordDto> historicoResponseList = new ArrayList<>();
    
            for (HistoricoModel historico : historicoList) {
                historicoResponseList.add(convertToHistoricoResponseRecordDto(historico));
            }
    
            return ResponseEntity.status(HttpStatus.OK).body(historicoResponseList);
        } catch (ParseException e) {
            throw new ResourceBadRequestException("Formato de data incorreto, insira (yyyy-MM-dd HH:mm:ss)");
        }
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

     private HistoricoResponseRecordDto convertToHistoricoResponseRecordDto(HistoricoModel historico) {
        ProfessorModel professor = historico.getProfessor();
        return new HistoricoResponseRecordDto(
                historico.getId(),
                new ProfessorWithoutSalasRecordDto(
                        professor.getId(),
                        professor.getNome(),
                        professor.getCpf()
                ),
                historico.getSala(),
                historico.getHorario(),
                historico.isAbriu()
        );
    }
}