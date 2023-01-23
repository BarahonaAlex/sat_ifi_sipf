/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;


import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesCreditoFiscalProjection;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Date;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ajabarrer
 */
public interface CreditoFiscalRepository extends CrudRepository<SipfSolicitudCreditoFiscal, Integer> {
    @Override
    public List<SipfSolicitudCreditoFiscal> findAll();
    
    @Query(value = "select sscf.id_solicitud,\n" +
                    "sscf.numero_formulario,\n" +
                    "sscf.nit_contribuyente,\n" +
                    "sscf.periodo_inicio,\n" +
                    "sscf.periodo_fin,\n" +
                    "sscf.actividad_economica,\n" +
                    "sscf.principal_producto,\n" +
                    "sscf.formulario_iva,\n" +
                    "sscf.credito_sujeto_devolucion,\n" +
                    "sscf.monto,\n" +
                    "sscf.credito_no_solicitado,\n" +
                    "sscf.monto_devolucion,\n" +
                    "sscf.multa,\n" +
                    "sscf.total,\n" +
                    "sscf.asignado,\n" +
                    "sscf.estado,\n" +
                    "sscf.usuario_modifica,\n" +
                    "sscf.fecha_modifica,\n" +
                    "sscf.ip_modifica\n" +
                    "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n" +
                    "where sscf.asignado = :nit and sscf.estado = :estado\n" +
                    "order by sscf.id_solicitud",
            nativeQuery = true)
    List<SipfSolicitudCreditoFiscal> findByNitAssignAndByStatus(@Param("nit") String nit, @Param("estado") Integer estado);
    
    @Query(value = "select \n"
            + "sscf.id_solicitud as IdSolicitud,\n"
            + "sscf.numero_formulario as NumeroFormulario,\n"
            + "(to_char(sscf.periodo_inicio, 'dd-MM-yyyy')  || '  a  ' || to_char(sscf.periodo_fin, 'dd-MM-yyyy') ) as Principio,\n"
            + "sscf.actividad_economica as ActividadEconomica,\n"
            + "sscf.periodo_inicio as Pinicio,\n"
            + "sscf.periodo_fin as Pfin,\n"
            + "sscf.estado as idestado,"
            + "scd.nombre as Estado,\n"
            + "sscf.numero_solicitud as Numero,\n"
            + "sscf.nit_contribuyente as Nit\n"
            + "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sscf.estado \n"
            + "where sscf.nit_contribuyente = :nit and sscf.estado in (:estado)", nativeQuery = true)
    public List<SolicitudesCreditoFiscalProjection> getSolicitudesCreditoFiscal(@Param("nit") String nit, @Param("estado") List<Integer> estados);

    public List<SipfSolicitudCreditoFiscal> findByEstado(Integer idEstado);
    
     @Query(value = "select sscf.id_solicitud as idSolicitud,\n"
            + "	sscf.nit_contribuyente as nitContribuyente,\n"
            + "	coalesce (\n"
            + "	        (select rcj.razon_social as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_juridico rcj\n"
            + "	        where rcj.nit = sscf.nit_contribuyente),\n"
            + "	        (select concat_ws(' ',\n"
            + "	        rci.primer_nombre,\n"
            + "	        rci.segundo_nombre,\n"
            + "	        rci.tercer_nombre,\n"
            + "	        rci.primer_apellido,\n"
            + "	        rci.segundo_apellido,\n"
            + "	        'DE ' || rci.apellido_casada) as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "	        where rci.nit = sscf.nit_contribuyente)\n"
            + "	    ) as nombreContribuyente,\n"
            + "	    sscf.estado as idEstado,\n"
            + "	    scd.nombre as estado,\n"
            + "	    sscf.multa as multa,\n"
            + "	    sscf.total as total,\n"
            + "	    sscf.numero_formulario as numeroFormulario\n"
            + "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd \n"
            + "on scd.codigo = sscf.estado \n"
            + "where sscf.estado in(1056,1077) and sscf.asignado = :profesional",
            nativeQuery = true)
    public List<BandejaCreditoFiscalProjection> getByAsignadoAndEstado(@Param("profesional") String profesional);
    
    @Query(value = "select \n"
            + "sscf.id_solicitud as IdSolicitud,\n"
            + "sscf.numero_formulario as NumeroFormulario,\n"
            + "(to_char(sscf.periodo_inicio, 'dd-MM-yyyy')  || '  a  ' || to_char(sscf.periodo_fin, 'dd-MM-yyyy') ) as Principio,\n"
            + "sscf.actividad_economica as ActividadEconomica,\n"
            + "sscf.periodo_inicio as Pinicio,\n"
            + "sscf.periodo_fin as Pfin,\n"
            + "scd.nombre as Estado,\n"
            + "sscf.estado as Idestado,\n"
            + "sscf.numero_solicitud as Numero,\n"
            + "sscf.nit_contribuyente as Nit\n"
            + "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sscf.estado \n"
            + "where sscf.estado in(965, 1057, 1076, 1075)", nativeQuery = true)
    public List<SolicitudesCreditoFiscalProjection> getSolicitudesCreditoFiscalAdmitidas();
    
    public Optional<SipfSolicitudCreditoFiscal> findByNumeroSolicitud(BigDecimal numeroSolicitud);
    
    @Query(value = "select sscf.id_solicitud as idSolicitud,\n"
            + "	sscf.nit_representante as nitRepresentante,\n"
            + "	coalesce (\n"
            + "	        (select rcj.razon_social as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_juridico rcj\n"
            + "	        where rcj.nit = sscf.nit_representante),\n"
            + "	        (select concat_ws(' ',\n"
            + "	        rci.primer_nombre,\n"
            + "	        rci.segundo_nombre,\n"
            + "	        rci.tercer_nombre,\n"
            + "	        rci.primer_apellido,\n"
            + "	        rci.segundo_apellido,\n"
            + "	        'DE ' || rci.apellido_casada) as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "	        where rci.nit = sscf.nit_representante)\n"
            + "	    ) as nombreRepresentante,\n"
            + "	    sscf.nit_contador as nitContador,\n"
            + "	coalesce (\n"
            + "	        (select rcj.razon_social as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_juridico rcj\n"
            + "	        where rcj.nit = sscf.nit_contador),\n"
            + "	        (select concat_ws(' ',\n"
            + "	        rci.primer_nombre,\n"
            + "	        rci.segundo_nombre,\n"
            + "	        rci.tercer_nombre,\n"
            + "	        rci.primer_apellido,\n"
            + "	        rci.segundo_apellido,\n"
            + "	        'DE ' || rci.apellido_casada) as contribuyente\n"
            + "	        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "	        where rci.nit = sscf.nit_contador)\n"
            + "	    ) as nombreContador,\n"
            + "	    sscf.estado as idEstado,\n"
            + "	    (select shc.comentarios \n"
            + "	    from sat_ifi_sipf.sipf_historial_comentarios shc\n"
            + "	    where shc.id_registro = CAST(sscf.id_solicitud AS varchar) and shc.id_tipo_comentario = 1080\n"
            + "	    order by shc.id_historial_comentario DESC LIMIT 1) as comentario\n"
            + "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf \n"
            + "where sscf.id_solicitud = :idSolicitud", nativeQuery = true)
    public BandejaCreditoFiscalProjection getExtraDataRequest(@Param("idSolicitud") Integer idSolicitud);
    
    public List<SipfSolicitudCreditoFiscal> findByNitContribuyenteAndPeriodoInicioAndPeriodoFin(String nit, Date periodoIncio, Date periodoFin);
}
