package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.DTOs.JoinClassDTO;
import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Services.ClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/class")
@RequiredArgsConstructor
public class ClassController {

    private final ClassService classService;

    @PostMapping
    public ResponseEntity<ClassEntity> createClass(@RequestBody ClassEntity classEntity) {
        ClassEntity created = classService.createClass(classEntity);
        return ResponseEntity.ok(created);
    }

    @PostMapping("/join")
    public ResponseEntity<String> joinClass(@RequestBody JoinClassDTO dto) {
        String result = classService.joinClass(dto.codigoDeEntrada());
        return ResponseEntity.ok(result);
}
}
