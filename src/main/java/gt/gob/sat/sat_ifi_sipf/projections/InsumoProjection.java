/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author rabaraho
 */
public interface InsumoProjection {

    int getIdInsumo();

    String getNombreInsumo();

    Integer getIdGerencia();

    Integer getIdEstado();

    Integer getIdEstadoAnterior();

    String getNitEncargado();

    Double getMontoRecaudo();

    Integer getIdDepartamento();

    Integer getIdTipoInsumo();

    String getDescripcion();

    Integer getIdProceso();

    Date getFechaPublicacion();
}
