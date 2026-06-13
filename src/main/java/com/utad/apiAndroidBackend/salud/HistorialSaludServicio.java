package com.utad.apiAndroidBackend.salud;

import com.utad.apiAndroidBackend.entidades.HistorialSalud;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class HistorialSaludServicio {

    private final HistorialSaludRepositorio historialSaludRepositorio;

    public HistorialSaludServicio(HistorialSaludRepositorio historialSaludRepositorio) {
        this.historialSaludRepositorio = historialSaludRepositorio;
    }

    // Tipos válidos según tu base de datos
    private final List<String> tiposValidos = List.of(
    	    "VACUNA",
    	    "DESPARASITACION",
    	    "CIRUGIA",
    	    "REVISION",
    	    "TRATAMIENTO",
    	    "URGENCIA",
    	    "OTRO"
    	);

    // Validar tipo de evento
    private void validarTipoEvento(String tipoEvento) {
        if (!tiposValidos.contains(tipoEvento)) {
            throw new Excepciones.DatosInvalidosException("Tipo de evento no válido: " + tipoEvento);
        }
    }

    // Registrar evento de salud
    public void registrarEvento(Long animalId, String tipoEvento, String descripcionEvento, String fechaEvento) {

        validarTipoEvento(tipoEvento);

        HistorialSalud evento = new HistorialSalud();
        evento.animal_id = animalId;
        evento.tipo_evento = tipoEvento;
        evento.descripcion = descripcionEvento;
        evento.fecha_evento = fechaEvento; 

        historialSaludRepositorio.registrar(evento);
    }

    // Obtener historial de salud de un animal
    public List<HistorialSalud> obtenerHistorialPorAnimal(Long animalId) {
        return historialSaludRepositorio.obtenerPorAnimal(animalId);
    }
}
