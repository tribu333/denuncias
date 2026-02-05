package com.registro.denuncias.controller;

import com.registro.denuncias.dto.complain.*;
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
    
    // ========== ENDPOINTS EXISTENTES ==========
    
    @PostMapping
    @Operation(summary = "Crear una nueva denuncia")
    public ResponseEntity<ComplaintResponseDTO> createComplaint(
            @Valid @RequestBody ComplaintRequestDTO requestDTO) {
        
        ComplaintResponseDTO response = complaintService.createComplaint(requestDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{complaintCode}")
    @Operation(summary = "Obtener denuncia por código")
    public ResponseEntity<ComplaintResponseDTO> getComplaintByCode(
            @PathVariable String complaintCode) {
        
        ComplaintResponseDTO response = complaintService.getComplaintByCode(complaintCode);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/all")
    @Operation(summary = "Obtener todas las denuncias (sin paginación)")
    public ResponseEntity<List<ComplaintResponseDTO>> getAllComplaints() {
        
        List<ComplaintResponseDTO> complaints = complaintService.getAllComplaints();
        return ResponseEntity.ok(complaints);
    }
    
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
    
    // ========== BÚSQUEDA EN TIEMPO REAL ==========
    
    @GetMapping("/search/real-time")
    @Operation(summary = "Búsqueda en tiempo real por código o nombre")
    public ResponseEntity<List<SearchResultDTO>> searchRealTime(
            @Parameter(description = "Término de búsqueda (código o nombre)", required = true)
            @RequestParam String q) {
        
        List<SearchResultDTO> results = complaintService.searchRealTime(q);
        return ResponseEntity.ok(results);
    }
    
    @GetMapping("/autocomplete/worker-names")
    @Operation(summary = "Autocompletado de nombres de trabajadores")
    public ResponseEntity<List<String>> autocompleteWorkerNames(
            @Parameter(description = "Prefijo para autocompletado", required = true)
            @RequestParam String prefix) {
        
        List<String> suggestions = complaintService.autocompleteWorkerNames(prefix);
        return ResponseEntity.ok(suggestions);
    }
    
    @GetMapping("/autocomplete/complaint-codes")
    @Operation(summary = "Autocompletado de códigos de denuncia")
    public ResponseEntity<List<String>> autocompleteComplaintCodes(
            @Parameter(description = "Prefijo para autocompletado", required = true)
            @RequestParam String prefix) {
        
        List<String> suggestions = complaintService.autocompleteComplaintCodes(prefix);
        return ResponseEntity.ok(suggestions);
    }
    
    // ========== BÚSQUEDAS ESPECÍFICAS ==========
    
    @GetMapping("/search/by-worker")
    @Operation(summary = "Buscar denuncias por nombre de trabajador")
    public ResponseEntity<Page<ComplaintResponseDTO>> searchByWorkerName(
            @Parameter(description = "Nombre del trabajador", required = true)
            @RequestParam String name,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        Page<ComplaintResponseDTO> result = complaintService.searchByWorkerName(name, pageable);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/search/by-code")
    @Operation(summary = "Buscar denuncias por código")
    public ResponseEntity<Page<ComplaintResponseDTO>> searchByComplaintCode(
            @Parameter(description = "Código de denuncia", required = true)
            @RequestParam String code,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        Page<ComplaintResponseDTO> result = complaintService.searchByComplaintCode(code, pageable);
        
        return ResponseEntity.ok(result);
    }
    
    // ========== BÚSQUEDA AVANZADA ==========
    
    @PostMapping("/search/advanced")
    @Operation(summary = "Búsqueda avanzada con múltiples filtros")
    public ResponseEntity<Page<ComplaintResponseDTO>> advancedSearch(
            @RequestBody AdvancedSearchDTO searchDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        
        Page<ComplaintResponseDTO> result = complaintService.advancedSearch(
                searchDTO.getDepartment(),
                searchDTO.getComplaintType(),
                searchDTO.getWorkerName(),

                pageable);
        
        return ResponseEntity.ok(result);
    }
    
    @GetMapping("/search/advanced/quick")
    @Operation(summary = "Búsqueda avanzada rápida (query params)")
    public ResponseEntity<Page<ComplaintResponseDTO>> advancedSearchQuick(
            @Parameter(description = "Departamento")
            @RequestParam(required = false) String department,
            
            @Parameter(description = "Tipo de denuncia")
            @RequestParam(required = false) String complaintType,
            
            @Parameter(description = "Nombre del trabajador")
            @RequestParam(required = false) String workerName,
            
            @Parameter(description = "Código de denuncia")
            @RequestParam(required = false) String complaintCode,
            
            @Parameter(description = "Estado")
            @RequestParam(required = false) String status,
            
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        
        Page<ComplaintResponseDTO> result = complaintService.advancedSearch(
                department, complaintType, workerName,  pageable);
        
        return ResponseEntity.ok(result);
    }
    
    // ========== ENDPOINTS EXISTENTES (actualizados) ==========
    
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
        
        Page<ComplaintResponseDTO> result = complaintService.advancedSearch(
                department, complaintType, workerName, pageable);
        
        return ResponseEntity.ok(result);
    }
    
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
    
    @GetMapping("/department/{department}")
    @Operation(summary = "Obtener denuncias por departamento")
    public ResponseEntity<Page<ComplaintResponseDTO>> getByDepartment(
            @PathVariable String department,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "submittedAt"));
        
        Page<ComplaintResponseDTO> result = complaintService.advancedSearch(
                department, null, null, pageable);
        
        return ResponseEntity.ok(result);
    }
}