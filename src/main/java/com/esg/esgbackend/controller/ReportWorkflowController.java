
package com.esg.esgbackend.controller;

import com.esg.esgbackend.entity.ReportWorkflow;
import com.esg.esgbackend.service.ReportWorkflowService;
import org.springframework.web.bind.annotation.*;
import com.esg.esgbackend.entity.ReportingPeriod;
import com.esg.esgbackend.repository.ReportingPeriodRepository;

@RestController
@RequestMapping("/api/workflows")
public class ReportWorkflowController {

    private final ReportWorkflowService workflowService;
    private final ReportingPeriodRepository reportingPeriodRepository;

    public ReportWorkflowController(
            ReportWorkflowService workflowService,
            ReportingPeriodRepository reportingPeriodRepository) {
        this.workflowService = workflowService;
        this.reportingPeriodRepository = reportingPeriodRepository;
    }

    @GetMapping("/{id}")
    public ReportWorkflow getWorkflow(@PathVariable Long id) {
        return workflowService.getWorkflow(id);
    }

    @PostMapping("/{id}/submit")
    public ReportWorkflow submit(@PathVariable Long id) {
        return workflowService.submitForReview(id);
    }

    @PostMapping("/{id}/request-changes")
    public ReportWorkflow requestChanges(
            @PathVariable Long id,
            @RequestBody ReviewRequest request) {
        return workflowService.requestChanges(id, request.comment());
    }

    @PostMapping("/{id}/approve")
    public ReportWorkflow approve(@PathVariable Long id) {
        return workflowService.approve(id);
    }

    public record ReviewRequest(String comment) {}
    

@PostMapping("/create/{reportingPeriodId}")
public ReportWorkflow createWorkflow(
        @PathVariable Long reportingPeriodId) {

    ReportingPeriod period = reportingPeriodRepository
            .findById(reportingPeriodId)
            .orElseThrow(() -> new IllegalArgumentException(
                    "Reporting period not found: " + reportingPeriodId));

    ReportWorkflow workflow = new ReportWorkflow();
    workflow.setReportingPeriod(period);

    return workflowService.createWorkflow(workflow);
}

}
