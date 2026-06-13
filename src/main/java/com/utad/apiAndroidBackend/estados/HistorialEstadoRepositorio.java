package com.utad.apiAndroidBackend.estados;

import com.utad.apiAndroidBackend.entidades.HistorialEstado;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class HistorialEstadoRepositorio {

    private final JdbcTemplate jdbcTemplate;

    public HistorialEstadoRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Registrar un cambio de estado
    public int registrar(HistorialEstado historialEstado) {

        String sql = """
                INSERT INTO historial_estados
                (animal_id, estado_anterior, estado_nuevo, fecha_cambio)
                VALUES (?, ?, ?, ?)
                """;

        return jdbcTemplate.update(sql, statement -> {
            statement.setLong(1, historialEstado.animal_id);
            statement.setString(2, historialEstado.estado_anterior);
            statement.setString(3, historialEstado.estado_nuevo);
            statement.setString(4, historialEstado.fecha_cambio);
        });
    }

    // Obtener historial por animal
    public List<HistorialEstado> obtenerPorAnimal(Long animalId) {

        String sql = """
                SELECT * FROM historial_estados
                WHERE animal_id = ?
                ORDER BY fecha_cambio DESC
                """;

        return jdbcTemplate.query(sql,
                statement -> statement.setLong(1, animalId),
                (result, rowNum) -> {

                    HistorialEstado historial = new HistorialEstado();
                    historial.id = result.getLong("id");
                    historial.animal_id = result.getLong("animal_id");
                    historial.estado_anterior = result.getString("estado_anterior");
                    historial.estado_nuevo = result.getString("estado_nuevo");
                    historial.fecha_cambio = result.getString("fecha_cambio");

                    return historial;
                });
    }
}
