package com.registro.denuncias.service;

import com.registro.denuncias.model.Complaint;
import com.registro.denuncias.model.Imagen;
import com.registro.denuncias.repository.ComplaintRepository;
import com.registro.denuncias.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImagenService {
    
    private final ImagenRepository imagenRepository;
    private final ComplaintRepository complaintRepository;
    private final Path rootLocation = Paths.get("uploads");
    
    public Imagen guardarImagen(MultipartFile file, Long denunciaId) throws IOException {
        // Verificar que la denuncia existe
        Complaint denuncia = complaintRepository.findById(denunciaId)
                .orElseThrow(() -> new RuntimeException("Denuncia no encontrada"));
        
        // Crear nombre único para el archivo
        String nombreOriginal = file.getOriginalFilename();
        String extension = nombreOriginal.substring(nombreOriginal.lastIndexOf("."));
        String nombreArchivo = UUID.randomUUID().toString() + extension;
        
        // Crear directorio si no existe
        if (!Files.exists(rootLocation)) {
            Files.createDirectories(rootLocation);
        }
        
        // Guardar archivo en disco
        Path destinationFile = rootLocation.resolve(nombreArchivo);
        Files.copy(file.getInputStream(), destinationFile);
        
        // Crear y guardar entidad Imagen
        Imagen imagen = Imagen.builder()
                .nombreArchivo(nombreArchivo)
                .nombreOriginal(nombreOriginal)
                .rutaCompleta(destinationFile.toString())
                .mimeType(file.getContentType())
                .tamanioBytes(file.getSize())
                .denuncia(denuncia)
                .build();
        
        Imagen savedImagen = imagenRepository.save(imagen);
        
        // Actualizar relación en la denuncia
        denuncia.addImagen(savedImagen);
        complaintRepository.save(denuncia);
        
        log.info("Imagen guardada: {} para denuncia: {}", nombreArchivo, denunciaId);
        return savedImagen;
    }
    
    public List<Imagen> getImagenesPorDenuncia(Long denunciaId) {
        return imagenRepository.findByDenunciaId(denunciaId);
    }
}