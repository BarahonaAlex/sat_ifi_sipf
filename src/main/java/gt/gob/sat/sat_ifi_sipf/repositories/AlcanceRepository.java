/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfAlcance;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraCasosProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jdaldana
 */
public interface AlcanceRepository extends CrudRepository<SipfAlcance, Integer> {

    @Query(value = "select T.*  \n"
            + "from (\n"
            + "select distinct on(sa.id_alcance) sa.id_alcance as IdAlcance,\n"
            + "scd.nombre as tipo,\n"
            + "scd2.nombre as estado,\n"
            + "case\n"
            + "when sa.id_tipo_alcance = 973 then spf.id_alcance \n"
            + "when sa.id_tipo_alcance = 974 then sdg.id_alcance \n"
            + "else 0\n"
            + "end as presencia\n"
            + "from sat_ifi_sipf.sipf_alcance sa \n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on sa.id_tipo_alcance = scd.codigo \n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on sa.id_estado  = scd2.codigo \n"
            + "left join sat_ifi_sipf.sipf_presencias_fiscales spf on sa.id_alcance = spf.id_alcance \n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on sa.id_alcance = sdg.id_alcance\n"
            + "where sa.id_tipo_alcance =:idalcance \n"
            + ") as T \n"
            + "where T.presencia is not null", nativeQuery = true)
    List<BandejaAlcanceProjection> getPointsPresenceCabinet(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	t1.id_alcance as IdAlcance,\n"
            + "	t2.id_seccion as IdSeccion,\n"
            + "	t2.detalle as Detalle,\n"
            + "	t3.id_formulario as IdFormulario\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance as t1\n"
            + "join sat_ifi_sipf.sipf_alcance_detalle as t2 on\n"
            + "	t2.id_alcance = t1.id_alcance\n"
            + "join sat_ifi_sipf.sipf_presencias_fiscales as t3 on\n"
            + "	t3.id_alcance = t1.id_alcance\n"
            + "where\n"
            + "	t1.id_alcance =:idAlcance", nativeQuery = true)
    List<AlcanceProjection> getAlcancebyIdPresencias(@Param("idAlcance") Integer idAlcance);

    @Query(value = "select\n"
            + "	t1.id_alcance as IdAlcance,\n"
            + "	t2.id_seccion as IdSeccion,\n"
            + "	t2.detalle as Detalle,\n"
            + "	t3.id_caso as IdCaso\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance as t1\n"
            + "join sat_ifi_sipf.sipf_alcance_detalle as t2 on\n"
            + "	t2.id_alcance = t1.id_alcance\n"
            + "join sat_ifi_sipf.sipf_detalle_caso as t3 on\n"
            + "	t3.id_alcance = t1.id_alcance\n"
            + "where\n"
            + "	t1.id_alcance =:idAlcance", nativeQuery = true)
    List<AlcanceProjection> getAlcancebyIdSelectiva(@Param("idAlcance") Integer idAlcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	scd2.nombre as estado,\n"
            + "	spf.id_formulario as idPresencia,\n"
            + "	sdg.id_proceso as idProceso\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_presencias_fiscales spf on\n"
            + "	sa.id_alcance = spf.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "	scd2.codigo = sa.id_estado\n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.id_alcance = sa.id_alcance\n"
            + "where\n"
            + "	sa.id_tipo_alcance = 973\n"
            + "	and (sdg.estado = 18 or spf.id_estado = 18) order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getPresence(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	sa.id_estado as idEstado,\n"
            + "	scd3.nombre as estado,\n"
            + "	sdc.id_caso as IdCaso,\n"
            + "	sa.id_tipo_alcance as idTipo,\n"
            + "	sdg.id_proceso as idProceso,\n"
            + "	sdg.correlativo \n"
            + "from\n"
            + "sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_detalle_caso sdc on\n"
            + "sdc.id_alcance = sa.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sdc.id_caso\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "scd2.codigo = sc.id_estado\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on\n"
            + "scd3.codigo = sa.id_estado \n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "sdg.id_alcance = sa.id_alcance\n"
            + "where\n"
            + "sa.id_tipo_alcance = 976\n"
            + "and sa.id_estado =18 order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getGabinet(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	sa.id_estado as idEstado,\n"
            + "	scd3.nombre as estado,\n"
            + "	sdc.id_caso as IdCaso,\n"
            + "	sdg.id_proceso as idProceso,\n"
            + "	sdg.correlativo \n"
            + "from\n"
            + "sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_detalle_caso sdc on\n"
            + "sdc.id_alcance = sa.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sdc.id_caso\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "scd2.codigo = sc.id_estado\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on\n"
            + "scd3.codigo = sa.id_estado \n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "sdg.id_alcance = sa.id_alcance\n"
            + "where\n"
            + "sa.id_tipo_alcance = 975\n"
            + "and sa.id_estado =18 order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getPoint(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	scd2.nombre as estado,\n"
            + "	scd3.nombre as presenciaEstad,\n"
            + "	spf.id_formulario as idPresencia,\n"
            + "	sdg.id_proceso as idProceso\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_presencias_fiscales spf on\n"
            + "	sa.id_alcance = spf.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "	scd2.codigo = sa.id_estado\n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.id_alcance = sa.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on \n"
            + "	scd3.codigo = sdg.estado \n"
            + "	where\n"
            + "	sa.id_tipo_alcance = 973\n"
            + "	and sa.id_estado = 19\n"
            + "	and (sdg.estado = 19\n"
            + "	or spf.id_estado = 19)\n"
            + " order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getPresenceJd(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	t1.id_alcance as idAlcance,\n"
            + "	t1.id_estado as estado ,\n"
            + "	t2.id_seccion as idSeccion ,\n"
            + "	t2.detalle as detalle\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance as t1\n"
            + "inner join sat_ifi_sipf.sipf_alcance_detalle as t2 on\n"
            + "	t2.id_alcance = t1.id_alcance\n"
            + "where\n"
            + "	t1.id_alcance =:idalcance", nativeQuery = true)
    List<BandejaAlcanceProjection> getPuntosFijosPresencias(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	sa.id_estado as idEstado,\n"
            + "	scd3.nombre as estado,\n"
            + "	sdc.id_caso as IdCaso,\n"
            + "	sa.id_tipo_alcance as idTipo,\n"
            + "	sdg.id_proceso as idProceso,\n"
            + "	sdg.correlativo \n"
            + "from\n"
            + "sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_detalle_caso sdc on\n"
            + "sdc.id_alcance = sa.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sdc.id_caso\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "scd2.codigo = sc.id_estado\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on\n"
            + "scd3.codigo = sa.id_estado \n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "sdg.id_alcance = sa.id_alcance\n"
            + "where\n"
            + "sa.id_tipo_alcance = 976\n"
            + "and sa.id_estado =19 order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getGabinetJd(@Param("idalcance") Integer idalcance);

    @Query(value = "select\n"
            + "	sa.id_alcance as IdAlcance,\n"
            + "	scd.nombre as tipo,\n"
            + "	sa.id_estado as idEstado,\n"
            + "	scd3.nombre as estado,\n"
            + "	sdc.id_caso as IdCaso,\n"
            + "	sdg.id_proceso as idProceso,\n"
            + "	sdg.correlativo \n"
            + "from\n"
            + "sat_ifi_sipf.sipf_alcance sa\n"
            + "left join sat_ifi_sipf.sipf_detalle_caso sdc on\n"
            + "sdc.id_alcance = sa.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sdc.id_caso\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sa.id_tipo_alcance\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "scd2.codigo = sc.id_estado\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on\n"
            + "scd3.codigo = sa.id_estado \n"
            + "left join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "sdg.id_alcance = sa.id_alcance\n"
            + "where\n"
            + "sa.id_tipo_alcance = 975\n"
            + "and sa.id_estado =19 order by sa.id_alcance desc", nativeQuery = true)
    List<BandejaAlcanceProjection> getPointJd(@Param("idalcance") Integer idalcance);

    /**
     * @author Luis Villagran (lfvillag)
     * @description Script para obtener información del caso en base a un estado de alcance y el tipo de alcance selectivo
     * @since 1/19/2023
     * @param states
     * @return 
     */
    @Query(value = "	select\n"
            + "	sa.id_estado as estadoAlcance,\n"
            + "	scd2.nombre as nombreEstadoAlcance,\n"
            + "    t1.nit_contribuyente as nitContribuyente,\n"
            + "    coalesce (\n"
            + "        (select rcj.razon_social as contribuyente\n"
            + "        from sat_rtu.sat_rtu.rtu_contrib_juridico rcj\n"
            + "        where rcj.nit = t1.nit_contribuyente),\n"
            + "        (select concat_ws(' ',\n"
            + "        rci.primer_nombre,\n"
            + "        rci.segundo_nombre,\n"
            + "        rci.tercer_nombre,\n"
            + "        rci.primer_apellido,\n"
            + "        rci.segundo_apellido,\n"
            + "        'DE ' || rci.apellido_casada) as contribuyente\n"
            + "        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "        where rci.nit = t1.nit_contribuyente)\n"
            + "    ) as nombreContribuyente,\n"
            + "    t1.monto_recaudado as montoRecaudado,\n"
            + "    t1.periodo_revision_inicio as periodoInicio,\n"
            + "    t1.periodo_revision_fin as periodoFin,\n"
            + "    cast((\n"
            + "    select array_to_json(array_agg(row_to_json(so))) from \n"
            + "    (\n"
            + "    select scd.nombre nombreImpuesto,\n"
            + "    sic.id_impuesto idImpuesto\n"
            + "    from sat_ifi_sipf.sipf_casos sc \n"
            + "    inner join sat_ifi_sipf.sipf_impuesto_caso sic\n"
            + "    on sic.id_caso = sc.id_caso \n"
            + "    inner join sat_ifi_sipf.sipf_cat_dato scd \n"
            + "    on scd.codigo = sic.id_impuesto \n"
            + "    where sc.id_caso = t1.id_caso\n"
            + "    )\n"
            + "    so\n"
            + "     ) as varchar) as impuestos,\n"
            + "    t1.id_caso as idCaso,\n"
            + "    t5.nit_colaborador as nitProfesionalResponsable,\n"
            + "    t1.id_origen as idOrigen,\n"
            + "    t6.nombre as nombreOrigen,\n"
            + "    t3.nombre as nombreEstado,\n"
            + "    t7.nombre as nombreGerencia,\n"
            + "    t1.id_estado as idEstado,\n"
            + "    t1.id_proceso as processId,\n"
            + "    t1.usuario_modifica as usuarioElaboro,\n"
            + "    t1.id_programa as noPrograma\n"
            + "from\n"
            + "    sat_ifi_sipf.sipf_casos as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t3 on\n"
            + "    t1.id_estado = t3.codigo\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t7 on\n"
            + "    t1.id_gerencia = t7.codigo\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos as t5 on\n"
            + "    t5.id_caso = t1.id_caso\n"
            + "left join sat_ifi_sipf.sipf_cat_dato as t6 on\n"
            + "    t6.codigo = t1.id_origen\n"
            + "inner join sat_ifi_sipf.sipf_detalle_caso sdc on\n"
            + "sdc.id_caso = t1.id_caso \n"
            + "inner join sat_ifi_sipf.sipf_alcance sa on\n"
            + "sa.id_alcance = sdc.id_alcance \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + " 	scd2.codigo = sa.id_estado \n"
            + "where\n"
            + "    sa.id_estado in(:states) and sa.id_tipo_alcance = 969\n"
            + " ORDER BY t1.id_caso DESC", nativeQuery = true)
    List<CarteraCasosProjection> getSelectScope(@Param("states") List<Integer> states);
}
