package com.agendaedu.educacional.Controller.usuario;

import lombok.RequiredArgsConstructor;

import com.agendaedu.educacional.DTOs.autenticação.LoginRequestDTO;
import com.agendaedu.educacional.DTOs.autenticação.LoginResponseDTO;
import com.agendaedu.educacional.DTOs.usuario.NewUserDTO;
import com.agendaedu.educacional.DTOs.usuario.UpdateEmailDTO;
import com.agendaedu.educacional.DTOs.usuario.UpdatePasswordDTO;
import com.agendaedu.educacional.DTOs.usuario.UserInfoDTO;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Services.usuario.UserService;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

       @PostMapping
        public ResponseEntity<NewUserDTO> registrar(@RequestBody User user) {
            NewUserDTO response = userService.registrar(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
   
       
       @PostMapping("/login")
       public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO dto) {
           return ResponseEntity.ok(userService.login(dto));
       }
       
       @PostMapping("/logout")
        public ResponseEntity<String> logout() {
            userService.logout();
            return ResponseEntity.ok("Logout realizado com sucesso!");
       }

       @GetMapping
        public ResponseEntity<UserInfoDTO> getPerfil() {
            return ResponseEntity.ok(userService.getPerfil());
        }

        @PutMapping("/email")
        public ResponseEntity<String> atualizarEmail(@RequestBody UpdateEmailDTO dto) {
            return ResponseEntity.ok(userService.atualizarEmail(dto));
        }

        @PutMapping("/senha")
        public ResponseEntity<String> atualizarSenha(@RequestBody UpdatePasswordDTO dto) {
            return ResponseEntity.ok(userService.atualizarSenha(dto));
        }

   
       
    //    @PostMapping("/ping")
    //    public ResponseEntity<Void> heartbeat(@RequestHeader("Authorization") String token) {
    //        token = token.replace("Bearer ", "");
    //        var session = sessionRepository.findByToken(token)
    //                .orElseThrow(() -> new UserNotFoundException("Sessão inválida"));
   
    //        session.setLastActivity(LocalDateTime.now());
    //        sessionRepository.save(session);
   
    //        return ResponseEntity.ok().build();
    //    }
   }