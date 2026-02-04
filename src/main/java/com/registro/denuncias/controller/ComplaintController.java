package com.registro.denuncias.controller;


import com.registro.denuncias.dto.complain.ComplaintRequestDTO;
import com.registro.denuncias.dto.complain.ComplaintResponseDTO;
import com.registro.denuncias.service.ComplaintService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@Tag(name = "Gestion de Denuncias", description = "registro de denuncia anonima")
public class ComplaintController {
    
    private final ComplaintService complaintService;
    
    @Operation(
        summary = "Registrar Denuncia",
        description = "se llena el formulario de la denuncia"
    )
    @PostMapping
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
        @Valid @RequestBody ComplaintRequestDTO complaintRequest) {
            
            ComplaintResponseDTO response = complaintService.createComplaint(complaintRequest);
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        }
        
    @Operation(
        summary = "Obtener denuncia",
        description = "obtener la denuncia en base al codigo de la denuncia"
    )
    @GetMapping("/{complaintCode}")
    public ResponseEntity<ComplaintResponseDTO> getComplaintByCode(
            @PathVariable String complaintCode) {
        
        ComplaintResponseDTO response = complaintService.getComplaintByCode(complaintCode);
        return ResponseEntity.ok(response);
    }
    
    // Simple health check endpoint
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Complaint system is running!");
    }
}