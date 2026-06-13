package com.utad.apiAndroidBackend.salud;

import com.utad.apiAndroidBackend.entidades.HistorialSalud;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/salud")
public class HistorialSaludControlador {

    private final HistorialSaludServicio historialSaludServicio;

    public HistorialSaludControlador(HistorialSaludServicio historialSaludServicio) {
        this.historialSaludServicio = historialSaludServicio;
    }

    // Registrar evento
    @PostMapping("/registrar")
    public void registrarEvento(@RequestBody RegistrarEventoRequest request) {
    	historialSaludServicio.registrarEvento(
    		    request.animalId,
    		    request.tipoEvento,
    		    request.descripcion,
    		    request.fechaEvento
    		);

    }

    // Obtener historial
    @GetMapping("/{animalId}")
    public List<HistorialSalud> obtenerHistorial(@PathVariable Long animalId) {
        return historialSaludServicio.obtenerHistorialPorAnimal(animalId);
    }

    // DTO interno
    static class RegistrarEventoRequest {
        public Long animalId;
        public String tipoEvento;
        public String descripcion;
        public String fechaEvento; 
    }

}

