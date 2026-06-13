package com.utad.apiAndroidBackend.admin;

import com.utad.apiAndroidBackend.entidades.CodigoAdmin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CodigoAdminRepositorio {

    private final JdbcTemplate jdbc;

    public CodigoAdminRepositorio(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    // Buscar un código de administrador por su valor
    public CodigoAdmin buscar(String codigoIngresado) {

        String sql = "SELECT * FROM codigos_admin WHERE codigo = ?";

        List<CodigoAdmin> codigosEncontrados = jdbc.query(sql,
                ps -> ps.setString(1, codigoIngresado),
                (rs, rowNum) -> {
                    CodigoAdmin codigo = new CodigoAdmin();
                    codigo.id = rs.getLong("id");
                    codigo.codigo = rs.getString("codigo");
                    codigo.usado = rs.getBoolean("usado");
                    codigo.fecha_creacion = rs.getString("fecha_creacion");
                    return codigo;
                });

        return codigosEncontrados.isEmpty() ? null : codigosEncontrados.get(0);
    }

    // Marcar un código como usado
    public void marcarUsado(Long codigoId) {

        String sql = "UPDATE codigos_admin SET usado = TRUE WHERE id = ?";

        jdbc.update(sql, ps -> ps.setLong(1, codigoId));
    }
}
