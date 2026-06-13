package com.utad.apiAndroidBackend.salud;

import com.utad.apiAndroidBackend.entidades.HistorialSalud;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistorialSaludRepositorio {

    private final JdbcTemplate jdbcTemplate;

    public HistorialSaludRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Registrar evento de salud
    public int registrar(HistorialSalud eventoSalud) {

        String sql = """
                INSERT INTO historial_salud
                (animal_id, tipo_evento, descripcion, fecha_evento)
                VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql, statement -> {
            statement.setLong(1, eventoSalud.animal_id);
            statement.setString(2, eventoSalud.tipo_evento);
            statement.setString(3, eventoSalud.descripcion);
            statement.setString(4, eventoSalud.fecha_evento);
        });
    }

    // Obtener historial por animal
    public List<HistorialSalud> obtenerPorAnimal(Long animalId) {

        String sql = """
                SELECT * FROM historial_salud
                WHERE animal_id = ?
                ORDER BY fecha_evento DESC
                """;

        return jdbcTemplate.query(sql,
                statement -> statement.setLong(1, animalId),
                (result, rowNum) -> {

                    HistorialSalud historial = new HistorialSalud();
                    historial.id = result.getLong("id");
                    historial.animal_id = result.getLong("animal_id");
                    historial.tipo_evento = result.getString("tipo_evento");
                    historial.descripcion = result.getString("descripcion");
                    historial.fecha_evento = result.getString("fecha_evento");
                    historial.proxima_cita = result.getString("proxima_cita");

                    return historial;
                });
    }
}

