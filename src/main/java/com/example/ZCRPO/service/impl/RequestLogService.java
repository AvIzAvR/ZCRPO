package com.example.ZCRPO.service.impl;

import com.example.ZCRPO.model.RequestLog;
import com.example.ZCRPO.repository.RequestLogRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RequestLogService {

    private final RequestLogRepository requestLogRepository;

    public RequestLogService(RequestLogRepository requestLogRepository) {
        this.requestLogRepository = requestLogRepository;
    }

    public void saveRequestLog(String username, String requestData, Double predictedRating) {
        // Создаем запись запроса
        RequestLog requestLog = new RequestLog(username, requestData, predictedRating, LocalDateTime.now());

        // Сохраняем запись в базе данных
        requestLogRepository.save(requestLog);
    }

    public List<RequestLog> getRequestsByUsername(String username) {
        // Получаем все запросы пользователя по имени
        return requestLogRepository.findByUsername(username);
    }
}
