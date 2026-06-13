package com.utad.apiAndroidBackend.excepciones;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

/**
 * Controlador global que captura TODAS las excepciones personalizadas
 * y devuelve respuestas JSON limpias y con el código HTTP correcto.
 */
@RestControllerAdvice
public class ControlExcepciones {

    // Modelo estándar de error
    private record ErrorRespuesta(
            String mensaje,
            String detalle,
            String timestamp
    ) {}

    private ResponseEntity<ErrorRespuesta> respuesta(HttpStatus status, String mensaje, Exception ex) {
        return ResponseEntity.status(status).body(
                new ErrorRespuesta(
                        mensaje,
                        ex.getMessage(),
                        LocalDateTime.now().toString()
                )
        );
    }


    // ============================================================
    //  AUTENTICACIÓN Y SEGURIDAD
    // ============================================================

    @ExceptionHandler({
            Excepciones.UsuarioNoAutenticadoException.class,
            Excepciones.TokenInvalidoException.class,
            Excepciones.TokenExpiradoException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar401(Exception ex) {
        return respuesta(HttpStatus.UNAUTHORIZED, "No autenticado", ex);
    }

    @ExceptionHandler({
            Excepciones.AccesoDenegadoException.class,
            Excepciones.PermisoInsuficienteException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar403(Exception ex) {
        return respuesta(HttpStatus.FORBIDDEN, "Acceso denegado", ex);
    }


    // ============================================================
    //  VALIDACIONES Y DATOS INCORRECTOS
    // ============================================================

    @ExceptionHandler({
            Excepciones.CampoObligatorioException.class,
            Excepciones.DatosInvalidosException.class,
            Excepciones.FechaInvalidaException.class,
            Excepciones.EnumInvalidoException.class,
            Excepciones.ContenidoMensajeVacioException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar400(Exception ex) {
        return respuesta(HttpStatus.BAD_REQUEST, "Datos inválidos", ex);
    }


    // ============================================================
    //  RECURSOS NO ENCONTRADOS
    // ============================================================

    @ExceptionHandler({
            Excepciones.UsuarioNoEncontradoException.class,
            Excepciones.AnimalNoEncontradoException.class,
            Excepciones.ImagenNoEncontradaException.class,
            Excepciones.ReporteNoEncontradoException.class,
            Excepciones.ConversacionNoEncontradaException.class,
            Excepciones.MensajeNoEncontradoException.class,
            Excepciones.AdminNoEncontradoException.class,
            Excepciones.AccionAdminNoEncontradaException.class,
            Excepciones.ObjetivoReportadoNoEncontradoException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar404(Exception ex) {
        return respuesta(HttpStatus.NOT_FOUND, "Recurso no encontrado", ex);
    }


    // ============================================================
    //  CONFLICTOS (duplicados, estados inválidos, etc.)
    // ============================================================

    @ExceptionHandler({
            Excepciones.EmailYaRegistradoException.class,
            Excepciones.RecursoDuplicadoException.class,
            Excepciones.ReporteDuplicadoException.class,
            Excepciones.ReporteYaResueltoException.class,
            Excepciones.CodigoAdminYaUsadoException.class,
            Excepciones.EstadoAnimalInvalidoException.class,
            Excepciones.PublicacionEliminadaException.class,
            Excepciones.PublicacionOcultaException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar409(Exception ex) {
        return respuesta(HttpStatus.CONFLICT, "Conflicto en la operación", ex);
    }


    // ============================================================
    //  IMÁGENES (tamaño, formato, etc.)
    // ============================================================

    @SuppressWarnings("deprecation") // a un que nes deprecation se que no rompe nada, por eso lo dejo 
	@ExceptionHandler({
            Excepciones.TamanoImagenExcedidoException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar413(Exception ex) {
        return respuesta(HttpStatus.PAYLOAD_TOO_LARGE, "Imagen demasiado grande", ex);
    }

    @ExceptionHandler({
            Excepciones.FormatoImagenNoValidoException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar415(Exception ex) {
        return respuesta(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Formato de imagen no válido", ex);
    }


    // ============================================================
    //  OPERACIONES NO PERMITIDAS
    // ============================================================

    @ExceptionHandler({
            Excepciones.AccionAdminNoPermitidaException.class,
            Excepciones.NoSePuedeBloquearAdminException.class,
            Excepciones.NoSePuedeEliminarAdminException.class,
            Excepciones.AnimalNoPerteneceAlUsuarioException.class,
            Excepciones.MensajeNoPerteneceAlUsuarioException.class,
            Excepciones.UsuarioNoParticipaEnConversacionException.class
    })
    public ResponseEntity<ErrorRespuesta> manejar403Operaciones(Exception ex) {
        return respuesta(HttpStatus.FORBIDDEN, "Operación no permitida", ex);
    }


    // ============================================================
    //  ERROR GENERAL (último recurso)
    // ============================================================

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorRespuesta> manejar500(Exception ex) {
        return respuesta(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor", ex);
    }
}
