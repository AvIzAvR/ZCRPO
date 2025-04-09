package com.example.ZCRPO.controller;

import com.example.ZCRPO.model.RequestLog;
import com.example.ZCRPO.service.impl.RequestLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class RequestLogController {

    private final RequestLogService requestLogService;

    public RequestLogController(RequestLogService requestLogService) {
        this.requestLogService = requestLogService;
    }

    // Принимаем запрос с рейтингом и информацией о запросе
    @PostMapping("/log")
    public ResponseEntity<String> logRequest(@RequestBody RequestLogRequest request) {
        // Сохраняем информацию о запросе в базу данных
        requestLogService.saveRequestLog(request.getUsername(), request.getRequestData(), request.getPredictedRating());

        // Отправляем успешный ответ
        return ResponseEntity.ok("Request logged successfully");
    }

    // Новый эндпоинт для получения всех запросов пользователя
    @GetMapping("/user/{username}")
    public ResponseEntity<List<RequestLog>> getUserRequests(@PathVariable String username) {
        // Получаем все запросы пользователя
        List<RequestLog> requestLogs = requestLogService.getRequestsByUsername(username);

        // Возвращаем список запросов в ответе
        return ResponseEntity.ok(requestLogs);
    }

    // Дополнительный класс для запроса (DTO)
    public static class RequestLogRequest {
        private String username;  // Имя пользователя
        private String requestData;  // Данные запроса
        private Double predictedRating;  // Конечный рейтинг

        // Геттеры и сеттеры
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
    }
}
