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
}