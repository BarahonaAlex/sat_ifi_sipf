/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author kaaguilr
 */
public class SdcfInconsistenciaDto implements Serializable {

    private BigDecimal id;
    private BigDecimal idSolicitudDeclaracion;
    private BigDecimal idTipoInconsistencia;
    private String descripcion;
    private Date fechaGrabacion;
    private String usuarioGrabacion;
    private BigDecimal numeroSolicitud;
    private BigDecimal tipoRepetida;
     private BigDecimal declaracionRepetida;
     private String facturaProveedor;
     private String facturaSerie;
     private String facturaNumero;

    /**
     * @return the id
     */
    public BigDecimal getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(BigDecimal id) {
        this.id = id;
    }

    /**
     * @return the idSolicitudDeclaracion
     */
    public BigDecimal getIdSolicitudDeclaracion() {
        return idSolicitudDeclaracion;
    }

    /**
     * @param idSolicitudDeclaracion the idSolicitudDeclaracion to set
     */
    public void setIdSolicitudDeclaracion(BigDecimal idSolicitudDeclaracion) {
        this.idSolicitudDeclaracion = idSolicitudDeclaracion;
    }

    /**
     * @return the idTipoInconsistencia
     */
    public BigDecimal getIdTipoInconsistencia() {
        return idTipoInconsistencia;
    }

    /**
     * @param idTipoInconsistencia the idTipoInconsistencia to set
     */
    public void setIdTipoInconsistencia(BigDecimal idTipoInconsistencia) {
        this.idTipoInconsistencia = idTipoInconsistencia;
    }

    /**
     * @return the descripcion
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * @param descripcion the descripcion to set
     */
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
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
     * @return the tipoRepetida
     */
    public BigDecimal getTipoRepetida() {
        return tipoRepetida;
    }

    /**
     * @param tipoRepetida the tipoRepetida to set
     */
    public void setTipoRepetida(BigDecimal tipoRepetida) {
        this.tipoRepetida = tipoRepetida;
    }

    /**
     * @return the declaracionRepetida
     */
    public BigDecimal getDeclaracionRepetida() {
        return declaracionRepetida;
    }

    /**
     * @param declaracionRepetida the declaracionRepetida to set
     */
    public void setDeclaracionRepetida(BigDecimal declaracionRepetida) {
        this.declaracionRepetida = declaracionRepetida;
    }

    /**
     * @return the facturaProveedor
     */
    public String getFacturaProveedor() {
        return facturaProveedor;
    }

    /**
     * @param facturaProveedor the facturaProveedor to set
     */
    public void setFacturaProveedor(String facturaProveedor) {
        this.facturaProveedor = facturaProveedor;
    }

    /**
     * @return the facturaSerie
     */
    public String getFacturaSerie() {
        return facturaSerie;
    }

    /**
     * @param facturaSerie the facturaSerie to set
     */
    public void setFacturaSerie(String facturaSerie) {
        this.facturaSerie = facturaSerie;
    }

    /**
     * @return the facturaNumero
     */
    public String getFacturaNumero() {
        return facturaNumero;
    }

    /**
     * @param facturaNumero the facturaNumero to set
     */
    public void setFacturaNumero(String facturaNumero) {
        this.facturaNumero = facturaNumero;
    }

    }
