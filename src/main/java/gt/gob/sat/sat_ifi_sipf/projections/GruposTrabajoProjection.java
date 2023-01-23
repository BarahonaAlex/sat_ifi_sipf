/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author crramosl
 */
public interface GruposTrabajoProjection {

    Integer getId();

    String getNombre();

    String getUnidadAdministrativa();

    String getSuperiorInmediato();

    Integer getUnidad();

    String getDescripcion();

    Integer getEstado();

    Integer getIntegrantes();

    Integer getPerfil();

    Integer getIdEstado();
    
    String getNombreEstado();

    String getNit();

    String getIdSolicitante();

    String getIdAceptacion();

    Integer getCasosActivos();
}
