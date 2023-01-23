/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 * @author Rudy Culajay (ruarcuse)
 * @since 07/01/2022
 * @version 1.0
 */
public interface UnidadAdministrativaProjection {

    long getId();

    String getNombre();

    String getDescripcion();

    String getIdEstado();

    String getNombreEstado();

    Long getIdPadre();

    Integer getIdUnidadProsis();

    String getHijos();

    String gethijosActivos();

    String gethijosInactivos();
    
    Integer getIdGrupo();
    
    Date getFechaCreacion();
    
    Integer getIdTipoProgramacion();
    
    String getNombreTipoProgramacion();
}
