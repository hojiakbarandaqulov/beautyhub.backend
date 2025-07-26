package com.example.controller;


import com.example.dto.root.RootCreateDTO;
import com.example.dto.root.RootDTO;
import com.example.service.RootService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roots")
public class RootController {
    private final RootService rootService;

    public RootController(RootService rootService) {
        this.rootService = rootService;
    }

    @GetMapping("/all/detail")
    public ResponseEntity<List<RootDTO>> getAllRoots() {
        return ResponseEntity.ok(rootService.getAllRoots());
    }

    @GetMapping("/{id}")
    public ResponseEntity<RootDTO> getRootById(@PathVariable Long id) {
        return ResponseEntity.ok(rootService.getRootById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<RootCreateDTO> createRoot(@RequestBody @Valid RootCreateDTO rootDTO) {
        return ResponseEntity.ok(rootService.createRoot(rootDTO));
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<RootCreateDTO> updateRoot(@PathVariable Long id, @RequestBody RootCreateDTO rootDTO) {
        return ResponseEntity.ok(rootService.updateRoot(id, rootDTO));
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Boolean> deleteRoot(@PathVariable Long id) {
        rootService.deleteRoot(id);
        return ResponseEntity.ok(true);
    }
}