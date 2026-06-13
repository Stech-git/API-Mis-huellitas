package com.utad.apiAndroidBackend.chat;

import com.utad.apiAndroidBackend.entidades.Conversacion;
import com.utad.apiAndroidBackend.entidades.Mensaje;
import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import com.utad.apiAndroidBackend.admin.AdminServicio;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatServicio {

    private final ConversacionRepositorio conversacionRepositorio;
    private final MensajeRepositorio mensajeRepositorio;
    private final AdminServicio adminServicio;

    public ChatServicio(
            ConversacionRepositorio conversacionRepositorio,
            MensajeRepositorio mensajeRepositorio,
            AdminServicio adminServicio
    ) {
        this.conversacionRepositorio = conversacionRepositorio;
        this.mensajeRepositorio = mensajeRepositorio;
        this.adminServicio = adminServicio;
    }

    // Crear conversación
    public Conversacion crearConversacion(Usuario u1, Usuario u2, boolean esSoporte, String categoria) {

        // SOLO 1 CHAT DE SOPORTE POR USUARIO
        if (esSoporte) {
            Conversacion soporteExistente = conversacionRepositorio.buscarSoporte(u1.id);
            if (soporteExistente != null) {
                return soporteExistente;
            }
        }

        // EVITAR DUPLICADOS ENTRE USUARIOS
        Conversacion existente = conversacionRepositorio.buscarEntreUsuarios(u1.id, u2.id);
        if (existente != null) {
            return existente;
        }

        // CREAR NUEVA CONVERSACIÓN
        Conversacion nueva = new Conversacion();
        nueva.usuario_1_id = u1.id;
        nueva.usuario_2_id = u2.id;
        nueva.es_soporte = esSoporte;
        nueva.categoria_soporte = categoria;
        nueva.ultimo_mensaje = null;
        nueva.fecha_ultimo_mensaje = LocalDateTime.now().toString();

        conversacionRepositorio.crear(nueva);

        // Registrar acción si es soporte
        if (esSoporte && u2.es_admin) {
            adminServicio.registrarAccion(
                    u2,
                    "CREAR_CONVERSACION_SOPORTE",
                    "USUARIO",
                    u1.id,
                    "El administrador abrió un chat de soporte con el usuario"
            );
        }

        return nueva;
    }

    // Enviar mensaje
    public Mensaje enviarMensaje(Long conversacionId, Usuario emisor, String contenidoMensaje) {

        if (contenidoMensaje == null || contenidoMensaje.isBlank()) {
            throw new Excepciones.DatosInvalidosException("El mensaje no puede estar vacío");
        }
        Conversacion conversacion = conversacionRepositorio.buscarPorId(conversacionId);
        if (conversacion == null) {
            throw new Excepciones.DatosInvalidosException("La conversación no existe");
        }
        // VALIDAR QUE EL USUARIO PERTENECE A LA CONVERSACIÓN
        if (!conversacion.usuario_1_id.equals(emisor.id) &&
            !conversacion.usuario_2_id.equals(emisor.id)) {
            throw new Excepciones.DatosInvalidosException("El usuario no pertenece a esta conversación");
        }

        // usar el método correcto del repositorio (cifra internamente)
        mensajeRepositorio.enviarMensaje(conversacionId, emisor.id, contenidoMensaje);

        // Crear objeto para devolver al frontend
        Mensaje nuevo = new Mensaje();
        nuevo.conversacion_id = conversacionId;
        nuevo.emisor_id = emisor.id;
        nuevo.contenido = contenidoMensaje; // ← TEXTO PLANO PARA LA APP
        nuevo.leido = false;
        nuevo.fecha_envio = LocalDateTime.now().toString();
        return nuevo;
    }

    // Listar conversaciones
    public List<Conversacion> listarConversaciones(Long usuarioId) {
        List<Conversacion> lista = conversacionRepositorio.listarPorUsuario(usuarioId);
        for (Conversacion c : lista) {
            c.no_leidos = mensajeRepositorio.contarNoLeidos(c.id, usuarioId);
        }
        return lista;
    }

    // Listar mensajes
    public List<Mensaje> listarMensajes(Long conversacionId) {
        return mensajeRepositorio.listarPorConversacion(conversacionId);
    }
    // Marcar mensajes como leídos
    public void marcarLeidos(Long conversacionId, Usuario usuario) {
        mensajeRepositorio.marcarLeidos(conversacionId, usuario.id);
        Conversacion conversacion = conversacionRepositorio.buscarPorId(conversacionId);
        if (conversacion.es_soporte && usuario.es_admin) {
            adminServicio.registrarAccion(
                    usuario,
                    "LEER_MENSAJES_SOPORTE",
                    "USUARIO",
                    conversacion.usuario_1_id,
                    "El administrador marcó mensajes como leídos en soporte"
            );
        }
    }
}

