package com.utad.apiAndroidBackend.chat;

import com.utad.apiAndroidBackend.entidades.Mensaje;
import com.utad.apiAndroidBackend.util.AESUtil;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MensajeRepositorio {

    private final JdbcTemplate jdbcTemplate;
    private final ConversacionRepositorio conversacionRepo;

    public MensajeRepositorio(JdbcTemplate jdbcTemplate, ConversacionRepositorio conversacionRepo) {
        this.jdbcTemplate = jdbcTemplate;
        this.conversacionRepo = conversacionRepo;
    }

    // enviar mensaje para soporte y chats normales
    public void enviarMensaje(Long conversacionId, Long emisorId, String contenidoPlano) {

        String contenidoCifrado = AESUtil.encrypt(contenidoPlano);

        String sql = """
            INSERT INTO mensajes
            (conversacion_id, emisor_id, contenido, leido, fecha_envio)
            VALUES (?, ?, ?, FALSE, NOW())
        """;

        jdbcTemplate.update(sql, ps -> {
            ps.setLong(1, conversacionId);
            ps.setLong(2, emisorId);
            ps.setString(3, contenidoCifrado);
        });

        // Actualizar último mensaje
        conversacionRepo.actualizarUltimoMensaje(conversacionId, contenidoPlano);
    }

    // Listar mensajes
    public List<Mensaje> listarPorConversacion(Long conversacionId) {

        String sql = """
            SELECT m.*, u.nombre AS emisor_nombre
            FROM mensajes m
            INNER JOIN usuarios u ON u.id = m.emisor_id
            WHERE m.conversacion_id = ?
            ORDER BY m.fecha_envio ASC
        """;

        return jdbcTemplate.query(sql,
                ps -> ps.setLong(1, conversacionId),
                (result, rowNum) -> {

                    Mensaje mensaje = new Mensaje();
                    mensaje.id = result.getLong("id");
                    mensaje.conversacion_id = result.getLong("conversacion_id");
                    mensaje.emisor_id = result.getLong("emisor_id");
                    mensaje.emisor_nombre = result.getString("emisor_nombre");

                    mensaje.contenido = AESUtil.decrypt(result.getString("contenido"));
                    mensaje.leido = result.getBoolean("leido");
                    mensaje.fecha_envio = result.getString("fecha_envio");

                    return mensaje;
                });
    }

    public int contarNoLeidos(Long conversacionId, Long usuarioId) {

        String sql = """
            SELECT COUNT(*) FROM mensajes
            WHERE conversacion_id = ?
            AND emisor_id <> ?
            AND leido = FALSE
        """;

        return jdbcTemplate.queryForObject(sql, Integer.class, conversacionId, usuarioId);
    }

    public void marcarLeidos(Long conversacionId, Long usuarioId) {

        String sql = """
            UPDATE mensajes
            SET leido = TRUE
            WHERE conversacion_id = ? AND emisor_id != ?
        """;

        jdbcTemplate.update(sql, ps -> {
            ps.setLong(1, conversacionId);
            ps.setLong(2, usuarioId);
        });
    }
}


