package com.cadeachave.cadeachave.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "professores_salas")
public class ProfessorSalaModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    private ProfessorModel professor;

    @ManyToOne
    @JoinColumn(name = "sala_id", referencedColumnName = "id")
    private SalaModel sala;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessorModel getProfessor() {
        return professor;
    }

    public void setProfessor(ProfessorModel professor) {
        this.professor = professor;
    }

    public SalaModel getSala() {
        return sala;
    }

    public void setSala(SalaModel sala) {
        this.sala = sala;
    }

    
}
