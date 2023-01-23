/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfHallazgosEncontrados;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgoDetalleProjection;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgosEncontradosProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author josed
 */
public interface HallazgosEncontradosRepository extends CrudRepository<SipfHallazgosEncontrados, Integer> {

    @Query(value = "select\n"
            + "t1.id_hallazgos as idHallazgos,\n"
            + "t1.id_tipo_hallazgo as idTipoHallazgo,\n"
            + "t1.descripcion_hallazgo as descripcionHallazgo,\n"
            + "t1.id_rubro as idRubro,\n"
            + "t1.id_estado as idEstado,\n"
            + "t1.id_caso as idCaso,\n"
            + "t1.id_impuesto as idImpuesto ,\n"
            + "t1.id_formulario as idFormulario,\n"
            + "t1.periodo as periodo,\n"
            + "t1.fecha_inicio as fechaInicio,\n"
            + "t1.fecha_fin as fechaFin,\n"
            + "t1.periodo_estados_financieros as periodoEstadosFinancieros,\n"
            + "t1.id_estados_financieros as idEstadosFinancieros ,\n"
            + "t1.id_fuente as idFuente,\n"
            + "t3.nombre as nombreFormulario ,\n"
            + "t2.nombre as nombreImpuesto,\n"
            + "t4.nombre as nombreEstado,\n"
            + "t5.nombre as nombreEstadoFinancieros,\n"
            + "t6.nombre as nombreFuente,\n"
            + "t7.nombre as nombreRubro,\n"
            + "t8.comentarios as solicitudCambios,\n"
            + "t11.id_estado as estadoCaso \n"
            + "from\n"
            + "sat_ifi_sipf.sipf_hallazgos_encontrados as t1\n"
            + "left outer join sat_ifi_sipf.sipf_cat_dato as t2\n"
            + "on\n"
            + "t2.codigo = t1.id_impuesto\n"
            + "left outer join sat_ifi_sipf.sipf_cat_dato as t3\n"
            + "on\n"
            + "t3.codigo = t1.id_formulario\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t4\n"
            + "on\n"
            + "t4.codigo = t1.id_estado\n"
            + "left outer join sat_ifi_sipf.sipf_cat_dato as t5\n"
            + "on\n"
            + "t5.codigo = t1.id_estados_financieros\n"
            + "left outer join sat_ifi_sipf.sipf_cat_dato as t6\n"
            + "on\n"
            + "	t6.codigo = t1.id_fuente\n"
            + "left outer join sat_ifi_sipf.sipf_cat_dato as t7\n"
            + "on\n"
            + "	t7.codigo = t1.id_rubro\n"
            + "left outer join sat_ifi_sipf.sipf_historial_comentarios t8 \n"
            + "	on\n"
            + "	t8.id_registro = text(t1.id_hallazgos)\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos t10\n"
            + "on \n"
            + "	t10.id_caso = t1.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_casos t11\n"
            + "on \n"
            + "t11.id_caso=t10.id_caso\n"
            + "where\n"
            + "	 t1.id_caso =:idCaso\n"
            + "	and t1.id_tipo_hallazgo =:idtipohallazgo\n"
            + "	and t1.id_estado != 13\n"
            + "	and t10.nit_colaborador =:nitColaborador\n"
            + "	or t8.id_historial_comentario = (\n"
            + "	select\n"
            + "	max(t9.id_historial_comentario)\n"
            + "	from\n"
            + "	sat_ifi_sipf.sipf_historial_comentarios t9\n"
            + "	where\n"
            + "	t9.id_registro = text (t1.id_hallazgos) and t9.id_tipo_comentario =:idTipoComentario )", nativeQuery = true)
    List<HallazgosEncontradosProjection> findFindingsCases(
            @Param("nitColaborador") String nitColaborador,
            @Param("idCaso") Integer idinsumo,
            @Param("idtipohallazgo") Integer idtipohallazgo,
            @Param("idTipoComentario") Integer idTipoComentario);

    @Query(value = "select\n"
            + "	t1.id_hallazgos as idHallazgos,\n"
            + "	t1.id_estado as idEstado,\n"
            + "	t1.*\n"
            + "from sat_ifi_sipf.sipf_hallazgos_encontrados as t1\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos as t2\n"
            + "	on \n"
            + "	t1.id_caso = t2.id_caso\n"
            + "where\n"
            + "t2.id_caso =:idInsumo\n"
            + "and t2.nit_colaborador =:nitPro\n"
            + "and t1.id_estado != 9 and t1.id_estado !=12 and t1.id_estado !=13 and t1.id_estado !=14 ", nativeQuery = true)
    List<HallazgosEncontradosProjection> getFindIds(
            @Param("nitPro") String nitPro,
            @Param("idInsumo") Integer idInsumo);

    @Query(value = "select\n"
            + "distinct concat_ws(' ', t3.primer_nombre, t3.segundo_nombre, t3.tercer_nombre, t3.primer_apellido, t3.segundo_apellido, 'DE ' || t3.apellido_casada) as nombreContribuyente,\n"
            + "t1.id_insumo as idInsumo , t2.nombre_inconsistencia as descripcionHallazgo, t2.process_id as processId, t2.nit_profesional_responsable as nitProfesional,\n"
            + "t2.nit_contribuyente as nitContribuyente, t2.id_programa as idPrograma, t2.usuario_creacion as usuarioAdiciono, fc.nombre as nombreProfesional\n"
            + "from sat_ifi_sipf.sipf_hallazgos_encontrados as t1 \n"
            + "inner join  sat_ifi_sipf.sipf_casos t2 on t1.id_caso =t2.id_caso \n"
            + "inner join sat_rtu.sat_rtu.rtu_contrib_individual t3 on t2.nit_contribuyente  =t3.nit\n"
            + "left join sat_ifi_sipf.sipf_asignacion_casos fc on t1.id_caso = fc.id_caso \n"
            + "where t1.id_estado =9  and fc.nit_colaborador  =:nitPro", nativeQuery = true)
    List<HallazgosEncontradosProjection> getFindingsFoundReview(@Param("nitPro") String nitPro);

    @Query(value = "select \n"
            + "	he.id_hallazgo id, he.nombre, he.descripcion, \n"
            + "	cast((\n"
            + "	select array_to_json(array_agg(row_to_json(so))) from (\n"
            + "		select\n"
            + "			rb.id,\n"
            + "			(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_rubro) rubro,\n"
            + "			(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_impuesto) impuesto\n"
            + "		from sat_ifi_sipf.sipf_rubro_hallazgo rh\n"
            + "		inner join sat_ifi_sipf.sipf_rubro rb on rh.id_rubro = rb.id\n"
            + "		where rh.id_hallazgo = he.id_hallazgo\n"
            + "	 ) so\n"
            + " ) as text) as rubros\n"
            + "from sat_ifi_sipf.sipf_hallazgos_encontrados he\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos ac on he.id_caso = ac.id_caso\n"
            + "where he.id_caso = :id and ac.nit_colaborador = :nit",
            nativeQuery = true)
    List<HallazgoDetalleProjection> getDetailFindings(@Param("nit") String nit, @Param("id") Integer id);
}
