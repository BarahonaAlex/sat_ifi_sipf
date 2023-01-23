/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfPlanAnual;
import gt.gob.sat.sat_ifi_sipf.projections.PlanAnualProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author crramosl
 */
public interface PlanAnualRepository extends CrudRepository<SipfPlanAnual, Integer> {

    Optional<SipfPlanAnual> findByValor(Integer valor);

    @Query(value = "select pa.id_plan as plan, pa.valor as year, cast((\n"
            + "	select array_to_json(array_agg(row_to_json(so))) from (\n"
            + "     select\n"
            + "         so.id_indicador as indicator, cd.nombre as name, so.valor as value\n"
            + "     from\n"
            + "         sat_ifi_sipf.sipf_indicador_plan_anual so\n"
            + "     inner join sat_ifi_sipf.sipf_cat_dato cd on cd.codigo = so.id_indicador\n"
            + "     where\n"
            + "         pa.id_plan = so.id_plan\n"
            + "     order by pa.id_plan\n"
            + "	 ) so\n"
            + " ) as varchar) as indicators\n"
            + "from sat_ifi_sipf.sipf_plan_anual pa\n"
            + "order by pa.valor", nativeQuery = true)
    List<PlanAnualProjection> getAllPlans();
}
