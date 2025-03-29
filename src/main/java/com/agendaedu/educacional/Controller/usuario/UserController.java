package com.agendaedu.educacional.Controller.usuario;

import lombok.RequiredArgsConstructor;

import com.agendaedu.educacional.DTOs.autenticacao.*;
import com.agendaedu.educacional.DTOs.usuario.*;
import com.agendaedu.educacional.Services.usuario.*;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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
        public ResponseEntity<String> atualizarEmail(@RequestBody GetNovoEmailDTO dto) {
            return ResponseEntity.ok(userService.atualizarEmail(dto));
        }

        @PutMapping("/senha")
        public ResponseEntity<String> atualizarSenha(@RequestBody UpdatePasswordDTO dto) {
            return ResponseEntity.ok(userService.atualizarSenha(dto));
        }
        @PostMapping("/enviar-email")
        public ResponseEntity<String> enviarCodigoAutenticacao(@RequestBody SendOldEmailDTO dto) {
            return userService.mandarEmail(dto); // sem ResponseEntity.ok()
        }
    
        @PutMapping("/redefinir-senha")
        public ResponseEntity<String> redefinirSenha(@RequestBody UpdatePasswordCodeDTO dto) {
            return userService.redefinirSenhaComCodigo(dto);
        }

   }