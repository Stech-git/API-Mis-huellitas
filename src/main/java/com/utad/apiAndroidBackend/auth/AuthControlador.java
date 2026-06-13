package com.utad.apiAndroidBackend.auth;

import com.utad.apiAndroidBackend.entidades.Usuario;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthControlador {

    private final AuthServicio authServicio;

    public AuthControlador(AuthServicio authServicio) {
        this.authServicio = authServicio;
    }

    @PostMapping("/register")
    public Usuario registrar(@RequestBody RegistroRequest request) {
        return authServicio.registrar(
                request.nombre,
                request.email,
                request.password,
                request.codigoAdmin
        );
    }

    @PostMapping("/login")
    public Usuario login(@RequestBody LoginRequest request) {
        return authServicio.login(request.email, request.password);
    }

    static class RegistroRequest {
        public String nombre;
        public String email;
        public String password;
        public String codigoAdmin;
    }

    static class LoginRequest {
        public String email;
        public String password;
    }
}
