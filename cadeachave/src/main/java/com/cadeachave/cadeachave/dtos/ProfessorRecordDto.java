package com.cadeachave.cadeachave.dtos;

import jakarta.validation.constraints.NotBlank;

public record ProfessorRecordDto(@NotBlank String nome, @NotBlank String cpf) {

}
