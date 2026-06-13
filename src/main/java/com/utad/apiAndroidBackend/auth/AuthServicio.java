package com.utad.apiAndroidBackend.auth;

import com.utad.apiAndroidBackend.entidades.Usuario;
import com.utad.apiAndroidBackend.entidades.CodigoAdmin;
import com.utad.apiAndroidBackend.excepciones.Excepciones;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServicio {

    private final AuthRepositorio authRepositorio;
    private final PasswordEncoder passwordEncoder;

    public AuthServicio(AuthRepositorio authRepositorio, PasswordEncoder passwordEncoder) {
        this.authRepositorio = authRepositorio;
        this.passwordEncoder = passwordEncoder;
    }

    public Usuario registrar(String nombre, String email, String password, String codigoAdminIngresado) {

        // Validaciones básicas
        if (email == null || email.isBlank()) {
            throw new Excepciones.DatosInvalidosException("El email es obligatorio");
        }

        if (password == null || password.isBlank()) {
            throw new Excepciones.DatosInvalidosException("La contraseña es obligatoria");
        }

        Usuario existente = authRepositorio.buscarPorEmail(email);
        if (existente != null) {
            throw new Excepciones.EmailYaRegistradoException("El email ya está registrado");
        }

        // Crear usuario base
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.nombre = nombre;
        nuevoUsuario.email = email;
        nuevoUsuario.password_hash = passwordEncoder.encode(password);
        nuevoUsuario.estado = "ACTIVO";
        nuevoUsuario.es_admin = false;

        // Datos opcionales iniciales
        nuevoUsuario.ciudad = null;
        nuevoUsuario.telefono = null;
        nuevoUsuario.biografia = null;
        nuevoUsuario.imagen_perfil_url = null;
        nuevoUsuario.mostrar_telefono = false;

        // --- SOLUCIÓN: Manejar código admin opcional ---
        if (codigoAdminIngresado != null && !codigoAdminIngresado.isBlank()) {

            CodigoAdmin codigo = authRepositorio.buscarCodigoAdmin(codigoAdminIngresado);

            if (codigo == null) {
                throw new Excepciones.CodigoAdminInvalidoException("Código de administrador inválido");
            }

            nuevoUsuario.es_admin = true;
            authRepositorio.marcarCodigoUsado(codigo.id);
        }

        // Guardar usuario
        authRepositorio.registrar(nuevoUsuario);

        // Devolver usuario con ID real
        return authRepositorio.buscarPorEmail(email);
    }

    public Usuario login(String email, String password) {

        Usuario usuario = authRepositorio.buscarPorEmail(email);

        if (usuario == null) {
            throw new Excepciones.CredencialesInvalidasException("Credenciales incorrectas");
        }

        if (!passwordEncoder.matches(password, usuario.password_hash)) {
            throw new Excepciones.CredencialesInvalidasException("Credenciales incorrectas");
        }

        return usuario;
    }
}

