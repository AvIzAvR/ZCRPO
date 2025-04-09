package com.example.ZCRPO.model;

import jakarta.persistence.*;


import java.time.LocalDateTime;

@Entity
@Table(name = "request_logs")  // Имя таблицы в базе данных
public class RequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // Автоматическое создание идентификатора
    private Long id;

    @Column(nullable = false)  // Указывает, что поле не может быть null
    private String username;

    @Column(nullable = false)  // Указывает, что поле не может быть null
    private String requestData;

    @Column(nullable = false)  // Указывает, что поле не может быть null
    private Double predictedRating;

    @Column(nullable = false)  // Указывает, что поле не может быть null
    private LocalDateTime timestamp;

    // Конструкторы
    public RequestLog() {
    }

    public RequestLog(String username, String requestData, Double predictedRating, LocalDateTime timestamp) {
        this.username = username;
        this.requestData = requestData;
        this.predictedRating = predictedRating;
        this.timestamp = timestamp;
    }

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRequestData() {
        return requestData;
    }

    public void setRequestData(String requestData) {
        this.requestData = requestData;
    }

    public Double getPredictedRating() {
        return predictedRating;
    }

    public void setPredictedRating(Double predictedRating) {
        this.predictedRating = predictedRating;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
