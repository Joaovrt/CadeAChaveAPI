package com.cadeachave.cadeachave.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cadeachave.cadeachave.dtos.HistoricoResponseRecordDto;
import com.cadeachave.cadeachave.services.HistoricoService;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {
    
    @Autowired
    HistoricoService historicoService;

     @GetMapping()
    public ResponseEntity<List <HistoricoResponseRecordDto>> findAll(){
        return historicoService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<HistoricoResponseRecordDto> findById(@PathVariable(value = "id") Long id){
        return historicoService.findById(id);
    }

    @GetMapping("/filtro/{dataInicial}/{dataFinal}")
    public ResponseEntity<List<HistoricoResponseRecordDto>> buscarHistoricoPorIntervaloDeDatas(
            @PathVariable(value = "dataInicial") String dataInicial,
            @PathVariable(value = "dataFinal") String dataFinal,
            @RequestParam(value = "professorId", required = false) Long professorId,
            @RequestParam(value = "salaId", required = false) Long salaId,
            @RequestParam(value = "abriu", required = false) Boolean abriu) {

    return historicoService.buscarHistoricoComFiltro(dataInicial, dataFinal, professorId, salaId, abriu);
}
}