package com.registro.denuncias.repository;

import com.registro.denuncias.model.Complaint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComplaintRepository extends JpaRepository<Complaint, Long> {
    
    Optional<Complaint> findByComplaintCode(String complaintCode);
    
    boolean existsByComplaintCode(String complaintCode);
}