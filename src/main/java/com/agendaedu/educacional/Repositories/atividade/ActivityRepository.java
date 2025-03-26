package com.agendaedu.educacional.Repositories.atividade;

import com.agendaedu.educacional.Entities.atividade.Activity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findBySalaId(Long id);
}
