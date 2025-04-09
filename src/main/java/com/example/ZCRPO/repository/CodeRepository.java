package com.example.ZCRPO.repository;

import com.example.ZCRPO.model.Code;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CodeRepository extends JpaRepository<Code, Long> {
    Optional<Code> findByCode(String code);

    @Transactional
    @Modifying
    @Query("DELETE FROM Code c WHERE c.user.id = ?1")
    void deleteByUserId(Long userId);
}