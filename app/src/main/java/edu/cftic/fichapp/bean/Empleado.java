package edu.cftic.fichapp.bean;

import java.io.Serializable;

public class Empleado implements Serializable {//TODO Hacer parcelable
    private int id_empleado;
    private String nombre;
    private String usuario;
    private String clave;
    private String rol;
    private boolean baja;
    private Empresa empresa;

    public Empleado() {
    }
    public Empleado(String usuario,String clave) {
    }
    public Empleado(int id_empleado, String nombre, String usuario, String clave, String rol, boolean baja, Empresa empresa) {
        this.id_empleado = id_empleado;
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.rol = rol;
        this.baja = baja;
        this.empresa = empresa;
    }

    public Empleado(String nombre, String usuario, String clave, String rol, boolean baja, Empresa empresa) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.clave = clave;
        this.rol = rol;
        this.baja = baja;
        this.empresa = empresa;
    }

    public int getId_empleado() {
        return id_empleado;
    }

    public void setId_empleado(int id_empleado) {
        this.id_empleado = id_empleado;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    public Empresa getEmpresa() {
        return empresa;
    }

    public void setEmpresa(Empresa empresa) {
        this.empresa = empresa;
    }

    @Override
    public String toString() {
        return "Empleado{" +
                "id_empleado=" + id_empleado +
                ", nombre='" + nombre + '\'' +
                ", usuario='" + usuario + '\'' +
                ", clave='" + clave + '\'' +
                ", rol='" + rol + '\'' +
                ", baja=" + baja +
                ", empresa=" + empresa +
                '}';
    }
}
