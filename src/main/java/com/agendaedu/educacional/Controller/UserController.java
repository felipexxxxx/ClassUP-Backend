package com.agendaedu.educacional.Controller;

import com.agendaedu.educacional.Entities.User;
import com.agendaedu.educacional.Repositories.SessionRepository;
import com.agendaedu.educacional.Services.UserService;
import com.agendaedu.educacional.DTOs.LoginRequestDTO;
import com.agendaedu.educacional.DTOs.LoginResponseDTO;
import com.agendaedu.educacional.Exceptions.UserNotFoundException;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final SessionRepository sessionRepository;

    public UserController(UserService userService, SessionRepository sessionRepository) {
        this.userService = userService;
        this.sessionRepository = sessionRepository;
    }

       // ✅ Criar novo usuário com feedback de sucesso
       @PostMapping
    public ResponseEntity<LoginResponseDTO> registrar(@RequestBody User user) {
    LoginResponseDTO response = userService.registrar(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
}
   
       // ✅ Login (Corrigido)
       @PostMapping("/login")
       public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
           return ResponseEntity.ok(userService.login(dto));
       }
   
       // ✅ Atualiza atividade da sessão (usado pelo frontend)
       @PostMapping("/heartbeat")
       public ResponseEntity<Void> heartbeat(@RequestHeader("Authorization") String token) {
           token = token.replace("Bearer ", "");
           var session = sessionRepository.findByToken(token)
                   .orElseThrow(() -> new UserNotFoundException("Sessão inválida"));
   
           session.setLastActivity(LocalDateTime.now());
           sessionRepository.save(session);
   
           return ResponseEntity.ok().build();
       }
   }