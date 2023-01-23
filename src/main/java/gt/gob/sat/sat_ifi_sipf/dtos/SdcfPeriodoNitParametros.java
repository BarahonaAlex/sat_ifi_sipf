/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author ajabarrer
 */
public class SdcfPeriodoNitParametros {
    @JsonProperty(value = "periodoDesde")
    private String periodoDesde;
    @JsonProperty(value = "periodoHasta")
    private String periodoHasta;
    @JsonProperty(value = "nit")
    private String nit;

    public String getPeriodoDesde() {
        return periodoDesde;
    }

    public void setPeriodoDesde(String periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public String getPeriodoHasta() {
        return periodoHasta;
    }

    public void setPeriodoHasta(String periodoHasta) {
        this.periodoHasta = periodoHasta;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public SdcfPeriodoNitParametros(String periodoDesde, String periodoHasta, String nit) {
        this.periodoDesde = periodoDesde;
        this.periodoHasta = periodoHasta;
        this.nit = nit;
    }

    public SdcfPeriodoNitParametros() {
    }

   
}
