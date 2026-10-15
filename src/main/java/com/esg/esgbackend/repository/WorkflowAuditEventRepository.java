
package com.esg.esgbackend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.esg.esgbackend.entity.WorkflowAuditEvent;

public interface WorkflowAuditEventRepository
        extends JpaRepository<WorkflowAuditEvent, Long> {

    List<WorkflowAuditEvent> findByWorkflowIdOrderByCreatedAtAsc(Long workflowId);
}
