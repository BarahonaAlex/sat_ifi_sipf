/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author rabaraho
 */
public class AsignacionDenunciaParamDto implements Serializable {

    private List<String> pLista = new ArrayList<>();
    private String pNitResponsable;

    public List<String> getpLista() {
        return pLista;
    }

    public void setpLista(List<String> pLista) {
        this.pLista = pLista;
    }

    public String getpNitResponsable() {
        return pNitResponsable;
    }

    public void setpNitResponsable(String pNitResponsable) {
        this.pNitResponsable = pNitResponsable;
    }

}
