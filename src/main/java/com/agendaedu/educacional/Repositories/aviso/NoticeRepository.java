package com.agendaedu.educacional.Repositories.aviso;

import com.agendaedu.educacional.Entities.aviso.Notice;
import com.agendaedu.educacional.Entities.sala.ClassEntity;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findBySala(ClassEntity classroom);
}
