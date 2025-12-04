package org.example.apidemo.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.example.apidemo.dto.ReminderRequest;
import org.example.apidemo.dto.ReminderResponse;
import org.example.apidemo.service.ReminderService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reminders")
@Tag(name = "Reminders", description = "API for managing reminders")
public class ReminderController {
    
    private final ReminderService reminderService;
    
    public ReminderController(ReminderService reminderService) {
        this.reminderService = reminderService;
    }
    
    @GetMapping("/{referenceNumber}")
    @Operation(
            summary = "Get reminder by reference number",
            description = "Retrieves a specific reminder using its reference number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder found",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public ResponseEntity<ReminderResponse> getByReferenceNumber(
            @Parameter(description = "Reference number (ref) of the reminder", required = true, example = "26011715655987")
            @PathVariable String referenceNumber) {
        ReminderResponse response = reminderService.getByReferenceNumber(referenceNumber);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(
            summary = "Get all reminders",
            description = "Retrieves all reminders with optional filtering by status and company number, and pagination support"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of reminders retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No reminders found matching the criteria")
    })
    public ResponseEntity<List<ReminderResponse>> getAllReminders(
            @Parameter(description = "Filter by reminder lifecycle status (e.g., POSTED, SENT, DELIVERED)", example = "POSTED")
            @RequestParam(required = false) String status,
            
            @Parameter(description = "Filter by company number", example = "15655987")
            @RequestParam(required = false) String companyNumber,
            
            @Parameter(description = "Page number (0-indexed)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            
            @Parameter(description = "Page size", example = "10")
            @RequestParam(defaultValue = "10") int size,
            
            @Parameter(description = "Sort field", example = "createdAt")
            @RequestParam(defaultValue = "createdAt") String sortBy,
            
            @Parameter(description = "Sort direction (ASC or DESC)", example = "DESC")
            @RequestParam(defaultValue = "DESC") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("ASC") ? 
                Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        List<ReminderResponse> reminders = reminderService.getAllReminders(status, companyNumber, pageable);
        
        // Return 404 if filters are applied and no results found
        boolean hasFilters = (status != null && !status.isEmpty()) || (companyNumber != null && !companyNumber.isEmpty());
        if (hasFilters && reminders.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        return ResponseEntity.ok(reminders);
    }
    
    @PostMapping
    @Operation(
            summary = "Create a new reminder",
            description = "Creates a new reminder with the provided details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Reminder created successfully",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input or duplicate reference number")
    })
    public ResponseEntity<ReminderResponse> createReminder(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Reminder details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReminderRequest.class))
            )
            @RequestBody ReminderRequest request) {
        ReminderResponse response = reminderService.createReminder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{referenceNumber}")
    @Operation(
            summary = "Update a reminder",
            description = "Updates an existing reminder identified by reference number. All fields must be provided."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder updated successfully",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reminder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ReminderResponse> updateReminder(
            @Parameter(description = "Reference number (ref) of the reminder to update", required = true, example = "26011715655987")
            @PathVariable String referenceNumber,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Updated reminder details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReminderRequest.class))
            )
            @RequestBody ReminderRequest request) {
        ReminderResponse response = reminderService.updateReminder(referenceNumber, request);
        return ResponseEntity.ok(response);
    }
    
    @PatchMapping("/{referenceNumber}")
    @Operation(
            summary = "Partially update a reminder",
            description = "Partially updates an existing reminder identified by reference number. Only provided fields will be updated."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reminder updated successfully",
                    content = @Content(schema = @Schema(implementation = ReminderResponse.class))),
            @ApiResponse(responseCode = "404", description = "Reminder not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input")
    })
    public ResponseEntity<ReminderResponse> patchReminder(
            @Parameter(description = "Reference number (ref) of the reminder to update", required = true, example = "26011715655987")
            @PathVariable String referenceNumber,
            
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Partial reminder details (only fields to update)",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReminderRequest.class))
            )
            @RequestBody ReminderRequest request) {
        ReminderResponse response = reminderService.patchReminder(referenceNumber, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{referenceNumber}")
    @Operation(
            summary = "Delete a reminder",
            description = "Deletes a reminder identified by reference number"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Reminder deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Reminder not found")
    })
    public ResponseEntity<Void> deleteReminder(
            @Parameter(description = "Reference number of the reminder to delete", required = true, example = "REF-2024-001")
            @PathVariable String referenceNumber) {
        reminderService.deleteReminder(referenceNumber);
        return ResponseEntity.noContent().build();
    }
}

