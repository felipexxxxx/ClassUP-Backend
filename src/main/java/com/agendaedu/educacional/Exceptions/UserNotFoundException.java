package com.agendaedu.educacional.Exceptions;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("Usuário não encontrado com: " + login);
    }
}
