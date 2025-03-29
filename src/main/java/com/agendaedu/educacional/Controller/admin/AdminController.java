package com.agendaedu.educacional.Controller.admin;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agendaedu.educacional.DTOs.admin.UsuarioImportadoDTO;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Services.usuario.UserService;
import com.agendaedu.educacional.DTOs.usuario.NewUserDTO;
import com.agendaedu.educacional.Services.admin.UserImportService;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
    public class AdminController {

        private final UserImportService userImportService;
        private final UserService userService;

        @PostMapping("/importar-usuarios")
        public ResponseEntity<String> importarUsuarios(@RequestBody List<UsuarioImportadoDTO> usuarios) {
            return ResponseEntity.ok(userImportService.importar(usuarios));
        }

        @PostMapping("/registrar")
        public ResponseEntity<NewUserDTO> registrar(@RequestBody User user) {
            NewUserDTO response = userService.registrar(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
}
