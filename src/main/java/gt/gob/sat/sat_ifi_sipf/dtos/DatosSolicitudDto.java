/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author arapenam
 */
public class DatosSolicitudDto implements Serializable{

    private BigDecimal numeroSolicitud;
    private String codigoFormulario;
    private String nitContribuyente;
    private String direccionNotificacion;
    private String correoNotificacion;
    private String nitRepresentante;
    private Date periodoDesde;
    private Date periodoHasta;
    private String productoExportacion;
    private BigDecimal montoSolicitud;
    private String usuarioGrabacion;
    private Date fechaGrabacion;
     private Date fechaBitacora;
    private BigDecimal idEstado;
    private String Estado;
    private String principalProducto;
    private String Anio;//Año en que se guarda la solicitud
    private String tooltip;
    private String numeroExpediente;
    private Date fechaImpresion;
    private String nitResponsableImpresion;
        private String expIvaGen;
     private String codFormIvaGen;
     private BigDecimal noFormIvaGen;
     private String desistimiento;
    
    
    /**
     * @return the numeroSolicitud
     */
    public BigDecimal getNumeroSolicitud() {
        return numeroSolicitud;
    }

    /**
     * @param numeroSolicitud the numeroSolicitud to set
     */
    public void setNumeroSolicitud(BigDecimal numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    /**
     * @return the codigoFormulario
     */
    public String getCodigoFormulario() {
        return codigoFormulario;
    }

    /**
     * @param codigoFormulario the codigoFormulario to set
     */
    public void setCodigoFormulario(String codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }

    /**
     * @return the nitContribuyente
     */
    public String getNitContribuyente() {
        return nitContribuyente;
    }

    /**
     * @param nitContribuyente the nitContribuyente to set
     */
    public void setNitContribuyente(String nitContribuyente) {
        this.nitContribuyente = nitContribuyente;
    }

    /**
     * @return the direccionNotificacion
     */
    public String getDireccionNotificacion() {
        return direccionNotificacion;
    }

    /**
     * @param direccionNotificacion the direccionNotificacion to set
     */
    public void setDireccionNotificacion(String direccionNotificacion) {
        this.direccionNotificacion = direccionNotificacion;
    }

    /**
     * @return the correoNotificacion
     */
    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    /**
     * @param correoNotificacion the correoNotificacion to set
     */
    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    /**
     * @return the nitRepresentante
     */
    public String getNitRepresentante() {
        return nitRepresentante;
    }

    /**
     * @param nitRepresentante the nitRepresentante to set
     */
    public void setNitRepresentante(String nitRepresentante) {
        this.nitRepresentante = nitRepresentante;
    }

    /**
     * @return the periodoDesde
     */
    public Date getPeriodoDesde() {
        return periodoDesde;
    }

    /**
     * @param periodoDesde the periodoDesde to set
     */
    public void setPeriodoDesde(Date periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    /**
     * @return the periodoHasta
     */
    public Date getPeriodoHasta() {
        return periodoHasta;
    }

    /**
     * @param periodoHasta the periodoHasta to set
     */
    public void setPeriodoHasta(Date periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

    /**
     * @return the productoExportacion
     */
    public String getProductoExportacion() {
        return productoExportacion;
    }

    /**
     * @param productoExportacion the productoExportacion to set
     */
    public void setProductoExportacion(String productoExportacion) {
        this.productoExportacion = productoExportacion;
    }

    /**
     * @return the montoSolicitud
     */
    public BigDecimal getMontoSolicitud() {
        return montoSolicitud;
    }

    /**
     * @param montoSolicitud the montoSolicitud to set
     */
    public void setMontoSolicitud(BigDecimal montoSolicitud) {
        this.montoSolicitud = montoSolicitud;
    }

    /**
     * @return the usuarioGrabacion
     */
    public String getUsuarioGrabacion() {
        return usuarioGrabacion;
    }

    /**
     * @param usuarioGrabacion the usuarioGrabacion to set
     */
    public void setUsuarioGrabacion(String usuarioGrabacion) {
        this.usuarioGrabacion = usuarioGrabacion;
    }

    /**
     * @return the fechaGrabacion
     */
    public Date getFechaGrabacion() {
        return fechaGrabacion;
    }

    /**
     * @param fechaGrabacion the fechaGrabacion to set
     */
    public void setFechaGrabacion(Date fechaGrabacion) {
        this.fechaGrabacion = fechaGrabacion;
    }

    /**
     * @return the idEstado
     */
    public BigDecimal getIdEstado() {
        return idEstado;
    }

    /**
     * @param idEstado the idEstado to set
     */
    public void setIdEstado(BigDecimal idEstado) {
        this.idEstado = idEstado;
    }

    /**
     * @return the Estado
     */
    public String getEstado() {
        return Estado;
    }

    /**
     * @param Estado the Estado to set
     */
    public void setEstado(String Estado) {
        this.Estado = Estado;
    }

    /**
     * @return the Anio
     */
    public String getAnio() {
        return Anio;
    }

    /**
     * @param Anio the Anio to set
     */
    public void setAnio(String Anio) {
        this.Anio = Anio;
    }

    /**
     * @return the principalProducto
     */
    public String getPrincipalProducto() {
        return principalProducto;
    }

    /**
     * @param principalProducto the principalProducto to set
     */
    public void setPrincipalProducto(String principalProducto) {
        this.principalProducto = principalProducto;
    }

    /**
     * @return the fechaBitacora
     */
    public Date getFechaBitacora() {
        return fechaBitacora;
    }

    /**
     * @param fechaBitacora the fechaBitacora to set
     */
    public void setFechaBitacora(Date fechaBitacora) {
        this.fechaBitacora = fechaBitacora;
    }

    /**
     * @return the tooltip
     */
    public String getTooltip() {
        return tooltip;
    }

    /**
     * @param tooltip the tooltip to set
     */
    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    /**
     * @return the numeroExpediente
     */
    public String getNumeroExpediente() {
        return numeroExpediente;
    }

    /**
     * @param numeroExpediente the numeroExpediente to set
     */
    public void setNumeroExpediente(String numeroExpediente) {
        this.numeroExpediente = numeroExpediente;
    }
    
        /**
     * @return the fechaImpresion
     */
    public Date getFechaImpresion() {
        return fechaImpresion;
    }

    /**
     * @param fechaImpresion the fechaImpresion to set
     */
    public void setFechaImpresion(Date fechaImpresion) {
        this.fechaImpresion = fechaImpresion;
    }

    /**
     * @return the nitResponsableImpresion
     */
    public String getNitResponsableImpresion() {
        return nitResponsableImpresion;
    }

    /**
     * @param nitResponsableImpresion the nitResponsableImpresion to set
     */
    public void setNitResponsableImpresion(String nitResponsableImpresion) {
        this.nitResponsableImpresion = nitResponsableImpresion;
    }
    
       /**
     * @return the expIvaGen
     */
    public String getExpIvaGen() {
        return expIvaGen;
    }

    /**
     * @param expIvaGen the expIvaGen to set
     */
    public void setExpIvaGen(String expIvaGen) {
        this.expIvaGen = expIvaGen;
    }

    /**
     * @return the codFormIvaGen
     */
    public String getCodFormIvaGen() {
        return codFormIvaGen;
    }

    /**
     * @param codFormIvaGen the codFormIvaGen to set
     */
    public void setCodFormIvaGen(String codFormIvaGen) {
        this.codFormIvaGen = codFormIvaGen;
    }

    /**
     * @return the noFormIvaGen
     */
    public BigDecimal getNoFormIvaGen() {
        return noFormIvaGen;
    }

    /**
     * @param noFormIvaGen the noFormIvaGen to set
     */
    public void setNoFormIvaGen(BigDecimal noFormIvaGen) {
        this.noFormIvaGen = noFormIvaGen;
    }

    /**
     * @return the desistimiento
     */
    public String getDesistimiento() {
        return desistimiento;
    }

    /**
     * @param desistimiento the desistimiento to set
     */
    public void setDesistimiento(String desistimiento) {
        this.desistimiento = desistimiento;
    }
    
   
}
