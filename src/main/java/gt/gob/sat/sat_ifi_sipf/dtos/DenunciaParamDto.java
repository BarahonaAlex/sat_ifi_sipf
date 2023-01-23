/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author rabaraho
 */
public class DenunciaParamDto {

    //@JsonProperty(value = "correlativo")
    private String correlativo;
    //@JsonProperty(value = "nitDenunciante")
    private String nitDenunciante;
    //@JsonProperty(value = "nombreDenunciante")
    private String nombreDenunciante;
    //@JsonProperty(value = "telefonoDenunciante")
    private String telefonoDenunciante;
    //@JsonProperty(value = "email")
    private String email;
    //@JsonProperty(value = "nitDenunciado")
    private String nitDenunciado;
    //@JsonProperty(value = "nombreDenunciado")
    private String nombreDenunciado;
    //@JsonProperty(value = "establecimientoDenunciado")
    private String establecimientoDenunciado;
    //@JsonProperty(value = "direccionFiscalDenunciado")
    private String direccionFiscalDenunciado;
    //@JsonProperty(value = "direccionEstDenunciado")
    private String direccionEstDenunciado;
    //@JsonProperty(value = "telefonoDenunciado")
    private String telefonoDenunciado;
    //@JsonProperty(value = "idMotivo")
    private Integer idMotivo;
    //@JsonProperty(value = "otroMotivo")
    private String otroMotivo;
    //@JsonProperty(value = "formaPago")
    private String formaPago;
    @JsonProperty(value = "fechaCompra")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaCompra;
    //@JsonProperty(value = "productoServicio")
    private String productoServicio;
    //@JsonProperty(value = "valorCompraServicio")
    private Integer valorCompraServicio;
    //@JsonProperty(value = "observaciones")
    private String observaciones;
    //@JsonProperty(value = "estado")
    private String estado;
    //@JsonProperty(value = "auditor")
    private String auditor;
    //@JsonProperty(value = "razonRechazo")
    private String razonRechazo;
    //@JsonProperty(value = "departamento")
    private Integer departamento;
    //@JsonProperty(value = "municipio")
    private Integer municipio;
    //@JsonProperty(value = "fechaGrabacion")
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaGrabacion;
    //@JsonProperty(value = "justificacion")
    private String justificacion;
    //@JsonProperty(value = "fechaAsignacion")
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaAsignacion;
    //@JsonProperty(value = "fechaAsignacionJs")
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaAsignacionJs;
    //@JsonProperty(value = "fechaCargaPlan")
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaCargaPlan;
    //@JsonProperty(value = "fechaRechazo")
    //@JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaRechazo;
    //@JsonProperty(value = "linea")
    private Integer linea;
    //@JsonProperty(value = "nitAp")
    private String nitAp;
    //@JsonProperty(value = "nitAuditorRechazo")
    private String nitAuditorRechazo;
    //@JsonProperty(value = "nitJs")
    private String nitJs;
    //@JsonProperty(value = "nitSn")
    private String nitSn;
    //@JsonProperty(value = "nombrePlan")
    private String nombrePlan;
    //@JsonProperty(value = "region")
    private Integer region;
    //@JsonProperty(value = "urlImagen")
    private String urlImagen;
    //@JsonProperty(value = "latitud")
    private String latitud;
    //@JsonProperty(value = "longitud")
    private String longitud;
    //@JsonProperty(value = "idInsumo")
    private Integer idInsumo;

    public String getCorrelativo() {
        return correlativo;
    }

    public void setCorrelativo(String correlativo) {
        this.correlativo = correlativo;
    }

    public String getNitDenunciante() {
        return nitDenunciante;
    }

    public void setNitDenunciante(String nitDenunciante) {
        this.nitDenunciante = nitDenunciante;
    }

    public String getNombreDenunciante() {
        return nombreDenunciante;
    }

    public void setNombreDenunciante(String nombreDenunciante) {
        this.nombreDenunciante = nombreDenunciante;
    }

    public String getTelefonoDenunciante() {
        return telefonoDenunciante;
    }

    public void setTelefonoDenunciante(String telefonoDenunciante) {
        this.telefonoDenunciante = telefonoDenunciante;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNitDenunciado() {
        return nitDenunciado;
    }

    public void setNitDenunciado(String nitDenunciado) {
        this.nitDenunciado = nitDenunciado;
    }

    public String getNombreDenunciado() {
        return nombreDenunciado;
    }

    public void setNombreDenunciado(String nombreDenunciado) {
        this.nombreDenunciado = nombreDenunciado;
    }

    public String getEstablecimientoDenunciado() {
        return establecimientoDenunciado;
    }

    public void setEstablecimientoDenunciado(String establecimientoDenunciado) {
        this.establecimientoDenunciado = establecimientoDenunciado;
    }

    public String getDireccionFiscalDenunciado() {
        return direccionFiscalDenunciado;
    }

    public void setDireccionFiscalDenunciado(String direccionFiscalDenunciado) {
        this.direccionFiscalDenunciado = direccionFiscalDenunciado;
    }

    public String getDireccionEstDenunciado() {
        return direccionEstDenunciado;
    }

    public void setDireccionEstDenunciado(String direccionEstDenunciado) {
        this.direccionEstDenunciado = direccionEstDenunciado;
    }

    public String getTelefonoDenunciado() {
        return telefonoDenunciado;
    }

    public void setTelefonoDenunciado(String telefonoDenunciado) {
        this.telefonoDenunciado = telefonoDenunciado;
    }

    public Integer getIdMotivo() {
        return idMotivo;
    }

    public void setIdMotivo(Integer idMotivo) {
        this.idMotivo = idMotivo;
    }

    public String getOtroMotivo() {
        return otroMotivo;
    }

    public void setOtroMotivo(String otroMotivo) {
        this.otroMotivo = otroMotivo;
    }

    public String getFormaPago() {
        return formaPago;
    }

    public void setFormaPago(String formaPago) {
        this.formaPago = formaPago;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getProductoServicio() {
        return productoServicio;
    }

    public void setProductoServicio(String productoServicio) {
        this.productoServicio = productoServicio;
    }

    public Integer getValorCompraServicio() {
        return valorCompraServicio;
    }

    public void setValorCompraServicio(Integer valorCompraServicio) {
        this.valorCompraServicio = valorCompraServicio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getRazonRechazo() {
        return razonRechazo;
    }

    public void setRazonRechazo(String razonRechazo) {
        this.razonRechazo = razonRechazo;
    }

    public Integer getDepartamento() {
        return departamento;
    }

    public void setDepartamento(Integer departamento) {
        this.departamento = departamento;
    }

    public Integer getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Integer municipio) {
        this.municipio = municipio;
    }

    public Date getFechaGrabacion() {
        return fechaGrabacion;
    }

    public void setFechaGrabacion(Date fechaGrabacion) {
        this.fechaGrabacion = fechaGrabacion;
    }

    public String getJustificacion() {
        return justificacion;
    }

    public void setJustificacion(String justificacion) {
        this.justificacion = justificacion;
    }

    public Date getFechaAsignacion() {
        return fechaAsignacion;
    }

    public void setFechaAsignacion(Date fechaAsignacion) {
        this.fechaAsignacion = fechaAsignacion;
    }

    public Date getFechaAsignacionJs() {
        return fechaAsignacionJs;
    }

    public void setFechaAsignacionJs(Date fechaAsignacionJs) {
        this.fechaAsignacionJs = fechaAsignacionJs;
    }

    public Date getFechaCargaPlan() {
        return fechaCargaPlan;
    }

    public void setFechaCargaPlan(Date fechaCargaPlan) {
        this.fechaCargaPlan = fechaCargaPlan;
    }

    public Date getFechaRechazo() {
        return fechaRechazo;
    }

    public void setFechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
    }

    public Integer getLinea() {
        return linea;
    }

    public void setLinea(Integer linea) {
        this.linea = linea;
    }

    public String getNitAp() {
        return nitAp;
    }

    public void setNitAp(String nitAp) {
        this.nitAp = nitAp;
    }

    public String getNitAuditorRechazo() {
        return nitAuditorRechazo;
    }

    public void setNitAuditorRechazo(String nitAuditorRechazo) {
        this.nitAuditorRechazo = nitAuditorRechazo;
    }

    public String getNitJs() {
        return nitJs;
    }

    public void setNitJs(String nitJs) {
        this.nitJs = nitJs;
    }

    public String getNitSn() {
        return nitSn;
    }

    public void setNitSn(String nitSn) {
        this.nitSn = nitSn;
    }

    public String getNombrePlan() {
        return nombrePlan;
    }

    public void setNombrePlan(String nombrePlan) {
        this.nombrePlan = nombrePlan;
    }

    public Integer getRegion() {
        return region;
    }

    public void setRegion(Integer region) {
        this.region = region;
    }

    public String getUrlImagen() {
        return urlImagen;
    }

    public void setUrlImagen(String urlImagen) {
        this.urlImagen = urlImagen;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public Integer getIdInsumo() {
        return idInsumo;
    }

    public void setIdInsumo(Integer idInsumo) {
        this.idInsumo = idInsumo;
    }

}
