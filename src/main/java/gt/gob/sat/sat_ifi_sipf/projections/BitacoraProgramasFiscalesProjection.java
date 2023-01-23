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
public interface BitacoraProgramasFiscalesProjection {
    
     int getId();

    int getIdPrograma();

    int getIdTipoOperacion();

    String getUsuarioModifica();

    Date getFechaModifica();

    int getIdEstadoAnterior();

    int getIdEstadoNuevo();

}
