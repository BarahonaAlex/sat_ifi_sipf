/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoPrograma;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoProgramaId;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author rabaraho
 */
public interface SipfImpuestoProgramaRepository extends CrudRepository<SipfImpuestoPrograma, SipfImpuestoProgramaId> {

    @Query(value = "select p from SipfImpuestoPrograma p where p.id.idPrograma=:idPrograma")
    List<SipfImpuestoPrograma> findByPrograma(@Param("idPrograma") Integer idPrograma);

}
