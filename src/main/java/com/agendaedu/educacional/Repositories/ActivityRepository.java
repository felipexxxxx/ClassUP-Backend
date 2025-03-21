package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findBySalaId(Long id);



}
