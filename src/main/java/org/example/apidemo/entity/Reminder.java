package org.example.apidemo.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders", schema = "companies_house_data")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reminder {
    
    @Id
    @Column(name = "ref", nullable = false, length = 100)
    private String ref;
    
    @Column(name = "company_number", nullable = false, length = 255)
    private String companyNumber;
    
    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;
    
    @Column(name = "reminder_lifecycle_status", length = 50)
    private String reminderLifecycleStatus;
    
    @Column(name = "reminder_generated_at", nullable = false)
    private LocalDateTime reminderGeneratedAt;
    
    @Column(name = "reminder_sent_at")
    private LocalDateTime reminderSentAt;
    
    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;
}

