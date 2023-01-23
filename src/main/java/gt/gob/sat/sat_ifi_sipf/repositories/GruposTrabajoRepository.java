/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfGrupoTrabajo;
import gt.gob.sat.sat_ifi_sipf.projections.GruposTrabajoProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author crramosl
 */
public interface GruposTrabajoRepository extends CrudRepository<SipfGrupoTrabajo, Long> {

    @Override
    List<SipfGrupoTrabajo> findAll();

    @Query(value = "select\n"
            + "	gt.id,\n"
            + "	gt.nombre,\n"
            + "	ua.nombre as unidadAdministrativa,\n"
            + "	sc.nombres as superiorInmediato,\n"
            + "	gt.id_estado as idEstado,\n"
            + "	scd.nombre as nombreEstado,\n"
            + "	(select\n"
            + "		count(*)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_integrante_grupo ig\n"
            + "	where\n"
            + "		ig.id_grupo = gt.id\n"
            + "		and ig.id_rol = 3\n"
            + "	) integrantes,\n"
            + "	(\n"
            + "	select\n"
            + "		count(*)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_asignacion_casos sac\n"
            + "	inner join sat_ifi_sipf.sipf_casos sc \n"
            + "on\n"
            + "		sc.id_caso = sac.id_caso\n"
            + "	left outer join sat_ifi_sipf.sipf_cat_dato_condicion_especial scdce \n"
            + "on\n"
            + "		scdce.codigo_dato = scd.codigo\n"
            + "	join sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "on\n"
            + "		sig.nit = sac.nit_colaborador\n"
            + "		and sig.id_grupo = gt.id\n"
            + "	where\n"
            + "		scdce.codigo is null\n"
            + "		and sig.id_rol = 3\n"
            + "		and sig.id_estado = 170\n"
            + "	) casosActivos\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_grupo_trabajo gt\n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa ua on\n"
            + "	gt.id_gerencia = ua.id\n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo ig on\n"
            + "	gt.id = ig.id_grupo\n"
            + "	and ig.id_rol = 8\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	ig.nit = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = gt.id_estado\n"
            + "where\n"
            + "	gt.id_estado in (:status);",
            nativeQuery = true
    )
    List<GruposTrabajoProjection> getWorkGroups(@Param("status") List<Integer> status);

    @Query(value = "select \n"
            + "	gt.id,\n"
            + "	gt.nombre,\n"
            + "	ua.nombre as unidadAdministrativa,\n"
            + "	sc.nombres as superiorInmediato,\n"
            + "	gt.id_gerencia as unidad,\n"
            + "	gt.descripcion as descripcion,\n"
            + "	gt.id_estado as estado,\n"
            + "	(\n"
            + "		select\n"
            + "			count(*)\n"
            + "		from \n"
            + "			sat_ifi_sipf.sipf_integrante_grupo ig\n"
            + "		where \n"
            + "			ig.id_grupo = gt.id and ig.id_rol = 3\n"
            + "	) integrantes,\n"
            + "	(select ig.id_perfil from sat_ifi_sipf.sipf_integrante_grupo ig where ig.id_rol = 3 and ig.id_grupo = gt.id limit 1) as perfil\n"
            + "from sat_ifi_sipf.sipf_grupo_trabajo gt \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa ua on gt.id_gerencia = ua.id \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo ig on gt.id = ig.id_grupo and ig.id_rol = 8\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on ig.nit = sc.nit \n"
            + "where gt.id = :id",
            nativeQuery = true
    )
    GruposTrabajoProjection getWorkGroup(@Param("id") Integer id);

    @Query(value = "select\n"
            + "	id_solicitud as id,\n"
            + "	id_estado as estado,\n"
            + "nit_profesional as nit,\n"
            + "id_autorizador_solicitante as idSolicitante,\n"
            + "id_autorizador_acepta as idAceptacion\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_traslado_colaborador\n"
            + "where\n"
            + "	(id_grupo_anterior = :id\n"
            + "	or id_grupo_nuevo =:id) and id_estado =193", nativeQuery = true)
    List<GruposTrabajoProjection> getSolictud(@Param("id") Integer id);

}
