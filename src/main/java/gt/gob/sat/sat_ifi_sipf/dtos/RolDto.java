/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

/**
 *
 * @author ruano
 */
public class RolDto {

    private String tipo;
    private int codigo;
    private String mensaje;
    private OperacionDto operacion;
    private boolean poseeRol;

    /**
     * @return the tipo
     */
    public String getTipo() {
        return tipo;
    }

    /**
     * @param tipo the tipo to set
     */
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    /**
     * @return the codigo
     */
    public int getCodigo() {
        return codigo;
    }

    /**
     * @param codigo the codigo to set
     */
    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    /**
     * @return the mensaje
     */
    public String getMensaje() {
        return mensaje;
    }

    /**
     * @param mensaje the mensaje to set
     */
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    /**
     * @return the operacion
     */
    public OperacionDto getOperacion() {
        return operacion;
    }

    /**
     * @param operacion the operacion to set
     */
    public void setOperacion(OperacionDto operacion) {
        this.operacion = operacion;
    }

    /**
     * @return the poseeRol
     */
    public boolean isPoseeRol() {
        return poseeRol;
    }

    /**
     * @param poseeRol the poseeRol to set
     */
    public void setPoseeRol(boolean poseeRol) {
        this.poseeRol = poseeRol;
    }

}


