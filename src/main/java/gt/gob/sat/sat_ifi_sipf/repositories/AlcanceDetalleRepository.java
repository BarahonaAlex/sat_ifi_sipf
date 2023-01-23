/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfAlcanceDetalleId;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcanceDetalle;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author jdaldana
 */
public interface AlcanceDetalleRepository extends CrudRepository<SipfAlcanceDetalle,SipfAlcanceDetalleId> {
    
}
