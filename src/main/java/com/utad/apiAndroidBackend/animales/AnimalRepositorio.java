package com.utad.apiAndroidBackend.animales;

import com.utad.apiAndroidBackend.entidades.Animal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AnimalRepositorio {

	private final JdbcTemplate jdbc;

	public AnimalRepositorio(JdbcTemplate jdbc) {
		this.jdbc = jdbc;
	}

	// Obtener un animal por su ID
	public Animal buscarPorId(Long animalId) {

		String sql = "SELECT * FROM animales WHERE id = ?";

		List<Animal> resultado = jdbc.query(sql, ps -> ps.setLong(1, animalId), (rs, rowNum) -> {

			Animal animal = new Animal();
			animal.id = rs.getLong("id");
			animal.usuario_id = rs.getLong("usuario_id");
			animal.nombre = rs.getString("nombre");
			animal.especie = rs.getString("especie");
			animal.raza = rs.getString("raza");
			animal.categoria_edad = rs.getString("categoria_edad");
			animal.genero = rs.getString("genero");
			animal.tamano = rs.getString("tamano");
			animal.descripcion = rs.getString("descripcion");
			animal.ciudad = rs.getString("ciudad");
			animal.estado_actual = rs.getString("estado_actual");
			animal.visibilidad = rs.getString("visibilidad");
			animal.contador_visitas = rs.getInt("contador_visitas");
			animal.fecha_creacion = rs.getString("fecha_creacion");

			return animal;
		});

		return resultado.isEmpty() ? null : resultado.get(0);
	}

	// Crear un nuevo animal (ID autogenerado)
	public Long crear(Animal animal) {

		String sql = """
				    INSERT INTO animales
				    (usuario_id, nombre, especie, raza, categoria_edad, genero, tamano, descripcion, ciudad, estado_actual, visibilidad, contador_visitas)
				    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
				""";

		jdbc.update(sql, ps -> {
			ps.setLong(1, animal.usuario_id);
			ps.setString(2, animal.nombre);
			ps.setString(3, animal.especie);
			ps.setString(4, animal.raza);
			ps.setString(5, animal.categoria_edad);
			ps.setString(6, animal.genero);
			ps.setString(7, animal.tamano);
			ps.setString(8, animal.descripcion);
			ps.setString(9, animal.ciudad);
			ps.setString(10, animal.estado_actual);
			ps.setString(11, animal.visibilidad);
			ps.setInt(12, animal.contador_visitas);
		});

		return jdbc.queryForObject("SELECT LAST_INSERT_ID()", Long.class);
	}

	public List<Animal> listarConFiltros(String estado, String especie, String edad) {

		StringBuilder sql = new StringBuilder("""
				    SELECT * FROM animales
				    WHERE visibilidad = 'PUBLICADA'
				""");

		if (estado != null && !estado.isBlank()) {
			sql.append(" AND estado_actual = '").append(estado).append("'");
		}

		if (especie != null && !especie.isBlank()) {
			sql.append(" AND especie = '").append(especie).append("'");
		}

		if (edad != null && !edad.isBlank()) {
			sql.append(" AND categoria_edad = '").append(edad).append("'");
		}

		sql.append(" ORDER BY fecha_creacion DESC");

		return jdbc.query(sql.toString(), (rs, rowNum) -> {
			Animal animal = new Animal();
			animal.id = rs.getLong("id");
			animal.usuario_id = rs.getLong("usuario_id");
			animal.nombre = rs.getString("nombre");
			animal.especie = rs.getString("especie");
			animal.raza = rs.getString("raza");
			animal.categoria_edad = rs.getString("categoria_edad");
			animal.genero = rs.getString("genero");
			animal.tamano = rs.getString("tamano");
			animal.descripcion = rs.getString("descripcion");
			animal.ciudad = rs.getString("ciudad");
			animal.estado_actual = rs.getString("estado_actual");
			animal.visibilidad = rs.getString("visibilidad");
			animal.fecha_creacion = rs.getString("fecha_creacion");
			return animal;
		});
	}

	public List<Animal> listarPorUsuario(Long usuarioId) {

		String sql = """
				    SELECT * FROM animales
				    WHERE usuario_id = ? AND visibilidad != 'ELIMINADA'
				    ORDER BY fecha_creacion DESC
				""";

		return jdbc.query(sql, ps -> ps.setLong(1, usuarioId), (rs, rowNum) -> {
			Animal animal = new Animal();
			animal.id = rs.getLong("id");
			animal.usuario_id = rs.getLong("usuario_id");
			animal.nombre = rs.getString("nombre");
			animal.especie = rs.getString("especie");
			animal.raza = rs.getString("raza");
			animal.categoria_edad = rs.getString("categoria_edad");
			animal.genero = rs.getString("genero");
			animal.tamano = rs.getString("tamano");
			animal.descripcion = rs.getString("descripcion");
			animal.ciudad = rs.getString("ciudad");
			animal.estado_actual = rs.getString("estado_actual");
			animal.visibilidad = rs.getString("visibilidad");
			animal.contador_visitas = rs.getInt("contador_visitas");
			animal.fecha_creacion = rs.getString("fecha_creacion");
			return animal;
		});
	}

	// Actualizar datos de un animal existente
	public int actualizar(Animal animal) {

		String sql = """
				UPDATE animales SET
				nombre = ?, especie = ?, raza = ?, categoria_edad = ?, genero = ?, tamano = ?, descripcion = ?, ciudad = ?, estado_actual = ?
				WHERE id = ?
				""";

		return jdbc.update(sql, ps -> {
			ps.setString(1, animal.nombre);
			ps.setString(2, animal.especie);
			ps.setString(3, animal.raza);
			ps.setString(4, animal.categoria_edad);
			ps.setString(5, animal.genero);
			ps.setString(6, animal.tamano);
			ps.setString(7, animal.descripcion);
			ps.setString(8, animal.ciudad);
			ps.setString(9, animal.estado_actual);
			ps.setLong(10, animal.id);
		});
	}

	// Listar animales visibles públicamente
	public List<Animal> listar() {

		String sql = "SELECT * FROM animales WHERE visibilidad = 'PUBLICADA'";

		return jdbc.query(sql, (rs, rowNum) -> {

			Animal animal = new Animal();
			animal.id = rs.getLong("id");
			animal.usuario_id = rs.getLong("usuario_id");
			animal.nombre = rs.getString("nombre");
			animal.especie = rs.getString("especie");
			animal.raza = rs.getString("raza");
			animal.categoria_edad = rs.getString("categoria_edad");
			animal.genero = rs.getString("genero");
			animal.tamano = rs.getString("tamano");
			animal.descripcion = rs.getString("descripcion");
			animal.ciudad = rs.getString("ciudad");
			animal.estado_actual = rs.getString("estado_actual");
			animal.visibilidad = rs.getString("visibilidad");
			animal.contador_visitas = rs.getInt("contador_visitas");
			animal.fecha_creacion = rs.getString("fecha_creacion");

			return animal;
		});
	}

	// Cambiar el estado actual del animal (EN_ADOPCION, PERDIDO, etc.)
	public int cambiarEstado(Long animalId, String nuevoEstado) {

		String sql = "UPDATE animales SET estado_actual = ? WHERE id = ?";

		return jdbc.update(sql, ps -> {
			ps.setString(1, nuevoEstado);
			ps.setLong(2, animalId);
		});
	}

	// Cambiar la visibilidad del animal (PUBLICADA, OCULTA, ELIMINADA)
	public int cambiarVisibilidad(Long animalId, String nuevaVisibilidad) {

		String sql = "UPDATE animales SET visibilidad = ? WHERE id = ?";

		return jdbc.update(sql, ps -> {
			ps.setString(1, nuevaVisibilidad);
			ps.setLong(2, animalId);
		});
	}

	public void actualizarEstadoModeracion(Long animalId, String nuevoEstado) {
		String sql = "UPDATE animales SET estado_moderacion = ? WHERE id = ?";
		jdbc.update(sql, nuevoEstado, animalId);
	}

	public void actualizarVisibilidad(Long animalId, boolean visible) {
		String sql = "UPDATE animales SET visibilidad = ? WHERE id = ?";
		jdbc.update(sql, visible ? "PUBLICADA" : "OCULTA", animalId);
	}

	public int contarAnimales() {
		String sql = "SELECT COUNT(*) FROM animales";
		return jdbc.queryForObject(sql, Integer.class);
	}

	public int contarPublicadas() {
		String sql = "SELECT COUNT(*) FROM animales WHERE visibilidad = 'PUBLICADA'";
		return jdbc.queryForObject(sql, Integer.class);
	}

	public Map<String, Integer> contarPorEspecie() {
		String sql = "SELECT especie, COUNT(*) AS total FROM animales GROUP BY especie";

		return jdbc.query(sql, rs -> {
			Map<String, Integer> mapa = new HashMap<>();
			while (rs.next()) {
				mapa.put(rs.getString("especie"), rs.getInt("total"));
			}
			return mapa;
		});
	}

	public List<Integer> publicacionesPorMes() {
		String sql = """
				    SELECT MONTH(fecha_creacion) AS mes, COUNT(*) AS total
				    FROM animales
				    WHERE fecha_creacion >= DATE_SUB(CURDATE(), INTERVAL 6 MONTH)
				    GROUP BY MONTH(fecha_creacion)
				    ORDER BY mes
				""";

		return jdbc.query(sql, (rs, rowNum) -> rs.getInt("total"));
	}

}
