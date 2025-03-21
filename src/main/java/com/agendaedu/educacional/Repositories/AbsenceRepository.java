// AbsenceRepository.java
package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.Absence;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AbsenceRepository extends JpaRepository<Absence, Long> {
    Optional<Absence> findByUserIdAndActivityId(Long userId, Long activityId);
}
