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

public interface CasosProjection   {

    Integer getIdCaso();

    Integer getIdEstado();

    Integer getIdOrigen();

    Integer getIdGerencia();

    String getIdProceso();

    Double getMontoRecaudado();

    Integer getIdTipoInsumo();

    Integer getIdPrograma();

    Integer getIdTipoAlcance();

    String getCorrelativoAprobacion();

    String getUsuarioModifica();

    Date getFechaModifica();

    String getIpModifica();

    String getNombreCaso();

    Integer getIdInsumo();

    Date getPeriodoRevisionInicio();

    Date getPeriodoRevisionFin();

    String getNitContribuyente();

    Integer getIdEntidadSolicitante();

    Integer getIdTipoSolicitud();

    String getNombreCuentaAuditar();

    String getNitContribuyenteCruce();

    String getNumeroFacturaCruce();

    String getSerieFacturaCruce();

    Date getFechaFacturaCruce();

    Float getMontoFacturaCruce();

    Date getFechaSolicitud();

    Date getFechaDocumento();

    Integer getNumeroDocumento();

    Date getPlazoEntrega();

    String getNombreContacto();

    String getTelefonoContacto();

    String getCorreoContacto();

    String getdetalleEntIdadSolicitante();

    String getObjetivoCasoMasiva();

    String getTerritorioMasivo();

    Integer getIdVersionAlcance();

    String getNitColaborador();

    String getNombreDepartamento();

    String getNombreEstado();

    String getNombreGerencia();
    
    String getDescripcion(); 
    
    String getNombrePrograma();
    
    String getImpuestos();
    
    Integer getIdAlcance();
    
    String getNombreColaborador();
    
    String getLoginProfesional();
}
