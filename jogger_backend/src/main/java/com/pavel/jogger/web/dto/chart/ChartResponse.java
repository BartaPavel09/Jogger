package com.pavel.jogger.web.dto.chart;

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
