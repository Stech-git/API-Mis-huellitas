package com.utad.apiAndroidBackend.admin;

import com.utad.apiAndroidBackend.entidades.AccionAdmin;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminControlador {

	private final AdminServicio servicio;

	public AdminControlador(AdminServicio servicio) {
		this.servicio = servicio;
	}

	// ACTIVAR ADMIN
	@PostMapping("/activar")
	public void activar(@RequestBody ActivarRequest req) {
		servicio.activarAdmin(req.usuario, req.codigo);
	}

	// ACCIONES ADMIN
	@PostMapping("/accion")
	public void registrarAccion(@RequestBody RegistrarAccionRequest req) {
		servicio.registrarAccion(req.admin, req.tipo_accion, req.tipo_objetivo, req.objetivo_id, req.descripcion);
	}

	@PostMapping("/acciones")
	public List<AccionAdmin> listar(@RequestBody Usuario admin) {
		return servicio.listarAcciones(admin);
	}

	// MODERACIÓN DE PUBLICACIONES
	// Cambiar estado moderado de una publicación
	@PutMapping("/publicacion/estado/{animalId}")
	public void cambiarEstadoPublicacion(@PathVariable Long animalId,
			@RequestBody CambiarEstadoPublicacionRequest req) {
		servicio.cambiarEstadoPublicacion(animalId, req.admin, req.nuevoEstado);
	}

	// Cambiar visibilidad (ocultar / mostrar)
	@PutMapping("/publicacion/visibilidad/{animalId}")
	public void cambiarVisibilidadPublicacion(@PathVariable Long animalId,
			@RequestBody CambiarVisibilidadPublicacionRequest req) {
		servicio.cambiarVisibilidadPublicacion(animalId, req.admin, req.visible);
	}

	// Resumen de reportes de una publicación
	@PostMapping("/publicacion/reportes-resumen/{animalId}")
	public ReportesResumenResponse obtenerResumenReportes(@PathVariable Long animalId, @RequestBody Usuario admin) {
		return servicio.obtenerResumenReportes(animalId, admin);
	}

	@GetMapping("/estadisticas")
	public EstadisticasResponse obtenerEstadisticas() {
		return servicio.obtenerEstadisticas();
	}
	
	@PutMapping("/usuario/bloquear/{usuarioId}")
	public void bloquearUsuario(@PathVariable Long usuarioId) {
	    servicio.bloquearUsuario(usuarioId);
	}

	@PutMapping("/usuario/activar/{usuarioId}")
	public void activarUsuario(@PathVariable Long usuarioId) {
	    servicio.activarUsuario(usuarioId);
	}

	@DeleteMapping("/usuario/{usuarioId}")
	public void eliminarUsuario(@PathVariable Long usuarioId) {
	    servicio.eliminarUsuario(usuarioId);
	}

	

	// Datos
	public static class ActivarRequest {
		public Usuario usuario;
		public String codigo;
	}

	public static class RegistrarAccionRequest {
		public Usuario admin;
		public String tipo_accion;
		public String tipo_objetivo;
		public Long objetivo_id;
		public String descripcion;
	}

	public static class CambiarEstadoPublicacionRequest {
		public Usuario admin;
		public String nuevoEstado; // REVISADA, PENDIENTE_VERIFICACION, OCULTA, INAPROPIADA
	}

	public static class CambiarVisibilidadPublicacionRequest {
		public Usuario admin;
		public boolean visible;
	}

	public static class ReportesResumenResponse {
		public Long animal_id;
		public int total_reportes;
		public Map<String, Integer> por_categoria;
		public String ultimo_motivo;
		public String ultima_fecha;
	}

	public static class EstadisticasResponse {
		public int totalUsuarios;
		public int totalAnimales;
		public int totalPublicaciones;
		public int totalReportes;
		public int totalAdopciones;
		public int visitasHoy;
		public Map<String, Integer> animalesPorEspecie;
		public List<Integer> adopcionesPorMes;
		public List<Integer> publicacionesPorMes;
		public Map<String, Integer> reportesPorCategoria;
	}

}
