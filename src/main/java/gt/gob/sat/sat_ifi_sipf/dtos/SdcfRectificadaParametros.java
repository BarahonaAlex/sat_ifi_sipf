/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 *
 * @author ajabarrer
 */
public class SdcfRectificadaParametros {
    @JsonProperty(value = "pCasilla")
    private Integer pCasilla;
    @JsonProperty(value = "pCodigoFormulario")
    private String pCodigoFormulario;
    @JsonProperty(value = "pNumeroVersion")
    private Integer pNumeroVersion;
    @JsonProperty(value = "pNumeroDocumento")
    private BigDecimal pNumeroDocumento;
    @JsonProperty(value = "pAnioFiscal")
    private BigDecimal pAnioFiscal;

    public SdcfRectificadaParametros() {
    }

    public SdcfRectificadaParametros(Integer pCasilla, String pCodigoFormulario, Integer pNumeroVersion, BigDecimal pNumeroDocumento, BigDecimal pAnioFiscal) {
        this.pCasilla = pCasilla;
        this.pCodigoFormulario = pCodigoFormulario;
        this.pNumeroVersion = pNumeroVersion;
        this.pNumeroDocumento = pNumeroDocumento;
        this.pAnioFiscal = pAnioFiscal;
    }

    public Integer getpCasilla() {
        return pCasilla;
    }

    public void setpCasilla(Integer pCasilla) {
        this.pCasilla = pCasilla;
    }

    public String getpCodigoFormulario() {
        return pCodigoFormulario;
    }

    public void setpCodigoFormulario(String pCodigoFormulario) {
        this.pCodigoFormulario = pCodigoFormulario;
    }

    public Integer getpNumeroVersion() {
        return pNumeroVersion;
    }

    public void setpNumeroVersion(Integer pNumeroVersion) {
        this.pNumeroVersion = pNumeroVersion;
    }

    public BigDecimal getpNumeroDocumento() {
        return pNumeroDocumento;
    }

    public void setpNumeroDocumento(BigDecimal pNumeroDocumento) {
        this.pNumeroDocumento = pNumeroDocumento;
    }

    public BigDecimal getpAnioFiscal() {
        return pAnioFiscal;
    }

    public void setpAnioFiscal(BigDecimal pAnioFiscal) {
        this.pAnioFiscal = pAnioFiscal;
    }
    
    
}
