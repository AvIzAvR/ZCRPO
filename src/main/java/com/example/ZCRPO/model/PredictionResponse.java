package com.example.ZCRPO.model;

public class PredictionResponse {
    private String status;
    private Double predictedRating;
    private String error;

    public PredictionResponse() {
    }

    public PredictionResponse(String status, Double predictedRating, String error) {
        this.status = status;
        this.predictedRating = predictedRating;
        this.error = error;
    }

    // Getters and setters
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(Double predictedRating) {
        this.predictedRating = predictedRating;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
