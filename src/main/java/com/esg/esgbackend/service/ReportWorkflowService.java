
package com.esg.esgbackend.service;

import com.esg.esgbackend.entity.ReportWorkflow;

import com.esg.esgbackend.repository.ReportWorkflowRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import com.esg.esgbackend.repository.WorkflowAuditEventRepository;
import com.esg.esgbackend.entity.WorkflowAuditEvent;
import org.springframework.security.core.context.SecurityContextHolder;

@Service
public class ReportWorkflowService {

    private final ReportWorkflowRepository workflowRepository;
    private final WorkflowAuditEventRepository auditRepository;


public ReportWorkflowService(
        ReportWorkflowRepository workflowRepository,
        WorkflowAuditEventRepository auditRepository) {
    this.workflowRepository = workflowRepository;
    this.auditRepository = auditRepository;
}


    @Transactional
    public ReportWorkflow createWorkflow(
            ReportWorkflow workflow) {

        if (workflow.getReportingPeriod() == null
                || workflow.getReportingPeriod().getId() == null) {
            throw new IllegalArgumentException(
                    "Reporting period is required");
        }

        if (workflowRepository.findByReportingPeriodId(
                workflow.getReportingPeriod().getId()).isPresent()) {
            throw new IllegalArgumentException(
                    "Workflow already exists for this reporting period");
        }

        workflow.setStatus("DRAFT");
        workflow.setUpdatedAt(LocalDateTime.now());

        ReportWorkflow saved = workflowRepository.save(workflow);

        auditRepository.save(new WorkflowAuditEvent(
            saved.getId(), "CREATED", getCurrentActor(), "Workflow created"
        ));

        return saved;
    }

    @Transactional
    public ReportWorkflow submitForReview(Long workflowId) {
        ReportWorkflow workflow = getWorkflow(workflowId);

        if (!"DRAFT".equals(workflow.getStatus())
                && !"CHANGES_REQUESTED".equals(workflow.getStatus())) {
            throw new IllegalStateException(
                    "Only draft or changes-requested reports can be submitted");
        }

        workflow.setStatus("SUBMITTED");
        workflow.setReviewerComment(null);
        workflow.setUpdatedAt(LocalDateTime.now());

        ReportWorkflow saved = workflowRepository.save(workflow);

        auditRepository.save(new WorkflowAuditEvent(
            saved.getId(),
            "SUBMITTED",
            getCurrentActor(),
            "Report submitted for review"
        ));

        return saved;
    }

    @Transactional
    public ReportWorkflow requestChanges(
            Long workflowId, String comment) {
        ReportWorkflow workflow = getWorkflow(workflowId);

        if (!"SUBMITTED".equals(workflow.getStatus())) {
            throw new IllegalStateException(
                    "Only submitted reports can be sent back for changes");
        }

        if (comment == null || comment.isBlank()) {
            throw new IllegalArgumentException(
                    "A reviewer comment is required");
        }

        workflow.setStatus("CHANGES_REQUESTED");
        workflow.setReviewerComment(comment);
        workflow.setUpdatedAt(LocalDateTime.now());

        ReportWorkflow saved = workflowRepository.save(workflow);

        auditRepository.save(new WorkflowAuditEvent(
            saved.getId(),
            "CHANGES_REQUESTED",
            getCurrentActor(),
            comment
        ));

        return saved;
    }

    @Transactional
    public ReportWorkflow approve(Long workflowId) {
        ReportWorkflow workflow = getWorkflow(workflowId);

        if (!"SUBMITTED".equals(workflow.getStatus())) {
            throw new IllegalStateException(
                    "Only submitted reports can be approved");
        }

        workflow.setStatus("APPROVED");
        workflow.setReviewerComment(null);
        workflow.setUpdatedAt(LocalDateTime.now());

        ReportWorkflow saved = workflowRepository.save(workflow);

        auditRepository.save(new WorkflowAuditEvent(
            saved.getId(),
            "APPROVED",
            getCurrentActor(),
            "Report approved"
        ));

        return saved;
    }

    public ReportWorkflow getWorkflow(Long workflowId) {
        return workflowRepository.findById(workflowId)
                .orElseThrow(() -> new IllegalArgumentException(
                        "Workflow not found: " + workflowId));
    }
    
    private String getCurrentActor() {
        String username = SecurityContextHolder.getContext()
                .getAuthentication().getName();

        return username;
    }
}
