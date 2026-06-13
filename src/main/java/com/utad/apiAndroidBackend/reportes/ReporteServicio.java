package com.utad.apiAndroidBackend.reportes;

import com.utad.apiAndroidBackend.admin.AdminServicio;
import com.utad.apiAndroidBackend.entidades.Reporte;
import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import com.utad.apiAndroidBackend.usuarios.UsuarioRepositorio;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReporteServicio {

	private final ReporteRepositorio reporteRepositorio;
	private final UsuarioRepositorio usuarioRepositorio;
	private final AdminServicio adminServicio;

	public ReporteServicio(ReporteRepositorio reporteRepositorio, UsuarioRepositorio usuarioRepositorio,
			AdminServicio adminServicio) {
		this.reporteRepositorio = reporteRepositorio;
		this.usuarioRepositorio = usuarioRepositorio;
		this.adminServicio = adminServicio;
	}

	private final List<String> estadosValidos = List.of("PENDIENTE", "INVESTIGANDO", "RESUELTO", "RECHAZADO");

	public void registrarReporte(Usuario reportador, Reporte datosReporte) {

	    if (datosReporte.motivo == null || datosReporte.motivo.isBlank()) {
	        throw new Excepciones.DatosInvalidosException("El motivo del reporte es obligatorio");
	    }
	    if (datosReporte.tipo_objetivo == null || datosReporte.tipo_objetivo.isBlank()) {
	        throw new Excepciones.DatosInvalidosException("El tipo de objetivo es obligatorio");
	    }
	    if (datosReporte.objetivo_id == null) {
	        throw new Excepciones.DatosInvalidosException("El ID del objetivo es obligatorio");
	    }

	    Long adminId = usuarioRepositorio.obtenerAdminDisponible();

	    Reporte nuevoReporte = new Reporte();
	    nuevoReporte.reportador_id = reportador.id;
	    nuevoReporte.admin_id = adminId;
	    nuevoReporte.tipo_objetivo = datosReporte.tipo_objetivo;
	    nuevoReporte.objetivo_id = datosReporte.objetivo_id;
	    nuevoReporte.categoria = datosReporte.categoria;
	    nuevoReporte.motivo = datosReporte.motivo;
	    nuevoReporte.estado = "PENDIENTE";
	    nuevoReporte.fecha_creacion = LocalDateTime.now().toString();

	    reporteRepositorio.registrar(nuevoReporte);
	}

	public List<Reporte> listarReportes(Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("Solo los administradores pueden ver los reportes");
		}
		return reporteRepositorio.listar();
	}

	public List<Reporte> listarPorCategoria(Usuario admin, String categoria) {

		if (!admin.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}
		return reporteRepositorio.listarPorCategoria(categoria);
	}
	public void cambiarEstado(Long reporteId, String nuevoEstado, Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("Solo los administradores pueden gestionar reportes");
		}
		if (!estadosValidos.contains(nuevoEstado)) {
			throw new Excepciones.DatosInvalidosException("Estado inválido: " + nuevoEstado);
		}
		Reporte reporte = reporteRepositorio.buscarPorId(reporteId);
		if (reporte == null) {
			throw new Excepciones.ReporteNoEncontradoException("Reporte no encontrado");
		}
		reporteRepositorio.cambiarEstado(reporteId, nuevoEstado);
		adminServicio.registrarAccion(administrador, "CAMBIAR_ESTADO_REPORTE", reporte.tipo_objetivo,
				reporte.objetivo_id, "El admin cambió el estado del reporte a " + nuevoEstado);
	}

	// listar por objetivo genérico
	public List<Reporte> listarPorObjetivo(String tipoObjetivo, Long objetivoId) {
		return reporteRepositorio.listarPorObjetivo(tipoObjetivo, objetivoId);
	}

	// comprobar si ya existe reporte
	public boolean existeReporte(Long reportadorId, String tipoObjetivo, Long objetivoId) {
		return reporteRepositorio.existeReportePorObjetivo(reportadorId, tipoObjetivo, objetivoId);
	}
}