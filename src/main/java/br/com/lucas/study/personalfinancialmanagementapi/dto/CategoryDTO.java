package br.com.lucas.study.personalfinancialmanagementapi.dto;

import javax.validation.constraints.NotEmpty;

public class CategoryDTO {

    private Long id;

    @NotEmpty
    private String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
