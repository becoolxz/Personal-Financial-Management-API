package br.com.lucas.study.personalfinancialmanagementapi.endpoint.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
