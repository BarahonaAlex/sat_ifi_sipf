/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfIndicadorPlanAnual;
import gt.gob.sat.sat_ifi_sipf.models.SipfIndicadorPlanAnualId;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author crramosl
 */
public interface IndicadorPlanAnualRepository extends CrudRepository<SipfIndicadorPlanAnual, SipfIndicadorPlanAnualId> {

    List<SipfIndicadorPlanAnual> findByIdPlan(Integer idPlan);
}
