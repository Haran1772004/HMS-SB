package com.hospital.hospital_spring;

import com.hospital.hospital_spring.entity.User;
import com.hospital.hospital_spring.service.JwtService;

import jakarta.servlet.Filter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class EndpointMatrixTest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private Filter springSecurityFilterChain;

    @Autowired
    private JwtService jwtService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                .addFilters(springSecurityFilterChain)
                .build();
    }

    private String createToken(int userId, String username, String role) {
        User user = new User();
        user.setUserId(userId);
        user.setUsername(username);
        user.setRole(role);
        return "Bearer " + jwtService.generateToken(user);
    }

    @Test
    @DisplayName("401 Unauthorized - Missing Authorization header")
    void testMissingTokenReturns401() throws Exception {
        mockMvc.perform(get("/patients/me"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    @DisplayName("401 Unauthorized - Invalid or forged JWT")
    void testInvalidTokenReturns401() throws Exception {
        mockMvc.perform(get("/patients/me")
                        .header("Authorization", "Bearer invalid-tampered-token-xyz"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.status").value(401))
                .andExpect(jsonPath("$.error").value("Unauthorized"));
    }

    @Test
    @DisplayName("401 Unauthorized - Bad credentials on login")
    void testLoginWithBadCredentialsReturns401() throws Exception {
        String badCredentials = "{\"username\":\"nonexistent_user_999\",\"password\":\"wrongpassword\"}";

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badCredentials))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("403 Forbidden - PATIENT accessing ADMIN endpoint")
    void testPatientAccessingAdminEndpointReturns403() throws Exception {
        String patientToken = createToken(101, "patient_tester", "PATIENT");

        mockMvc.perform(get("/admin/doctors/pending")
                        .header("Authorization", patientToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("403 Forbidden - DOCTOR accessing another doctor's appointments")
    void testDoctorAccessingOtherDoctorAppointmentsReturns403() throws Exception {
        // Token has userId 200, but request is for doctorId 999
        String doctorToken = createToken(200, "doctor_tester", "DOCTOR");

        mockMvc.perform(get("/appointments/doctor/999/today")
                        .header("Authorization", doctorToken))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("404 Not Found - Non-existent department")
    void testNonExistentDepartmentReturns404() throws Exception {
        String adminToken = createToken(1, "admin_tester", "ADMIN");

        mockMvc.perform(get("/departments/999999")
                        .header("Authorization", adminToken))
                .andExpect(status().isNotFound())
                .andExpect(content().string(containsString("Department not found")));
    }

    @Test
    @DisplayName("400 Bad Request - Blank username in patient registration")
    void testRegisterWithBlankUsernameReturns400() throws Exception {
        String badPayload = "{\"username\":\" \",\"password\":\"pwd123\",\"fullName\":\"Test\"}";

        mockMvc.perform(post("/auth/register/patient")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(badPayload))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName("200 OK - Authorized access to public departments list")
    void testGetDepartmentsWithTokenReturns200() throws Exception {
        String userToken = createToken(1, "admin_tester", "ADMIN");

        mockMvc.perform(get("/departments")
                        .header("Authorization", userToken))
                .andExpect(status().isOk());
    }
}
