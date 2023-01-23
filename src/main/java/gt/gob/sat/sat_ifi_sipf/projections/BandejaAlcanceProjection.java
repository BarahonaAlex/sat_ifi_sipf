/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author garuanom
 */
public interface BandejaAlcanceProjection {
        
    Integer getEstadoAlcance();
    
    String getNombreEstadoAlcance();
    
    String getTipo();

    String getEstado();

    Integer getIdAlcance();

    Integer getIdPresencia();
    
    Integer getIdCaso();
    
    Integer getIdProceso();
    
    Integer getIdTipo();
    
    Integer getIdSeccion();
    
    String getDetalle();
}
