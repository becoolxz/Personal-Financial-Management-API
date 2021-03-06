package br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto;

import javax.validation.constraints.NotEmpty;

public class TransactionDTO {

    private Long id;

    @NotEmpty
    private Long categoryId;

    @NotEmpty
    private String categoryName;

    @NotEmpty
    private String description;

    @NotEmpty
    private Double value;

    @NotEmpty
    private String typeTransaction;

    @NotEmpty
    private String year;

    @NotEmpty
    private String month;

    public Long getId() {
        return id;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public Double getValue() {
        return value;
    }

    public String getTypeTransaction() {
        return typeTransaction;
    }

    public String getYear() {
        return year;
    }

    public String getMonth() {
        return month;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public void setTypeTransaction(String typeTransaction) {
        this.typeTransaction = typeTransaction;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setMonth(String month) {
        this.month = month;
    }

}
