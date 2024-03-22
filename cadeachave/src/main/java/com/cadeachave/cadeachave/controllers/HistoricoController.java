package com.cadeachave.cadeachave.controllers;

import java.util.List;
import java.sql.Timestamp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.cadeachave.cadeachave.models.HistoricoModel;
import com.cadeachave.cadeachave.services.HistoricoService;

@RestController
@RequestMapping("/api/historico")
public class HistoricoController {
    
    @Autowired
    HistoricoService historicoService;

     @GetMapping()
    public ResponseEntity<List <HistoricoModel>> findAll(){
        return historicoService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<HistoricoModel> findById(@PathVariable(value = "id") Long id){
        return historicoService.findById(id);
    }

    @GetMapping("/filtro")
    public List<HistoricoModel> buscarHistoricoPorIntervaloDeDatas(
            @RequestParam("dataInicial") Timestamp dataInicial,
            @RequestParam("dataFinal") Timestamp dataFinal) {
        return historicoService.buscarRegistrosPorIntervaloDeDatas(dataInicial, dataFinal);
    }
}