package com.example.controller;

import com.example.dto.attach.AttachDTO;
import com.example.service.AttachService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attach")
public class AttachController {

    private final AttachService attachService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "upload api", description = "Api list attach create")
    public ResponseEntity<AttachDTO> create(@RequestParam("file") MultipartFile file) {
        log.info("upload attach  = {}", file.getOriginalFilename());
        AttachDTO response = attachService.upload(file);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/open_general/{fileName}", produces = MediaType.ALL_VALUE)
    public byte[] open_general(@PathVariable("fileName") String fileName) {
        return attachService.open_general(fileName);
    }

    @GetMapping("/download/{fileName}")
    @Operation(summary = "download file api", description = "")
    public ResponseEntity<Resource> download(@PathVariable("fileName") String fileName) {
        log.info("download attach  ={}", fileName);
        return attachService.download(fileName);
    }

}


