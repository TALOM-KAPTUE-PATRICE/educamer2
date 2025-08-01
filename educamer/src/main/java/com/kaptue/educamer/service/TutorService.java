package com.kaptue.educamer.service;

import com.kaptue.educamer.dto.response.HelpRequestDTO;
import com.kaptue.educamer.entity.*;
import com.kaptue.educamer.repository.*;
import com.kaptue.educamer.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;
import com.kaptue.educamer.entity.enums.HelpRequestStatus;


@Service
public class TutorService {

    @Autowired private HelpRequestRepository helpRequestRepository;
    @Autowired private TutorRepository tutorRepository;

    /**
     * Récupère toutes les demandes d'aide ouvertes (non encore assignées).
     */
    @Transactional(readOnly = true)
    public List<HelpRequestDTO> getOpenHelpRequests() {
        return helpRequestRepository.findByStatus(HelpRequestStatus.OPEN).stream()
            .map(HelpRequestDTO::fromEntity)
            .collect(Collectors.toList());
    }

    /**
     * Permet à un tuteur de s'assigner une demande d'aide.
     */
    @Transactional
    public HelpRequestDTO assignRequestToSelf(Long requestId, Long tutorId) {
        HelpRequest helpRequest = helpRequestRepository.findById(requestId)
            .orElseThrow(() -> new ResourceNotFoundException("Demande d'aide non trouvée"));
        Tutor tutor = tutorRepository.findById(tutorId)
            .orElseThrow(() -> new ResourceNotFoundException("Tuteur non trouvé"));

        if (helpRequest.getStatus() != HelpRequestStatus.OPEN) {
            throw new IllegalStateException("Cette demande a déjà été assignée.");
        }

        helpRequest.setTutor(tutor);
        helpRequest.setStatus(HelpRequestStatus.ASSIGNED);
        
        // Envoyer une notification à l'élève...
        
        return HelpRequestDTO.fromEntity(helpRequestRepository.save(helpRequest));
    }
}