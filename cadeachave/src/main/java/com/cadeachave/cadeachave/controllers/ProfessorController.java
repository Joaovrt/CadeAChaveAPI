package com.cadeachave.cadeachave.controllers;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cadeachave.cadeachave.dtos.ProfessorRecordDto;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.services.ProfessorService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/professor/v1")
public class ProfessorController {

    @Autowired
    ProfessorService professorService;

    @PostMapping()
    public ResponseEntity<ProfessorModel> create(@RequestBody @Valid ProfessorRecordDto professorRecordDto){
        var professorModel = new ProfessorModel();
        BeanUtils.copyProperties(professorRecordDto, professorModel);
        return professorService.create(professorModel);
    }

    @GetMapping()
    public ResponseEntity<List <ProfessorModel>> findAll(){
        return professorService.findAll();
    }

    @GetMapping(value="/{id}")
    public ResponseEntity<ProfessorModel> findById(@PathVariable(value = "id") Long id){
        return professorService.findById(id);
    }

    @PutMapping(value="/{id}")
    public ResponseEntity<ProfessorModel> update(@PathVariable(value = "id") Long id, @RequestBody @Valid ProfessorRecordDto professorRecordDto){
        var professorModel = new ProfessorModel();
        BeanUtils.copyProperties(professorRecordDto, professorModel);
        return professorService.update(professorModel, id);
    }
}
