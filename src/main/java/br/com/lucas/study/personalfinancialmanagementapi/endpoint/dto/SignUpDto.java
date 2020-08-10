package br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto;

import javax.validation.constraints.NotEmpty;

public class SignUpDto {

    @NotEmpty(message = "Name not be empty")
    private String name;

    @NotEmpty(message = "Email not be empty")
    private String email;

    @NotEmpty(message = "Password not be empty")
    private String password;

    public SignUpDto() {}

    public SignUpDto(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

}
