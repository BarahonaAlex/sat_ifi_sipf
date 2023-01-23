/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author Jamier
 */
public interface AlcanceProjection {

    //String getContribuyente();
    String getNit();

    Date getPeriodoDel();

    Date getPeriodoAl();

    String getPrograma();

    Integer getTipoAlcance();
    
    Integer getIdAlcance();
    
    Integer getIdFormulario();
    
    Integer getIdSeccion();
    
    String getDetalle();
    

}
