/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author josed
 */
public interface CarteraCasosProjection {
    
    Integer getEstadoAlcance();
    
    String getNombreEstadoAlcance();
    
    String getNitContribuyente();

    Integer getMontoRecaudado();

    Date getPeriodoInicio();

    Date getPeriodoFin();

    Integer getIdTipoInsumo();

    String getNombreTipoCaso();

    String getNombreImpuesto();

    String getNombreGerencia();

    Integer getIdImpuesto();

    String getNombreCaso();

    String getNombreOrigen();

    Integer getIdCaso();

    String getNitProfesionalResponsable();

    Integer getIdOrigen();

    String getNombreEstado();

    String getIdEstado();

    Long getProcessId();

    String getUsuarioElaboro();

    Integer getNoPrograma();

    String getNombreContribuyente();

    String getImpuestos();
       
    Integer getTipoCaso();
}
