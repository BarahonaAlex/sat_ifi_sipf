/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfArchivosCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.projections.ArchivoDataCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author adftopvar
 */

public interface ArchivosCreditoFiscalRepository extends CrudRepository<SipfArchivosCreditoFiscal, Long> {

    Optional<SipfArchivosCreditoFiscal> findByNitContribuyenteAndPeriodoAndIdestadoAndNombre(String nit, String periodo, Integer estado, String nombre);

    @Query(value = "select\n"
            + "	sacf.id_archivo as idArchivo,\n"
            + "	sacf.nombre as nombre,\n"
            + "	sacf.idestado as idEstado,\n"
            + "	sacf.periodo as periodo,\n"
            + "	sacf.nit_contribuyente as nitContribuyente,\n"
            + "	sacf.comentario as comentario\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_archivos_credito_fiscal sacf\n"
            + "inner join sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "on\n"
            + "	sacf.nit_contribuyente = sscf.nit_contribuyente\n"
            + "where\n"
            + "	sscf.id_solicitud = :idSolicitud\n"
            + "	and sacf.idestado = :idEstado\n"
            + "	and sacf.periodo = (\n"
            + "	select\n"
            + "		case\n"
            + "			when (to_char(sscf.periodo_inicio, 'MM') = to_char(sscf.periodo_fin , 'MM')) \n"
            + "		then to_char(sscf.periodo_inicio, 'MM') || '-' || to_char(sscf.periodo_inicio, 'YYYY')\n"
            + "			else ( to_char(sscf.periodo_inicio, 'MM') || to_char(sscf.periodo_fin , 'MM') || '-' || to_char(sscf.periodo_inicio, 'YYYY'))\n"
            + "		end as periodo2\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_solicitud_credito_fiscal k\n"
            + "	where\n"
            + "		k.id_solicitud = :idSolicitud)", nativeQuery = true)
    List<ArchivoDataCreditoFiscalProjection> findByIdSolicitud(@Param("idSolicitud") Integer idSolicitud, @Param("idEstado") Integer idEstado);
 
    @Query(value = "select\n"
            + "		case\n"
            + "			when (to_char(k.periodo_inicio, 'MM') = to_char(k.periodo_fin , 'MM')) \n"
            + "		then to_char(k.periodo_inicio, 'MM') || '-' || to_char(k.periodo_inicio, 'YYYY')\n"
            + "			else ( to_char(k.periodo_inicio, 'MM') || to_char(k.periodo_fin , 'MM') || '-' || to_char(k.periodo_inicio, 'YYYY'))\n"
            + "		end as periodo\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_solicitud_credito_fiscal k\n"
            + "	where\n"
            + "		k.id_solicitud = :idSolicitud", nativeQuery = true)
    ArchivoDataCreditoFiscalProjection findByRequest(@Param("idSolicitud") Integer idSolicitud);
    
    List<SipfArchivosCreditoFiscal> findByNitContribuyenteAndPeriodo(String nit, String periodo);

    @Query(value = "select 	scd.codigo,\n"
            + "		scd.nombre,\n"
            + "		scd.descripcion \n"
            + "from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "inner join sat_ifi_sipf.sipf_cat_catalogo scc \n"
            + "on scc.codigo = scd.codigo_catalogo \n"
            + "where scd.codigo_catalogo  = 104 and scd.descripcion not in (select sacf.nombre \n"
            + "from sat_ifi_sipf.sipf_archivos_credito_fiscal sacf\n"
            + "where sacf.periodo= :periodo and sacf.nit_contribuyente = :nitContribuyente\n"
            + "and sacf.idestado in (:estado))", nativeQuery = true)
    List<CatalogDataProjection> getFileNames (@Param("periodo") String periodo, @Param("nitContribuyente") String nit, @Param("estado") List<Integer> estados);

}
