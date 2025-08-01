package com.example.service;

import com.example.dto.base.ApiResult;
import com.example.dto.category.CategoryCreateDTO;
import com.example.dto.category.CategoryDTO;
import com.example.entity.home_pages.Category;
import com.example.enums.LanguageEnum;
import com.example.exp.AppBadException;
import com.example.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ResourceBundleService messageService;

    public CategoryService(CategoryRepository categoryRepository, ResourceBundleService messageService) {
        this.categoryRepository = categoryRepository;
        this.messageService = messageService;
    }

    public ApiResult<List<CategoryDTO>> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryDTO> dtos = categories.stream().map(cat -> {
            CategoryDTO dto = new CategoryDTO();
            dto.setId(cat.getId());
            dto.setNameUz(cat.getNameUz());
            dto.setNameEn(cat.getNameEn());
            dto.setNameRu(cat.getNameRu());
            dto.setIconUrl(cat.getIconUrl());
            return dto;
        }).collect(Collectors.toList());
        return ApiResult.successResponse(dtos);
    }


    public ApiResult<CategoryDTO> getCategory(Long id, LanguageEnum lang) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("category.not.found", lang)));
        CategoryDTO dto = new CategoryDTO();
        dto.setId(category.getId());
        dto.setNameUz(category.getNameUz());
        dto.setNameEn(category.getNameEn());
        dto.setNameRu(category.getNameRu());
        dto.setIconUrl(category.getIconUrl());
        return ApiResult.successResponse(dto);
    }

    public ApiResult<CategoryCreateDTO> createCategory(CategoryCreateDTO dto, LanguageEnum lang) {
        boolean existsByNameUz = categoryRepository.existsByNameUz(dto.getNameUz());
        if (existsByNameUz) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }
        boolean existsByNameEn = categoryRepository.existsByNameEn(dto.getNameEn());
        if (existsByNameEn) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }
        boolean existsByNameRu = categoryRepository.existsByNameRu(dto.getNameRu());
        if (existsByNameRu) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }
        Category category = new Category();
        category.setNameUz(dto.getNameUz());
        category.setNameEn(dto.getNameEn());
        category.setNameRu(dto.getNameRu());
        category.setIconUrl(dto.getIconUrl());
        category.setCreatedDate(LocalDateTime.now());
        category = categoryRepository.save(category);

        CategoryCreateDTO result = new CategoryCreateDTO();
        result.setNameUz(category.getNameUz());
        result.setNameEn(category.getNameEn());
        result.setNameRu(category.getNameEn());
        result.setIconUrl(category.getIconUrl());
        return ApiResult.successResponse(result);
    }

    public ApiResult<CategoryCreateDTO> updateCategory(Long id, CategoryCreateDTO dto, LanguageEnum lang) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("category.not.found", lang)));

        // Optionally check for name uniqueness
        if (!category.getNameUz().equals(dto.getNameUz()) && categoryRepository.existsByNameUz(dto.getNameUz())) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }
        if (!category.getNameEn().equals(dto.getNameEn()) && categoryRepository.existsByNameEn(dto.getNameEn())) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }
        if (!category.getNameRu().equals(dto.getNameRu()) && categoryRepository.existsByNameRu(dto.getNameRu())) {
            throw new AppBadException(messageService.getMessage("category.already.exists", lang));
        }

        category.setNameUz(dto.getNameUz());
        category.setNameEn(dto.getNameEn());
        category.setNameRu(dto.getNameRu());
        category.setIconUrl(dto.getIconUrl());
        category = categoryRepository.save(category);

        CategoryCreateDTO result = new CategoryCreateDTO();
        result.setNameUz(category.getNameUz());
        result.setNameEn(category.getNameEn());
        result.setNameRu(category.getNameRu());
        result.setIconUrl(category.getIconUrl());
        return ApiResult.successResponse(result);
    }

    public ApiResult<Boolean> deleteCategory(Long id, LanguageEnum lang) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new AppBadException(messageService.getMessage("category.not.found", lang)));
        categoryRepository.delete(category);
        return ApiResult.successResponse(true);
    }
}
