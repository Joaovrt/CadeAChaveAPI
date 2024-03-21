package com.cadeachave.cadeachave.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadeachave.cadeachave.dtos.SalaRecordDto;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.services.SalaService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/sala")
public class SalaController {

    @Autowired
    SalaService salaService;

    @PostMapping()
    public ResponseEntity<SalaModel> create(@RequestBody @Valid SalaRecordDto salaRecordDto){
        var salaModel = new SalaModel();
        BeanUtils.copyProperties(salaRecordDto, salaModel);
        return salaService.create(salaModel);
    }

    @GetMapping()
    public ResponseEntity<List <SalaModel>> findAll(){
        return salaService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<SalaModel> findById(@PathVariable(value = "id") Long id){
        return salaService.findById(id);
    }

    @GetMapping(value="/nome/{nome}")
    public ResponseEntity<SalaModel> findByNome(@PathVariable(value = "nome") String nome){
        return salaService.findByNome(nome);
    }

    @GetMapping(value="/nomeCom/{termo}")
    public ResponseEntity<List<SalaModel>> findByNomeContaining(@PathVariable(value = "termo") String termo){
        return salaService.findByNomeContaining(termo);
    }

    @GetMapping(value="/aberta/{aberta}")
    public ResponseEntity<List<SalaModel>> findByAberta(@PathVariable(value = "aberta") boolean aberta){
        return salaService.findByAberta(aberta);
    }

    @GetMapping(value="/nomeEAberta/{nome}/{aberta}")
    public ResponseEntity<List<SalaModel>> findByNomeContainingAndAberta(@PathVariable(value = "nome") String nome, @PathVariable(value = "aberta") boolean aberta) {
        return salaService.findByNomeContainingAndAberta(nome,aberta);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<SalaModel> update(@PathVariable(value = "id") Long id, @RequestBody @Valid SalaRecordDto salaRecordDto){
        var salaModel = new SalaModel();
        BeanUtils.copyProperties(salaRecordDto, salaModel);
        return salaService.update(salaModel, id);
    }

    @DeleteMapping(value="/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") Long id){
        return salaService.delete(id);
    }
}
