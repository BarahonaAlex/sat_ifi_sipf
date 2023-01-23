/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author lfvillag
 */
public interface DetalleDenunciasProjection {  
    String getMotivo();

    Integer getIdMotivo();

    String getEstado();

    Date getCompra();

    Integer getValor();

    String getFormaPago();
    
    String getNFormaPago();

    Integer getRegion();

    String getNRegion();

    String getDepartamento();

    String getEstablecimiento();

    String getDireccion();

    String getMunicipio();

    String getNit();

    String getNombre();

    String getTelefono();

    String getDireDenunciado();
    
    String getObservaciones();
    
    Integer getTipo();
    
    Integer getIdEstado();
}
