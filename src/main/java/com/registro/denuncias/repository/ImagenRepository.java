package com.registro.denuncias.repository;

import com.registro.denuncias.model.Imagen;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ImagenRepository extends JpaRepository<Imagen, Long> {
    
    List<Imagen> findByDenunciaId(Long denunciaId);
    
    void deleteByNombreArchivo(String nombreArchivo);
}