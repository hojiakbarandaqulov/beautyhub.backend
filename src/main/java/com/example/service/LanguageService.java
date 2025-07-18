/*
package com.example.service;

import com.example.dto.language.LanguageCreateUpdateDto;
import com.example.dto.language.LanguageDto;
import com.example.entity.LanguageEntity;
import com.example.exp.AppBadException;
import com.example.repository.LanguageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class LanguageService {
    private final LanguageRepository languageRepository;

    */
/**
     * Barcha tillar ro'yxatini qaytaradi.
     * Til nomlari 'currentLangCode'ga qarab tarjimalanadi.
     *
     * @param currentLangCode Foydalanuvchining joriy tili kodi (masalan, "uz", "ru").
     * @return LanguageDto ro'yxati.
     *//*

    public List<LanguageDto> getAllLanguages(String currentLangCode) {
        List<LanguageEntity> languages = languageRepository.findAllByOrderByOrderNumberAsc(); // Tartib bo'yicha olish
        return languages.stream()
                .map(lang -> mapToLanguageDto(lang, currentLangCode))
                .collect(Collectors.toList());
    }

    public LanguageDto getLanguageByCode(String code, String currentLangCode) {
        LanguageEntity language = languageRepository.findByCode(code)
                .orElseThrow(() -> new AppBadException("Til topilmadi: " + code, HttpStatus.NOT_FOUND));
        return mapToLanguageDto(language, currentLangCode);
    }

    @Transactional
    public LanguageDto createLanguage(LanguageCreateUpdateDto dto) {
        if (languageRepository.existsById(dto.getCode().toLowerCase())) {
            throw new AppBadException("Bu til kodi allaqachon mavjud!", HttpStatus.BAD_REQUEST);
        }

        LanguageEntity language = new LanguageEntity();
        language.setCode(dto.getCode().toLowerCase());
        language.setNameUz(dto.getNameUz());
        language.setNameRu(dto.getNameRu());
        language.setNameEn(dto.getNameEn());
        language.setFlagIconUrl(dto.getFlagIconUrl());
        language.setOrderNumber(dto.getOrderNumber());

        Language savedLanguage = languageRepository.save(language);
        return mapToLanguageDto(savedLanguage, savedLanguage.getCode()); // Yaratilgan tilning o'z tilida qaytarish
    }

    */
/**
     * Mavjud til ma'lumotlarini yangilaydi. (Admin paneli uchun)
     *
     * @param code Yangilanadigan til kodi.
     * @param dto Yangilangan til ma'lumotlari.
     * @return Yangilangan LanguageDto obyekti.
     *//*

    @Transactional
    public LanguageDto updateLanguage(String code, LanguageCreateUpdateDto dto) {
        LanguageEntity language = languageRepository.findByCode(code)
                .orElseThrow(() -> new AppBadException("Til topilmadi: " + code, HttpStatus.NOT_FOUND));

        if (dto.getNameUz() != null) language.setNameUz(dto.getNameUz());
        if (dto.getNameRu() != null) language.setNameRu(dto.getNameRu());
        if (dto.getNameEn() != null) language.setNameEn(dto.getNameEn());
        if (dto.getFlagIconUrl() != null) language.setFlagIconUrl(dto.getFlagIconUrl());
        if (dto.getOrderNumber() != null) language.setOrderNumber(dto.getOrderNumber());

        Language updatedLanguage = languageRepository.save(language);
        return mapToLanguageDto(updatedLanguage, updatedLanguage.getCode());
    }

    */
/**
     * Tilni ma'lumotlar bazasidan o'chiradi. (Admin paneli uchun)
     *
     * @param code O'chiriladigan til kodi.
     * @throws AppBadException() Agar til topilmasa.
     *//*

    @Transactional
    public void deleteLanguage(String code) {
        if (!languageRepository.existsById(code)) {
            throw new AppBadException("Til topilmadi: ");
        }
        languageRepository.deleteById(code);
    }

    */
/**
     * Language Entity'dan LanguageDto'ga ma'lumotlarni o'tkazish metodi.
     * Til nomini 'currentLangCode'ga qarab tarjimalaydi.
     *
     * @param language Language Entity obyekti.
     * @param currentLangCode Foydalanuvchining joriy tili kodi.
     * @return LanguageDto obyekti.
     *//*

    private LanguageDto mapToLanguageDto(LanguageEntity language, String currentLangCode) {
        LanguageDto dto = new LanguageDto();
        dto.setCode(language.getCode());
        if ("ru".equalsIgnoreCase(currentLangCode)) {
            dto.setName(language.getNameRu());
        } else if ("en".equalsIgnoreCase(currentLangCode)) {
            dto.setName(language.getNameEn() != null ? language.getNameEn() : language.getNameUz()); // Agar EN nomi bo'lmasa, UZ nomini ishlatish
        } else { // Default o'zbekcha
            dto.setName(language.getNameUz());
        }
        dto.setFlagIconUrl(language.getFlagIconUrl());
        dto.setOrderNumber(language.getOrderNumber());
        // isSelected maydoni Controllerda yoki Frontendda foydalanuvchi tanlagan tilga qarab o'rnatiladi
        return dto;
    }
}
*/
