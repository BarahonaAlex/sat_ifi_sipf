/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * @author crramosl
 */
public class DuasDucasDto {

    private String proveedorDestinatario;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private Date fechaAceptacion;
    private String docImpoExpo;
    private String razonSocialImpoExpo;
    private BigDecimal tipoCambio;
    private String docAgente;
    private String razonSocialAgente;
    private String codModoTrasnporte;
    private String regimenModalidad;
    private String claseDeclaracion;
    private String paisProceDestino;
    private String codDepositoFiscal;
    private BigDecimal fobDolares;
    private BigDecimal fleteDolares;
    private BigDecimal seguroDolares;
    private BigDecimal otrosGastos;
    private BigDecimal numeroBultos;
    private BigDecimal pesoNeto;
    private BigDecimal unidadesFisicas;
    private String incoterm;
    private String numeroOrden;
    private String puertoEntrada;
    private String aduanaEntrada;
    private String anio;
    private BigDecimal secuencia;
    private BigDecimal version;
    private String depositoTemporal;
    private BigDecimal totalValorAduanaMPI;
    private String selectivo;

    public String getProveedorDestinatario() {
        return proveedorDestinatario;
    }

    public void setProveedorDestinatario(String proveedorDestinatario) {
        this.proveedorDestinatario = proveedorDestinatario;
    }

    public Date getFechaAceptacion() {
        return fechaAceptacion;
    }

    public void setFechaAceptacion(Date fechaAceptacion) {
        this.fechaAceptacion = fechaAceptacion;
    }

    public String getDocImpoExpo() {
        return docImpoExpo;
    }

    public void setDocImpoExpo(String docImpoExpo) {
        this.docImpoExpo = docImpoExpo;
    }

    public String getRazonSocialImpoExpo() {
        return razonSocialImpoExpo;
    }

    public void setRazonSocialImpoExpo(String razonSocialImpoExpo) {
        this.razonSocialImpoExpo = razonSocialImpoExpo;
    }

    public BigDecimal getTipoCambio() {
        return tipoCambio;
    }

    public void setTipoCambio(BigDecimal tipoCambio) {
        this.tipoCambio = tipoCambio;
    }

    public String getDocAgente() {
        return docAgente;
    }

    public void setDocAgente(String docAgente) {
        this.docAgente = docAgente;
    }

    public String getRazonSocialAgente() {
        return razonSocialAgente;
    }

    public void setRazonSocialAgente(String razonSocialAgente) {
        this.razonSocialAgente = razonSocialAgente;
    }

    public String getCodModoTrasnporte() {
        return codModoTrasnporte;
    }

    public void setCodModoTrasnporte(String codModoTrasnporte) {
        this.codModoTrasnporte = codModoTrasnporte;
    }

    public String getRegimenModalidad() {
        return regimenModalidad;
    }

    public void setRegimenModalidad(String regimenModalidad) {
        this.regimenModalidad = regimenModalidad;
    }

    public String getClaseDeclaracion() {
        return claseDeclaracion;
    }

    public void setClaseDeclaracion(String claseDeclaracion) {
        this.claseDeclaracion = claseDeclaracion;
    }

    public String getPaisProceDestino() {
        return paisProceDestino;
    }

    public void setPaisProceDestino(String paisProceDestino) {
        this.paisProceDestino = paisProceDestino;
    }

    public String getCodDepositoFiscal() {
        return codDepositoFiscal;
    }

    public void setCodDepositoFiscal(String codDepositoFiscal) {
        this.codDepositoFiscal = codDepositoFiscal;
    }

    public BigDecimal getFobDolares() {
        return fobDolares;
    }

    public void setFobDolares(BigDecimal fobDolares) {
        this.fobDolares = fobDolares;
    }

    public BigDecimal getFleteDolares() {
        return fleteDolares;
    }

    public void setFleteDolares(BigDecimal fleteDolares) {
        this.fleteDolares = fleteDolares;
    }

    public BigDecimal getSeguroDolares() {
        return seguroDolares;
    }

    public void setSeguroDolares(BigDecimal seguroDolares) {
        this.seguroDolares = seguroDolares;
    }

    public BigDecimal getOtrosGastos() {
        return otrosGastos;
    }

    public void setOtrosGastos(BigDecimal otrosGastos) {
        this.otrosGastos = otrosGastos;
    }

    public BigDecimal getNumeroBultos() {
        return numeroBultos;
    }

    public void setNumeroBultos(BigDecimal numeroBultos) {
        this.numeroBultos = numeroBultos;
    }

    public BigDecimal getPesoNeto() {
        return pesoNeto;
    }

    public void setPesoNeto(BigDecimal pesoNeto) {
        this.pesoNeto = pesoNeto;
    }

    public BigDecimal getUnidadesFisicas() {
        return unidadesFisicas;
    }

    public void setUnidadesFisicas(BigDecimal unidadesFisicas) {
        this.unidadesFisicas = unidadesFisicas;
    }

    public String getIncoterm() {
        return incoterm;
    }

    public void setIncoterm(String incoterm) {
        this.incoterm = incoterm;
    }

    public String getNumeroOrden() {
        return numeroOrden;
    }

    public void setNumeroOrden(String numeroOrden) {
        this.numeroOrden = numeroOrden;
    }

    public String getPuertoEntrada() {
        return puertoEntrada;
    }

    public void setPuertoEntrada(String puertoEntrada) {
        this.puertoEntrada = puertoEntrada;
    }

    public String getAduanaEntrada() {
        return aduanaEntrada;
    }

    public void setAduanaEntrada(String aduanaEntrada) {
        this.aduanaEntrada = aduanaEntrada;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public BigDecimal getSecuencia() {
        return secuencia;
    }

    public void setSecuencia(BigDecimal secuencia) {
        this.secuencia = secuencia;
    }

    public BigDecimal getVersion() {
        return version;
    }

    public void setVersion(BigDecimal version) {
        this.version = version;
    }

    public String getDepositoTemporal() {
        return depositoTemporal;
    }

    public void setDepositoTemporal(String depositoTemporal) {
        this.depositoTemporal = depositoTemporal;
    }

    public BigDecimal getTotalValorAduanaMPI() {
        return totalValorAduanaMPI;
    }

    public void setTotalValorAduanaMPI(BigDecimal totalValorAduanaMPI) {
        this.totalValorAduanaMPI = totalValorAduanaMPI;
    }

    public String getSelectivo() {
        return selectivo;
    }

    public void setSelectivo(String selectivo) {
        this.selectivo = selectivo;
    }
}
