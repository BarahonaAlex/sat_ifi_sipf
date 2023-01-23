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
public interface TrasladoColaboradorProjection {

    String getNitColaborador();

    String getNombreColaborador();

    String getNitAprobador();

    String getNombreAprobador();

    Integer getGrupo();

    String getNombreGrupo();

    Integer getIdUnidadAdmin();

    String getUnidadAdmin();

    String getEstado();
    
    Integer getIdPerfil();
    
    Integer getIdRol();
    
    Integer getEstadoGrupo();
    
    Integer getEstadoColaboradorGrupo();

}
