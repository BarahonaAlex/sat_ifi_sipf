/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 * @author Rudy Culajay (ruarcuse)
 * @since 12/01/2022
 * @version 1.0
 */
public interface PuestosTrabajoProjection {

    long getId();

    String getNombre();

    String getDescripcion();

    Long getIdEstado();

    Long getIdPadre();

    Long getIdUnidadAdministrativa();

    Long getHijos();

    String getUnidadAdministrativaDetalle();

    String getEstadoDetalle();
}
