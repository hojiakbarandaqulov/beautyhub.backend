/*
package com.example.controller;

import com.example.dto.language.LanguageDto;
import com.example.service.LanguageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/languages")
public class LanguageController {

    private final LanguageService languageService;

    @GetMapping
    public ResponseEntity<List<LanguageDto>> getAllLanguages(@RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        List<LanguageDto> languages = languageService.getAllLanguages(lang);
        return ResponseEntity.ok(languages);
    }


    @GetMapping("/{code}")
    public ResponseEntity<LanguageDto> getLanguageByCode(@PathVariable String code, @RequestHeader(value = "Accept-Language", defaultValue = "uz") String lang) {
        LanguageDto language = languageService.getLanguageByCode(code, lang);
        return ResponseEntity.ok(language);
    }

    */
/**
     * Yangi tilni qo'shadi. (Admin paneli uchun)
     *
     * @param dto Yangi til ma'lumotlari.
     * @return Yaratilgan LanguageDto obyekti.
     *//*

    @PostMapping
    public ResponseEntity<LanguageDto> createLanguage(@RequestBody LanguageCreateUpdateDto dto) {
        LanguageDto newLanguage = languageService.createLanguage(dto);
        return ResponseEntity.status(201).body(newLanguage);
    }

    */
/**
     * Mavjud tilni yangilaydi. (Admin paneli uchun)
     *
     * @param code Yangilanadigan til kodi.
     * @param dto Yangilangan til ma'lumotlari.
     * @return Yangilangan LanguageDto obyekti.
     *//*

    @PutMapping("/{code}")
    public ResponseEntity<LanguageDto> updateLanguage(@PathVariable String code, @RequestBody LanguageCreateUpdateDto dto) {
        LanguageDto updatedLanguage = languageService.updateLanguage(code, dto);
        return ResponseEntity.ok(updatedLanguage);
    }

    */
/**
     * Tilni o'chiradi. (Admin paneli uchun)
     *
     * @param code O'chiriladigan til kodi.
     * @return Bo'sh javob (204 No Content).
     *//*

    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteLanguage(@PathVariable String code) {
        languageService.deleteLanguage(code);
        return ResponseEntity.noContent().build();
    }
}
*/
