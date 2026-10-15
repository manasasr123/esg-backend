
package com.esg.esgbackend.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.esg.esgbackend.entity.WorkflowAuditEvent;
import com.esg.esgbackend.repository.WorkflowAuditEventRepository;

@RestController
@RequestMapping("/api/workflows")
public class WorkflowAuditEventController {

    private final WorkflowAuditEventRepository auditRepository;

    public WorkflowAuditEventController(
            WorkflowAuditEventRepository auditRepository) {
        this.auditRepository = auditRepository;
    }

    @GetMapping("/{workflowId}/history")
    public List<WorkflowAuditEvent> getWorkflowHistory(
            @PathVariable Long workflowId) {

        return auditRepository
                .findByWorkflowIdOrderByCreatedAtAsc(workflowId);
    }
}
