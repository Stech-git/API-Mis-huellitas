package com.utad.apiAndroidBackend.reportes;

import com.utad.apiAndroidBackend.entidades.Reporte;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reportes")
public class ReporteControlador {

	private final ReporteServicio reporteServicio;

	public ReporteControlador(ReporteServicio reporteServicio) {
		this.reporteServicio = reporteServicio;
	}

	@PostMapping("/registrar")
	public void registrarReporte(@RequestBody RegistrarReporteRequest request) {
		reporteServicio.registrarReporte(request.reportador, request.reporte);
	}

	@PostMapping("/admin/listar")
	public List<Reporte> listarReportes(@RequestBody Usuario admin) {
		return reporteServicio.listarReportes(admin);
	}

	@PutMapping("/admin/estado/{reporteId}")
	public void cambiarEstadoReporte(@PathVariable Long reporteId, @RequestBody CambiarEstadoRequest request) {
		reporteServicio.cambiarEstado(reporteId, request.nuevoEstado, request.admin);
	}

	@PostMapping("/admin/categoria/{categoria}")
	public List<Reporte> listarPorCategoria(@PathVariable String categoria, @RequestBody Usuario admin) {
		return reporteServicio.listarPorCategoria(admin, categoria);
	}

	// listar reportes de un usuario (tipo_objetivo = "USUARIO")
	@GetMapping("/usuario/{usuarioId}")
	public List<Reporte> listarPorUsuario(@PathVariable Long usuarioId) {
		return reporteServicio.listarPorObjetivo("USUARIO", usuarioId);
	}

	// listar reportes de un animal (tipo_objetivo = "ANIMAL")
	@GetMapping("/animal/{animalId}")
	public List<Reporte> listarPorAnimal(@PathVariable Long animalId) {
		return reporteServicio.listarPorObjetivo("ANIMAL", animalId);
	}

	// comprobar si ya existe un reporte concreto
	@PostMapping("/existe")
	public boolean existeReporte(@RequestBody ExisteReporteRequest request) {
		return reporteServicio.existeReporte(request.reportadorId, request.tipoObjetivo, request.objetivoId);
	}

	public static class RegistrarReporteRequest {
		public Usuario reportador;
		public Reporte reporte;
	}

	public static class CambiarEstadoRequest {
		public Usuario admin;
		public String nuevoEstado;
	}

	public static class ExisteReporteRequest {
		public Long reportadorId;
		public String tipoObjetivo; // "USUARIO" o "ANIMAL"
		public Long objetivoId;
	}
}
