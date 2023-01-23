/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author jdaldana
 */
public interface PresenciasFiscalesProjetion {

    Integer getIdFormulario();

    Date getFechaInicio();

    String getLugarDepartamental();

    Integer getIdPrograma();

    Date getFechaEjecucion();

    Integer getMeta();

    Date getFechaFin();

    String getIdProceso();

    Integer getIdEstado();

    String getUsuarioModifica();

    Date getFechaModifica();

    String getUsuarioCreacion();

    String getNombreEstado();

    Integer getIdAlcance();

    Integer getIdGerencia();

    String getLugarEjecucion();
}
