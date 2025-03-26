package com.agendaedu.educacional.Repositories.aviso;

import com.agendaedu.educacional.Entities.aviso.*;
import com.agendaedu.educacional.Entities.sala.*;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    List<Notice> findBySala(ClassEntity classroom);
}
