/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author kaaguilr
 */
public class SdcfSatEnLineaBitacoraParametros implements Serializable {
    
    @JsonProperty(value = "nit")
    private String nit;
    @JsonProperty(value = "correlativo")
    private long correlativo;
    @JsonProperty(value = "anioOperacion")
    private short anioOperacion;
    @JsonProperty(value = "codigoOperacion")
    private short codigoOperacion;
    @JsonProperty(value = "maquina")
    private String maquina;
    @JsonProperty(value = "pais")
    private String pais;
    @JsonProperty(value = "comentarios")
    private String comentarios;
    @JsonProperty(value = "hora")
    private String hora;
    @JsonProperty(value = "usuarioAdiciono")
    private String usuarioAdiciono;
    @JsonProperty(value = "fechaAdiciono")
    private Date fechaAdiciono;
    @JsonProperty(value = "usuarioModifico")
    private String usuarioModifico;
    @JsonProperty(value = "fechaModifico")
    private Date fechaModifico;

    /**
     * @return the nit
     */
    public String getNit() {
        return nit;
    }

    /**
     * @param nit the nit to set
     */
    public void setNit(String nit) {
        this.nit = nit;
    }

    /**
     * @return the correlativo
     */
    public long getCorrelativo() {
        return correlativo;
    }

    /**
     * @param correlativo the correlativo to set
     */
    public void setCorrelativo(long correlativo) {
        this.correlativo = correlativo;
    }

    /**
     * @return the anioOperacion
     */
    public short getAnioOperacion() {
        return anioOperacion;
    }

    /**
     * @param anioOperacion the anioOperacion to set
     */
    public void setAnioOperacion(short anioOperacion) {
        this.anioOperacion = anioOperacion;
    }

    /**
     * @return the codigoOperacion
     */
    public short getCodigoOperacion() {
        return codigoOperacion;
    }

    /**
     * @param codigoOperacion the codigoOperacion to set
     */
    public void setCodigoOperacion(short codigoOperacion) {
        this.codigoOperacion = codigoOperacion;
    }

    /**
     * @return the maquina
     */
    public String getMaquina() {
        return maquina;
    }

    /**
     * @param maquina the maquina to set
     */
    public void setMaquina(String maquina) {
        this.maquina = maquina;
    }

    /**
     * @return the pais
     */
    public String getPais() {
        return pais;
    }

    /**
     * @param pais the pais to set
     */
    public void setPais(String pais) {
        this.pais = pais;
    }

    /**
     * @return the comentarios
     */
    public String getComentarios() {
        return comentarios;
    }

    /**
     * @param comentarios the comentarios to set
     */
    public void setComentarios(String comentarios) {
        this.comentarios = comentarios;
    }

    /**
     * @return the hora
     */
    public String getHora() {
        return hora;
    }

    /**
     * @param hora the hora to set
     */
    public void setHora(String hora) {
        this.hora = hora;
    }

    /**
     * @return the usuarioAdiciono
     */
    public String getUsuarioAdiciono() {
        return usuarioAdiciono;
    }

    /**
     * @param usuarioAdiciono the usuarioAdiciono to set
     */
    public void setUsuarioAdiciono(String usuarioAdiciono) {
        this.usuarioAdiciono = usuarioAdiciono;
    }

    /**
     * @return the fechaAdiciono
     */
    public Date getFechaAdiciono() {
        return fechaAdiciono;
    }

    /**
     * @param fechaAdiciono the fechaAdiciono to set
     */
    public void setFechaAdiciono(Date fechaAdiciono) {
        this.fechaAdiciono = fechaAdiciono;
    }

    /**
     * @return the usuarioModifico
     */
    public String getUsuarioModifico() {
        return usuarioModifico;
    }

    /**
     * @param usuarioModifico the usuarioModifico to set
     */
    public void setUsuarioModifico(String usuarioModifico) {
        this.usuarioModifico = usuarioModifico;
    }

    /**
     * @return the fechaModifico
     */
    public Date getFechaModifico() {
        return fechaModifico;
    }

    /**
     * @param fechaModifico the fechaModifico to set
     */
    public void setFechaModifico(Date fechaModifico) {
        this.fechaModifico = fechaModifico;
    }
}
