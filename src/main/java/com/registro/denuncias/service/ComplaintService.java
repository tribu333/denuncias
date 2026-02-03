package com.registro.denuncias.service;

import com.registro.denuncias.dto.ComplaintRequestDTO;
import com.registro.denuncias.dto.ComplaintResponseDTO;
import com.registro.denuncias.model.Complaint;
import com.registro.denuncias.repository.ComplaintRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}