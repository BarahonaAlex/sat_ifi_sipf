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
public interface DenunciaColaboradorProjection {

    String getCorrelativoDenuncia();

    String getNitResponsable();

    String getUsuarioModifica();

    Date getFechaModifica();

    String getIpModifica();

}
