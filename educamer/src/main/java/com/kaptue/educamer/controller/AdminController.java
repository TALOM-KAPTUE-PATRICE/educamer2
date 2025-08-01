package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.request.RejectionRequestDTO;
import com.kaptue.educamer.dto.response.InstructorApplicationDTO;
import com.kaptue.educamer.dto.response.UserManagementDTO;
import com.kaptue.educamer.security.Permission;
import com.kaptue.educamer.service.AdminApplicationService;
import com.kaptue.educamer.service.AdminService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private AdminApplicationService adminApplicationService;


    @GetMapping("/users")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "') ")
    public ResponseEntity<List<UserManagementDTO>> getAllUsers() {
        return ResponseEntity.ok(adminService.getAllUsers());
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        adminService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/applications")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "') ")
    public ResponseEntity<List<InstructorApplicationDTO>> getPendingApplications() {
        return ResponseEntity.ok(adminApplicationService.getPendingApplications());
    }
    
    
    @PostMapping("/applications/{appId}/approve")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "') ")
    public ResponseEntity<String> approveApplication(@PathVariable Long appId) {
        adminApplicationService.approveApplication(appId);
        return ResponseEntity.ok("La candidature a été approuvée et un email a été envoyé au nouvel instructeur.");
    }

    @PostMapping("/applications/{appId}/reject")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "') ")
    public ResponseEntity<String> rejectApplication(@PathVariable Long appId, @Valid @RequestBody RejectionRequestDTO request) {
        adminApplicationService.rejectApplication(appId, request.getReason());
        return ResponseEntity.ok("La candidature a été rejetée et un email a été envoyé au candidat.");
    }

    @GetMapping("/applications/{appId}")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "') ")
    public ResponseEntity<InstructorApplicationDTO> getApplicationDetails(@PathVariable Long appId) {
        return ResponseEntity.ok(adminApplicationService.getApplicationById(appId));
    }

    @PostMapping("/users/{userId}/disable")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "')")
    public ResponseEntity<Void> disableUser(@PathVariable Long userId) {
        adminService.setUserStatus(userId, false);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/users/{userId}/enable")
    @PreAuthorize("hasAuthority('" + Permission.USER_MANAGE + "') and hasAuthority('" + Permission.USER_LIST + "')")
    public ResponseEntity<Void> enableUser(@PathVariable Long userId) {
        adminService.setUserStatus(userId, true);
        return ResponseEntity.ok().build();
    }

}
