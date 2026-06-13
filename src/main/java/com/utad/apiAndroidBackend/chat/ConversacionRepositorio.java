package com.utad.apiAndroidBackend.chat;

import com.utad.apiAndroidBackend.entidades.Conversacion;
import com.utad.apiAndroidBackend.util.AESUtil;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;

@Repository
public class ConversacionRepositorio {

    private final JdbcTemplate jdbcTemplate;

    public ConversacionRepositorio(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Crear conversación normal (ya lo tenías)
    public void crear(Conversacion conversacion) {

        String sql = """
            INSERT INTO conversaciones
            (usuario_1_id, usuario_2_id, es_soporte, categoria_soporte, ultimo_mensaje, fecha_ultimo_mensaje)
            VALUES (?, ?, ?, ?, ?, ?)
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, conversacion.usuario_1_id);
            ps.setLong(2, conversacion.usuario_2_id);
            ps.setBoolean(3, conversacion.es_soporte);
            ps.setString(4, conversacion.categoria_soporte);
            ps.setString(5, conversacion.ultimo_mensaje);
            ps.setString(6, conversacion.fecha_ultimo_mensaje);
            return ps;
        }, keyHolder);

        conversacion.id = keyHolder.getKey().longValue();
    }

    // 🔥 NUEVO: Crear conversación de soporte
    public Long crearSoporte(Long usuarioId, Long adminId, String categoria) {

        String sql = """
            INSERT INTO conversaciones
            (usuario_1_id, usuario_2_id, es_soporte, categoria_soporte, ultimo_mensaje, fecha_ultimo_mensaje)
            VALUES (?, ?, TRUE, ?, NULL, NOW())
        """;

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setLong(1, usuarioId);
            ps.setLong(2, adminId);
            ps.setString(3, categoria);
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    // Buscar conversación por ID
    public Conversacion buscarPorId(Long conversacionId) {

        String sql = "SELECT * FROM conversaciones WHERE id = ?";

        List<Conversacion> conversaciones = jdbcTemplate.query(sql,
                ps -> ps.setLong(1, conversacionId),
                (result, rowNum) -> mapConversacion(result));

        return conversaciones.isEmpty() ? null : conversaciones.get(0);
    }

    // Listar conversaciones de un usuario
    public List<Conversacion> listarPorUsuario(Long usuarioId) {

        String sql = """
            SELECT * FROM conversaciones
            WHERE usuario_1_id = ? OR usuario_2_id = ?
            ORDER BY fecha_ultimo_mensaje DESC
        """;

        return jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, usuarioId);
            ps.setLong(2, usuarioId);
        }, (result, rowNum) -> {

            Conversacion conversacion = mapConversacion(result);

            // DESCIFRAR EL ÚLTIMO MENSAJE
            String ultimo = conversacion.ultimo_mensaje;
            conversacion.ultimo_mensaje = (ultimo != null) ? AESUtil.decrypt(ultimo) : null;

            return conversacion;
        });
    }

    // Buscar conversación normal entre dos usuarios
    public Conversacion buscarEntreUsuarios(Long u1, Long u2) {

        String sql = """
            SELECT * FROM conversaciones
            WHERE
                (usuario_1_id = ? AND usuario_2_id = ?)
                OR
                (usuario_1_id = ? AND usuario_2_id = ?)
            LIMIT 1
        """;

        List<Conversacion> lista = jdbcTemplate.query(sql, ps -> {
            ps.setLong(1, u1);
            ps.setLong(2, u2);
            ps.setLong(3, u2);
            ps.setLong(4, u1);
        }, (rs, rowNum) -> mapConversacion(rs));

        return lista.isEmpty() ? null : lista.get(0);
    }

    // Buscar si ya tiene chat de soporte
    public Conversacion buscarSoporte(Long usuarioId) {

        String sql = """
            SELECT * FROM conversaciones
            WHERE usuario_1_id = ? AND es_soporte = TRUE
            LIMIT 1
        """;

        List<Conversacion> lista = jdbcTemplate.query(sql,
                ps -> ps.setLong(1, usuarioId),
                (rs, rowNum) -> mapConversacion(rs));

        return lista.isEmpty() ? null : lista.get(0);
    }

    // Mapeo general
    private Conversacion mapConversacion(ResultSet rs) throws SQLException {
        Conversacion c = new Conversacion();
        c.id = rs.getLong("id");
        c.usuario_1_id = rs.getLong("usuario_1_id");
        c.usuario_2_id = rs.getLong("usuario_2_id");
        c.es_soporte = rs.getBoolean("es_soporte");
        c.categoria_soporte = rs.getString("categoria_soporte");
        c.ultimo_mensaje = rs.getString("ultimo_mensaje");
        c.fecha_ultimo_mensaje = rs.getString("fecha_ultimo_mensaje");
        return c;
    }

    public void actualizarUltimoMensaje(Long conversacionId, String mensajePlano) {

        String mensajeCifrado = AESUtil.encrypt(mensajePlano);

        String sql = """
            UPDATE conversaciones
            SET ultimo_mensaje = ?, fecha_ultimo_mensaje = NOW()
            WHERE id = ?
        """;

        jdbcTemplate.update(sql, ps -> {
            ps.setString(1, mensajeCifrado);
            ps.setLong(2, conversacionId);
        });
    }
}


