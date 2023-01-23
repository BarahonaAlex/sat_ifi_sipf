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
public interface SolicitudesCreditoFiscalProjection {

    Integer getIdSolicitud();

    String getNumeroFormulario();

    Date getPinicio();

    Date getPfin();

    String getPeriodo();

    String getActividadEconomica();

    String getEstado();
    
    Integer getIdestado();
    
    String getNit();
    
    Long getNumero();
}
