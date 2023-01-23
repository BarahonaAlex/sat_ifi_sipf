/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 *
 * @author ajabarrer
 */
public class RetenIsrParametrosDto {
    
    public RetenIsrParametrosDto() {
    }
    
    @JsonProperty(value = "pNitAgenteR")
    private String pNitAgenteR;
    
    @JsonProperty(value = "pPeriodoR")
    private String pPeriodoR;
    
    @JsonProperty(value = "pNitSujetoR")
    private String pNitSujetoR;
    
    @JsonProperty(value = "pTipoConsulta")
    private String pTipoConsulta;
    
    @JsonProperty(value = "pTipoRetencion")
    private String pTipoRetencion;
    
    @JsonProperty(value = "pEstado")
    private String pEstado;

   
    
    public String getpNitAgenteR() {
        return pNitAgenteR;
    }

    public void setpNitAgenteR(String pNitAgenteR) {
        this.pNitAgenteR = pNitAgenteR;
    }

    public String getpPeriodoR() {
        return pPeriodoR;
    }

    public void setpPeriodoR(String pPeriodoR) {
        this.pPeriodoR = pPeriodoR;
    }

    public String getpNitSujetoR() {
        return pNitSujetoR;
    }

    public void setpNitSujetoR(String pNitSujetoR) {
        this.pNitSujetoR = pNitSujetoR;
    }

    public String getpTipoConsulta() {
        return pTipoConsulta;
    }

    public void setpTipoConsulta(String pTipoConsulta) {
        this.pTipoConsulta = pTipoConsulta;
    }

    public String getpTipoRetencion() {
        return pTipoRetencion;
    }

    public void setpTipoRetencion(String pTipoRetencion) {
        this.pTipoRetencion = pTipoRetencion;
    }

    public String getpEstado() {
        return pEstado;
    }

    public void setpEstado(String pEstado) {
        this.pEstado = pEstado;
    }
}
