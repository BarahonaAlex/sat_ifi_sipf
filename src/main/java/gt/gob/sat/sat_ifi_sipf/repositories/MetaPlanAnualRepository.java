/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfMetaPlanAnual;
import gt.gob.sat.sat_ifi_sipf.projections.PlanAnualProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author crramosl
 */
public interface MetaPlanAnualRepository extends CrudRepository<SipfMetaPlanAnual, Integer> {

    @Query(value = "select pa.id_plan as plan, pa.valor as year, cast((\n"
            + "	select array_to_json(array_agg(row_to_json(so))) from (\n"
            + "     select\n"
            + "         so.id_indicador as indicator, cd.nombre as name, so.valor as value\n"
            + "     from\n"
            + "         sat_ifi_sipf.sipf_indicador_plan_anual so\n"
            + "     inner join sat_ifi_sipf.sipf_cat_dato cd on cd.codigo = so.id_indicador\n"
            + "     where\n"
            + "         pa.id_plan = so.id_plan\n"
            + "     order by so.id_indicador\n"
            + "	 ) so\n"
            + " ) as varchar) as indicators,\n"
            + " cast((\n"
            + "	select array_to_json(array_agg(row_to_json(so))) from (\n"
            + "		select	\n"
            + "			row_number() over (order by mpa.id_tipo_meta, mpa.mes) as id, mpa.mes as month, mpa.id_tipo_meta as type, ct.nombre as \"typeName\", (\n"
            + "			select array_to_json(array_agg(row_to_json(so))) from (\n"
            + "					select	\n"
            + "						mda.id_meta as id, mda.id_gerencia as management, mda.id_departamento as departament, mda.valor as value\n"
            + "					from sat_ifi_sipf.sipf_meta_plan_anual mda\n"
            + "					where mda.id_plan = pa.id_plan and mpa.mes = mda.mes and mpa.id_tipo_meta = mda.id_tipo_meta\n"
            + "					order by mda.id_meta\n"
            + "				 ) so\n"
            + "			 ) as goals\n"
            + "		from sat_ifi_sipf.sipf_meta_plan_anual mpa\n"
            + "		inner join sat_ifi_sipf.sipf_cat_dato ct on ct.codigo = mpa.id_tipo_meta\n"
            + "		where mpa.id_plan = pa.id_plan\n"
            + "		group by mpa.mes, mpa.id_tipo_meta, ct.nombre\n"
            + "		order by mpa.id_tipo_meta, mpa.mes\n"
            + "	 ) so\n"
            + " ) as varchar) as months\n"
            + "from sat_ifi_sipf.sipf_plan_anual pa\n"
            + "where pa.id_plan = :id or pa.valor = :id ", nativeQuery = true)
    Optional<PlanAnualProjection> getPlanById(@Param("id") Integer id);

    List<SipfMetaPlanAnual> findByIdPlan(Integer idPlan);

    List<SipfMetaPlanAnual> findByIdPlanAndMesAndIdTipoMeta(Integer idPlan, Integer mes, Integer idTipoMeta);

    boolean existsByIdPlanAndMesAndIdTipoMeta(Integer idPlan, Integer mes, Integer idTipoMeta);
}
