/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author ruano
 */
public interface ColaboradoresProjection {

    String getnit();

    String getnombres();

    String getlogin();

    String getcorreo();

    Integer getid_estado();

    String getpuesto_trabajo();

    Integer getid_gerencia();

    String getnombreEstado();

    String getNombreUnidadAdministrativa();

    Date getFechaFin();

    Date getFechaInicio();

    String getnombreTipoProgramacion();

    Integer getidTipoProgramacion();

}
