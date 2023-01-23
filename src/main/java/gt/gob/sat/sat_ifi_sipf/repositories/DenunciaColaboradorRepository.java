/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaColaboradorId;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaColaboradorProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author rabaraho
 */
public interface DenunciaColaboradorRepository extends CrudRepository<SipfDenunciaColaborador, SipfDenunciaColaboradorId> {

    @Query("select p.id.nitResponsable as nitResponsable,"
            + " p.id.correlativoDenuncia as correlativoDenuncia,"
            + " p.fechaModifica as fechaModifica,"
            + " p.ipModifica as ipModifica, "
            + " p.usuarioModifica as usuarioModifica "
            + "from SipfDenunciaColaborador p")
    public List<DenunciaColaboradorProjection> findWhole();
}
