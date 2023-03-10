package ru.ewm.service.category.web;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.ewm.service.category.dto.NewCategoryDto;
import ru.ewm.service.category.service.AdminCategoryService;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(path = "/admin/categories")
@Slf4j
@Validated
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<Object> addCategory(@RequestBody @NotNull @Valid NewCategoryDto newCategoryDto) {
        log.info("add new category = {}", newCategoryDto);
        return new ResponseEntity<>(adminCategoryService.addCategory(newCategoryDto), HttpStatus.CREATED);
    }

    @DeleteMapping("{catId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable Long catId) {
        log.info("delete category with id = {}", catId);
        adminCategoryService.deleteCategory(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("{catId}")
    public ResponseEntity<Object> updateCategory(@PathVariable Long catId,
                                                 @RequestBody @NotNull @Valid NewCategoryDto newCategoryDto) {
        log.info("update category with id = {} with new category = {}", catId, newCategoryDto);
        return new ResponseEntity<>(adminCategoryService.updateCategory(catId, newCategoryDto), HttpStatus.OK);
    }
}
