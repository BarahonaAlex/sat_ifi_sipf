/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author ajsbatzmo
 */
public interface CatalogoHijoProjection {

    Integer getCodigo();

    String getNombre();

    String getDescripcion();

    String getEstado();
    
    String getNombrePadre();
    
    String getCodigoIngresado();

}
