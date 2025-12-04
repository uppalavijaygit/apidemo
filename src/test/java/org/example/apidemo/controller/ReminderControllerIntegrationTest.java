package org.example.apidemo.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.apidemo.dto.ReminderResponse;
import org.example.apidemo.repository.ReminderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class ReminderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReminderRepository reminderRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        // Clean up test data if needed
        // Note: Using @Transactional, so data will be rolled back after each test
    }

    @Test
    void testGetAllReminders_ShouldReturnList() throws Exception {
        // Given: There are existing reminders in the database
        
        // When: GET /api/reminders
        MvcResult result = mockMvc.perform(get("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        // Then: Should return a list of reminders
        String content = result.getResponse().getContentAsString();
        List<ReminderResponse> reminders = objectMapper.readValue(content, 
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReminderResponse.class));
        
        assertNotNull(reminders);
        assertTrue(reminders.size() > 0);
    }

    @Test
    void testGetAllReminders_WithPagination() throws Exception {
        // Given: Request with pagination parameters
        
        // When: GET /api/reminders?page=0&size=5
        mockMvc.perform(get("/api/reminders")
                        .param("page", "0")
                        .param("size", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(5)));
    }

    @Test
    void testGetAllReminders_WithStatusFilter() throws Exception {
        // Given: Request with status filter
        
        // When: GET /api/reminders?status=POSTED
        mockMvc.perform(get("/api/reminders")
                        .param("status", "POSTED")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].reminderLifecycleStatus").value("POSTED"));
    }

    @Test
    void testGetAllReminders_WithCompanyNumberFilter() throws Exception {
        // Given: Request with company number filter
        
        // When: GET /api/reminders?companyNumber=15655987
        mockMvc.perform(get("/api/reminders")
                        .param("companyNumber", "15655987")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].companyNumber").value("15655987"));
    }

    @Test
    void testGetAllReminders_WithSorting() throws Exception {
        // Given: Request with sorting parameters
        
        // When: GET /api/reminders?sortBy=createdAt&sortDir=DESC
        mockMvc.perform(get("/api/reminders")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "DESC")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllReminders_WithMultipleFilters() throws Exception {
        // Given: Request with multiple filters
        
        // When: GET /api/reminders?status=POSTED&companyNumber=15655987&page=0&size=10
        mockMvc.perform(get("/api/reminders")
                        .param("status", "POSTED")
                        .param("companyNumber", "15655987")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGetAllReminders_EmptyResult() throws Exception {
        // Given: Request with filter that returns no results
        
        // When: GET /api/reminders?status=NONEXISTENT
        mockMvc.perform(get("/api/reminders")
                        .param("status", "NONEXISTENT")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(0));
    }

    @Test
    void testGetAllReminders_ResponseStructure() throws Exception {
        // Given: Request for reminders
        
        // When: GET /api/reminders
        mockMvc.perform(get("/api/reminders")
                        .param("page", "0")
                        .param("size", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].ref").exists())
                .andExpect(jsonPath("$[0].companyNumber").exists())
                .andExpect(jsonPath("$[0].dueDate").exists())
                .andExpect(jsonPath("$[0].reminderLifecycleStatus").exists())
                .andExpect(jsonPath("$[0].createdAt").exists());
    }

    @Test
    void testGetAllReminders_DefaultPagination() throws Exception {
        // Given: Request without pagination parameters
        
        // When: GET /api/reminders (should use defaults: page=0, size=10)
        MvcResult result = mockMvc.perform(get("/api/reminders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andReturn();

        String content = result.getResponse().getContentAsString();
        List<ReminderResponse> reminders = objectMapper.readValue(content,
                objectMapper.getTypeFactory().constructCollectionType(List.class, ReminderResponse.class));
        
        // Then: Should return at most 10 items (default page size)
        assertTrue(reminders.size() <= 10);
    }

    @Test
    void testGetAllReminders_LargePageSize() throws Exception {
        // Given: Request with large page size
        
        // When: GET /api/reminders?page=0&size=100
        mockMvc.perform(get("/api/reminders")
                        .param("page", "0")
                        .param("size", "100")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(lessThanOrEqualTo(100)));
    }

    @Test
    void testGetAllReminders_InvalidPageNumber() throws Exception {
        // Given: Request with negative page number
        
        // When: GET /api/reminders?page=-1
        mockMvc.perform(get("/api/reminders")
                        .param("page", "-1")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()); // Spring Data handles negative pages gracefully
    }

    @Test
    void testGetAllReminders_WithSortingAscending() throws Exception {
        // Given: Request with ascending sort
        
        // When: GET /api/reminders?sortBy=createdAt&sortDir=ASC
        mockMvc.perform(get("/api/reminders")
                        .param("sortBy", "createdAt")
                        .param("sortDir", "ASC")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray());
    }
}

