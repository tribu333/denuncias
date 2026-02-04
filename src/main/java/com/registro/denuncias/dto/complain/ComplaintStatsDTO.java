package com.registro.denuncias.dto.complain;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComplaintStatsDTO {
    private Long total;
    private Long pending;
    private Long resolved;
}
