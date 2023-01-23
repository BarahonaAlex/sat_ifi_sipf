/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfPerfil;
import gt.gob.sat.sat_ifi_sipf.projections.FuncionesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.PerfilProjections;
import gt.gob.sat.sat_ifi_sipf.projections.RolProjections;
import gt.gob.sat.sat_ifi_sipf.projections.UrlProjections;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ruano
 */
public interface PerfilRepository extends CrudRepository<SipfPerfil, Integer> {

    List<PerfilProjections> findByIdRol(Integer pIdRol);

    @Query(value = "select t from SipfPerfil t "
            + " inner join SipfRol p on p.idRol = t.idRol "
            + "where p.nombre in :pListaRol and t.estado = 159 ")
    List<PerfilProjections> findByListRol(@Param("pListaRol") List<String> pListaRol);

    @Query(value = "select t from SipfPerfil t "
            + " inner join SipfRol p on p.idRol = t.idRol "
            + "where p.nombre = :pNombre and t.estado = 159")
    List<PerfilProjections> findByRolName(@Param("pNombre") String pNombre);

    @Query(value = "select t.* from sat_ifi_sipf.sipf_opciones_menu t \n"
            + "inner join sat_ifi_sipf.sipf_perfil_funcion p on p.id_opcion_menu = t.codigo \n"
            + "inner join sat_ifi_sipf.sipf_colaborador_perfil c on c.id_perfil = p.id_perfil\n"
            + "where c.nit = :nit and c.estado = 161", nativeQuery = true)
    List<FuncionesProjection> findByLogin(@Param("nit") String nit);

    @Query(value = "select t.* from sat_ifi_sipf.sipf_url t \n"
            + "inner join sat_ifi_sipf.sipf_perfil_url p on p.id_url = t.id_url\n"
            + "inner join sat_ifi_sipf.sipf_colaborador_perfil c on c.id_perfil = p.id_perfil\n"
            + "where c.nit = :nit and c.estado = 161", nativeQuery = true)
    List<UrlProjections> findByLoginUrl(@Param("nit") String nit);

    @Query(value = "select t from SipfRol t "
            + "where t.nombre in :pListaRol ")
    List<RolProjections> findByLsitaRol(@Param("pListaRol") List<String> pListaRol);
}
