package com.registro.denuncias.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.time.LocalDateTime;
/* import java.util.ArrayList;
import java.util.List; */

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
    @UuidGenerator
    @Column(name = "id", updatable = false, nullable = false)
    private String id;

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

    // Status tracking
/*     @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    @Builder.Default
    private ComplaintStatus status = ComplaintStatus.PENDING; */

/*     @Enumerated(EnumType.STRING)
    @Column(name = "priority")
    @Builder.Default
    private ComplaintPriority priority = ComplaintPriority.MEDIUM; */

    // Timestamps
    @CreationTimestamp
    @Column(name = "submitted_at", updatable = false)
    private LocalDateTime submittedAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    /* @OneToMany(mappedBy = "complaint", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<StatusUpdate> statusUpdates = new ArrayList<>();

    // Helper methods
    public void addStatusUpdate(StatusUpdate statusUpdate) {
        statusUpdates.add(statusUpdate);
        statusUpdate.setComplaint(this);
    }

    public void removeStatusUpdate(StatusUpdate statusUpdate) {
        statusUpdates.remove(statusUpdate);
        statusUpdate.setComplaint(null);
    } */

    // Generate complaint code (optional, can be done in service layer)
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
