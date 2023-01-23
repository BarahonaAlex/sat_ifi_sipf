/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

/**
 *
 * @author sacanoes
 */
public class DeclaracionParametrosDto {
    
    @JsonProperty(value = "pNit")
    private String pNit;
    
    @JsonProperty(value = "pAnio")
    private List<Integer> pAnio;

    @JsonProperty(value = "pCodigo")
    private List<Integer> pCodigo;
    
    public DeclaracionParametrosDto() {
    }

    public String getpNit() {
        return pNit;
    }

    public void setpNit(String pNit) {
        this.pNit = pNit;
    }

    public List<Integer> getpAnio() {
        return pAnio;
    }

    public void setpAnio(List<Integer> pAnio) {
        this.pAnio = pAnio;
    }

    public List<Integer> getpCodigo() {
        return pCodigo;
    }

    public void setpCodigo(List<Integer> pCodigo) {
        this.pCodigo = pCodigo;
    }   
}
