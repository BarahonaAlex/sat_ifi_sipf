/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfConsultaCasoJson;
import java.util.List;
import org.springframework.data.repository.CrudRepository;
import gt.gob.sat.sat_ifi_sipf.projections.CasoConsultaJsonProjection;

/**
 *
 * @author rabaraho
 */
public interface CasoConsultaJsonRepository extends CrudRepository<SipfConsultaCasoJson, Integer> {

    List<CasoConsultaJsonProjection> findByIdCasoAndIdTipoConsulta(Integer pIdCaso, Integer pIdTipoConsulta);
}
