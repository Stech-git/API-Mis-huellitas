package com.utad.apiAndroidBackend.entidades;

public class Mensaje {

    public Long id;
    public Long conversacion_id;
    public Long emisor_id;

    public String emisor_nombre;

    public String contenido;
    public boolean leido;
    public String fecha_envio;
}
