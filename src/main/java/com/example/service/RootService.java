package com.example.service;


import com.example.dto.root.RootCreateDTO;
import com.example.dto.root.RootDTO;
import com.example.entity.Root;
import com.example.exp.AppBadException;
import com.example.mapper.RootMapper;
import com.example.repository.RootRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class RootService {
    private final RootRepository rootRepository;
    private final RootMapper rootMapper;


    public List<RootDTO> getAllRoots() {
        return rootRepository.findAll().stream()
                .map(rootMapper::toDto)
                .collect(Collectors.toList());
    }

    public RootDTO getRootById(Long id) {
        Root root = rootRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Root not found with id: " + id));
        return rootMapper.toDto(root);
    }

    public RootCreateDTO createRoot(RootCreateDTO rootDTO) {
        Root root = rootMapper.toCreateRootEntity(rootDTO);
        Root savedRoot = rootRepository.save(root);
        return rootMapper.toRootCreateDto(savedRoot);
    }

    public RootCreateDTO updateRoot(Long id, RootCreateDTO rootDTO) {
        Root existingRoot = rootRepository.findById(id)
                .orElseThrow(() -> new AppBadException("Root not found with id: " + id));

        existingRoot.setName(rootDTO.getName());
        existingRoot.setWidth(rootDTO.getWidth());
        existingRoot.setHeight(rootDTO.getHeight());
        existingRoot.setDescription(rootDTO.getDescription());

        Root updatedRoot = rootRepository.save(existingRoot);
        return rootMapper.toRootCreateDto(updatedRoot);
    }

    public void deleteRoot(Long id) {
        if (!rootRepository.existsById(id)) {
            throw new AppBadException("Root not found with id: " + id);
        }
        rootRepository.deleteById(id);
    }
}