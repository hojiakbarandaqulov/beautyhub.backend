package com.example.controller;

import com.example.dto.base.ApiResult;
import com.example.dto.category.CategoryCreateDTO;
import com.example.dto.category.CategoryDTO;
import com.example.entity.home_pages.Category;
import com.example.enums.LanguageEnum;
import com.example.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@Tag(name = "Kategoriya", description = "Kategoriya yaratish, tahrirlash, o'chirish va ko'rish APIlari")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/getAll")
    @Operation(
            summary = "Barcha kategoriyalar",
            description = "Tizimdagi barcha kategoriyalar ro'yxatini qaytaradi."
    )
    public ApiResult<List<CategoryDTO>> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_MASTER', 'ROLE_SALON_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/get/{id}")
    @Operation(
            summary = "Kategoriya tafsilotlari",
            description = "Berilgan id bo'yicha kategoriyaning to'liq ma'lumotini qaytaradi."
    )
    public ApiResult<CategoryDTO> getCategory(@PathVariable Long id, @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.getCategory(id, lang);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/create")
    @Operation(
            summary = "Kategoriya yaratish",
            description = "Yangi kategoriya yaratadi. Faqat adminlar foydalanishi mumkin."
    )
    public ApiResult<CategoryCreateDTO> createCategory(@RequestBody CategoryCreateDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.createCategory(dto,lang);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PutMapping("/update/{id}")
    @Operation(
            summary = "Kategoriya tahrirlash",
            description = "Berilgan id bo'yicha kategoriyani tahrirlaydi. Faqat adminlar uchun."
    )
    public ApiResult<CategoryCreateDTO> updateCategory(@PathVariable Long id, @RequestBody CategoryCreateDTO dto,
                                                       @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.updateCategory(id, dto,lang);
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/delete/{id}")
    @Operation(
            summary = "Kategoriya o'chirish",
            description = "Berilgan id bo'yicha kategoriyani o'chiradi. Faqat adminlar uchun."
    )
    public ApiResult<Boolean> deleteCategory(@PathVariable Long id,
                                             @RequestHeader(value = "Accept-Language",defaultValue = "ru") LanguageEnum lang) {
        return categoryService.deleteCategory(id,lang);
    }
}