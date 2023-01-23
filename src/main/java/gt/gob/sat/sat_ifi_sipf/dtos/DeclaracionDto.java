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
 * @author arapenam
 */
public class DeclaracionDto implements Serializable {

    /**
     * @return the creditoNoSolicitado
     */
    public BigDecimal getCreditoNoSolicitado() {
        return creditoNoSolicitado;
    }

    /**
     * @param creditoNoSolicitado the creditoNoSolicitado to set
     */
    public void setCreditoNoSolicitado(BigDecimal creditoNoSolicitado) {
        this.creditoNoSolicitado = creditoNoSolicitado;
    }

    /**
     * @return the totalExportaciones
     */
    public BigDecimal getTotalExportaciones() {
        return totalExportaciones;
    }

    /**
     * @param totalExportaciones the totalExportaciones to set
     */
    public void setTotalExportaciones(BigDecimal totalExportaciones) {
        this.totalExportaciones = totalExportaciones;
    }

    private short mes;
    private short anio;
    private BigDecimal codigoDeclaracion;
    private String version;
    private BigDecimal codigoFormulario;
    private BigDecimal numeroDocumento;
    private String numeroFormulario;
    private BigDecimal valor;
    private char solicitudRegimen;
    private BigDecimal valorSolicitar;
    private Date periodoDel;
    private Date periodoAl;
    private int numeroMes;
    private String nombreMes;
    private BigDecimal creditos_locales;
        private BigDecimal creditos_exportacion;
         private BigDecimal RETENCIONES;
        private BigDecimal creditoMenosDebito;
        private int multa;
        private String productoExportacion;
    private BigDecimal debitos;    
    private BigDecimal montoCalculado;
    private BigDecimal montoSolicitud;
    private BigDecimal anioFiscal;
    private Date fechaRecaudo;
    private BigDecimal ivaExcenciones;
    private BigDecimal remanentes;
    private BigDecimal facturasEspecialesCF;
    private BigDecimal totalExportaciones;
    
    private BigDecimal creditoNoSolicitado = BigDecimal.ZERO;

    /**
     * @return the mes
     */
    public short getMes() {
        return mes;
    }

    /**
     * @param mes the mes to set
     */
    public void setMes(short mes) {
        this.mes = mes;
    }

    /**
     * @return the anio
     */
    public short getAnio() {
        return anio;
    }

    /**
     * @param anio the anio to set
     */
    public void setAnio(short anio) {
        this.anio = anio;
    }

    /**
     * @return the codigoDeclaracion
     */
    public BigDecimal getCodigoDeclaracion() {
        return codigoDeclaracion;
    }

    /**
     * @param codigoDeclaracion the codigoDeclaracion to set
     */
    public void setCodigoDeclaracion(BigDecimal codigoDeclaracion) {
        this.codigoDeclaracion = codigoDeclaracion;
    }

    /**
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version the version to set
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return the codigoFormulario
     */
    public BigDecimal getCodigoFormulario() {
        return codigoFormulario;
    }

    /**
     * @param codigoFormulario the codigoFormulario to set
     */
    public void setCodigoFormulario(BigDecimal codigoFormulario) {
        this.codigoFormulario = codigoFormulario;
    }

    /**
     * @return the valor
     */
    public BigDecimal getValor() {
        return valor;
    }

    /**
     * @param valor the valor to set
     */
    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    /**
     * @return the solicitudRegimen
     */
    public char getSolicitudRegimen() {
        return solicitudRegimen;
    }

    /**
     * @param solicitudRegimen the solicitudRegimen to set
     */
    public void setSolicitudRegimen(char solicitudRegimen) {
        this.solicitudRegimen = solicitudRegimen;
    }

    /**
     * @return the valorSolicitar
     */
    public BigDecimal getValorSolicitar() {
        return valorSolicitar;
    }

    /**
     * @param valorSolicitar the valorSolicitar to set
     */
    public void setValorSolicitar(BigDecimal valorSolicitar) {
        this.valorSolicitar = valorSolicitar;
    }

    /**
     * @return the periodoDel
     */
    public Date getPeriodoDel() {
        return periodoDel;
    }

    /**
     * @param periodoDel the periodoDel to set
     */
    public void setPeriodoDel(Date periodoDel) {
        this.periodoDel = periodoDel;
    }

    /**
     * @return the periodoAl
     */
    public Date getPeriodoAl() {
        return periodoAl;
    }

    /**
     * @param periodoAl the periodoAl to set
     */
    public void setPeriodoAl(Date periodoAl) {
        this.periodoAl = periodoAl;
    }

    /**
     * @return the numeroFormulario
     */
    public String getNumeroFormulario() {
        return numeroFormulario;
    }

    /**
     * @param numeroFormulario the numeroFormulario to set
     */
    public void setNumeroFormulario(String numeroFormulario) {
        this.numeroFormulario = numeroFormulario;
    }

    /**
     * @return the nombreMes
     */
    public String getNombreMes() {
        return nombreMes;
    }

    /**
     * @param nombreMes the nombreMes to set
     */
    public void setNombreMes(String nombreMes) {
        this.nombreMes = nombreMes;
    }

    /**
     * @return the numeroMes
     */
    public int getNumeroMes() {
        return numeroMes;
    }

    /**
     * @param numeroMes the numeroMes to set
     */
    public void setNumeroMes(int numeroMes) {
        this.numeroMes = numeroMes;
    }



    /**
     * @return the debitos
     */
    public BigDecimal getDebitos() {
        return debitos;
    }

    /**
     * @param debitos the debitos to set
     */
    public void setDebitos(BigDecimal debitos) {
        this.debitos = debitos;
    }
         

    /**
     * @return the montoCalculado
     */
    public BigDecimal getMontoCalculado() {
        return montoCalculado;
    }

    /**
     * @param montoCalculado the montoCalculado to set
     */
    public void setMontoCalculado(BigDecimal montoCalculado) {
        this.montoCalculado = montoCalculado;
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
     * @return the numeroDocumento
     */
    public BigDecimal getNumeroDocumento() {
        return numeroDocumento;
    }

    /**
     * @param numeroDocumento the numeroDocumento to set
     */
    public void setNumeroDocumento(BigDecimal numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    /**
     * @return the anioFiscal
     */
    public BigDecimal getAnioFiscal() {
        return anioFiscal;
    }

    /**
     * @param anioFiscal the anioFiscal to set
     */
    public void setAnioFiscal(BigDecimal anioFiscal) {
        this.anioFiscal = anioFiscal;
    }

    /**
     * @return the fechaRecaudo
     */
    public Date getFechaRecaudo() {
        return fechaRecaudo;
    }

    /**
     * @param fechaRecaudo the fechaRecaudo to set
     */
    public void setFechaRecaudo(Date fechaRecaudo) {
        this.fechaRecaudo = fechaRecaudo;
    }

    /**
     * @return the creditos_locales
     */
    public BigDecimal getCreditos_locales() {
        return creditos_locales;
    }

    /**
     * @param creditos_locales the creditos_locales to set
     */
    public void setCreditos_locales(BigDecimal creditos_locales) {
        this.creditos_locales = creditos_locales;
    }

    /**
     * @return the creditos_exportacion
     */
    public BigDecimal getCreditos_exportacion() {
        return creditos_exportacion;
    }

    /**
     * @param creditos_exportacion the creditos_exportacion to set
     */
    public void setCreditos_exportacion(BigDecimal creditos_exportacion) {
        this.creditos_exportacion = creditos_exportacion;
    }



    /**
     * @return the creditoMenosDebito
     */
    public BigDecimal getCreditoMenosDebito() {
        return creditoMenosDebito;
    }

    /**
     * @param creditoMenosDebito the creditoMenosDebito to set
     */
    public void setCreditoMenosDebito(BigDecimal creditoMenosDebito) {
        this.creditoMenosDebito = creditoMenosDebito;
    }



    /**
     * @return the RETENCIONES
     */
    public BigDecimal getRETENCIONES() {
        return RETENCIONES;
    }

    /**
     * @param RETENCIONES the RETENCIONES to set
     */
    public void setRETENCIONES(BigDecimal RETENCIONES) {
        this.RETENCIONES = RETENCIONES;
    }

    /**
     * @return the multa
     */
    public int getMulta() {
        return multa;
    }

    /**
     * @param multa the multa to set
     */
    public void setMulta(int multa) {
        this.multa = multa;
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
     * @return the ivaExcenciones
     */
    public BigDecimal getIvaExcenciones() {
        return ivaExcenciones;
    }

    /**
     * @param ivaExcenciones the ivaExcenciones to set
     */
    public void setIvaExcenciones(BigDecimal ivaExcenciones) {
        this.ivaExcenciones = ivaExcenciones;
    }

    /**
     * @return the remanentes
     */
    public BigDecimal getRemanentes() {
        return remanentes;
    }

    /**
     * @param remanentes the remanentes to set
     */
    public void setRemanentes(BigDecimal remanentes) {
        this.remanentes = remanentes;
    }

   /**
     * @return the facturasEspecialesCF
     */
    public BigDecimal getFacturasEspecialesCF() {
        return facturasEspecialesCF;
    }

    /**
     * @param facturasEspecialesCF the facturasEspecialesCF to set
     */
    public void setFacturasEspecialesCF(BigDecimal facturasEspecialesCF) {
        this.facturasEspecialesCF = facturasEspecialesCF;
    }

}
