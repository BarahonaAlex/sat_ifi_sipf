/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfil;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfilId;
import gt.gob.sat.sat_ifi_sipf.projections.PerfilProjections;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ruano
 */
public interface ColaboradorPerfilRepository extends CrudRepository<SipfColaboradorPerfil, SipfColaboradorPerfilId> {

    @Query(value = "select p.idPerfil as idPerfil, p.nombre as nombre from SipfColaboradorPerfil t "
            + " inner join SipfPerfil p on p.idPerfil = t.id.idPerfil "
            + "where t.id.nit = :pNit and t.estado = 161")
    List<PerfilProjections> findByNit(@Param("pNit") String pNit);

    List<SipfColaboradorPerfil> findByIdNitAndEstado(String pNit, Integer pEstado);

    Optional<SipfColaboradorPerfil> findByIdAndEstado(SipfColaboradorPerfilId id, Integer pStatus);

    @Override
    Optional<SipfColaboradorPerfil> findById(SipfColaboradorPerfilId id);
}
