package com.utad.apiAndroidBackend.animales;

import com.utad.apiAndroidBackend.entidades.Animal;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/animales")
public class AnimalControlador {

    private final AnimalServicio servicio;

    public AnimalControlador(AnimalServicio servicio) {
        this.servicio = servicio;
    }

    @GetMapping("/{id}")
    public Animal obtener(@PathVariable Long id) {
        return servicio.obtener(id);
    }

    @PostMapping
    public Animal crear(@RequestBody CrearRequest req) {
        return servicio.crear(req.usuario, req.animal);
    }

    @PutMapping("/{id}")
    public Animal editar(@PathVariable Long id,
                         @RequestBody EditarRequest req) {
        return servicio.editar(id, req.usuario, req.animal);
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<Animal> listarPorUsuario(@PathVariable Long usuarioId) {
        return servicio.listarPorUsuario(usuarioId);
    }

    @GetMapping("/todos")
    public List<Animal> listar() {
        return servicio.listar();
    }

    @GetMapping
    public List<Animal> listarConFiltros(
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) String especie,
            @RequestParam(required = false) String edad
    ) {
        return servicio.listarConFiltros(estado, especie, edad);
    }

    @PutMapping("/{id}/ocultar")
    public void ocultar(@PathVariable Long id,
                        @RequestBody Usuario usuario) {
        servicio.ocultar(id, usuario);
    }

    @PutMapping("/{id}/restaurar")
    public void restaurar(@PathVariable Long id,
                          @RequestBody Usuario usuario) {
        servicio.restaurar(id, usuario);
    }

    @PutMapping("/{id}/eliminar")
    public void eliminar(@PathVariable Long id,
                         @RequestBody Usuario usuario) {
        servicio.eliminar(id, usuario);
    }

    static class CrearRequest {
        public Usuario usuario;
        public Animal animal;
    }

    static class EditarRequest {
        public Usuario usuario;
        public Animal animal;
    }
}