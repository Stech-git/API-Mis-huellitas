package com.utad.apiAndroidBackend.entidades;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Usuario {

    public Long id;
    public String nombre;
    public String email;

    @JsonIgnore
    public String password_hash;

    public boolean es_admin;
    public String estado;
    public String ciudad;
    public String telefono;
    public String biografia;
    public String imagen_perfil_url;
    public String imagen_perfil_public_id;
    public boolean mostrar_telefono;
    public String fecha_creacion;

    public Usuario() {}

    public Usuario(Long id) {
        this.id = id;
    }
}