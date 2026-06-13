package com.utad.apiAndroidBackend.excepciones;

/**
 * Clase contenedora de TODAS las excepciones personalizadas de la aplicación.
 * 
 * Ventajas: - Todo está en un solo archivo. - Fácil de mantener y buscar. -
 * Evita tener decenas de clases sueltas. - Organización profesional por
 * categorías.
 */
public class Excepciones {

	// ============================================================
	// EXCEPCIÓN GENERAL (equivalente a tu antigua MiExcepcion)
	// ============================================================

	/** Excepción genérica para errores personalizados. */
	public static class MiExcepcion extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MiExcepcion(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// AUTENTICACIÓN Y SEGURIDAD
	// ============================================================

	/** Usuario no está autenticado (no ha iniciado sesión). */
	public static class UsuarioNoAutenticadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UsuarioNoAutenticadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Email o contraseña incorrectos. */
	public static class CredencialesInvalidasException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CredencialesInvalidasException(String mensaje) {
			super(mensaje);
		}
	}

	/** Token JWT inválido o manipulado. */
	public static class TokenInvalidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TokenInvalidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Token JWT expirado. */
	public static class TokenExpiradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TokenExpiradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario intenta acceder a un recurso sin estar autenticado. */
	public static class AccesoDenegadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AccesoDenegadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario autenticado pero sin permisos suficientes. */
	public static class PermisoInsuficienteException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PermisoInsuficienteException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// USUARIOS
	// ============================================================

	/** Usuario no encontrado en la base de datos. */
	public static class UsuarioNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UsuarioNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Email ya registrado por otro usuario. */
	public static class EmailYaRegistradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EmailYaRegistradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario está baneado y no puede acceder. */
	public static class UsuarioBaneadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UsuarioBaneadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario está desactivado temporalmente. */
	public static class UsuarioDesactivadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UsuarioDesactivadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Código de administrador incorrecto. */
	public static class CodigoAdminInvalidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CodigoAdminInvalidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Código de administrador ya fue usado. */
	public static class CodigoAdminYaUsadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CodigoAdminYaUsadoException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// ANIMALES / PUBLICACIONES
	// ============================================================

	/** Animal no encontrado. */
	public static class AnimalNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AnimalNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario intenta editar un animal que no le pertenece. */
	public static class AnimalNoPerteneceAlUsuarioException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AnimalNoPerteneceAlUsuarioException(String mensaje) {
			super(mensaje);
		}
	}

	/** Publicación está oculta y no puede mostrarse. */
	public static class PublicacionOcultaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PublicacionOcultaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Publicación fue eliminada. */
	public static class PublicacionEliminadaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public PublicacionEliminadaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Estado del animal no es válido. */
	public static class EstadoAnimalInvalidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EstadoAnimalInvalidoException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// IMÁGENES
	// ============================================================

	/** Imagen no encontrada. */
	public static class ImagenNoEncontradaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ImagenNoEncontradaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Imagen no pertenece al animal indicado. */
	public static class ImagenNoPerteneceAlAnimalException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ImagenNoPerteneceAlAnimalException(String mensaje) {
			super(mensaje);
		}
	}

	/** No se puede eliminar la imagen principal. */
	public static class ImagenPrincipalNoEliminableException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ImagenPrincipalNoEliminableException(String mensaje) {
			super(mensaje);
		}
	}

	/** Formato de imagen no permitido. */
	public static class FormatoImagenNoValidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public FormatoImagenNoValidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Imagen demasiado grande. */
	public static class TamanoImagenExcedidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TamanoImagenExcedidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Error al subir imagen (Firebase, disco, etc.). */
	public static class ErrorSubidaImagenException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ErrorSubidaImagenException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// REPORTES
	// ============================================================

	/** Reporte no encontrado. */
	public static class ReporteNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ReporteNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Reporte ya fue resuelto. */
	public static class ReporteYaResueltoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ReporteYaResueltoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Reporte duplicado sobre el mismo objetivo. */
	public static class ReporteDuplicadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ReporteDuplicadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Tipo de objetivo inválido (animal, usuario, mensaje, etc.). */
	public static class TipoObjetivoInvalidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public TipoObjetivoInvalidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** El objetivo reportado no existe. */
	public static class ObjetivoReportadoNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ObjetivoReportadoNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// CHAT Y MENSAJES
	// ============================================================

	/** Conversación no encontrada. */
	public static class ConversacionNoEncontradaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ConversacionNoEncontradaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario intenta acceder a una conversación donde no participa. */
	public static class UsuarioNoParticipaEnConversacionException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public UsuarioNoParticipaEnConversacionException(String mensaje) {
			super(mensaje);
		}
	}

	/** Mensaje no encontrado. */
	public static class MensajeNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MensajeNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Usuario intenta borrar un mensaje que no es suyo. */
	public static class MensajeNoPerteneceAlUsuarioException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public MensajeNoPerteneceAlUsuarioException(String mensaje) {
			super(mensaje);
		}
	}

	/** Mensaje vacío o inválido. */
	public static class ContenidoMensajeVacioException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public ContenidoMensajeVacioException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// ADMIN
	// ============================================================

	/** Acción administrativa no permitida. */
	public static class AccionAdminNoPermitidaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AccionAdminNoPermitidaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Administrador no encontrado. */
	public static class AdminNoEncontradoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AdminNoEncontradoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Acción administrativa no encontrada. */
	public static class AccionAdminNoEncontradaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public AccionAdminNoEncontradaException(String mensaje) {
			super(mensaje);
		}
	}

	/** No se puede bloquear a un administrador. */
	public static class NoSePuedeBloquearAdminException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NoSePuedeBloquearAdminException(String mensaje) {
			super(mensaje);
		}
	}

	/** No se puede eliminar a un administrador. */
	public static class NoSePuedeEliminarAdminException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public NoSePuedeEliminarAdminException(String mensaje) {
			super(mensaje);
		}
	}

	// ============================================================
	// VALIDACIONES GENERALES
	// ============================================================

	/** Campo obligatorio vacío. */
	public static class CampoObligatorioException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public CampoObligatorioException(String mensaje) {
			super(mensaje);
		}
	}

	/** Datos enviados no cumplen formato. */
	public static class DatosInvalidosException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public DatosInvalidosException(String mensaje) {
			super(mensaje);
		}
	}

	/** Fecha inválida o mal formateada. */
	public static class FechaInvalidaException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public FechaInvalidaException(String mensaje) {
			super(mensaje);
		}
	}

	/** Valor de enum inválido. */
	public static class EnumInvalidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public EnumInvalidoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Recurso duplicado (email, reporte, etc.). */
	public static class RecursoDuplicadoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public RecursoDuplicadoException(String mensaje) {
			super(mensaje);
		}
	}

	/** Se excedió un límite (tamaño, cantidad, etc.). */
	public static class LimiteExcedidoException extends RuntimeException {
		private static final long serialVersionUID = 1L;

		public LimiteExcedidoException(String mensaje) {
			super(mensaje);
		}
	}
}
