package com.utad.apiAndroidBackend.imagenes;

import com.utad.apiAndroidBackend.entidades.ImagenAnimal;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

@Repository
public class ImagenRepositorio {

	private final JdbcTemplate jdbcTemplate;

	public ImagenRepositorio(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public ImagenAnimal crear(ImagenAnimal imagen) {

		String sql = """
				    INSERT INTO imagenes_animales
				    (animal_id, url_imagen, public_id, es_principal)
				    VALUES (?, ?, ?, ?)
				""";

		KeyHolder keyHolder = new GeneratedKeyHolder();

		jdbcTemplate.update(connection -> {
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setLong(1, imagen.animalId);
			ps.setString(2, imagen.urlImagen);
			ps.setString(3, imagen.publicId);
			ps.setBoolean(4, imagen.esPrincipal);
			return ps;
		}, keyHolder);

		if (keyHolder.getKey() != null) {
			Long idGenerado = keyHolder.getKey().longValue();
			return buscarPorId(idGenerado);
		}

		return imagen;
	}

	public List<ImagenAnimal> obtenerPorAnimal(Long animalId) {

		String sql = """
				    SELECT *
				    FROM imagenes_animales
				    WHERE animal_id = ?
				    ORDER BY es_principal DESC, fecha_creacion ASC
				""";

		return jdbcTemplate.query(sql, (result, rowNum) -> {
			ImagenAnimal imagen = new ImagenAnimal();

			imagen.id = result.getLong("id");
			imagen.animalId = result.getLong("animal_id");
			imagen.urlImagen = result.getString("url_imagen");
			imagen.publicId = result.getString("public_id");
			imagen.esPrincipal = result.getBoolean("es_principal");
			imagen.fechaCreacion = result.getString("fecha_creacion");

			return imagen;
		}, animalId);
	}

	public ImagenAnimal buscarPorId(Long imagenId) {

		String sql = """
				    SELECT *
				    FROM imagenes_animales
				    WHERE id = ?
				""";

		List<ImagenAnimal> imagenes = jdbcTemplate.query(sql, (result, rowNum) -> {
			ImagenAnimal imagen = new ImagenAnimal();

			imagen.id = result.getLong("id");
			imagen.animalId = result.getLong("animal_id");
			imagen.urlImagen = result.getString("url_imagen");
			imagen.publicId = result.getString("public_id");
			imagen.esPrincipal = result.getBoolean("es_principal");
			imagen.fechaCreacion = result.getString("fecha_creacion");

			return imagen;
		}, imagenId);

		return imagenes.isEmpty() ? null : imagenes.get(0);
	}

	public void desmarcarPrincipales(Long animalId) {
		String sql = """
				    UPDATE imagenes_animales
				    SET es_principal = FALSE
				    WHERE animal_id = ?
				""";

		jdbcTemplate.update(sql, animalId);
	}

	public void marcarComoPrincipal(Long imagenId) {
		String sql = """
				    UPDATE imagenes_animales
				    SET es_principal = TRUE
				    WHERE id = ?
				""";

		jdbcTemplate.update(sql, imagenId);
	}

	public void eliminar(Long imagenId) {
		String sql = """
				    DELETE FROM imagenes_animales
				    WHERE id = ?
				""";

		jdbcTemplate.update(sql, imagenId);
	}
}