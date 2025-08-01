package com.kaptue.educamer.repository;

import com.kaptue.educamer.entity.HelpRequest;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.kaptue.educamer.entity.enums.HelpRequestStatus;

@Repository

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {
    List<HelpRequest> findByStatus(HelpRequestStatus status);
}
