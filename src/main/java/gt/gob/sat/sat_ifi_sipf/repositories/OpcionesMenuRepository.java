/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfOpcionesMenu;
import gt.gob.sat.sat_ifi_sipf.projections.OpcionMenuProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author crramosl
 */
public interface OpcionesMenuRepository extends CrudRepository<SipfOpcionesMenu, String> {

    @Query(value = "select om.codigo as id, om.titulo as title, om.icono as icon, cast(("
            + "	select array_to_json(array_agg(row_to_json(so))) from ("
            + "     select"
            + "         so.codigo id, so.titulo title, so.icono icon, so.ruta route"
            + "     from"
            + "         sat_ifi_sipf.sipf_opciones_menu so"
            + "     where"
            + "         om.codigo = so.codigo_padre and so.codigo in (:functions)"
            + "     order by so.orden"
            + "	 ) so"
            + " ) as varchar) as children"
            + " from"
            + "     sat_ifi_sipf.sipf_opciones_menu om"
            + " where"
            + "     om.codigo_padre is null"
            + " order by om.orden",
            nativeQuery = true
    )
    List<OpcionMenuProjection> getAllowMenuOption(@Param("functions") List<String> functions);

    @Query(value = "select om.codigo as id, om.titulo as title, om.icono as icon, cast(("
            + "	select array_to_json(array_agg(row_to_json(so))) from ("
            + "     select"
            + "         so.codigo id, so.titulo title, so.icono icon, so.ruta route"
            + "     from"
            + "         sat_ifi_sipf.sipf_opciones_menu so"
            + "     where"
            + "         om.codigo = so.codigo_padre"
            + "     order by so.orden"
            + "	 ) so"
            + " ) as varchar) as children"
            + " from"
            + "     sat_ifi_sipf.sipf_opciones_menu om"
            + " where"
            + "     om.codigo_padre is null"
            + " order by om.orden",
            nativeQuery = true
    )
    List<OpcionMenuProjection> getAllowMenuOption();
}
