package com.utad.apiAndroidBackend.reportes;

import com.utad.apiAndroidBackend.entidades.Reporte;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class ReporteRepositorio {

	private final JdbcTemplate jdbcTemplate;

	public ReporteRepositorio(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	// Registrar reporte
	public int registrar(Reporte reporte) {

		String sql = """
				INSERT INTO reportes
				(reportador_id, admin_id, tipo_objetivo, objetivo_id, categoria, motivo, estado, fecha_creacion)
				VALUES (?, ?, ?, ?, ?, ?, ?, ?)
				""";

		return jdbcTemplate.update(sql, statement -> {
			statement.setLong(1, reporte.reportador_id);
			statement.setLong(2, reporte.admin_id);
			statement.setString(3, reporte.tipo_objetivo);
			statement.setLong(4, reporte.objetivo_id);
			statement.setString(5, reporte.categoria);
			statement.setString(6, reporte.motivo);
			statement.setString(7, reporte.estado);
			statement.setString(8, reporte.fecha_creacion);
		});
	}

	// Listar todos los reportes
	public List<Reporte> listar() {
		String sql = "SELECT * FROM reportes ORDER BY fecha_creacion DESC";
		return jdbcTemplate.query(sql, (rs, rowNum) -> map(rs));
	}

	// Buscar reporte por ID
	public Reporte buscarPorId(Long reporteId) {
		String sql = "SELECT * FROM reportes WHERE id = ?";
		List<Reporte> reportes = jdbcTemplate.query(sql, ps -> ps.setLong(1, reporteId), (rs, rowNum) -> map(rs));
		return reportes.isEmpty() ? null : reportes.get(0);
	}

	// Cambiar estado del reporte
	public void cambiarEstado(Long reporteId, String nuevoEstado) {
		String sql = "UPDATE reportes SET estado = ?, fecha_resolucion = CURRENT_TIMESTAMP WHERE id = ?";
		jdbcTemplate.update(sql, ps -> {
			ps.setString(1, nuevoEstado);
			ps.setLong(2, reporteId);
		});
	}

	// Listar por categoría
	public List<Reporte> listarPorCategoria(String categoria) {
		String sql = "SELECT * FROM reportes WHERE categoria = ? ORDER BY fecha_creacion DESC";
		return jdbcTemplate.query(sql, ps -> ps.setString(1, categoria), (rs, rowNum) -> map(rs));
	}

	// listar por objetivo (tipo_objetivo + objetivo_id)
	public List<Reporte> listarPorObjetivo(String tipoObjetivo, Long objetivoId) {
		String sql = "SELECT * FROM reportes WHERE tipo_objetivo = ? AND objetivo_id = ? ORDER BY fecha_creacion DESC";
		return jdbcTemplate.query(sql, ps -> {
			ps.setString(1, tipoObjetivo);
			ps.setLong(2, objetivoId);
		}, (rs, rowNum) -> map(rs));
	}

	// Mapeo general
	private Reporte map(ResultSet rs) throws SQLException {
		Reporte r = new Reporte();
		r.id = rs.getLong("id");
		r.reportador_id = rs.getLong("reportador_id");
		r.admin_id = rs.getLong("admin_id");
		r.tipo_objetivo = rs.getString("tipo_objetivo");
		r.objetivo_id = rs.getLong("objetivo_id");
		r.categoria = rs.getString("categoria");
		r.motivo = rs.getString("motivo");
		r.estado = rs.getString("estado");
		r.fecha_creacion = rs.getString("fecha_creacion");
		r.fecha_resolucion = rs.getString("fecha_resolucion");
		return r;
	}
	
	public int contarReportes() {
	    String sql = "SELECT COUNT(*) FROM reportes";
	    return jdbcTemplate.queryForObject(sql, Integer.class);
	}

	public Map<String, Integer> contarPorCategoria() {
	    String sql = "SELECT categoria, COUNT(*) AS total FROM reportes GROUP BY categoria";

	    return jdbcTemplate.query(sql, rs -> {
	        Map<String, Integer> mapa = new HashMap<>();
	        while (rs.next()) {
	            mapa.put(rs.getString("categoria"), rs.getInt("total"));
	        }
	        return mapa;
	    });
	}
	
	// comprobar si ya existe un reporte de ese usuario sobre ese objetivo
    public boolean existeReportePorObjetivo(Long reportadorId,
                                            String tipoObjetivo,
                                            Long objetivoId) {

        String sql = """
            SELECT COUNT(*) 
            FROM reportes 
            WHERE reportador_id = ? 
              AND tipo_objetivo = ? 
              AND objetivo_id = ?
            """;

        Integer total = jdbcTemplate.queryForObject(sql, Integer.class,
                reportadorId, tipoObjetivo, objetivoId);

        return total != null && total > 0;
    }

}
