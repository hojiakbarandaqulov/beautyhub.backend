package com.example.service;

import com.example.dto.attach.AttachDTO;
import com.example.entity.AttachEntity;
import com.example.exp.AppBadException;
import com.example.repository.AttachRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import org.springframework.core.io.Resource;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class AttachService {
    @Value("${server.url}")
    private String serverUrl;
    @Value("${attach.upload.url}")
    public String attachUrl;

    private final AttachRepository attachRepository;

    private static final Map<String, Object> imageExtensionMap = new HashMap<>();

    static {
        imageExtensionMap.put("jpg", new Object());
        imageExtensionMap.put("png", new Object());
        imageExtensionMap.put("jpeg", new Object());
    }

    public AttachDTO upload(MultipartFile file) {
        if (file.isEmpty()) {
            log.warn("Attach error : file not found");
            throw new AppBadException("File not found");
        }

        String pathFolder = getYmDString();
        File folder = new File(attachUrl + "/" + pathFolder);
        if (!folder.exists()) {
            boolean t = folder.mkdirs();
        }

        String key = UUID.randomUUID().toString(); // dasdasd-dasdasda-asdasda-asdasd
        String extension = getExtension(Objects.requireNonNull(file.getOriginalFilename()));
        try {
            AttachEntity entity = new AttachEntity();
            entity.setId(key+"."+extension);
            entity.setPath(pathFolder);
            entity.setSize(file.getSize());
            entity.setCreatedDate(LocalDateTime.now());
            entity.setOrigenName(file.getOriginalFilename());
            entity.setExtension(extension);
            attachRepository.save(entity);

            byte[] bytes = file.getBytes();
            Path path = Paths.get(attachUrl + "/" + pathFolder + "/" + entity.getId() + "." + extension);
            Files.write(path, bytes);
            return toDTO(entity);
        } catch (IOException e) {
            log.warn("Attach error : {}", e.getMessage());
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    public byte[] open_general(String attachId) {
        AttachEntity entity = getEntity(attachId);
        try {
            BufferedImage originalImage = ImageIO.read(new File(getPath(entity)));
            ByteArrayOutputStream boas = new ByteArrayOutputStream();
            ImageIO.write(originalImage, getTempExtension(entity.getExtension()), boas);
            boas.flush();
            byte[] imageInByte = boas.toByteArray();
            boas.close();
            return imageInByte;
        } catch (Exception e) {
            log.warn("Attach error : {}", e.getMessage());
            return new byte[0];
        }
    }

    public ResponseEntity<Resource> download(String fileName) {
        AttachEntity entity = getAttach(fileName);
        try {
            Path file = Paths.get(getPath(entity));
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + entity.getOrigenName() + "\"").body(resource);
            } else {
                log.warn("Attach error : Could not read the file!");
                throw new RuntimeException("Could not read the file!");
            }
        } catch (MalformedURLException e) {
            log.warn("Attach error : {}", e.getMessage());
            throw new RuntimeException("Error: " + e.getMessage());
        }
    }

    public boolean delete(String id){
        AttachEntity entity = get(id);
        attachRepository.delete(id);
        File file=new File(attachUrl + "/" + entity.getPath() + "/" + entity.getId());
        boolean b=false;
        if(file.exists()){
            b=file.delete();
        }
        return b;
    }

    private AttachEntity get(String attachId) {
        return attachRepository.findById(attachId).orElseThrow(() -> {
            throw new AppBadException("Attach not found");
        });
    }

    public String getYmDString() {
        int year = Calendar.getInstance().get(Calendar.YEAR);
        int month = Calendar.getInstance().get(Calendar.MONTH) + 1;
        int day = Calendar.getInstance().get(Calendar.DATE);

        return year + "/" + month + "/" + day; // 2024/06/08
    }

    public String getExtension(String fileName) { // mp3/jpg/npg/mp4.....
        int lastIndex = fileName.lastIndexOf(".");
        return fileName.substring(lastIndex + 1);
    }

    public AttachDTO toDTO(AttachEntity entity) {
        AttachDTO dto = new AttachDTO();
        dto.setId(entity.getId());
        dto.setCreatedDate((entity.getCreatedDate()));
        dto.setExtension(entity.getExtension());
        dto.setPath(entity.getPath());
        dto.setSize(entity.getSize());
        dto.setOriginalName(entity.getOrigenName());
        dto.setUrl(serverUrl + "/attach/upload/" + entity.getId());
        return dto;
    }

    public AttachDTO attachDTO(String photoId) {
        if (photoId == null){
            return null;
        }
        AttachDTO attachDTO=new AttachDTO();
        attachDTO.setId(photoId);
        attachDTO.setUrl(openUrl(photoId));
        return attachDTO;
    }

    public AttachEntity getEntity(String id) {
        Optional<AttachEntity> optional = attachRepository.findById(id);
        if (optional.isEmpty()) {
            log.warn("Attach error : file not found");
            throw new AppBadException("File not found");
        }
        return optional.get();
    }
    public AttachEntity getAttach(String origenName) {
        Optional<AttachEntity> optional = attachRepository.findByOrigenName(origenName);
        if (optional.isEmpty()) {
            log.warn("Attach error : file not found");
            throw new AppBadException("File not found");
        }
        return optional.get();
    }


    public String getTempExtension(String extension) {
        String extSml = extension.toLowerCase();
        return extSml.equals("jpg") || extSml.equals("png") ? "png" : extension;
    }

    private String openUrl(String fileName) {
        return attachUrl + "/"+fileName;
    }
    private String getPath(AttachEntity entity) {
        return  attachUrl + "/" + entity.getPath() + "/" + entity.getId() + "." + entity.getExtension();
    }

    public Optional<AttachEntity> getImage(String imageId) {
        Optional<AttachEntity> byId = attachRepository.findById(imageId);
        if (byId.isEmpty()){
            throw new AppBadException("File not found");
        }
        return byId;
    }
}

