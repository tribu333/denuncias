package com.registro.denuncias.dto.complain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


/**
 * DTO para autocompletado
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
class AutocompleteResponseDTO {
    private List<String> suggestions;
}