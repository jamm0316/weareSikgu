package com.evan.wearesikgu.domain.calendar.model.repository;

import com.evan.wearesikgu.domain.calendar.entity.Participation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
