package com.utad.apiAndroidBackend.usuarios;

import com.utad.apiAndroidBackend.config.CloudinaryServicio;
import com.utad.apiAndroidBackend.entidades.ResultadoCloudinary;
import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public class UsuarioServicio {

	private final UsuarioRepositorio usuarioRepositorio;
	private final PasswordEncoder passwordEncoder;
	private final CloudinaryServicio cloudinaryServicio;

	public UsuarioServicio(UsuarioRepositorio usuarioRepositorio, PasswordEncoder passwordEncoder,
			CloudinaryServicio cloudinaryServicio) {
		this.usuarioRepositorio = usuarioRepositorio;
		this.passwordEncoder = passwordEncoder;
		this.cloudinaryServicio = cloudinaryServicio;
	}

	// Obtener usuario por ID
	public Usuario obtenerPorId(Long usuarioId) {

		Usuario usuario = usuarioRepositorio.buscarPorId(usuarioId);

		if (usuario == null) {
			throw new Excepciones.UsuarioNoEncontradoException("Usuario no encontrado");
		}

		return usuario;
	}

	// Editar perfil
	public Usuario editarPerfil(Long usuarioId, Usuario datosActualizados) {

		Usuario usuarioExistente = obtenerPorId(usuarioId);

		if (usuarioExistente.estado.equals("BANEADO")) {
			throw new Excepciones.UsuarioBaneadoException("El usuario está baneado");
		}

		usuarioExistente.nombre = datosActualizados.nombre;
		usuarioExistente.ciudad = datosActualizados.ciudad;
		usuarioExistente.telefono = datosActualizados.telefono;
		usuarioExistente.biografia = datosActualizados.biografia;
		usuarioExistente.imagen_perfil_url = datosActualizados.imagen_perfil_url;
		usuarioExistente.mostrar_telefono = datosActualizados.mostrar_telefono;

		usuarioRepositorio.actualizar(usuarioExistente);
		return usuarioExistente;
	}

	// Cambiar contraseña
	public void cambiarPassword(Long usuarioId, String passwordActual, String passwordNueva) {

		Usuario usuario = obtenerPorId(usuarioId);

		if (!passwordEncoder.matches(passwordActual, usuario.password_hash)) {
			throw new Excepciones.CredencialesInvalidasException("La contraseña actual no es correcta");
		}

		String nuevoHash = passwordEncoder.encode(passwordNueva);
		usuarioRepositorio.cambiarPassword(usuarioId, nuevoHash);
	}

	// Buscar usuario
	public List<Usuario> buscarUsuarios(String texto) {

		if (texto == null || texto.trim().isEmpty()) {
			return List.of();
		}

		return usuarioRepositorio.buscarUsuarios(texto.trim());
	}

	// Listar usuarios (solo admin)
	public List<Usuario> listar(Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException(
					"Solo los administradores pueden ver la lista de usuarios");
		}

		return usuarioRepositorio.listar();
	}

	// Subir Imagene de perfil nada mas
	public Usuario subirImagenPerfil(Long usuarioId, MultipartFile archivo) {

		Usuario usuario = obtenerPorId(usuarioId);
		if (usuario.estado.equals("BANEADO")) {
			throw new RuntimeException("El usuario está baneado");
		}
		ResultadoCloudinary resultado = cloudinaryServicio.subirImagen(archivo);
		usuarioRepositorio.actualizarImagenPerfil(usuarioId, resultado.url, resultado.publicId);
		usuario.imagen_perfil_url = resultado.url;
		usuario.imagen_perfil_public_id = resultado.publicId;

		return usuario;
	}

	// Eliminar imagen
	public Usuario eliminarImagenPerfil(Long usuarioId) {

		Usuario usuario = obtenerPorId(usuarioId);
		cloudinaryServicio.eliminarImagen(usuario.imagen_perfil_public_id);
		usuarioRepositorio.eliminarImagenPerfil(usuarioId);
		usuario.imagen_perfil_url = null;
		usuario.imagen_perfil_public_id = null;

		return usuario;
	}

	// Bloquear usuario
	public void bloquear(Long usuarioId, Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("Solo los administradores pueden bloquear usuarios");
		}

		Usuario usuarioObjetivo = obtenerPorId(usuarioId);

		if (usuarioObjetivo.es_admin) {
			throw new Excepciones.NoSePuedeBloquearAdminException("No puedes bloquear a un administrador");
		}

		usuarioRepositorio.cambiarEstado(usuarioId, "BANEADO");
	}

	// Desbloquear usuario
	public void desbloquear(Long usuarioId, Usuario administrador) {

		if (!administrador.es_admin) {
			throw new Excepciones.PermisoInsuficienteException("Solo los administradores pueden desbloquear usuarios");
		}

		obtenerPorId(usuarioId); // valida que exista

		usuarioRepositorio.cambiarEstado(usuarioId, "ACTIVO");
	}

	// Eliminar cuenta
	public void eliminarCuenta(Long usuarioId, Usuario solicitante) {

		Usuario objetivo = obtenerPorId(usuarioId);

		// Un usuario solo puede eliminar su propia cuenta
		if (!solicitante.es_admin && !solicitante.id.equals(usuarioId)) {
			throw new Excepciones.PermisoInsuficienteException("No puedes eliminar cuentas de otros usuarios");
		}

		// Un admin NO puede eliminar a otro admin
		if (objetivo.es_admin && !solicitante.id.equals(usuarioId)) {
			throw new Excepciones.NoSePuedeBloquearAdminException("No puedes eliminar a un administrador");
		}

		usuarioRepositorio.eliminarLogico(usuarioId);
	}

}
