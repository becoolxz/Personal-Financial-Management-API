package br.com.lucas.study.personalfinancialmanagementapi.endpoint.resource;

import br.com.lucas.study.personalfinancialmanagementapi.endpoint.dto.CategoryDTO;
import br.com.lucas.study.personalfinancialmanagementapi.model.Category;
import br.com.lucas.study.personalfinancialmanagementapi.endpoint.response.Response;
import br.com.lucas.study.personalfinancialmanagementapi.service.CategoryService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/categories")
public class CategoryResource {

    private final CategoryService categoryService;
    private final ModelMapper modelMapper;

    public CategoryResource(CategoryService categoryService, ModelMapper modelMapper) {
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("{id}")
    public ResponseEntity<Response<CategoryDTO>> get(@PathVariable Long id) {

        Response<CategoryDTO> response = new Response<>();

        return categoryService
                .getById(id)
                .map( category -> {
                            response.setData(modelMapper.map(category, CategoryDTO.class));
                            return ResponseEntity.status(HttpStatus.OK).body(response);
                    }
                ).orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Response<CategoryDTO>> create(@RequestBody @Valid CategoryDTO categoryDTO) {

        Response<CategoryDTO> response = new Response<>();

        Category category = categoryService.save(modelMapper.map(categoryDTO, Category.class));

        response.setData(modelMapper.map(category, CategoryDTO.class));

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("{id}")
    public ResponseEntity<Response<CategoryDTO>> update(@PathVariable Long id, CategoryDTO categoryDTO) {

        Response<CategoryDTO> response = new Response<>();

        return categoryService.getById(id)
                .map(category -> {

                    category.setDescription(categoryDTO.getDescription());
                    category = categoryService.update(category);

                    response.setData(modelMapper.map(category, CategoryDTO.class));

                    return ResponseEntity.status(HttpStatus.OK).body(response);

                }).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        Category category = categoryService.getById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        categoryService.delete(category);
    }

}
