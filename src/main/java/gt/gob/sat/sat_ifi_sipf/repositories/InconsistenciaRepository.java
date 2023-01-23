/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfInconsistencia;
import gt.gob.sat.sat_ifi_sipf.projections.InconsistenciaProjections;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author adftopvar
 */
public interface InconsistenciaRepository extends CrudRepository<SipfInconsistencia, Integer> {
    
    
    
    @Query(value = "select \n"
            + "	si.id_inconsistencia as idInconsistencia,\n"
            + "	(select scd1.nombre \n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd1\n"
            + "	where scd1.codigo_catalogo = 105 and scd1.codigo = si.tipo_inconsistencia) as tipoInconsistencia,\n"
            + "	sscf.periodo_inicio as periodoDesde,\n"
            + "	sscf.periodo_fin as periodoHasta,\n"
            + "	si.tipo_repetida as tipoRepetida,\n"
            + "	si.declaracion_repetida as declaracionRepetida,\n"
            + "	si.factura_proveedor as facturaProveedor,\n"
            + "	si.factura_serie as facturaSerie,\n"
            + "	si.factura_numero as noFactura,\n"
            + "	scd.descripcion as observacion,\n"
            + "	si.estado as idEstado,\n"
            + "	(select scd.nombre \n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd\n"
            + "	where scd.codigo_catalogo = 109 and scd.codigo = si.estado) as estado\n"
            + "from sat_ifi_sipf.sipf_inconsistencia si\n"
            + "inner join sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "on sscf.id_solicitud = si.numero_solicitud \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd \n"
            + "on scd.codigo = si.tipo_inconsistencia \n"
            + "where si.numero_solicitud = :idSolicitud",
            nativeQuery = true)
    List<InconsistenciaProjections> findByRequest(@Param("idSolicitud") Integer idSolicitud);
}
