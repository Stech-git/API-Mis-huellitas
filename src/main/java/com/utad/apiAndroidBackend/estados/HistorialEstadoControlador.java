package com.utad.apiAndroidBackend.estados;

import com.utad.apiAndroidBackend.entidades.HistorialEstado;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/estados")
public class HistorialEstadoControlador {

    private final HistorialEstadoServicio historialEstadoServicio;

    public HistorialEstadoControlador(HistorialEstadoServicio historialEstadoServicio) {
        this.historialEstadoServicio = historialEstadoServicio;
    }

    @GetMapping("/{animalId}")
    public List<HistorialEstado> obtenerHistorial(@PathVariable Long animalId) {
        return historialEstadoServicio.obtenerHistorialPorAnimal(animalId);
    }
}

