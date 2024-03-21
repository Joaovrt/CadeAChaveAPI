package com.cadeachave.cadeachave.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.sql.Timestamp;

import org.springframework.hateoas.RepresentationModel;

@Entity
@Table(name = "historico")
public class HistoricoModel extends RepresentationModel<HistoricoModel> implements Serializable{

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "professores_salas_id", referencedColumnName = "id")
    private ProfessorSalaModel professorSala;

    @Column(name = "horario")
    private Timestamp horario;

    @Column(name = "acao", length = 1)
    private char acao;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ProfessorSalaModel getProfessorSala() {
        return professorSala;
    }

    public void setProfessorSala(ProfessorSalaModel professorSala) {
        this.professorSala = professorSala;
    }

    public Timestamp getHorario() {
        return horario;
    }

    public void setHorario(Timestamp horario) {
        this.horario = horario;
    }

    public char getAcao() {
        return acao;
    }

    public void setAcao(char acao) {
        this.acao = acao;
    }
}
