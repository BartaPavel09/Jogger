package com.pavel.jogger.service;

import com.pavel.jogger.persistence.repository.ActivityRepository;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class ReportService {

    private final ActivityRepository activityRepository;

    public ReportService(ActivityRepository activityRepository) {
        this.activityRepository = activityRepository;
    }

    public byte[] generateProgressReport(Long runnerId) {

        Double totalKmObj = activityRepository.totalDistance(runnerId);
        Long totalRunsObj = activityRepository.totalActivities(runnerId);

        double totalKm = (totalKmObj == null) ? 0.0 : totalKmObj;
        long totalRuns = (totalRunsObj == null) ? 0L : totalRunsObj;

        try (PDDocument document = new PDDocument()) {

            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream content = new PDPageContentStream(document, page)) {

                content.beginText();
                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD), 18);
                content.setLeading(20f);
                content.newLineAtOffset(50, 750);

                content.showText("Jogger Progress Report");
                content.newLine();
                content.newLine();

                content.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);

                content.showText("Runner ID: " + runnerId);
                content.newLine();

                content.showText("Total distance: " + totalKm + " km");
                content.newLine();

                content.showText("Total runs: " + totalRuns);
                content.newLine();

                content.endText();
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }
}
