/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 *
 * @author ajabarrer
 */
public class SdcfUpdateStatusDto implements Serializable{
    private BigDecimal numeroSolicitud;
    private String codigoFormulario;
    private Integer nuevoStatus;
    private String texto;

    public BigDecimal getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public void setNumeroSolicitud(BigDecimal numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public String getCodigoFormulario() {
        return codigoFormulario;
    }

    public void setCodigoFormulario(String codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }

    public Integer getNuevoStatus() {
        return nuevoStatus;
    }

    public void setNuevoStatus(Integer nuevoStatus) {
        this.nuevoStatus = nuevoStatus;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public SdcfUpdateStatusDto() {
    }
 
}
