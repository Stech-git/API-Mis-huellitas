package com.utad.apiAndroidBackend.chat;

import com.utad.apiAndroidBackend.entidades.Conversacion;
import com.utad.apiAndroidBackend.entidades.Mensaje;
import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatControlador {

    private final ChatServicio servicio;

    public ChatControlador(ChatServicio servicio) {
        this.servicio = servicio;
    }

    @PostMapping("/crear")
    public Conversacion crear(@RequestBody CrearRequest req) {
        return servicio.crearConversacion(
                req.usuario_1,
                req.usuario_2,
                req.es_soporte,
                req.categoria_soporte
        );
    }

    @PostMapping("/enviar")
    public Mensaje enviar(@RequestBody EnviarRequest req) {
        return servicio.enviarMensaje(
                req.conversacion_id,
                req.emisor,
                req.contenido
        );
    }

    @GetMapping("/conversaciones/{usuarioId}")
    public List<Conversacion> conversaciones(@PathVariable Long usuarioId) {
        return servicio.listarConversaciones(usuarioId);
    }

    @GetMapping("/mensajes/{conversacionId}")
    public List<Mensaje> mensajes(@PathVariable Long conversacionId) {
        return servicio.listarMensajes(conversacionId);
    }

    @PostMapping("/leidos")
    public void marcarLeidos(@RequestBody MarcarLeidosRequest req) {
        servicio.marcarLeidos(req.conversacion_id, req.usuario);
    }

    // DTOs internos
    public static class CrearRequest {
        public Usuario usuario_1;
        public Usuario usuario_2;
        public boolean es_soporte;
        public String categoria_soporte;
    }

    public static class EnviarRequest {
        public Long conversacion_id;
        public Usuario emisor;
        public String contenido;
    }

    public static class MarcarLeidosRequest {
        public Long conversacion_id;
        public Usuario usuario;
    }
    
    public static class ConversacionResumen {
        public Long id;
        public Usuario usuario_1;
        public Usuario usuario_2;
        public boolean es_soporte;
        public String categoria_soporte;
        public String ultimoMensaje;
        public String fechaUltimoMensaje;
        public int noLeidos;
    }

}
