/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author ajdaldana
 */
public interface InsumoProjecionStatus extends InsumoProjection {

    String getNombreGerencia();

    String getNombreDepartamento();

    String getNombreImpuesto();

    String getNombreEstado();

    Integer getCantidadCasos();

    Integer getCantidadCasosAsingar();

    String getUsuarioPublica();

    Date getFechaPublica();
    
    String getComentarioSuspender();
}
