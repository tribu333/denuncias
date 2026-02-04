package com.registro.denuncias.dto.image;
import java.time.LocalDateTime;
import lombok.*;
@Getter // Genera todos los getters
@Setter // Genera todos los setters
@NoArgsConstructor // Genera constructor sin argumentos
@AllArgsConstructor // Genera constructor con todos los argumentos
@ToString // Genera el método toString
@Builder // Permite usar el patrón Builder
public class ImagenResponseDTO {
    
    private Long idImagen;
    private String nombreArchivo;
    private String nombreOriginal;
    private String urlDescarga; // URL para descargar la imagen
    private String mimeType;
    private Long tamanioBytes;
    private String tamanioFormateado;
    private LocalDateTime fechaSubida;
    private Long idComplaint;
    /* // ========== CONSTRUCTORES ==========
    
    public ImagenResponseDTO() {
    }

    public ImagenResponseDTO(Integer idImagen, String nombreArchivo, String nombreOriginal, 
                            String urlDescarga, String mimeType, Long tamanioBytes, 
                            String tamanioFormateado,LocalDateTime fechaSubida,
                             Integer idComplaint) {
        this.idImagen = idImagen;
        this.nombreArchivo = nombreArchivo;
        this.nombreOriginal = nombreOriginal;
        this.urlDescarga = urlDescarga;
        this.mimeType = mimeType;
        this.tamanioBytes = tamanioBytes;
        this.tamanioFormateado = tamanioFormateado;
        this.fechaSubida = fechaSubida;
        this.idComplaint = idComplaint;
    }

    // ========== GETTERS Y SETTERS ==========
    
    public Integer getIdImagen() {
        return idImagen;
    }

    public void setIdImagen(Integer idImagen) {
        this.idImagen = idImagen;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getNombreOriginal() {
        return nombreOriginal;
    }

    public void setNombreOriginal(String nombreOriginal) {
        this.nombreOriginal = nombreOriginal;
    }

    public String getUrlDescarga() {
        return urlDescarga;
    }

    public void setUrlDescarga(String urlDescarga) {
        this.urlDescarga = urlDescarga;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public Long getTamanioBytes() {
        return tamanioBytes;
    }

    public void setTamanioBytes(Long tamanioBytes) {
        this.tamanioBytes = tamanioBytes;
    }

    public String getTamanioFormateado() {
        return tamanioFormateado;
    }

    public void setTamanioFormateado(String tamanioFormateado) {
        this.tamanioFormateado = tamanioFormateado;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Integer getIdComplaint() {
        return idComplaint;
    }

    public void setIdComplaint(Integer idComplaint) {
        this.idComplaint = idComplaint;
    } */
}