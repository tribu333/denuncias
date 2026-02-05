package com.registro.denuncias.dto.complain;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * DTO para resultados de búsqueda en tiempo real
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResultDTO {
    private Long id;
    private String complaintCode;
    private String workerFullName;
    private String department;
    private String complaintType;
    private String status;
    
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate incidentDate;
}