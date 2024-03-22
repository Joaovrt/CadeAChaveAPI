package com.cadeachave.cadeachave.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.io.Serializable;
import java.sql.Timestamp;


@Entity
@Table(name = "historico")
public class HistoricoModel implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professor_id", referencedColumnName = "id")
    private ProfessorModel professor;

    @ManyToOne
    @JoinColumn(name = "sala_id", referencedColumnName = "id")
    private SalaModel sala;

    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp horario;

    private boolean abriu;

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

    public Timestamp getHorario() {
        return horario;
    }

    public void setHorario(Timestamp horario) {
        this.horario = horario;
    }

    public boolean isAbriu() {
        return abriu;
    }

    public void setAbriu(boolean abriu) {
        this.abriu = abriu;
    }

}
