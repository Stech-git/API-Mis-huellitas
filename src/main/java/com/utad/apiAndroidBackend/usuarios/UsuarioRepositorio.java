package com.utad.apiAndroidBackend.usuarios;

import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UsuarioRepositorio {

	private final JdbcTemplate jdbc;

	public UsuarioRepositorio(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public Usuario buscarPorId(Long usuarioId) {

		String sql = "SELECT * FROM usuarios WHERE id = ?";

		List<Usuario> usuarios = jdbc.query(sql, statement -> statement.setLong(1, usuarioId), (result, rowNum) -> {

			Usuario usuario = new Usuario();
			usuario.id = result.getLong("id");
			usuario.nombre = result.getString("nombre");
			usuario.email = result.getString("email");
			usuario.password_hash = result.getString("password_hash");
			usuario.es_admin = result.getBoolean("es_admin");
			usuario.estado = result.getString("estado");
			usuario.ciudad = result.getString("ciudad");
			usuario.telefono = result.getString("telefono");
			usuario.biografia = result.getString("biografia");
			usuario.imagen_perfil_url = result.getString("imagen_perfil_url");
			usuario.imagen_perfil_public_id = result.getString("imagen_perfil_public_id");
			usuario.mostrar_telefono = result.getBoolean("mostrar_telefono");
			usuario.fecha_creacion = result.getString("fecha_creacion");

			return usuario;
		});

		return usuarios.isEmpty() ? null : usuarios.get(0);
	}

	public List<Usuario> buscarUsuarios(String texto) {

		String sql = """
				SELECT *
				FROM usuarios
				WHERE estado = 'ACTIVO'
				AND (
				    LOWER(nombre) LIKE LOWER(?)
				    OR LOWER(email) LIKE LOWER(?)
				)
				ORDER BY nombre ASC
				LIMIT 20
				""";

		String busqueda = "%" + texto + "%";

		return jdbc.query(sql, ps -> {
			ps.setString(1, busqueda);
			ps.setString(2, busqueda);
		}, (result, rowNum) -> {

			Usuario usuario = new Usuario();
			usuario.id = result.getLong("id");
			usuario.nombre = result.getString("nombre");
			usuario.email = result.getString("email");
			usuario.es_admin = result.getBoolean("es_admin");
			usuario.estado = result.getString("estado");
			usuario.ciudad = result.getString("ciudad");
			usuario.telefono = result.getString("telefono");
			usuario.biografia = result.getString("biografia");
			usuario.imagen_perfil_url = result.getString("imagen_perfil_url");
			usuario.imagen_perfil_public_id = result.getString("imagen_perfil_public_id");
			usuario.mostrar_telefono = result.getBoolean("mostrar_telefono");
			usuario.fecha_creacion = result.getString("fecha_creacion");

			return usuario;
		});
	}

	public int actualizar(Usuario usuario) {

		String sql = """
				UPDATE usuarios SET
				nombre = ?, ciudad = ?, telefono = ?, biografia = ?, imagen_perfil_url = ?, mostrar_telefono = ?
				WHERE id = ?
				""";

		return jdbc.update(sql, statement -> {
			statement.setString(1, usuario.nombre);
			statement.setString(2, usuario.ciudad);
			statement.setString(3, usuario.telefono);
			statement.setString(4, usuario.biografia);
			statement.setString(5, usuario.imagen_perfil_url);
			statement.setBoolean(6, usuario.mostrar_telefono);
			statement.setLong(7, usuario.id);
		});
	}

	public int cambiarPassword(Long usuarioId, String nuevoPasswordHash) {

		String sql = "UPDATE usuarios SET password_hash = ? WHERE id = ?";

		return jdbc.update(sql, statement -> {
			statement.setString(1, nuevoPasswordHash);
			statement.setLong(2, usuarioId);
		});
	}

	public List<Usuario> listar() {

		String sql = "SELECT * FROM usuarios";

		return jdbc.query(sql, (result, rowNum) -> {

			Usuario usuario = new Usuario();
			usuario.id = result.getLong("id");
			usuario.nombre = result.getString("nombre");
			usuario.email = result.getString("email");
			usuario.es_admin = result.getBoolean("es_admin");
			usuario.estado = result.getString("estado");

			return usuario;
		});
	}

	public int cambiarEstado(Long usuarioId, String nuevoEstado) {

		String sql = "UPDATE usuarios SET estado = ? WHERE id = ?";

		return jdbc.update(sql, statement -> {
			statement.setString(1, nuevoEstado);
			statement.setLong(2, usuarioId);
		});
	}

	public int eliminarLogico(Long usuarioId) {
		String sql = "UPDATE usuarios SET estado = 'ELIMINADO' WHERE id = ?";
		return jdbc.update(sql, ps -> ps.setLong(1, usuarioId));
	}

	// Admin con menos reportes pendientes
	public Long obtenerAdminDisponible() {

		String sql = """
				    SELECT u.id
				    FROM usuarios u
				    LEFT JOIN reportes r ON r.admin_id = u.id AND r.estado = 'PENDIENTE'
				    WHERE u.es_admin = 1
				    GROUP BY u.id
				    ORDER BY COUNT(r.id) ASC
				    LIMIT 1
				""";

		return jdbc.queryForObject(sql, Long.class);
	}

	// Actualizar imagen de perfil
	public int actualizarImagenPerfil(Long usuarioId, String imagenPerfilUrl, String publicId) {

		String sql = """
				    UPDATE usuarios
				    SET imagen_perfil_url = ?, imagen_perfil_public_id = ?
				    WHERE id = ?
				""";

		return jdbc.update(sql, ps -> {
			ps.setString(1, imagenPerfilUrl);
			ps.setString(2, publicId);
			ps.setLong(3, usuarioId);
		});
	}

	// Eliminar foto de perfil
	public int eliminarImagenPerfil(Long usuarioId) {

		String sql = """
				    UPDATE usuarios
				    SET imagen_perfil_url = NULL,
				        imagen_perfil_public_id = NULL
				    WHERE id = ?
				""";

		return jdbc.update(sql, ps -> ps.setLong(1, usuarioId));
	}

	public int contarUsuarios() {
		String sql = "SELECT COUNT(*) FROM usuarios";
		return jdbc.queryForObject(sql, Integer.class);
	}

	// BLOQUEAR USUARIO
	public int bloquear(Long id) {
		String sql = "UPDATE usuarios SET estado = 'BLOQUEADO' WHERE id = ?";
		return jdbc.update(sql, id);
	}

	// ACTIVAR USUARIO
	public int activar(Long id) {
		String sql = "UPDATE usuarios SET estado = 'ACTIVO' WHERE id = ?";
		return jdbc.update(sql, id);
	}

	// ELIMINAR USUARIO
	public int eliminar(Long id) {
		String sql = "DELETE FROM usuarios WHERE id = ?";
		return jdbc.update(sql, id);
	}

}