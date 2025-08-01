package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.category.CategoryCreateDTO;
import com.example.dto.category.CategoryDTO;
import com.example.entity.home_pages.Category;
import com.example.enums.LanguageEnum;
import com.example.service.CategoryService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    // Barcha role'lar ko'ra oladi
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/getAll")
    public ApiResult<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    // Bitta kategoriya: barcha role'lar ko'ra oladi
    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    public ApiResult<CategoryDTO> getCategory(@PathVariable Long id, @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.getCategory(id, lang);
    }

    // Yaratish: faqat admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    public ApiResult<CategoryCreateDTO> createCategory(@RequestBody CategoryCreateDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.createCategory(dto,lang);
    }

    // Tahrirlash: faqat admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    public ApiResult<CategoryCreateDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDTO dto,
                                                 @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.updateCategory(id, dto,lang);
    }

    // O'chirish: faqat admin
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    public ApiResult<Boolean> deleteCategory(@PathVariable Long id,
                                             @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.deleteCategory(id,lang);
    }
}
