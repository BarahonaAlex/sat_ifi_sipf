/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgo;
import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgoId;
import gt.gob.sat.sat_ifi_sipf.projections.RubroProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author crramosl
 */
public interface RubroHallazgoRepository extends CrudRepository<SipfRubroHallazgo, SipfRubroHallazgoId> {

    @Query(value = "select rb.id, \n"
            + "	(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_rubro) rubro,\n"
            + "	(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_impuesto) impuesto,\n"
            + "	(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_seccion) seccion,\n"
            + "	(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_subseccion) subseccion\n"
            + "from sat_ifi_sipf.sipf_rubro rb", nativeQuery = true)
    List<RubroProjection> findAllItems();

    void deleteByIdHallazgo(Integer idHallazgo);
}
