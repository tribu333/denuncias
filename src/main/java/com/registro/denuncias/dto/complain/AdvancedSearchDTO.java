package com.registro.denuncias.dto.complain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;


/**
 * DTO para búsqueda avanzada
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdvancedSearchDTO {
    private String department;
    private String complaintType;
    private String workerName;
    private String complaintCode;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
}