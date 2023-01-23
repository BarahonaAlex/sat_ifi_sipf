/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgoDenunciaGabinete;
import gt.gob.sat.sat_ifi_sipf.models.SipfRubroHallazgoDenunciaGabineteId;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author adftopvar
 */
public interface RubroHallazgoDenunciaGabineteRepository extends CrudRepository<SipfRubroHallazgoDenunciaGabinete, SipfRubroHallazgoDenunciaGabineteId> {
    
    void deleteByIdHallazgo(Integer idHallazgo);
}
