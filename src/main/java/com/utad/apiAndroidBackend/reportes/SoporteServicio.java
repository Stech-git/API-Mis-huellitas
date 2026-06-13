package com.utad.apiAndroidBackend.reportes;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.utad.apiAndroidBackend.chat.ConversacionRepositorio;
import com.utad.apiAndroidBackend.chat.MensajeRepositorio;
import com.utad.apiAndroidBackend.entidades.Reporte;
import com.utad.apiAndroidBackend.usuarios.UsuarioRepositorio;

@Service
public class SoporteServicio {

    private final UsuarioRepositorio usuarioRepo;
    private final ReporteRepositorio reporteRepo;
    private final ConversacionRepositorio conversacionRepo;
    private final MensajeRepositorio mensajeRepo;

    public SoporteServicio(
            UsuarioRepositorio usuarioRepo,
            ReporteRepositorio reporteRepo,
            ConversacionRepositorio conversacionRepo,
            MensajeRepositorio mensajeRepo
    ) {
        this.usuarioRepo = usuarioRepo;
        this.reporteRepo = reporteRepo;
        this.conversacionRepo = conversacionRepo;
        this.mensajeRepo = mensajeRepo;
    }

    public Map<String, Object> procesarSoporte(Long usuarioId, String categoria, String mensaje) {

        // 1. Asignar admin automáticamente
        Long adminId = usuarioRepo.obtenerAdminDisponible();

        // 2. Crear reporte
        Reporte r = new Reporte();
        r.reportador_id = usuarioId;
        r.admin_id = adminId;
        r.tipo_objetivo = "USUARIO";
        r.objetivo_id = usuarioId;
        r.categoria = categoria;
        r.motivo = mensaje;
        r.estado = "PENDIENTE";
        r.fecha_creacion = LocalDateTime.now().toString();

        reporteRepo.registrar(r);

        // 3. Crear conversación de soporte
        Long conversacionId = conversacionRepo.crearSoporte(usuarioId, adminId, categoria);

        // 4. Insertar primer mensaje (cifrado dentro del repositorio)
        mensajeRepo.enviarMensaje(conversacionId, usuarioId, mensaje);

        // 5. Respuesta para Android
        Map<String, Object> resp = new HashMap<>();
        resp.put("conversacion_id", conversacionId);
        resp.put("admin_id", adminId);

        return resp;
    }
}