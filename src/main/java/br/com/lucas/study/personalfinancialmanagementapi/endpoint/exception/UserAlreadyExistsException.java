package br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception;

public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
