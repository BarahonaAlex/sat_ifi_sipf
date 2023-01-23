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
public interface DesasignarCasosProjection {
    String getNitProfesional();
    String getNombreProfesional();
    String getNombreInsumo();
    Integer getCantidadCasos();
    String getDescripcionCaso();
    Date getFecha();
    String getNombreEstado();
}
