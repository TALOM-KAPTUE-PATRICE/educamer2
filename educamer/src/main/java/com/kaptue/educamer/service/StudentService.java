package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.LinkingCodeResponse;
import com.kaptue.educamer.entity.Student;
import com.kaptue.educamer.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.time.LocalDateTime;

@Service
public class StudentService {
    @Autowired private StudentRepository studentRepository;

    @Transactional
    public LinkingCodeResponse generateLinkingCode(Long studentId) {
        Student student = studentRepository.findById(studentId).orElseThrow(/*...*/);
        
        // Générer un code aléatoire à 6 chiffres
        String code = new DecimalFormat("000000").format(new SecureRandom().nextInt(999999));
        
        student.setLinkingCode(code);
        student.setLinkingCodeExpiry(LocalDateTime.now().plusMinutes(10)); // Valide 10 minutes
        studentRepository.save(student);
        
        return new LinkingCodeResponse(code, "Ce code expire dans 10 minutes.");
    }
}