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
public interface CasosOnlyProjection {

    public int getIdCaso();

    public int getIdEstado();

    public Integer getIdOrigen();

    public Integer getIdGerencia();

    public String getIdProceso();

    public Double getMontoRecaudado();

    public Integer getIdPrograma();

    // public Integer getIdImpuesto();
    public int getIdDepartamento();

    public String getCorrelativoAprobacion();

    public String getUsuarioModifica();

    public Date getFechaModifica();

    public String getIpModifica();

    public Integer getIdInsumo();

    public Date getPeriodoRevisionInicio();

    public Date getPeriodoRevisionFin();

    public String getNitContribuyente();

    public Integer getIdZona();

    public Integer getIdMunicipio();

}
