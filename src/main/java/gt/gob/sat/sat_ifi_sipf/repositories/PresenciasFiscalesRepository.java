/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfPresenciasFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.PresenciasFiscalesProjetion;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author jdaldana
 */
public interface PresenciasFiscalesRepository extends CrudRepository<SipfPresenciasFiscales, Integer> {

    @Query(value = "select\n"
            + "	t1.id_formulario as IdFormulario,\n"
            + "	t1.fecha_fin as FechaFin,\n"
            + "	t1.fecha_inicio as FechaInicio,\n"
            + "	t1.lugar_departamental as LugarDepartamental,\n"
            + "	t1.id_estado as IdEstado,\n"
            + "	t1.meta as Meta,\n"
            + " t1.id_gerencia as idGerencia,\n"
            + "	t2.nombre as NombreEstado\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_presencias_fiscales as t1\n"
            + "join sat_ifi_sipf.sipf_cat_dato as t2 on\n"
            + "	t2.codigo = t1.id_estado "
            + "where t1.usuario_creacion=:nitP and t1.id_estado!=182 order by t1.id_formulario desc ", nativeQuery = true)
    List<PresenciasFiscalesProjetion> findAllPresencias(@Param("nitP") String nitP);
    
    @Query(value = "select\n"
            + "	t1.id_formulario as IdFormulario,\n"
            + "	t1.fecha_fin as FechaFin,\n"
            + "	t1.fecha_inicio as FechaInicio,\n"
            + "	t1.lugar_departamental as LugarDepartamental,\n"
            + " t1.lugar_ejecucion as LugarEjecucion,\n"
            + "	t1.meta as Meta,\n"
            + " t1.id_proceso as IdProceso,\n"
            + " t1.id_estado as IdEstado,\n"
            + " t1.id_alcance as IdAlcance,\n"
            + " t1.id_gerencia as IdGerencia,\n"
            + " t1.id_programa as IdPrograma\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_presencias_fiscales as t1\n"
            + " where t1.id_formulario=:id" ,nativeQuery = true)
    PresenciasFiscalesProjetion findByIdFormulario(@Param("id") Integer idFormulario);
    
    List<SipfPresenciasFiscales>  findByIdAlcance(Integer idAlcance); 
    
    
     @Query(value = "select * from sat_ifi_sipf.sipf_presencias_fiscales spf where spf.id_formulario =:idFormulario" ,nativeQuery = true)
    SipfPresenciasFiscales IdFormulario(@Param("idFormulario") Integer idFormulario);

}
