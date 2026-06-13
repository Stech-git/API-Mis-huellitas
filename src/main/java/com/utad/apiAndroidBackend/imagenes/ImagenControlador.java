package com.utad.apiAndroidBackend.imagenes;

import com.utad.apiAndroidBackend.entidades.ImagenAnimal;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/imagenes")
public class ImagenControlador {

    private final ImagenServicio imagenServicio;

    public ImagenControlador(ImagenServicio imagenServicio) {
        this.imagenServicio = imagenServicio;
    }

    @PostMapping("/agregar")
    public ImagenAnimal agregarImagen(@RequestBody AgregarImagenRequest request) {
        return imagenServicio.agregarImagen(
                request.animalId,
                request.usuario,
                request.urlImagen
        );
    }

    @GetMapping("/animal/{animalId}")
    public List<ImagenAnimal> listarImagenes(@PathVariable Long animalId) {
        return imagenServicio.listarImagenes(animalId);
    }

    @PutMapping("/principal/{imagenId}")
    public void marcarPrincipal(
            @PathVariable Long imagenId,
            @RequestBody Usuario usuario
    ) {
        imagenServicio.marcarPrincipal(imagenId, usuario);
    }

    @DeleteMapping("/{imagenId}")
    public void eliminarImagen(
            @PathVariable Long imagenId,
            @RequestBody Usuario usuario
    ) {
        imagenServicio.eliminarImagen(imagenId, usuario);
    }
    
    @PostMapping("/subir-archivo")
    public ImagenAnimal subirImagenArchivo(
            @RequestParam("animalId") Long animalId,
            @RequestParam("usuarioId") Long usuarioId,
            @RequestParam("file") MultipartFile file
    ) {
        Usuario usuario = new Usuario();
        usuario.id = usuarioId;

        return imagenServicio.subirImagenArchivo(animalId, usuario, file);
    }

    static class AgregarImagenRequest {
        public Long animalId;
        public Usuario usuario;
        public String urlImagen;
    }
}
