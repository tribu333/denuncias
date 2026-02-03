package com.registro.denuncias.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLoginDTO {
    
    @NotBlank(message = "Username es requerido")
    private String username;
    
    @NotBlank(message = "Password es requerido")
    private String password;
    
    private boolean recordar;
}