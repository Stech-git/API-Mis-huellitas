package com.utad.apiAndroidBackend.auth;

import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.entidades.CodigoAdmin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuthRepositorio {

    private final JdbcTemplate jdbc;

    public AuthRepositorio(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Usuario buscarPorEmail(String email) {

        String sql = "SELECT * FROM usuarios WHERE email = ?";

        List<Usuario> usuarios = jdbc.query(sql,
                ps -> ps.setString(1, email),
                (rs, rowNum) -> mapUsuario(rs));

        return usuarios.isEmpty() ? null : usuarios.get(0);
    }

    public int registrar(Usuario usuario) {

        String sql = """
            INSERT INTO usuarios
            (nombre, email, password_hash, es_admin, estado)
            VALUES (?, ?, ?, ?, ?)
        """;

        return jdbc.update(sql, ps -> {
            ps.setString(1, usuario.nombre);
            ps.setString(2, usuario.email);
            ps.setString(3, usuario.password_hash);
            ps.setBoolean(4, usuario.es_admin);
            ps.setString(5, usuario.estado);
        });
    }

    public CodigoAdmin buscarCodigoAdmin(String codigoIngresado) {

        String sql = "SELECT * FROM codigos_admin WHERE codigo = ? AND usado = FALSE";

        List<CodigoAdmin> codigos = jdbc.query(sql,
                ps -> ps.setString(1, codigoIngresado),
                (rs, rowNum) -> {
                    CodigoAdmin codigo = new CodigoAdmin();
                    codigo.id = rs.getLong("id");
                    codigo.codigo = rs.getString("codigo");
                    codigo.usado = rs.getBoolean("usado");
                    return codigo;
                });

        return codigos.isEmpty() ? null : codigos.get(0);
    }

    public void marcarCodigoUsado(Long codigoId) {
        String sql = "UPDATE codigos_admin SET usado = TRUE WHERE id = ?";
        jdbc.update(sql, ps -> ps.setLong(1, codigoId));
    }

    private Usuario mapUsuario(java.sql.ResultSet rs) throws java.sql.SQLException {
        Usuario usuario = new Usuario();
        usuario.id = rs.getLong("id");
        usuario.nombre = rs.getString("nombre");
        usuario.email = rs.getString("email");
        usuario.password_hash = rs.getString("password_hash");
        usuario.es_admin = rs.getBoolean("es_admin");
        usuario.estado = rs.getString("estado");
        usuario.ciudad = rs.getString("ciudad");
        usuario.telefono = rs.getString("telefono");
        usuario.biografia = rs.getString("biografia");
        usuario.imagen_perfil_url = rs.getString("imagen_perfil_url");
        usuario.mostrar_telefono = rs.getBoolean("mostrar_telefono");
        usuario.fecha_creacion = rs.getString("fecha_creacion");
        return usuario;
    }
}
