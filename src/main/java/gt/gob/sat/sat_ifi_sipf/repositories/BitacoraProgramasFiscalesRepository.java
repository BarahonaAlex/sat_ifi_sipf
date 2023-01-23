/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfBitacoraProgramasFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.BitacoraProgramasFiscalesProjection;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jdaldana
 */
public interface BitacoraProgramasFiscalesRepository extends CrudRepository<SipfBitacoraProgramasFiscales, Integer> {

    @Query(value = "select\n"
            + "	shc.id as id ,\n"
            + "	shc.id_programa as idPrograma,\n"
            + "	shc.id_tipo_operacion as idTipoOperacion,\n"
            + "	shc.id_estado_nuevo as idEstadoNuevo,\n"
            + "	shc.id_estado_anterior as idEstadoAnterior\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_bitacora_programas_fiscales shc\n"
            + "where\n"
            + "	shc.id = (\n"
            + "	select\n"
            + "		max(t.id)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_bitacora_programas_fiscales t\n"
            + "	where\n"
            + "		t.id_tipo_operacion in( :pIdTipoOperacion)\n"
            + "		and t.id_programa =:pIdPrograma)", nativeQuery = true)
    public BitacoraProgramasFiscalesProjection findMaxIdRegistroBitacoraByTypeByIdPrograma(@Param("pIdTipoOperacion") Integer pIdTipoOperacion,@Param("pIdPrograma") Integer pIdPrograma );
    
    

}
