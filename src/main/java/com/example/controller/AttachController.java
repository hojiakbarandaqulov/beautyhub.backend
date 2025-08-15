package com.example.controller;

import com.example.dto.attach.AttachDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attach")
@Tag(name = "Fayl biriktirish", description = "Fayl biriktirish va olish uchun APIlar")
public class AttachController {

    private final AttachService attachService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Fayl yuklash API",
            description = "Ushbu endpoint orqali foydalanuvchi faylni serverga yuklaydi. Yuklangan fayl haqida ma'lumot (id, nomi va h.k.) qaytariladi. Bu API orqali profil rasmi, hujjat yoki boshqa fayllarni yuklashingiz mumkin."
    )
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        log.info("upload attach  = {}", file.getOriginalFilename());
        AttachDTO response = attachService.upload(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
    @Operation(
            summary = "Faylni ochish (preview) API",
            description = "Ushbu endpoint yuklangan faylni nomi orqali ochib beradi. Asosan rasm yoki boshqa fayllarni preview qilish uchun ishlatiladi. Bunda faylning o'zi byte[] ko'rinishida qaytariladi."
    )
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        return attachService.open_general(fileName);
    }

    @GetMapping("/download/{fileName}")
    @Operation(
            summary = "Faylni yuklab olish API",
            description = "Bu endpoint orqali faylni nomi orqali yuklab olishingiz mumkin. Fayl serverdan ko'chirib olinadi va Resource ko'rinishida javob qaytariladi. Profil rasmi, hujjat yoki boshqa fayllarni yuklab olish uchun ishlatiladi."
    )
    public ResponseEntity<Resource> download(@PathVariable("fileName") String fileName) {
        log.info("download attach  ={}", fileName);
        return attachService.download(fileName);
    }
}