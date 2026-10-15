
package com.esg.esgbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.esg.esgbackend.entity.ReportingPeriod;

public interface ReportingPeriodRepository
        extends JpaRepository<ReportingPeriod, Long> {

    List<ReportingPeriod> findByOrganizationId(Long organizationId);

    List<ReportingPeriod> findByStatus(String status);
}
