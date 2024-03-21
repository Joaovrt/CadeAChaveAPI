package com.cadeachave.cadeachave.dtos;

import org.hibernate.validator.constraints.br.CPF;

import jakarta.validation.constraints.NotBlank;

public record ProfessorRecordDto(@NotBlank String nome, @NotBlank @CPF String cpf) {

}
