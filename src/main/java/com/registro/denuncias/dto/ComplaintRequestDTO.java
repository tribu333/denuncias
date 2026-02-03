package com.registro.denuncias.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintRequestDTO {
    
    @NotBlank(message = "Complaint type is required")
    private String complaintType;
    
    @NotNull(message = "Incident date is required")
    @PastOrPresent(message = "Incident date cannot be in the future")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incidentDate;
    
    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "Worker full name is required")
    private String workerFullName;
    
    private String workerDescription;
    
    private String location;
    
    private String department;
    
    private String workerPosition;
    
}