package com.pavel.jogger.web.dto.chart;

/**
 * Generic DTO for sending chart data points.
 * <p>
 * This simple structure (Label + Value) is flexible enough to power
 * different types of charts.
 * </p>
 */
public class ChartResponse {

    private String label;
    private double value;

    public ChartResponse(String label, double value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public double getValue() {
        return value;
    }
}
