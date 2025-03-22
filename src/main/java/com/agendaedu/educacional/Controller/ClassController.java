package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.Entities.ClassEntity;
import com.agendaedu.educacional.Services.ClassService;
import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<?> joinClass(@RequestParam Long userId, @RequestParam String codigoDeEntrada) {
        try {
            String result = classService.joinClass(userId, codigoDeEntrada);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}
