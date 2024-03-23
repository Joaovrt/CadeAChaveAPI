package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
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
        SalaModel sala = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<SalaModel> findByNome(String nome){
        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com esse nome: "+nome);
        return ResponseEntity.status(HttpStatus.OK).body(sala);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContaining(String termo){
       List<SalaModel> salaList = salaRepository.findByNomeContaining(termo);
        if(salaList.isEmpty())
            throw new ResourceNotFoundException("Nenhuma sala encontrada com o nome contendo: "+termo);
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByAberta(boolean aberta){
        List<SalaModel> salaList = salaRepository.findByAberta(aberta);
        if(salaList.isEmpty())
            throw new ResourceNotFoundException("Nenhuma sala encontrada "+ (aberta ? "aberta" : "fechada"));
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findByNomeContainingAndAberta(String nome, boolean aberta) {
        List<SalaModel> salaList = salaRepository.findByNomeContainingAndAberta(nome, aberta);
        if (salaList.isEmpty()) {
            throw new ResourceNotFoundException("Nenhuma sala encontrada com contendo o nome: "+nome+" e status correspondente a "+(aberta ? "aberta" : "fechada"));
        }
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }

    public ResponseEntity<List<SalaModel>> findAll(){
        List<SalaModel> salaList = salaRepository.findAll();
        return ResponseEntity.status(HttpStatus.OK).body(salaList);
    }
    
    public ResponseEntity<SalaModel> create (SalaRecordDto salaDto){
        try{
            SalaModel sala = new SalaModel();
            sala.setNome(salaDto.nome());
            sala.setAberta(salaDto.aberta());
        
            return ResponseEntity.status(HttpStatus.CREATED).body(salaRepository.save(sala));
        }
        catch (DataIntegrityViolationException e) {
            throw new ResourceConflictException("Outro sala já está cadastrada com esse nome: " + salaDto.nome());
        }
    }

    public ResponseEntity<SalaModel> update (SalaRecordDto salaDto, Long id){
        try{
            var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
            entity.setNome(salaDto.nome());
            entity.setAberta(salaDto.aberta());
            return ResponseEntity.status(HttpStatus.OK).body(salaRepository.save(entity));
        }
        catch (DataIntegrityViolationException e) {
            throw new ResourceConflictException("Outro sala já está cadastrada com esse nome: " + salaDto.nome());
        }
    }

    public ResponseEntity<Object> delete (Long id){
        var entity = salaRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhuma sala encontrada com esse id."));
        salaRepository.delete(entity);
        return ResponseEntity.status(HttpStatus.OK).body("Sala deletada.");
    }

    public ResponseEntity<HistoricoModel> Abrir(String cpf, String nome){
        ProfessorModel professor = professorRepository.findByCpf(cpf);
        if(professor==null)
            throw new ResourceNotFoundException("Nenhum professor encontrado com o CPF: "+cpf);
        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
            throw new ResourceNotFoundException("Nenhuma sala encontrada com o nome: " +nome);
        if(professor.getSalas().contains(sala)){
            if(sala.isAberta())
                throw new ResourceConflictException("Sala "+nome+" já está aberta.");
            else{
                sala.setAberta(true);
                salaRepository.save(sala);
                return historicoService.create(professor, sala, true);
            }
        }
        else
            throw new ResourceUnauthorizedException("Professor com cpf: "+cpf+" não tem permissão de acesso à sala "+nome);
    }

    public ResponseEntity<HistoricoModel> Fechar(String cpf, String nome){
        ProfessorModel professor = professorRepository.findByCpf(cpf);
        if(professor==null)
            throw new ResourceNotFoundException("Nenhum professor encontrado com o CPF: "+cpf);
        SalaModel sala = salaRepository.findByNome(nome);
        if(sala==null)
        throw new ResourceNotFoundException("Nenhuma sala encontrada com o nome: " +nome);
        if(professor.getSalas().contains(sala)){
            if(!sala.isAberta())
            throw new ResourceConflictException("Sala "+nome+" já está fechada.");
            else{
                if(historicoService.validaUltimoAAbrir(professor, sala)){
                    sala.setAberta(false);
                    salaRepository.save(sala);
                    return historicoService.create(professor, sala, false);
                }
                else
                throw new ResourceUnauthorizedException("Professor com cpf: "+cpf+" não foi o último a abrir a sala "+nome);
            }
        }
        else
        throw new ResourceUnauthorizedException("Professor com cpf: "+cpf+" não tem permissão de acesso à sala "+nome);
    }
}