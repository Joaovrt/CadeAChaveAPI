package com.cadeachave.cadeachave.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import com.cadeachave.cadeachave.configuration.security.TokenService;
import com.cadeachave.cadeachave.dtos.AuthenticationDto;
import com.cadeachave.cadeachave.dtos.LoginResponseDto;
import com.cadeachave.cadeachave.dtos.RegisterDto;
import com.cadeachave.cadeachave.dtos.SalaRecordDto;
import com.cadeachave.cadeachave.exceptions.ResourceBadRequestException;
import com.cadeachave.cadeachave.exceptions.ResourceConflictException;
import com.cadeachave.cadeachave.exceptions.ResourceNotFoundException;
import com.cadeachave.cadeachave.models.ProfessorModel;
import com.cadeachave.cadeachave.models.SalaModel;
import com.cadeachave.cadeachave.models.UserModel;
import com.cadeachave.cadeachave.repositories.ProfessorRepository;
import com.cadeachave.cadeachave.repositories.UserRepository;

import jakarta.validation.Valid;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenService tokenService;
    @Autowired
    UserRepository userRepository;
    @Autowired 
    ProfessorRepository professorRepository;

    public ResponseEntity<UserModel> findById(String id){
        UserModel user = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum usuario encontrado com esse id."));
        user.setPassword(null);
        return ResponseEntity.status(HttpStatus.OK).body(user);
    }

    public Page<UserModel> findAll(Pageable pageable){
        Page<UserModel> users = userRepository.findAll(pageable);
        for(int i=0;i<users.getNumberOfElements();i++)
            users.getContent().get(i).setPassword(null);
        return users;
    }

    public ResponseEntity<UserModel> update (RegisterDto registerDto, String id){
        try{
            var entity = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum usuario encontrado com esse id."));
            ProfessorModel professor = null;
            if(registerDto.professor_id()!=null){
                professor = professorRepository.findById(registerDto.professor_id()).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id: "+registerDto.professor_id()));
                var userWithProfessor = userRepository.findByProfessor(professor);
                if(userWithProfessor!=null&&userWithProfessor.getId()!=id)
                    throw new ResourceBadRequestException("Professor ja cadastrado como usuario.");
            }
            entity.setProfessor(professor);
            entity.setLogin(registerDto.login());
            if(registerDto.password()!=entity.getPassword()){
                String encryptedPassword = new BCryptPasswordEncoder().encode(registerDto.password());
                entity.setPassword(encryptedPassword);
            }
            entity.setRole(registerDto.role());
            var savedUser = userRepository.save(entity);
            savedUser.setPassword(null); 
            return ResponseEntity.status(HttpStatus.OK).body(savedUser);
        }
        catch (DataIntegrityViolationException e) {
            throw new ResourceConflictException("Outro usuario já está cadastrado com esse login: " + registerDto.login());
        }
    }

    public ResponseEntity<Object> delete (String id){
        var entity = userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Nenhum usuario encontrado com esse id."));
        userRepository.delete(entity);
        return ResponseEntity.status(HttpStatus.OK).body("Usuario deletada.");
    }

    public ResponseEntity<Object> login(AuthenticationDto data){
         var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDto(token));
    }

    public ResponseEntity<Object> register(RegisterDto data){
        if(this.userRepository.findByLogin(data.login()) != null) throw new ResourceBadRequestException("Usuario ja cadastrado.");
        ProfessorModel professor = null;
        if(data.professor_id()!=null){
            professor = professorRepository.findById(data.professor_id()).orElseThrow(()-> new ResourceNotFoundException("Nenhum professor encontrado com esse id: "+data.professor_id()));
            if(this.userRepository.findByProfessor(professor) != null) throw new ResourceBadRequestException("Professor ja cadastrado como usuario.");
        }
        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        UserModel newUser = new UserModel(data.login(), encryptedPassword, data.role(), professor);

        UserModel userCreated =  this.userRepository.save(newUser);
        userCreated.setPassword(null);
        return ResponseEntity.ok().body(userCreated);
    }
}
