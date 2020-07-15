package br.com.lucas.study.personalfinancialmanagementapi.resource.dto;

import javax.validation.constraints.NotEmpty;

public class CategoryDTO {

    private Long id;

    @NotEmpty(message = "The field 'description' must not be empty.")
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
