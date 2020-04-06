package cl.genesys.appchofer.entities;

import java.io.Serializable;

public class SpinnerHelper implements Serializable {

    String cod;
    String nombre;
    int estado;
    int tipo;

    public SpinnerHelper(String cod, String nombre) {
        super();
        this.cod = cod;
        this.nombre = nombre;
    }

    public String getCod() {
        return cod;
    }

    public void setCod(String cod) {
        this.cod = cod;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }
}
