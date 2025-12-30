package com.pavel.jogger.web.controller;

import com.pavel.jogger.service.AccessService;
import com.pavel.jogger.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/runners/{id}/reports")
public class ReportController {

    private final ReportService reportService;
    private final AccessService accessService;

    public ReportController(ReportService reportService, AccessService accessService) {
        this.reportService = reportService;
        this.accessService = accessService;
    }

    @GetMapping("/progress")
    public ResponseEntity<byte[]> getProgressReport(
            @PathVariable Long id,
            Authentication authentication
    ) {
        accessService.checkRunnerAccess(authentication, id);

        byte[] pdf = reportService.generateProgressReport(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=progress_report_runner_" + id + ".pdf")
                .contentType(MediaType.APPLICATION_PDF)
                .body(pdf);
    }
}
