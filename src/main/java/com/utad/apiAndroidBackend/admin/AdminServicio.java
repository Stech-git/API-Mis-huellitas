package com.utad.apiAndroidBackend.admin;

import com.utad.apiAndroidBackend.animales.AnimalRepositorio;
import com.utad.apiAndroidBackend.entidades.AccionAdmin;
import com.utad.apiAndroidBackend.entidades.CodigoAdmin;
import com.utad.apiAndroidBackend.entidades.Reporte;
import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import com.utad.apiAndroidBackend.reportes.ReporteRepositorio;
import com.utad.apiAndroidBackend.usuarios.UsuarioRepositorio;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class AdminServicio {

	private final AccionAdminRepositorio accionAdminRepositorio;
	private final CodigoAdminRepositorio codigoAdminRepositorio;
	private final UsuarioRepositorio usuarioRepositorio;
	private final AnimalRepositorio animalRepositorio;
	private final ReporteRepositorio reporteRepositorio;

	public AdminServicio(AccionAdminRepositorio accionAdminRepositorio, CodigoAdminRepositorio codigoAdminRepositorio,
			UsuarioRepositorio usuarioRepositorio, AnimalRepositorio animalRepositorio,
			ReporteRepositorio reporteRepositorio) {
		this.accionAdminRepositorio = accionAdminRepositorio;
		this.codigoAdminRepositorio = codigoAdminRepositorio;
		this.usuarioRepositorio = usuarioRepositorio;
		this.animalRepositorio = animalRepositorio;
		this.reporteRepositorio = reporteRepositorio;
	}

	public Usuario buscarUsuarioPorId(Long id) {
		return usuarioRepositorio.buscarPorId(id);
	}

	public void bloquearUsuario(Long id) {
		usuarioRepositorio.bloquear(id);
	}

	public void activarUsuario(Long id) {
		usuarioRepositorio.activar(id);
	}

	public void eliminarUsuario(Long id) {
		usuarioRepositorio.eliminar(id);
	}

	public void activarAdmin(Usuario usuario, String codigoIngresado) {

		CodigoAdmin codigoEncontrado = codigoAdminRepositorio.buscar(codigoIngresado);

		if (codigoEncontrado == null) {
			throw new Excepciones.DatosInvalidosException("Código inválido");
		}

		if (codigoEncontrado.usado) {
			throw new Excepciones.DatosInvalidosException("Este código ya fue usado");
		}

		usuario.es_admin = true;
		codigoAdminRepositorio.marcarUsado(codigoEncontrado.id);
	}

	public void registrarAccion(Usuario administrador, String tipoAccion, String tipoObjetivo, Long objetivoId,
			String descripcion) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}

		AccionAdmin nuevaAccion = new AccionAdmin();
		nuevaAccion.admin_id = administrador.id;
		nuevaAccion.tipo_accion = tipoAccion;
		nuevaAccion.tipo_objetivo = tipoObjetivo;
		nuevaAccion.objetivo_id = objetivoId;
		nuevaAccion.descripcion = descripcion;
		nuevaAccion.fecha_accion = LocalDateTime.now().toString();

		accionAdminRepositorio.registrar(nuevaAccion);
	}

	public List<AccionAdmin> listarAcciones(Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}

		return accionAdminRepositorio.listar();
	}

	// MODERACIÓN DE PUBLICACIONES
	public void cambiarEstadoPublicacion(Long animalId, Usuario admin, String nuevoEstado) {

		if (!admin.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}

		animalRepositorio.actualizarEstadoModeracion(animalId, nuevoEstado);

		registrarAccion(admin, "CAMBIAR_ESTADO_PUBLICACION", "ANIMAL", animalId, "Estado cambiado a: " + nuevoEstado);
	}

	public void cambiarVisibilidadPublicacion(Long animalId, Usuario admin, boolean visible) {

		if (!admin.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}

		animalRepositorio.actualizarVisibilidad(animalId, visible);

		registrarAccion(admin, "CAMBIAR_VISIBILIDAD_PUBLICACION", "ANIMAL", animalId,
				visible ? "Publicación visible" : "Publicación oculta");
	}

	public AdminControlador.ReportesResumenResponse obtenerResumenReportes(Long animalId, Usuario admin) {

		if (!admin.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("No eres administrador");
		}

		List<Reporte> reportes = reporteRepositorio.listarPorObjetivo("ANIMAL", animalId);

		AdminControlador.ReportesResumenResponse resp = new AdminControlador.ReportesResumenResponse();
		resp.animal_id = animalId;
		resp.total_reportes = reportes.size();
		resp.por_categoria = new HashMap<>();

		String ultimoMotivo = null;
		String ultimaFecha = null;

		for (Reporte r : reportes) {
			resp.por_categoria.merge(r.categoria, 1, Integer::sum);
			ultimoMotivo = r.motivo;
			ultimaFecha = r.fecha_creacion;
		}

		resp.ultimo_motivo = ultimoMotivo;
		resp.ultima_fecha = ultimaFecha;

		return resp;
	}

	public AdminControlador.EstadisticasResponse obtenerEstadisticas() {

		AdminControlador.EstadisticasResponse resp = new AdminControlador.EstadisticasResponse();

		// Totales
		resp.totalUsuarios = usuarioRepositorio.contarUsuarios();
		resp.totalAnimales = animalRepositorio.contarAnimales();
		resp.totalPublicaciones = animalRepositorio.contarPublicadas();
		resp.totalReportes = reporteRepositorio.contarReportes();

		// Si no tienes adopciones, lo dejamos en 0
		resp.totalAdopciones = 0;

		// Si no tienes visitas hoy, lo dejamos en 0
		resp.visitasHoy = 0;

		// Animales por especie
		resp.animalesPorEspecie = animalRepositorio.contarPorEspecie();

		// Publicaciones por mes (últimos 6 meses)
		resp.publicacionesPorMes = animalRepositorio.publicacionesPorMes();

		// Adopciones por mes (si no existe, devolvemos lista vacía)
		resp.adopcionesPorMes = Arrays.asList(0, 0, 0, 0, 0, 0);

		// Reportes por categoría
		resp.reportesPorCategoria = reporteRepositorio.contarPorCategoria();

		return resp;
	}

}
