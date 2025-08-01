package com.kaptue.educamer.controller;

import com.kaptue.educamer.dto.response.HelpRequestDTO;
import com.kaptue.educamer.entity.User;
import com.kaptue.educamer.security.CurrentUser;
import com.kaptue.educamer.service.TutorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/tutor")
@PreAuthorize("hasRole('TUTOR') or hasRole('ADMIN')")
public class TutorController {

    @Autowired private TutorService tutorService;

    @GetMapping("/help-requests/open")
    public ResponseEntity<List<HelpRequestDTO>> getOpenRequests() {
        return ResponseEntity.ok(tutorService.getOpenHelpRequests());
    }
    
    @PostMapping("/help-requests/{requestId}/assign")
    public ResponseEntity<HelpRequestDTO> assignRequest(@PathVariable Long requestId, @CurrentUser User currentUser) {
        HelpRequestDTO assignedRequest = tutorService.assignRequestToSelf(requestId, currentUser.getId());
        return ResponseEntity.ok(assignedRequest);
    }
}