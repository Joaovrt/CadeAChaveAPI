package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.cadeachave.cadeachave.dtos.SalaRecordDto;
import com.cadeachave.cadeachave.exceptions.ResourceConflictException;
import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.exceptions.ResourceUnauthorizedException;
import com.cadeachave.cadeachave.models.HistoricoModel;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.repositories.ProfessorRepository;
import com.cadeachave.cadeachave.repositories.SalaRepository;


import java.util.List;
import java.util.logging.Logger;

@Service
public class SalaService {
    private Logger logger = Logger.getLogger(SalaService.class.getName());

    @Autowired
    SalaRepository salaRepository;

    @Autowired
    ProfessorRepository professorRepository;

    @Autowired
    HistoricoService historicoService;

    public ResponseEntity<SalaModel> findById(Long id){

        logger.info("Buscando sala...");

        SalaModel sala = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<SalaModel> findByNome(String nome){

        logger.info("Buscando sala...");

        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse nome.");
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContaining(String termo){

        logger.info("Buscando salas...");

       List<SalaModel> salaList = salaRepository.findByNomeContaining(termo);
        if(salaList.isEmpty())
            throw new ResourceNotFoundException("Nenhum professor encontrado com o CPF ou nome correspondente.");
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByAberta(boolean aberta){

        logger.info("Buscando salas...");

        List<SalaModel> salaList = salaRepository.findByAberta(aberta);
        if(salaList.isEmpty())
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse aberta.");
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContainingAndAberta(String nome, boolean aberta) {
        logger.info("Buscando sala por nome e aberta...");

        List<SalaModel> salaList = salaRepository.findByNomeContainingAndAberta(nome, aberta);
        if (salaList.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma sala encontrada com o nome e status correspondente.");
        }
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findAll(){

        logger.info("Buscando salas...");

        List<SalaModel> salaList = salaRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }
    
    public ResponseEntity<SalaModel> create (SalaRecordDto salaDto){

        logger.info("Cadastrando sala.");
        SalaModel sala = new SalaModel();
        sala.setNome(salaDto.nome());
        sala.setAberta(salaDto.aberta());
       
        return ResponseEntity.status(HttpStatus.CREATED).body(salaRepository.save(sala));
    }

    public ResponseEntity<SalaModel> update (SalaRecordDto salaDto, Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        entity.setNome(salaDto.nome());
        entity.setAberta(salaDto.aberta());
       
        logger.info("Atualizando sala.");
        return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(entity));
    }

    public ResponseEntity<Object> delete (Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        salaRepository.delete(entity);
        logger.info("Deletando sala.");
        return ResponseEntity.status(HttpStatus.OK).body("Sala deletada.");
    }

    public ResponseEntity<HistoricoModel> Abrir(String cpf, String nome){
        ProfessorModel professor = professorRepository.findByCpf(cpf);
        if(professor==null)
            throw new ResourceNotFoundException("Nenhum professor encontrado com esse CPF.");
        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse nome.");
        if(professor.getSalas().contains(sala)){
            if(sala.isAberta())
                throw new ResourceConflictException("Sala já está aberta.");
            else{
                sala.setAberta(true);
                salaRepository.save(sala);
                return historicoService.create(professor, sala, true);
            }
        }
        else
            throw new ResourceUnauthorizedException("Acesso negado!");
    }

    public ResponseEntity<HistoricoModel> Fechar(String cpf, String nome){
        ProfessorModel professor = professorRepository.findByCpf(cpf);
        if(professor==null)
            throw new ResourceNotFoundException("Nenhum professor encontrado com esse CPF.");
        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse nome.");
        if(professor.getSalas().contains(sala)){
            if(!sala.isAberta())
                throw new ResourceConflictException("Sala já está fechada.");
            else{
                if(historicoService.validaUltimoAAbrir(professor, sala)){
                    sala.setAberta(false);
                    salaRepository.save(sala);
                    return historicoService.create(professor, sala, false);
                }
                else
                    throw new ResourceUnauthorizedException("Acesso negado!");
            }
        }
        else
            throw new ResourceUnauthorizedException("Acesso negado!");
    }
}