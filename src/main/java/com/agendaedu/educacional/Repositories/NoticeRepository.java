package com.agendaedu.educacional.Repositories;

import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Entities.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findBySala(ClassEntity classroom);
}
