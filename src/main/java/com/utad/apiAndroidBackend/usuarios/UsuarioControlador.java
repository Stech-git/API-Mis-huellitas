package com.utad.apiAndroidBackend.usuarios;

import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioControlador {

	private final UsuarioServicio usuarioServicio;

	public UsuarioControlador(UsuarioServicio usuarioServicio) {
		this.usuarioServicio = usuarioServicio;
	}

	// Obtener usuario por ID
	@GetMapping("/{usuarioId}")
	public Usuario obtenerUsuario(@PathVariable Long usuarioId) {
		return usuarioServicio.obtenerPorId(usuarioId);
	}

	// Buscar usuario
	@GetMapping("/buscar")
	public List<Usuario> buscarUsuarios(@RequestParam String texto) {
		return usuarioServicio.buscarUsuarios(texto);
	}

	// Editar perfil
	@PutMapping("/{usuarioId}")
	public Usuario editarPerfil(@PathVariable Long usuarioId, @RequestBody Usuario datosActualizados) {
		return usuarioServicio.editarPerfil(usuarioId, datosActualizados);
	}

	// Cambiar contraseña
	@PutMapping("/{usuarioId}/password")
	public void cambiarPassword(@PathVariable Long usuarioId, @RequestBody PasswordRequest request) {
		usuarioServicio.cambiarPassword(usuarioId, request.passwordActual, request.passwordNueva);
	}

	// Listar usuarios (solo admin)
	@PostMapping("/admin/lista")
	public List<Usuario> listarUsuarios(@RequestBody Usuario administrador) {
		return usuarioServicio.listar(administrador);
	}

	// Subir imagen de perfil
	@PostMapping("/{usuarioId}/imagen-perfil")
	public Usuario subirImagenPerfil(@PathVariable Long usuarioId, @RequestParam("file") MultipartFile file) {
		return usuarioServicio.subirImagenPerfil(usuarioId, file);
	}

	// Eliminar imagen de perfil.
	@DeleteMapping("/{usuarioId}/imagen-perfil")
	public Usuario eliminarImagenPerfil(@PathVariable Long usuarioId) {
		return usuarioServicio.eliminarImagenPerfil(usuarioId);
	}

	// Bloquear usuario
	@PutMapping("/admin/bloquear/{usuarioId}")
	public void bloquearUsuario(@PathVariable Long usuarioId, @RequestBody Usuario administrador) {
		usuarioServicio.bloquear(usuarioId, administrador);
	}

	// Desbloquear usuario
	@PutMapping("/admin/desbloquear/{usuarioId}")
	public void desbloquearUsuario(@PathVariable Long usuarioId, @RequestBody Usuario administrador) {
		usuarioServicio.desbloquear(usuarioId, administrador);
	}

	// Eliminar cuenta
	@DeleteMapping("/{usuarioId}")
	public void eliminarCuenta(@PathVariable Long usuarioId, @RequestBody Usuario solicitante) {
		usuarioServicio.eliminarCuenta(usuarioId, solicitante);
	}

	// DTO interno
	static class PasswordRequest {
		public String passwordActual;
		public String passwordNueva;
	}
}
