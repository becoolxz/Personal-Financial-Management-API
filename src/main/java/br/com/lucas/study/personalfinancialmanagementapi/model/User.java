package br.com.lucas.study.personalfinancialmanagementapi.model;

import br.com.lucas.study.personalfinancialmanagementapi.model.enums.Role;

import javax.persistence.*;

@Entity
public class User {

    private Long id;
    private String name;
    private String email;
    private String password;
    private Role profile;

    public User(){}

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Enumerated(EnumType.STRING)
    public Role getProfile() {
        return profile;
    }

    public void setProfile(Role profile) {
        this.profile = profile;
    }
}
