
package com.esg.esgbackend.repository;

import com.esg.esgbackend.entity.ReportWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ReportWorkflowRepository
        extends JpaRepository<ReportWorkflow, Long> {

    Optional<ReportWorkflow> findByReportingPeriodId(Long reportingPeriodId);
}
