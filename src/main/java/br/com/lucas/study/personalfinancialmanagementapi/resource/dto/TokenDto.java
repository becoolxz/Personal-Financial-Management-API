package br.com.lucas.study.personalfinancialmanagementapi.resource.dto;

public class TokenDto {

    private String token;

    public TokenDto() {}

    public TokenDto(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

}
