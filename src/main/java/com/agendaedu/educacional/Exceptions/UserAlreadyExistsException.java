package com.agendaedu.educacional.Exceptions;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String field, String value) {
        super("O " + field + " '" + value + "' já está cadastrado.");
    }
}