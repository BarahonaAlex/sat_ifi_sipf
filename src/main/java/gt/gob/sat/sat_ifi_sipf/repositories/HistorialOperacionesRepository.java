/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author asacanoes
 */
public interface HistorialOperacionesRepository extends CrudRepository<SipfHistoricoOperaciones, Integer> {

    @Query(value = "select sho.*\n"
            + "from sat_ifi_sipf.sipf_historico_operaciones sho \n"
            + "inner join sat_ifi_sipf.sipf_insumo si on sho.id_cambio_registro = cast ( si.id_insumo as varchar)\n"
            + "where sho.nombre_tabla = 'sipf_insumo' \n"
            + "and si.id_insumo = :idInsumo\n"
            + "order by sho.fecha_modifica desc\n"
            + "limit 1", nativeQuery = true)
    SipfHistoricoOperaciones getCaseLastStateByInput(@Param("idInsumo") Integer idRegistroCambio);
}
