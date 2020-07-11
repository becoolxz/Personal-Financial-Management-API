package br.com.lucas.study.personalfinancialmanagementapi.controller;

import br.com.lucas.study.personalfinancialmanagementapi.dto.CategoryDTO;
import br.com.lucas.study.personalfinancialmanagementapi.exception.ApiErrors;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryController(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryDTO create(@RequestBody @Valid CategoryDTO categoryDTO) {

        Category category = categoryService.save(modelMapper.map(categoryDTO, Category.class));

        return modelMapper.map(category, CategoryDTO.class);
    }

    @GetMapping("{id}")
    public CategoryDTO get(@PathVariable Long id) {
        return categoryService
                .getById(id)
                .map( category -> modelMapper.map(category, CategoryDTO.class))
                .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Category category = categoryService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        categoryService.delete(category);
    }

    @PutMapping("{id}")
    public CategoryDTO update(@PathVariable Long id, CategoryDTO categoryDTO) {
        return categoryService.getById(id)
                .map(category -> {

                    category.setDescription(categoryDTO.getDescription());
                    category = categoryService.update(category);
                    return modelMapper.map(category, CategoryDTO.class);

        }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiErrors handleValidationExceptions(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();
        return new ApiErrors(bindingResult);
    }




}
