package com.registro.denuncias.service;

import com.registro.denuncias.dto.complain.ComplaintRequestDTO;
import com.registro.denuncias.dto.complain.ComplaintResponseDTO;
import com.registro.denuncias.dto.complain.ComplaintStatsDTO;
import com.registro.denuncias.dto.complain.SearchResultDTO;
import com.registro.denuncias.model.Complaint;
import com.registro.denuncias.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComplaintService {
    
    private final ComplaintRepository complaintRepository;
    
    @Transactional
    public ComplaintResponseDTO createComplaint(ComplaintRequestDTO requestDTO) {
        log.info("Creating new anonymous complaint");
        
        // Convert DTO to Entity
        Complaint complaint = Complaint.builder()
                .complaintType(requestDTO.getComplaintType())
                .incidentDate(requestDTO.getIncidentDate())
                .description(requestDTO.getDescription())
                .workerFullName(requestDTO.getWorkerFullName())
                .workerDescription(requestDTO.getWorkerDescription())
                .location(requestDTO.getLocation())
                .department(requestDTO.getDepartment())
                .workerPosition(requestDTO.getWorkerPosition())
                .build();
        
        // Save the complaint
        Complaint savedComplaint = complaintRepository.save(complaint);
        log.info("Complaint created successfully with code: {}", savedComplaint.getComplaintCode());
        
        // Convert Entity to Response DTO
        return mapToResponseDTO(savedComplaint);
    }
    
    public ComplaintResponseDTO getComplaintByCode(String complaintCode) {
        log.info("Fetching complaint with code: {}", complaintCode);
        
        Complaint complaint = complaintRepository.findByComplaintCode(complaintCode)
                .orElseThrow(() -> new RuntimeException("Complaint not found with code: " + complaintCode));
        
        return mapToResponseDTO(complaint);
    }
     // Obtener todas las denuncias (sin paginación)
    public List<ComplaintResponseDTO> getAllComplaints() {
        log.info("Fetching all complaints");
        
        List<Complaint> complaints = complaintRepository.findAll();
        
        return complaints.stream()
                .map(this::mapToResponseDTO)
                .collect(Collectors.toList());
    }
    
    // Obtener denuncias con paginación
    public Page<ComplaintResponseDTO> getComplaintsPage(Pageable pageable) {
        log.info("Fetching complaints with pagination - page: {}, size: {}", 
                pageable.getPageNumber(), pageable.getPageSize());
        
        Page<Complaint> complaintsPage = complaintRepository.findAll(pageable);
        
        return complaintsPage.map(this::mapToResponseDTO);
    }
    
    // Obtener denuncias con filtros opcionales
    public Page<ComplaintResponseDTO> getComplaintsWithFilters(
            String department, 
            String complaintType, 
            Pageable pageable) {
        
        log.info("Fetching complaints with filters - department: {}, type: {}", 
                department, complaintType);
        
        Page<Complaint> complaintsPage;
        
        if (department != null && complaintType != null) {
            complaintsPage = complaintRepository
                    .findByDepartmentAndComplaintType(department, complaintType, pageable);
        } else if (department != null) {
            complaintsPage = complaintRepository
                    .findByDepartment(department, pageable);
        } else if (complaintType != null) {
            complaintsPage = complaintRepository
                    .findByComplaintType(complaintType, pageable);
        } else {
            complaintsPage = complaintRepository.findAll(pageable);
        }
        
        return complaintsPage.map(this::mapToResponseDTO);
    }
    
    // Obtener estadísticas
    public ComplaintStatsDTO getComplaintStats() {
        long total = complaintRepository.count();
        long pending = complaintRepository.countByStatus("PENDING");
        long resolved = complaintRepository.countByStatus("RESOLVED");
        
        return ComplaintStatsDTO.builder()
                .total(total)
                .pending(pending)
                .resolved(resolved)
                .build();
    }
    // Simple mapper method (no MapStruct needed for now)
    private ComplaintResponseDTO mapToResponseDTO(Complaint complaint) {
        return ComplaintResponseDTO.builder()
                .id(complaint.getId())
                .complaintCode(complaint.getComplaintCode())
                .complaintType(complaint.getComplaintType())
                .incidentDate(complaint.getIncidentDate())
                .description(complaint.getDescription())
                .workerFullName(complaint.getWorkerFullName())
                .workerDescription(complaint.getWorkerDescription())
                .location(complaint.getLocation())
                .department(complaint.getDepartment())
                .workerPosition(complaint.getWorkerPosition())
                .submittedAt(complaint.getSubmittedAt())
                .updatedAt(complaint.getUpdatedAt())
                .build();
    }
        private SearchResultDTO mapToSearchResultDTO(Complaint complaint) {
        return SearchResultDTO.builder()
                .id(complaint.getId())
                .complaintCode(complaint.getComplaintCode())
                .workerFullName(complaint.getWorkerFullName())
                .department(complaint.getDepartment())
                .complaintType(complaint.getComplaintType())
                .status(complaint.getStatus())
                .incidentDate(complaint.getIncidentDate())
                .build();
    }
        // ========== BÚSQUEDAS EN TIEMPO REAL ==========
    
    /**
     * Búsqueda general por término (código o nombre)
     */
    public List<SearchResultDTO> searchRealTime(String searchTerm) {
        log.info("Real-time search for: {}", searchTerm);
        
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return List.of();
        }
        
        // Limitar resultados para respuesta rápida
        Pageable limit = PageRequest.of(0, 10);
        
        List<Complaint> complaints = complaintRepository.searchByCodeOrWorkerName(searchTerm.trim());
        
        return complaints.stream()
                .limit(10) // Máximo 10 resultados para tiempo real
                .map(this::mapToSearchResultDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Autocompletado para nombres de trabajadores
     */
    public List<String> autocompleteWorkerNames(String prefix) {
        log.info("Autocomplete worker names starting with: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        Pageable limit = PageRequest.of(0, 10);
        return complaintRepository.findWorkerNamesStartingWith(prefix.trim(), limit);
    }
    
    /**
     * Autocompletado para códigos de denuncia
     */
    public List<String> autocompleteComplaintCodes(String prefix) {
        log.info("Autocomplete complaint codes starting with: {}", prefix);
        
        if (prefix == null || prefix.trim().isEmpty()) {
            return List.of();
        }
        
        Pageable limit = PageRequest.of(0, 10);
        return complaintRepository.findComplaintCodesStartingWith(prefix.trim(), limit);
    }
    
    /**
     * Búsqueda específica por nombre de trabajador
     */
    public Page<ComplaintResponseDTO> searchByWorkerName(String workerName, Pageable pageable) {
        log.info("Searching complaints by worker name: {}", workerName);
        
        Page<Complaint> complaints = complaintRepository
                .findByWorkerFullNameContainingIgnoreCase(workerName, pageable);
        
        return complaints.map(this::mapToResponseDTO);
    }
    
    /**
     * Búsqueda específica por código de denuncia
     */
public Page<ComplaintResponseDTO> searchByComplaintCode(String complaintCode, Pageable pageable) {
        log.info("Searching complaints by code: {}", complaintCode);
        
        // Usar el método con paginación que creamos en el repository
        Page<Complaint> complaints = complaintRepository
                .findByComplaintCodeContainingIgnoreCase(complaintCode, pageable);
        
        return complaints.map(this::mapToResponseDTO);
    }
    
    /**
     * Búsqueda avanzada con múltiples filtros
     */
    public Page<ComplaintResponseDTO> advancedSearch(
            String department,
            String complaintType,
            String workerName,
            Pageable pageable) {
        
        log.info("Advanced search - department: {}, type: {}, worker: {}, code: {}", 
                department, complaintType, workerName);
        
        Page<Complaint> complaints = complaintRepository.searchComplaints(
                department, complaintType, workerName, pageable);
        
        return complaints.map(this::mapToResponseDTO);
    }
    
}