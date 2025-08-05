package com.kaptue.educamer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.kaptue.educamer.dto.request.HelpRequestCreateDTO;
import com.kaptue.educamer.dto.response.HelpRequestDTO;
import com.kaptue.educamer.entity.HelpRequest;
import com.kaptue.educamer.entity.Student;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import com.kaptue.educamer.repository.HelpRequestRepository;
import com.kaptue.educamer.repository.StudentRepository;

@Service
public class HelpRequestService {
    @Autowired private HelpRequestRepository helpRequestRepository;
    @Autowired private StudentRepository studentRepository;

    @Transactional
    public HelpRequestDTO createHelpRequest(Long studentId, HelpRequestCreateDTO request) {
        Student student = studentRepository.findById(studentId)
            .orElseThrow(() -> new ResourceNotFoundException("Élève non trouvé"));
        
        HelpRequest helpRequest = new HelpRequest();
        helpRequest.setStudent(student);
        helpRequest.setSubject(request.getSubject());
        helpRequest.setDescription(request.getDescription());
        // Le statut est 'OPEN' par défaut
        
        HelpRequest saved = helpRequestRepository.save(helpRequest);
        return HelpRequestDTO.fromEntity(saved);
    }
}