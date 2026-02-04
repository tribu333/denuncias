package com.registro.denuncias.controller;

import com.registro.denuncias.model.Imagen;
import com.registro.denuncias.service.FileStorageService;
import com.registro.denuncias.service.ImagenServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
@RestController
@RequestMapping("/api/imagenes")
@RequiredArgsConstructor
public class ImagenController {
    
    private final ImagenServiceImpl imagenService;

    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImagen(
            @RequestParam("file") MultipartFile file,
            @RequestParam("denunciaId") Long denunciaId) {
        
        try {
            var imagen = imagenService.subirImagen(file, denunciaId);
            return ResponseEntity.ok(imagen);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/denuncia/{denunciaId}")
    public ResponseEntity<?> getImagenesPorDenuncia(@PathVariable Long denunciaId) {
        return ResponseEntity.ok(imagenService.getImagenesPorDenuncia(denunciaId));
    }
     // ========== DESCARGAR/VER IMAGEN ==========
    
    /**
     * Descarga una imagen por nombre de archivo
     * GET /api/imagenes/descargar/{nombreArchivo}
     * 
     * Ejemplo: GET /api/imagenes/descargar/abc123-def456.jpg
     * 
     * Esta URL se usa en el frontend para mostrar imágenes:
     * <img src="http://localhost:8080/api/imagenes/descargar/abc123-def456.jpg" />
     */
    @Operation(
        summary = "Descargar imagen por nombre de archivo",
        description = "Recupera una imagen por su nombre de archivo. Este endpoint se puede usar directamente en etiquetas <img> del frontend."
    )
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "Imagen recuperada exitosamente",
            content = {
                @Content(mediaType = "image/jpeg"),
                @Content(mediaType = "image/png"),
                @Content(mediaType = "image/gif")
            }
        ),
        @ApiResponse(
            responseCode = "404",
            description = "Imagen no encontrada"
        )
    })
    @GetMapping("/descargar/{nombreArchivo:.+}")
    public ResponseEntity<Resource> descargarImagen(
            @PathVariable String nombreArchivo,
            HttpServletRequest request) {
        try {
            // Cargar el archivo como Resource
            Resource resource = fileStorageService.loadFileAsResource(nombreArchivo);
            
            // Obtener información de la imagen de la BD
            Imagen imagen = imagenService.findEntityByNombreArchivo(nombreArchivo);
            
            // Determinar el tipo de contenido
            String contentType = imagen.getMimeType();
            if (contentType == null) {
                contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
            }
            if (contentType == null) {
                contentType = "application/octet-stream";
            }
            
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, 
                            "inline; filename=\"" + imagen.getNombreOriginal() + "\"")
                    .body(resource);
                    
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}