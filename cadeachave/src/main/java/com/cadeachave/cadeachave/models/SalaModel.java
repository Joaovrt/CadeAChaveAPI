package com.cadeachave.cadeachave.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

@Entity
@Table(name = "salas", uniqueConstraints = @UniqueConstraint(columnNames = "nome"))
public class SalaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", nullable = false, length = 10, unique = true)
    private String nome;

    @Column(name = "status", nullable = false)
    private boolean status;
    
    @Column(name = "codigo", nullable = false, length = 250)
    private String codigo;
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public boolean isStatus() {
        return status;
    }
    
    public void setStatus(boolean status) {
        this.status = status;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }
}
