package com.registro.denuncias.controller;

import com.registro.denuncias.dto.complain.ComplaintRequestDTO;
import com.registro.denuncias.dto.complain.ComplaintResponseDTO;
import com.registro.denuncias.service.ComplaintService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/complaints")
@RequiredArgsConstructor
@Tag(name = "Denuncias", description = "Endpoints para gestión de denuncias")
public class ComplaintController {
    
    private final ComplaintService complaintService;
    
    // ========== CREAR DENUNCIA ==========
    @PostMapping
    @Operation(summary = "Crear una nueva denuncia")
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
            @Valid @RequestBody ComplaintRequestDTO requestDTO) {
        
        ComplaintResponseDTO response = complaintService.createComplaint(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    // ========== OBTENER POR CÓDIGO ==========
    @GetMapping("/{complaintCode}")
    @Operation(summary = "Obtener denuncia por código")
    public ResponseEntity<ComplaintResponseDTO> getComplaintByCode(
            @PathVariable String complaintCode) {
        
        ComplaintResponseDTO response = complaintService.getComplaintByCode(complaintCode);
        return ResponseEntity.ok(response);
    }
    
    // ========== OBTENER TODAS (sin paginación) ==========
    @GetMapping("/all")
    @Operation(summary = "Obtener todas las denuncias (sin paginación)")
    public ResponseEntity<List<ComplaintResponseDTO>> getAllComplaints() {
        
        List<ComplaintResponseDTO> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }
    
    // ========== OBTENER CON PAGINACIÓN ==========
    @GetMapping
    @Operation(summary = "Obtener denuncias con paginación")
    public ResponseEntity<Page<ComplaintResponseDTO>> getComplaintsPage(
            @Parameter(description = "Número de página (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Tamaño de página", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Campo para ordenar", example = "submittedAt")
            @RequestParam(defaultValue = "submittedAt") String sortBy,
            
            @Parameter(description = "Dirección de ordenamiento", example = "desc")
            @RequestParam(defaultValue = "desc") String direction) {
        
        Sort.Direction sortDirection = "asc".equalsIgnoreCase(direction) 
                ? Sort.Direction.ASC 
                : Sort.Direction.DESC;
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        
        Page<ComplaintResponseDTO> complaintsPage = complaintService.getComplaintsPage(pageable);
        
        return ResponseEntity.ok(complaintsPage);
    }
    
    // ========== OBTENER CON FILTROS ==========
    @GetMapping("/filter")
    @Operation(summary = "Obtener denuncias con filtros")
    public ResponseEntity<Page<ComplaintResponseDTO>> getComplaintsWithFilters(
            @Parameter(description = "Departamento")
            @RequestParam(required = false) String department,
            
            @Parameter(description = "Tipo de denuncia")
            @RequestParam(required = false) String complaintType,
            
            @Parameter(description = "Nombre del trabajador (búsqueda parcial)")
            @RequestParam(required = false) String workerName,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        
        Page<ComplaintResponseDTO> complaintsPage;
        
        if (department != null || complaintType != null || workerName != null) {
            // Para búsqueda avanzada necesitarías un método adicional en el servicio
            complaintsPage = complaintService.getComplaintsWithFilters(department, complaintType, pageable);
        } else {
            complaintsPage = complaintService.getComplaintsPage(pageable);
        }
        
        return ResponseEntity.ok(complaintsPage);
    }
    
    // ========== ESTADÍSTICAS ==========
    @GetMapping("/stats")
    @Operation(summary = "Obtener estadísticas de denuncias")
    public ResponseEntity<Map<String, Object>> getComplaintStats() {
        var stats = complaintService.getComplaintStats();
        
        Map<String, Object> response = Map.of(
            "total", stats.getTotal(),
            "pending", stats.getPending(),
            "resolved", stats.getResolved()
        );
        
        return ResponseEntity.ok(response);
    }
    
    // ========== BUSCAR POR NOMBRE DE TRABAJADOR ==========
    @GetMapping("/search/worker")
    @Operation(summary = "Buscar denuncias por nombre de trabajador")
    public ResponseEntity<Page<ComplaintResponseDTO>> searchByWorkerName(
            @Parameter(description = "Nombre del trabajador", required = true)
            @RequestParam String name,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size);
        
        // Necesitarías implementar este método en el servicio
        // Page<ComplaintResponseDTO> result = complaintService.searchByWorkerName(name, pageable);
        
        // Temporalmente devuelve todas
        Page<ComplaintResponseDTO> result = complaintService.getComplaintsPage(pageable);
        
        return ResponseEntity.ok(result);
    }
    
    // ========== OBTENER POR DEPARTAMENTO ==========
    @GetMapping("/department/{department}")
    @Operation(summary = "Obtener denuncias por departamento")
    public ResponseEntity<Page<ComplaintResponseDTO>> getByDepartment(
            @PathVariable String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        
        // Implementar en servicio
        Page<ComplaintResponseDTO> result = complaintService.getComplaintsPage(pageable);
        
        return ResponseEntity.ok(result);
    }
}