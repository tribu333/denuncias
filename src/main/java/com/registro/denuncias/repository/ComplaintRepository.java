package com.registro.denuncias.repository;

import com.registro.denuncias.model.Complaint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    Optional<Complaint> findByComplaintCode(String complaintCode);
    
    boolean existsByComplaintCode(String complaintCode);
    
    // Métodos para filtros
    Page<Complaint> findByDepartment(String department, Pageable pageable);
    
    Page<Complaint> findByComplaintType(String complaintType, Pageable pageable);
    
    Page<Complaint> findByDepartmentAndComplaintType(String department, String complaintType, Pageable pageable);
    
    // Búsqueda por nombre de trabajador
    Page<Complaint> findByWorkerFullNameContainingIgnoreCase(String workerName, Pageable pageable);
    
    // Búsqueda por fecha
    Page<Complaint> findByIncidentDateBetween(java.time.LocalDate startDate, java.time.LocalDate endDate, Pageable pageable);
    
    // Métodos para estadísticas
    @Query("SELECT COUNT(c) FROM Complaint c")
    long countAll();
    
    @Query("SELECT COUNT(c) FROM Complaint c WHERE c.status = :status")
    long countByStatus(@Param("status") String status);
    
    // Obtener conteo por departamento
    @Query("SELECT c.department, COUNT(c) FROM Complaint c GROUP BY c.department")
    List<Object[]> countByDepartment();
    
    // Obtener conteo por tipo
    @Query("SELECT c.complaintType, COUNT(c) FROM Complaint c GROUP BY c.complaintType")
    List<Object[]> countByComplaintType();
    
    // Búsqueda avanzada
    @Query("SELECT c FROM Complaint c WHERE " +
           "(:department IS NULL OR c.department = :department) AND " +
           "(:complaintType IS NULL OR c.complaintType = :complaintType) AND " +
           "(:workerName IS NULL OR LOWER(c.workerFullName) LIKE LOWER(CONCAT('%', :workerName, '%')))")
    Page<Complaint> searchComplaints(
            @Param("department") String department,
            @Param("complaintType") String complaintType,
            @Param("workerName") String workerName,
            Pageable pageable);

           // Búsqueda por código de denuncia (parcial)
    List<Complaint> findByComplaintCodeContainingIgnoreCase(String complaintCode);
        // Búsqueda combinada por código y nombre
    List<Complaint> findByComplaintCodeContainingIgnoreCaseOrWorkerFullNameContainingIgnoreCase(
            String complaintCode, String workerName);
                // Búsqueda avanzada con JPQL para autocompletado
    @Query("SELECT c FROM Complaint c WHERE " +
           "LOWER(c.complaintCode) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(c.workerFullName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<Complaint> searchByCodeOrWorkerName(@Param("searchTerm") String searchTerm);
    // Búsqueda para autocompletado (limitado a 10 resultados)
    @Query("SELECT DISTINCT c.workerFullName FROM Complaint c WHERE " +
           "LOWER(c.workerFullName) LIKE LOWER(CONCAT(:prefix, '%')) " +
           "ORDER BY c.workerFullName")
    List<String> findWorkerNamesStartingWith(@Param("prefix") String prefix, Pageable pageable);

        // Búsqueda para autocompletado de códigos
    @Query("SELECT DISTINCT c.complaintCode FROM Complaint c WHERE " +
           "LOWER(c.complaintCode) LIKE LOWER(CONCAT(:prefix, '%')) " +
           "ORDER BY c.complaintCode")
    List<String> findComplaintCodesStartingWith(@Param("prefix") String prefix, Pageable pageable);
}