package com.evan.wearesikgu.domain.history.model.repository;

import com.evan.wearesikgu.domain.history.entity.History;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HistoryRepository extends JpaRepository<History, Long> {
}
