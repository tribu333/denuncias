package com.registro.denuncias.service;

import com.registro.denuncias.model.Complaint;
import com.registro.denuncias.model.Imagen;
import com.registro.denuncias.repository.ComplaintRepository;
import com.registro.denuncias.repository.ImagenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import com.registro.denuncias.dto.image.ImagenResponseDTO;
import com.registro.denuncias.model.Imagen;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface ImagenService {
    
    // Operaciones CRUD
    ImagenResponseDTO subirImagen(MultipartFile file, Long idComplaint);
    Optional<ImagenResponseDTO> findById(Long id);
    List<ImagenResponseDTO> getImagenesPorDenuncia(Long idComplaint);
    void deleteById(Long id);
    
    // Operaciones específicas
    Imagen findEntityById(Long id);
    Imagen findEntityByNombreArchivo(String nombreArchivo);
    long contarImagenesPorComplaint(Long idComplaint);
    void eliminarTodasImagenesComplaint(Long idComplaint);
    // Subir múltiples imágenes para un complaint
    List<ImagenResponseDTO> subirImagenesMasivas(MultipartFile[] files, Long idComplaint);
}