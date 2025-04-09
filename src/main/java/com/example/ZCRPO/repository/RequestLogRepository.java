package com.example.ZCRPO.repository;

import com.example.ZCRPO.model.RequestLog;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestLogRepository extends JpaRepository<RequestLog, Long> {
    List<RequestLog> findByUsername(String username);
}
