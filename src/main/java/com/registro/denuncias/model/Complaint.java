package com.registro.denuncias.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
/* import java.util.ArrayList;
import java.util.List; */
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
//@ToString(exclude = {"statusUpdates"})
public class Complaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "complaint_code", unique = true, nullable = false)
    private String complaintCode;

/*     // Core fields you identified
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "complaint_type_id", nullable = false) */
    @Column(name = "complaint_type", nullable = false)
    private String complaintType;

    @Column(name = "incident_date", nullable = false)
    private LocalDate incidentDate;

    @Column(name = "description", nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "worker_full_name", nullable = false)
    private String workerFullName;

    @Column(name = "worker_description", columnDefinition = "TEXT")
    private String workerDescription;

    // Optional fields
    @Column(name = "location")
    private String location;

    @Column(name = "department")
    private String department;

    @Column(name = "worker_position")
    private String workerPosition;

    // Timestamps
    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    // RELACIÓN CON IMÁGENES - AÑADIDO
    @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @ToString.Exclude
    private List<Imagen> imagenes = new ArrayList<>();
    // Métodos para manejar la relación
    public void addImagen(Imagen imagen) {
        imagenes.add(imagen);
        imagen.setComplaint(this);
    }
    
    public void removeImagen(Imagen imagen) {
        imagenes.remove(imagen);
        imagen.setComplaint(null);
    }
    @PrePersist
    public void generateComplaintCode() {
        if (this.complaintCode == null) {
            int randomNum = (int) (Math.random() * 9000) + 1000; // 1000-9999
            this.complaintCode = "CMP-" + randomNum;
        }
    }
}

// Enum for complaint status
/* enum ComplaintStatus {
    PENDING,
    UNDER_REVIEW,
    INVESTIGATING,
    RESOLVED,
    DISMISSED
} */

// Enum for complaint priority
/* enum ComplaintPriority {
    LOW,
    MEDIUM,
    HIGH,
    URGENT
} */
