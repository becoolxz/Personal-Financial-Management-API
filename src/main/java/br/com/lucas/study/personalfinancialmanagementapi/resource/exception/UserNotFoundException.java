package br.com.lucas.study.personalfinancialmanagementapi.resource.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
