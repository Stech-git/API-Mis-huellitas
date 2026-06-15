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

    public Conversacion crearConversacion(Usuario u1, Usuario u2, boolean esSoporte, String categoria) {

        Conversacion existente = conversacionRepositorio.buscarEntreUsuarios(u1.id, u2.id);

        if (existente != null) {
            return existente;
        }

        Conversacion nueva = new Conversacion();
        nueva.usuario_1_id = u1.id;
        nueva.usuario_2_id = u2.id;
        nueva.es_soporte = esSoporte;
        nueva.categoria_soporte = categoria;
        nueva.ultimo_mensaje = null;
        nueva.fecha_ultimo_mensaje = LocalDateTime.now().toString();

        Long idGenerado = conversacionRepositorio.crear(nueva);
        nueva.id = idGenerado;

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

    public Mensaje enviarMensaje(Long conversacionId, Usuario emisor, String contenidoMensaje) {

        if (contenidoMensaje == null || contenidoMensaje.isBlank()) {
            throw new Excepciones.DatosInvalidosException("El mensaje no puede estar vacío");
        }

        Conversacion conversacion = conversacionRepositorio.buscarPorId(conversacionId);

        if (conversacion == null) {
            throw new Excepciones.DatosInvalidosException("La conversación no existe");
        }

        if (!conversacion.usuario_1_id.equals(emisor.id) &&
                !conversacion.usuario_2_id.equals(emisor.id)) {
            throw new Excepciones.DatosInvalidosException("El usuario no pertenece a esta conversación");
        }

        mensajeRepositorio.enviarMensaje(conversacionId, emisor.id, contenidoMensaje);

        Mensaje nuevo = new Mensaje();
        nuevo.conversacion_id = conversacionId;
        nuevo.emisor_id = emisor.id;
        nuevo.contenido = contenidoMensaje;
        nuevo.leido = false;
        nuevo.fecha_envio = LocalDateTime.now().toString();

        return nuevo;
    }

    public List<Conversacion> listarConversaciones(Long usuarioId) {
        List<Conversacion> lista = conversacionRepositorio.listarPorUsuario(usuarioId);

        for (Conversacion c : lista) {
            c.no_leidos = mensajeRepositorio.contarNoLeidos(c.id, usuarioId);
        }

        return lista;
    }

    public List<Mensaje> listarMensajes(Long conversacionId) {
        return mensajeRepositorio.listarPorConversacion(conversacionId);
    }

    public void marcarLeidos(Long conversacionId, Usuario usuario) {
        mensajeRepositorio.marcarLeidos(conversacionId, usuario.id);

        Conversacion conversacion = conversacionRepositorio.buscarPorId(conversacionId);

        if (conversacion != null && conversacion.es_soporte && usuario.es_admin) {
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

