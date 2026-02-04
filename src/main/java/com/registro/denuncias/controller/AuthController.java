package com.registro.denuncias.controller;

import com.registro.denuncias.dto.login.LoginResponseDTO;
import com.registro.denuncias.dto.login.UsuarioLoginDTO;
import com.registro.denuncias.dto.usuario.UsuarioRegistroDTO;
import com.registro.denuncias.dto.usuario.UsuarioResponseDTO;
import com.registro.denuncias.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;
    
    @PostMapping("/registro")
    public ResponseEntity<UsuarioResponseDTO> registrarUsuario(
            @Valid @RequestBody UsuarioRegistroDTO registroDTO) {
        
        UsuarioResponseDTO response = authService.registrarUsuario(registroDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(
            @Valid @RequestBody UsuarioLoginDTO loginDTO) {
        
        LoginResponseDTO response = authService.login(loginDTO);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/perfil")
    public ResponseEntity<UsuarioResponseDTO> obtenerPerfil() {
        UsuarioResponseDTO response = authService.getUsuarioActual();
        return ResponseEntity.ok(response);
    }
    
    // Endpoint para crear usuario administrador inicial
    @PostMapping("/admin-inicial")
    public ResponseEntity<UsuarioResponseDTO> crearAdminInicial() {
        UsuarioRegistroDTO adminDTO = UsuarioRegistroDTO.builder()
                .username("admin")
                .email("admin@denuncias.com")
                .password("admin123")
                .nombreCompleto("Administrador Sistema")
                .rol("ADMINISTRADOR")
                .build();
        
        UsuarioResponseDTO response = authService.registrarUsuario(adminDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}