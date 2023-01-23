/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfRol;
import gt.gob.sat.sat_ifi_sipf.projections.RolProjections;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author ruano
 */
public interface RolRepository extends CrudRepository<SipfRol, Integer> {

    List<RolProjections> findByNombre(String pNombre);
}
