package com.utad.apiAndroidBackend.reportes;

import java.util.Map;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/soporte")
public class SoporteControlador {

	private final SoporteServicio soporteServicio;

	public SoporteControlador(SoporteServicio soporteServicio) {
		this.soporteServicio = soporteServicio;
	}

	@PostMapping("/enviar")
	public Map<String, Object> enviar(@RequestBody SoporteRequest req) {
		return soporteServicio.procesarSoporte(req.usuario_id, req.categoria, req.mensaje);
	}

	static class SoporteRequest {
		public Long usuario_id;
		public String categoria;
		public String mensaje;
	}
}
