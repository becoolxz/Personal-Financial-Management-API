package br.com.lucas.study.personalfinancialmanagementapi.resource.dto;

public class SignUpDto {

    private String name;
    private String email;
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
