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
 * @author aaehernan
 */
public class DatosContribuyenteCreditoFiscalDto implements Serializable {

    private static final long serialVersionUID = 1L;
    private String nit;
    private BigDecimal cui;
    private String nombre;
    private String calleAvenida;
    private String numeroCasa;
    private String letra;
    private String apartamento;
    private String zona;
    private String colonia;
    private String municipio;
    private String departamento;
    private String pasaporte;
    private Byte tipoPersona;
    private String representanteLegal;
    private String domicilioFiscal;
    private String domicilioNotificacion;
    private String codigoExportador;
    private String exportadorHabitual;
    private String nitContador;
    private String codigoActividad;//codigo de actividad
    private String nombreActividad;//nombre de actividad
    private String email;// email del contribuyente
    private Date fechaNombramientoContador;
    private String direccionInvalida;
    private String status;
    private String clasificacion;

    public DatosContribuyenteCreditoFiscalDto(String nit, BigDecimal cui, String nombre, String calleAvenida, String numeroCasa, String letra, String apartamento, String zona, String colonia, String municipio, String departamento, String pasaporte, Byte tipoPersona, String representanteLegal, String domicilioFiscal, String domicilioNotificacion, String codigoExportador, String exportadorHabitual, String nitContador, String codigoActividad, String nombreActividad, String email, Date fechaNombramientoContador, String direccionInvalida, String status, String clasificacion) {
        this.nit = nit;
        this.cui = cui;
        this.nombre = nombre;
        this.calleAvenida = calleAvenida;
        this.numeroCasa = numeroCasa;
        this.letra = letra;
        this.apartamento = apartamento;
        this.zona = zona;
        this.colonia = colonia;
        this.municipio = municipio;
        this.departamento = departamento;
        this.pasaporte = pasaporte;
        this.tipoPersona = tipoPersona;
        this.representanteLegal = representanteLegal;
        this.domicilioFiscal = domicilioFiscal;
        this.domicilioNotificacion = domicilioNotificacion;
        this.codigoExportador = codigoExportador;
        this.exportadorHabitual = exportadorHabitual;
        this.nitContador = nitContador;
        this.codigoActividad = codigoActividad;
        this.nombreActividad = nombreActividad;
        this.email = email;
        this.fechaNombramientoContador = fechaNombramientoContador;
        this.direccionInvalida = direccionInvalida;
        this.status = status;
        this.clasificacion = clasificacion;
    }

    public DatosContribuyenteCreditoFiscalDto() {
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setCui(BigDecimal cui) {
        this.cui = cui;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setCalleAvenida(String calleAvenida) {
        this.calleAvenida = calleAvenida;
    }

    public void setNumeroCasa(String numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public void setLetra(String letra) {
        this.letra = letra;
    }

    public void setApartamento(String apartamento) {
        this.apartamento = apartamento;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setDepartamento(String departamento) {
        this.departamento = departamento;
    }

    public void setPasaporte(String pasaporte) {
        this.pasaporte = pasaporte;
    }

    public void setTipoPersona(Byte tipoPersona) {
        this.tipoPersona = tipoPersona;
    }

    public void setRepresentanteLegal(String representanteLegal) {
        this.representanteLegal = representanteLegal;
    }

    public void setDomicilioFiscal(String domicilioFiscal) {
        this.domicilioFiscal = domicilioFiscal;
    }

    public void setDomicilioNotificacion(String domicilioNotificacion) {
        this.domicilioNotificacion = domicilioNotificacion;
    }

    public void setCodigoExportador(String codigoExportador) {
        this.codigoExportador = codigoExportador;
    }

    public void setExportadorHabitual(String exportadorHabitual) {
        this.exportadorHabitual = exportadorHabitual;
    }

    public void setNitContador(String nitContador) {
        this.nitContador = nitContador;
    }

    public void setCodigoActividad(String codigoActividad) {
        this.codigoActividad = codigoActividad;
    }

    public void setNombreActividad(String nombreActividad) {
        this.nombreActividad = nombreActividad;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFechaNombramientoContador(Date fechaNombramientoContador) {
        this.fechaNombramientoContador = fechaNombramientoContador;
    }

    public void setDireccionInvalida(String direccionInvalida) {
        this.direccionInvalida = direccionInvalida;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setClasificacion(String clasificacion) {
        this.clasificacion = clasificacion;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getNit() {
        return nit;
    }

    public BigDecimal getCui() {
        return cui;
    }

    public String getNombre() {
        return nombre;
    }

    public String getCalleAvenida() {
        return calleAvenida;
    }

    public String getNumeroCasa() {
        return numeroCasa;
    }

    public String getLetra() {
        return letra;
    }

    public String getApartamento() {
        return apartamento;
    }

    public String getZona() {
        return zona;
    }

    public String getColonia() {
        return colonia;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getDepartamento() {
        return departamento;
    }

    public String getPasaporte() {
        return pasaporte;
    }

    public Byte getTipoPersona() {
        return tipoPersona;
    }

    public String getRepresentanteLegal() {
        return representanteLegal;
    }

    public String getDomicilioFiscal() {
        return domicilioFiscal;
    }

    public String getDomicilioNotificacion() {
        return domicilioNotificacion;
    }

    public String getCodigoExportador() {
        return codigoExportador;
    }

    public String getExportadorHabitual() {
        return exportadorHabitual;
    }

    public String getNitContador() {
        return nitContador;
    }

    public String getCodigoActividad() {
        return codigoActividad;
    }

    public String getNombreActividad() {
        return nombreActividad;
    }

    public String getEmail() {
        return email;
    }

    public Date getFechaNombramientoContador() {
        return fechaNombramientoContador;
    }

    public String getDireccionInvalida() {
        return direccionInvalida;
    }

    public String getStatus() {
        return status;
    }

    public String getClasificacion() {
        return clasificacion;
    }
}
