package com.agendaedu.educacional.Services.admin;

import com.agendaedu.educacional.DTOs.admin.UsuarioImportadoDTO;
import com.agendaedu.educacional.Entities.usuario.User;
import com.agendaedu.educacional.Enums.Role;
import com.agendaedu.educacional.Repositories.usuario.UserRepository;
import com.agendaedu.educacional.Services.usuario.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserImportService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final SecureRandom random = new SecureRandom();

    // Método público e limpo
    public String importar(List<UsuarioImportadoDTO> lista) {
        int totalImportados = importarUsuarios(lista);

        if (totalImportados == 0) {
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Nenhum usuário foi importado. Todos os registros são duplicados ou inválidos."
            );
        }

        return "✅ " + totalImportados + " usuários importados com sucesso!";
    }

    // Método privado com a lógica de criação
    private int importarUsuarios(List<UsuarioImportadoDTO> lista) {
        int totalImportados = 0;

        for (UsuarioImportadoDTO dto : lista) {
            if (userRepository.existsByEmail(dto.email()) || userRepository.existsByCpf(dto.cpf())) {
                continue;
            }

            String senhaSimples = gerarSenhaNumerica(8);
            String senhaCriptografada = passwordEncoder.encode(senhaSimples);
            String matricula = gerarMatriculaUnica(dto.role());

            User user = new User();
            user.setNomeCompleto(dto.nomeCompleto());
            user.setEmail(dto.email());
            user.setCpf(dto.cpf());
            user.setRole(Role.valueOf(dto.role().toUpperCase()));
            user.setMatricula(matricula);
            user.setSenha(senhaCriptografada);
            user.setDataNascimento(LocalDate.parse(dto.dataNascimento()));

            userRepository.save(user);

            emailService.sendEmail(
            dto.email(),
            "Bem-vindo ao ClassUP - A plataforma de gestão educacional",
            """
            Olá %s,

            Seja muito bem-vindo(a) ao ClassUP — a plataforma que conecta alunos e professores de forma simples e eficiente! 🎓✨

            Sua conta foi criada com sucesso. Aqui estão os seus dados de acesso:

            👉 Matrícula: %s  
            🔐 Senha temporária: %s

            Acesse nossa plataforma com essas credenciais em:  
            🌐 https://classup-web.netlify.app (ou o endereço fornecido pela sua instituição)

            Recomendamos que você altere sua senha no primeiro acesso, para garantir maior segurança.

            Em caso de dúvidas, procure o responsável pela sua instituição ou entre em contato com nossa equipe de suporte.

            Um excelente semestre para você!  
            Atenciosamente,  
            Equipe ClassUP 💙
            """.formatted(dto.nomeCompleto(), matricula, senhaSimples)
        );


            totalImportados++;
        }

        return totalImportados;
    }

    private String gerarSenhaNumerica(int tamanho) {
        StringBuilder senha = new StringBuilder();
        for (int i = 0; i < tamanho; i++) {
            senha.append(random.nextInt(10));
        }
        return senha.toString();
    }

    private String gerarMatriculaUnica(String role) {
        String prefixo = role.equalsIgnoreCase("ALUNO") ? "ALU" : "PROF";
        String matricula;
        do {
            int numero = 10000 + random.nextInt(90000);
            matricula = prefixo + numero;
        } while (userRepository.existsByMatricula(matricula));
        return matricula;
    }
}
