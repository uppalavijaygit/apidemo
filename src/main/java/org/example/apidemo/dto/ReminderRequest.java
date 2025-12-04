package org.example.apidemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDate;

@Data
@Schema(description = "Request DTO for creating or updating a reminder")
public class ReminderRequest {
    
    @Schema(description = "Reference number (ref) for the reminder", example = "26011715655987")
    private String ref;
    
    @Schema(description = "Company number", example = "15655987", required = true)
    private String companyNumber;
    
    @Schema(description = "Due date for the reminder", example = "2026-01-17", required = true)
    private LocalDate dueDate;
    
    @Schema(description = "Reminder lifecycle status", example = "POSTED", allowableValues = {"POSTED", "SENT", "DELIVERED", "PENDING"})
    private String reminderLifecycleStatus;
}

