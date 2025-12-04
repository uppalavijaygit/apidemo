package org.example.apidemo.service;

import org.example.apidemo.dto.ReminderRequest;
import org.example.apidemo.dto.ReminderResponse;
import org.example.apidemo.entity.Reminder;
import org.example.apidemo.repository.ReminderRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReminderService {
    
    private final ReminderRepository reminderRepository;
    
    public ReminderService(ReminderRepository reminderRepository) {
        this.reminderRepository = reminderRepository;
    }
    
    public ReminderResponse getByReferenceNumber(String ref) {
        Reminder reminder = reminderRepository.findByRef(ref)
                .orElseThrow(() -> new RuntimeException("Reminder not found with reference number: " + ref));
        return mapToResponse(reminder);
    }
    
    public List<ReminderResponse> getAllReminders(String status, String companyNumber, Pageable pageable) {
        Specification<Reminder> spec = Specification.where(null);
        
        if (status != null && !status.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("reminderLifecycleStatus"), status));
        }
        
        if (companyNumber != null && !companyNumber.isEmpty()) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("companyNumber"), companyNumber));
        }
        
        Page<Reminder> reminders = reminderRepository.findAll(spec, pageable);
        return reminders.getContent().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    public ReminderResponse createReminder(ReminderRequest request) {
        if (reminderRepository.existsByRef(request.getRef())) {
            throw new RuntimeException("Reminder with reference number already exists: " + request.getRef());
        }
        
        Reminder reminder = mapToEntity(request);
        reminder = reminderRepository.save(reminder);
        return mapToResponse(reminder);
    }
    
    public ReminderResponse updateReminder(String ref, ReminderRequest request) {
        Reminder reminder = reminderRepository.findByRef(ref)
                .orElseThrow(() -> new RuntimeException("Reminder not found with reference number: " + ref));
        
        // Check if new reference number conflicts with existing one
        if (!ref.equals(request.getRef()) && 
            reminderRepository.existsByRef(request.getRef())) {
            throw new RuntimeException("Reminder with reference number already exists: " + request.getRef());
        }
        
        updateEntityFromRequest(reminder, request);
        reminder = reminderRepository.save(reminder);
        return mapToResponse(reminder);
    }
    
    public ReminderResponse patchReminder(String ref, ReminderRequest request) {
        Reminder reminder = reminderRepository.findByRef(ref)
                .orElseThrow(() -> new RuntimeException("Reminder not found with reference number: " + ref));
        
        // Partial update - only update non-null fields
        if (request.getRef() != null && !ref.equals(request.getRef())) {
            if (reminderRepository.existsByRef(request.getRef())) {
                throw new RuntimeException("Reminder with reference number already exists: " + request.getRef());
            }
            reminder.setRef(request.getRef());
        }
        if (request.getCompanyNumber() != null) {
            reminder.setCompanyNumber(request.getCompanyNumber());
        }
        if (request.getDueDate() != null) {
            reminder.setDueDate(request.getDueDate());
        }
        if (request.getReminderLifecycleStatus() != null) {
            reminder.setReminderLifecycleStatus(request.getReminderLifecycleStatus());
        }
        
        reminder = reminderRepository.save(reminder);
        return mapToResponse(reminder);
    }
    
    public void deleteReminder(String ref) {
        Reminder reminder = reminderRepository.findByRef(ref)
                .orElseThrow(() -> new RuntimeException("Reminder not found with reference number: " + ref));
        reminderRepository.delete(reminder);
    }
    
    private Reminder mapToEntity(ReminderRequest request) {
        Reminder reminder = new Reminder();
        reminder.setRef(request.getRef());
        reminder.setCompanyNumber(request.getCompanyNumber());
        reminder.setDueDate(request.getDueDate());
        reminder.setReminderLifecycleStatus(request.getReminderLifecycleStatus());
        reminder.setReminderGeneratedAt(LocalDateTime.now());
        return reminder;
    }
    
    private void updateEntityFromRequest(Reminder reminder, ReminderRequest request) {
        reminder.setRef(request.getRef());
        reminder.setCompanyNumber(request.getCompanyNumber());
        reminder.setDueDate(request.getDueDate());
        if (request.getReminderLifecycleStatus() != null) {
            reminder.setReminderLifecycleStatus(request.getReminderLifecycleStatus());
        }
    }
    
    private ReminderResponse mapToResponse(Reminder reminder) {
        return new ReminderResponse(
                reminder.getRef(),
                reminder.getCompanyNumber(),
                reminder.getDueDate(),
                reminder.getReminderLifecycleStatus(),
                reminder.getReminderGeneratedAt(),
                reminder.getReminderSentAt(),
                reminder.getCreatedAt(),
                reminder.getUpdatedAt()
        );
    }
}
