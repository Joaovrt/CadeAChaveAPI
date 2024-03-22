package com.cadeachave.cadeachave.models;

import java.io.Serializable;
import java.util.List;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;


@Entity
@Table(name = "professores", uniqueConstraints = @UniqueConstraint(columnNames = "cpf"))
public class ProfessorModel implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nome;

    @Column(nullable = false, unique = true, length = 11)
    private String cpf;

    @ManyToMany
    @JoinTable(
        name = "professores_salas",
        joinColumns = @JoinColumn(name = "professor_id"),
        inverseJoinColumns = @JoinColumn(name = "sala_id")
    )
    private List<SalaModel> salas;

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

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public List<SalaModel> getSalas() {
        return salas;
    }

    public void setSalas(List<SalaModel> salas) {
        this.salas = salas;
    }
}

