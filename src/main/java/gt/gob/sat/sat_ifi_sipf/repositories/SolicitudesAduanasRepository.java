/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudesPosteriori;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesAduanasProjections;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author garuanom
 */
public interface SolicitudesAduanasRepository extends CrudRepository<SipfSolicitudesPosteriori, Integer> {

    @Query(value = "select ssp.id_solicitud as idSolicitud,\n"
            + " ssp.nit as nit,\n"
            + " ssp.nombre as nombre,\n"
            + " scd.nombre as estado,\n"
            + " ssp.no_solicitud as solicitud\n"
            + " from sat_ifi_sipf.sipf_solicitudes_posteriori ssp \n"
            + " inner join sat_ifi_sipf.sipf_cat_dato scd on ssp.id_estado = scd.codigo \n"
            + "where ssp.id_estado = 967", nativeQuery = true)
    List<SolicitudesAduanasProjections> getSolitudesAduanas();
}
