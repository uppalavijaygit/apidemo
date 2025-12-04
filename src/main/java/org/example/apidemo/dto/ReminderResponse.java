package org.example.apidemo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response DTO for reminder data")
public class ReminderResponse {
    
    @Schema(description = "Reference number (ref)", example = "26011715655987")
    private String ref;
    
    @Schema(description = "Company number", example = "15655987")
    private String companyNumber;
    
    @Schema(description = "Due date", example = "2026-01-17")
    private LocalDate dueDate;
    
    @Schema(description = "Reminder lifecycle status", example = "POSTED")
    private String reminderLifecycleStatus;
    
    @Schema(description = "Reminder generated timestamp", example = "2025-12-03T09:00:10")
    private LocalDateTime reminderGeneratedAt;
    
    @Schema(description = "Reminder sent timestamp", example = "2025-12-03T09:00:10")
    private LocalDateTime reminderSentAt;
    
    @Schema(description = "Creation timestamp", example = "2025-12-03T09:00:10")
    private LocalDateTime createdAt;
    
    @Schema(description = "Last update timestamp", example = "2025-12-03T09:00:10")
    private LocalDateTime updatedAt;
}

