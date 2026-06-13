package com.utad.apiAndroidBackend.admin;

import com.utad.apiAndroidBackend.entidades.AccionAdmin;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AccionAdminRepositorio {

	private final JdbcTemplate jdbc;

	public AccionAdminRepositorio(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	public int registrar(AccionAdmin accion) {

		String sql = """
				INSERT INTO acciones_admin
				(admin_id, tipo_accion, tipo_objetivo, objetivo_id, descripcion, fecha_accion)
				VALUES (?, ?, ?, ?, ?, ?)
				""";

		return jdbc.update(sql, ps -> {
			ps.setLong(1, accion.admin_id);
			ps.setString(2, accion.tipo_accion);
			ps.setString(3, accion.tipo_objetivo);
			ps.setLong(4, accion.objetivo_id);
			ps.setString(5, accion.descripcion);
			ps.setString(6, accion.fecha_accion);
		});
	}

	public List<AccionAdmin> listar() {

		String sql = "SELECT * FROM acciones_admin ORDER BY fecha_accion DESC";

		return jdbc.query(sql, (rs, rowNum) -> {
			AccionAdmin accion = new AccionAdmin();
			accion.id = rs.getLong("id");
			accion.admin_id = rs.getLong("admin_id");
			accion.tipo_accion = rs.getString("tipo_accion");
			accion.tipo_objetivo = rs.getString("tipo_objetivo");
			accion.objetivo_id = rs.getLong("objetivo_id");
			accion.descripcion = rs.getString("descripcion");
			accion.fecha_accion = rs.getString("fecha_accion");
			return accion;
		});
	}

	public int bloquear(Long id) {
		return jdbc.update("UPDATE usuarios SET estado = 'BLOQUEADO' WHERE id = ?", id);
	}

	public int activar(Long id) {
		return jdbc.update("UPDATE usuarios SET estado = 'ACTIVO' WHERE id = ?", id);
	}

	public int eliminar(Long id) {
		return jdbc.update("DELETE FROM usuarios WHERE id = ?", id);
	}

}
