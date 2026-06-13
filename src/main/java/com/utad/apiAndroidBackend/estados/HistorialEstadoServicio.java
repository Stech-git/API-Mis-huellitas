package com.utad.apiAndroidBackend.estados;

import com.utad.apiAndroidBackend.entidades.HistorialEstado;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class HistorialEstadoServicio {

    private final HistorialEstadoRepositorio historialEstadoRepositorio;

    public HistorialEstadoServicio(HistorialEstadoRepositorio historialEstadoRepositorio) {
        this.historialEstadoRepositorio = historialEstadoRepositorio;
    }

    // Estados válidos según tu base de datos
    private final List<String> estadosValidos = List.of(
            "EN_ADOPCION",
            "PERDIDO",
            "ENCONTRADO",
            "VULNERABLE",
            "ADOPTADO",
            "EN_TRATAMIENTO"
    );

    // Validar estado
    private void validarEstado(String nuevoEstado) {
        if (!estadosValidos.contains(nuevoEstado)) {
            throw new Excepciones.EstadoAnimalInvalidoException("Estado inválido: " + nuevoEstado);
        }
    }

    // Registrar cambio de estado
    public void registrarCambio(Long animalId, String estadoAnterior, String estadoNuevo) {

        validarEstado(estadoNuevo);

        HistorialEstado historial = new HistorialEstado();
        historial.animal_id = animalId;
        historial.estado_anterior = estadoAnterior;
        historial.estado_nuevo = estadoNuevo;
        historial.fecha_cambio = LocalDateTime.now().toString();

        historialEstadoRepositorio.registrar(historial);
    }

    // Obtener historial completo de un animal
    public List<HistorialEstado> obtenerHistorialPorAnimal(Long animalId) {
        return historialEstadoRepositorio.obtenerPorAnimal(animalId);
    }
}

