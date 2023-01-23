/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

/**
 *
 * @author adftopvar
 */

public class EfaParametrosDto {
    private String pDocFirmado;
    private String pPeriodoDesde;
    private String pPeriodoHasta;
    private String pNit; 

    public EfaParametrosDto() {
    }

    public String getpDocFirmado() {
        return pDocFirmado;
    }

    public void setpDocFirmado(String pDocFirmado) {
        this.pDocFirmado = pDocFirmado;
    }

    public String getpPeriodoDesde() {
        return pPeriodoDesde;
    }

    public void setpPeriodoDesde(String pPeriodoDesde) {
        this.pPeriodoDesde = pPeriodoDesde;
    }

    public String getpPeriodoHasta() {
        return pPeriodoHasta;
    }

    public void setpPeriodoHasta(String pPeriodoHasta) {
        this.pPeriodoHasta = pPeriodoHasta;
    }

    public String getpNit() {
        return pNit;
    }

    public void setpNit(String pNit) {
        this.pNit = pNit;
    }
    
    
}
