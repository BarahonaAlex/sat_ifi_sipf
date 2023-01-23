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
 * @author sacanoes && ajabarrer
 */
public class SolicitudGuardadaCreditoFiscalDto implements Serializable {

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
    private BigDecimal idEstado;
    private String principalProducto;
    private String Anio;
    private String expIvaGen;
    private String codFormIvaGen;
    private BigDecimal noFormIvaGen;
    private String desistimiento;

    // Getter Methods 
    public BigDecimal getNumeroSolicitud() {
        return numeroSolicitud;
    }

    public String getCodigoFormulario() {
        return codigoFormulario;
    }

    public String getNitContribuyente() {
        return nitContribuyente;
    }

    public String getDireccionNotificacion() {
        return direccionNotificacion;
    }

    public String getCorreoNotificacion() {
        return correoNotificacion;
    }

    public String getNitRepresentante() {
        return nitRepresentante;
    }

    public Date getPeriodoDesde() {
        return periodoDesde;
    }

    public Date getPeriodoHasta() {
        return periodoHasta;
    }

    

    public String getProductoExportacion() {
        return productoExportacion;
    }

    public BigDecimal getMontoSolicitud() {
        return montoSolicitud;
    }

    public BigDecimal getEstado() {
        return idEstado;
    }

    public String getPrincipalProducto() {
        return principalProducto;
    }

    public String getAnio() {
        return Anio;
    }

    public String getExpIvaGen() {
        return expIvaGen;
    }

    public String getCodFormIvaGen() {
        return codFormIvaGen;
    }

    public BigDecimal getNoFormIvaGen() {
        return noFormIvaGen;
    }

    public String getDesistimiento() {
        return desistimiento;
    }

    // Setter Methods 
    public void setNumeroSolicitud(BigDecimal numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }

    public void setCodigoFormulario(String codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }

    public void setNitContribuyente(String nitContribuyente) {
        this.nitContribuyente = nitContribuyente;
    }

    public void setDireccionNotificacion(String direccionNotificacion) {
        this.direccionNotificacion = direccionNotificacion;
    }

    public void setCorreoNotificacion(String correoNotificacion) {
        this.correoNotificacion = correoNotificacion;
    }

    public void setNitRepresentante(String nitRepresentante) {
        this.nitRepresentante = nitRepresentante;
    }

    public void setPeriodoDesde(Date periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public void setPeriodoHasta(Date periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

  
    public void setProductoExportacion(String productoExportacion) {
        this.productoExportacion = productoExportacion;
    }

    public void setMontoSolicitud(BigDecimal montoSolicitud) {
        this.montoSolicitud = montoSolicitud;
    }

    public void setEstado(BigDecimal idEstado) {
        this.idEstado = idEstado;
    }

    public void setPrincipalProducto(String principalProducto) {
        this.principalProducto = principalProducto;
    }

    public void setAnio(String Anio) {
        this.Anio = Anio;
    }

    public void setExpIvaGen(String expIvaGen) {
        this.expIvaGen = expIvaGen;
    }

    public void setCodFormIvaGen(String codFormIvaGen) {
        this.codFormIvaGen = codFormIvaGen;
    }

    public void setNoFormIvaGen(BigDecimal noFormIvaGen) {
        this.noFormIvaGen = noFormIvaGen;
    }

    public void setDesistimiento(String desistimiento) {
        this.desistimiento = desistimiento;
    }
}
