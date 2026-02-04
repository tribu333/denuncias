package com.registro.denuncias.controller;

import com.registro.denuncias.service.ImagenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {
    
    private final ImagenService imagenService;
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("denunciaId") Long denunciaId) {
        
        try {
            var imagen = imagenService.guardarImagen(file, denunciaId);
            return ResponseEntity.ok(imagen);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/denuncia/{denunciaId}")
    public ResponseEntity<?> getImagenesPorDenuncia(@PathVariable Long denunciaId) {
        return ResponseEntity.ok(imagenService.getImagenesPorDenuncia(denunciaId));
    }
}