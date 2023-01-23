/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaAprobadaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaGuardadaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetalleDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GerenciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosMasivosProjections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author rabaraho
 */
public interface DenunciaGrabadaRepository extends CrudRepository<SipfDenunciaGrabada, String> {

    @Query("select t from SipfDenunciaGrabada t")
    List<DenunciaGuardadaProjection> findWhole();

    List<SipfDenunciaGrabada> findByFechaGrabacionBetween(Date pFechaInicio, Date pFechaFin);

    //@Query("select t from SipfDenunciaGrabada t where t.fechaGrabacion between :pFechaInicio and :pFechaFin and t.idInsumo is null ")
    //List<DenunciaGuardadaProjection> findByDateAndNotAssigned(Date pFechaInicio, Date pFechaFin);
    //  @Query(value = "	select \n"
    @Query(value = "select t from SipfDenunciaGrabada t"
            + " left join SipfDenunciaColaborador b on b.id.correlativoDenuncia = t.correlativo "
            + " where t.fechaGrabacion between :pFechaInicio and :pFechaFin "
            + " and b.id.nitResponsable is null", nativeQuery = true)

    List<DenunciaGuardadaProjection> findByDateAndNotAssigned(@Param("pFechaInicio") Date pFechaInicio, @Param("pFechaFin") Date pFechaFin);

    List<DenunciaGuardadaProjection> findByEstadoAndFechaGrabacionBetween(Integer pStrado, Date pFechaInicio, Date pFechaFin);

    //METODO PARA LAS DENUNCIAS QUE SI APLICAN
    @Query(value = "select\n"
            + "	sc.nit as Nit,\n"
            + "	sdc.correlativo_denuncia as Correlativo,\n"
            + "	sdg.id_motivo as motivo,\n"
            + "	scd.nombre as nombre,\n"
            + "	sdg.producto_servicio as producto,\n"
            + "	sdg.valor_compra_servicio as compra\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sdg.id_motivo\n"
            + "where\n"
            + "	sc.nit = :nit\n"
            + "	and sdg.estado = 957", nativeQuery = true)
    List<DenunciaAprobadaProjection> getApplyComplaints(@Param("nit") String nit);

    //METODO QUE OTIENE LOS ALCANCES CREADOS POR UN OPERADOR.
    @Query(value = "select DISTINCT\n"
            + "	sa.id_alcance as idAlcance ,\n"
            + "	sa.id_tipo_alcance as Tipo,\n"
            + "	sa.id_estado as Estado,\n"
            + "	scd.nombre as NombreEstado,\n"
            + "	scd2.nombre as NombreTipo,\n"
            + "	sdg.correlativo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_alcance sa\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.id_alcance = sa.id_alcance\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sa.id_estado\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "	scd2.codigo = sa.id_tipo_alcance\n"
            + "where\n"
            + "	sdc.nit_responsable = :nit and not sa.id_tipo_alcance = 976 Order by sa.id_alcance desc", nativeQuery = true)
    List<AlcanceDenunciaProjection> getScope(@Param("nit") String nit);

    //METODO PARA OBTENER LAS DENUNCIAS QUE APLICAN Y SON DE GABINETE.
    @Query(value = "select\n"
            + "	sdg.nit_denunciado as Nit,\n"
            + "	sdc.correlativo_denuncia as Correlativo,\n"
            + "	sdg.id_motivo as motivo,\n"
            + "	scd.nombre as nombre,\n"
            + "	sdg.producto_servicio as producto,\n"
            + "	sdg.valor_compra_servicio as compra\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on \n"
            + "	scd.codigo = sdg.id_motivo\n"
            + "where\n"
            + "	sdg.id_proceso = 962\n"
            + "	and sdg.estado = 957", nativeQuery = true)
    List<BandejaDenunciasProjection> getComplaintsGabineteProcess();

    ///TRAE LAS DENUNCIAS EN BASE A SU REGION Y PROCESO PARA GENERAR EL CORESPONDIENTE ALCANCE.
    @Query(value = "	select\n"
            + "	sdg.correlativo as Correlativo,\n"
            + "	sdg.nit_denunciante as NitDenunciante,\n"
            + "	sdg.nit_denunciado as NitDenunciado,\n"
            + "	scd.nombre as EstadoNombre,\n"
            + "	sdg.estado as Estado\n"
            + "	from sat_ifi_sipf.sipf_denuncia_grabada sdg\n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sdg.estado \n"
            + "	where sdg.region = :idR and sdg.id_proceso = :idP and sdg.estado = 957", nativeQuery = true)
    List<DenunciaAprobadaProjection> getApplyComplaintsByGerencyProcess(@Param("idR") Integer idR, @Param("idP") Integer idP);

    ///TRAE LAS DENUNCIAS DE GABINETE EN BASE A SU REGION
    @Query(value = "	select\n"
            + "	sdg.correlativo as Correlativo,\n"
            + "	sdg.nit_denunciante as NitDenunciante,\n"
            + "	sdg.nit_denunciado as NitDenunciado,\n"
            + "	scd.nombre as EstadoNombre,\n"
            + "	sdg.estado as Estado\n"
            + "	from sat_ifi_sipf.sipf_denuncia_grabada sdg\n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sdg.estado \n"
            + "	where sdg.id_proceso = 962 and sdg.estado = 957 and sdg.region = :idR", nativeQuery = true)
    List<DenunciaAprobadaProjection> getApplyComplaintsCabinet(@Param("idR") Integer idR);

    ///TRAE LOS PROCESOS MASIVOS PARA UTILIZAR EN EL ANALISIS DE UNA DENUNCIA.
    @Query(value = "	select \n"
            + "	scda.codigo ,\n"
            + "	scda.codigo_ingresado as CodigoIngresado ,\n"
            + "	scda.nombre ,\n"
            + "	scda.descripcion \n"
            + "	from sat_ifi_sipf.sipf_cat_dato scda \n"
            + "	where scda.codigo_catalogo = :idCatalogo", nativeQuery = true)
    List<ProcesosDenunciaProjection> getCatalogProcessComplaints(@Param("idCatalogo") Integer idCatalogo);

    //TRAE LAS DENUNCIAS QUE NO APLICAN
    @Query(value = "	select\n"
            + "	sc.nit as Nit,\n"
            + "	sdc.correlativo_denuncia as Correlativo,\n"
            + "	sdg.id_motivo as motivo,\n"
            + "	scd.nombre as nombre,\n"
            + "	sdg.producto_servicio as producto,\n"
            + "	sdg.valor_compra_servicio as compra\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on \n"
            + "	scd.codigo = sdg.id_motivo\n"
            + "where\n"
            + "	sc.nit = :nit\n"
            + "	and sdg.estado = 958", nativeQuery = true)
    List<DenunciaAprobadaProjection> getRejectedComplaints(@Param("nit") String nit);

    //TRAE LAS DENUNCIAS QUE NO APLICAN EN BASE A UN FILTRO DE FECHAS
    @Query(value = "select\n"
            + "	sc.nit as Nit,\n"
            + "	sdc.correlativo_denuncia as Correlativo,\n"
            + "	sdg.id_motivo as motivo,\n"
            + "	scd.nombre as nombre,\n"
            + "	sdg.producto_servicio as producto,\n"
            + "	sdg.valor_compra_servicio as compra,\n"
            + "	sdg.fecha_rechazo as fecha\n"
            + "	--to_char(sdg.fecha_rechazo, 'YYYY/MM/DD') as FechaRechazo\n"
            + "\n"
            + "	from sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sdg.id_motivo\n"
            + "where\n"
            + "	sc.nit = :nit\n"
            + "	and sdg.estado = 958\n"
            + "	and (cast(sdg.fecha_rechazo as date) >= to_date(:fecha, 'YYYY/MM/DD') and cast(sdg.fecha_rechazo as date) <= to_date(:fechaDos, 'YYYY/MM/DD'))", nativeQuery = true)
    List<DenunciaAprobadaProjection> getRejectedComplaintsForDate(@Param("nit") String nit, @Param("fecha") String fecha, @Param("fechaDos") String fechaDos);

    //TRAE LOS PROCESOS MASIVOS QUE SON NECESARIOS PARA GENERAR EL ALCANCE.
    @Query(value = "	select \n"
            + "	scd.nombre as Nombre,\n"
            + "	scd.codigo as Codigo\n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "	where scd.codigo_catalogo = 83 and not scd.codigo =962", nativeQuery = true)
    List<ProcesosMasivosProjections> getProcessMasive();

    //TRAE LOS ESTADOS QUE PUEDE TENER UNA DENUNCIA.
    @Query(value = "	select \n"
            + "	scd.nombre as Nombre,\n"
            + "	scd.codigo as Codigo\n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "	where scd.codigo_catalogo = 82", nativeQuery = true)
    List<ProcesosMasivosProjections> getStateComplaints();

    //TRAE TODOS LOS PROCESOS MASIVOS QUE PUEDE TENER UNA DEUNCIA.
    @Query(value = "	select \n"
            + "	scd.nombre as Nombre,\n"
            + "	scd.codigo as Codigo\n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "	where scd.codigo_catalogo = 83", nativeQuery = true)
    List<ProcesosMasivosProjections> getAllProcessMassive();

    public Optional<SipfDenunciaGrabada> findByCorrelativo(String id);

    public List<SipfDenunciaGrabada> findBycorrelativoIn(List<String> pCorrelativos);

    List<SipfDenunciaGrabada> findByIdAlcance(Integer idAlcance);

    @Query(value = "select\n"
            + "	sc.nit as Nit,\n"
            + "	sdc.correlativo_denuncia as correlativo,\n"
            + "	sdg.id_motivo as tipo,\n"
            + "	scd.nombre as nombre,\n"
            + "	sdg.producto_servicio as producto,\n"
            + "	sdg.valor_compra_servicio as compra\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on\n"
            + "	sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on\n"
            + "	sdg.correlativo = sdc.correlativo_denuncia\n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on \n"
            + "	scd.codigo = sdg.id_motivo  \n"
            + "where\n"
            + "	sc.nit = :nit\n"
            + "	and sdg.estado = 956", nativeQuery = true)
    List<BandejaDenunciasProjection> getComplaints(@Param("nit") String nit);

    /**
     * @author lfvillag (Luis Villagrán)
     * @param id
     * @return
     */
    @Query(value
            = "select\n"
            + "	scd2.nombre as motivo,\n"
            + "	sdg.id_motivo as idMotivo,\n"
            + "	scd.nombre as estado,\n"
            + "	sdg.estado as idEstado,\n"
            + "	sdg.fecha_compra as compra,\n"
            + "	sdg.valor_compra_servicio as valor,\n"
            + "	scd6.nombre as formaPago,\n"
            + "	sdg.region as region,\n"
            + "	scd3.nombre as nRegion,\n"
            + "	scd4.nombre as departamento,\n"
            + "	sdg.establecimiento_denunciado as establecimiento,\n"
            + "	sdg.direccion_fiscal_denunciado as direccion,\n"
            + "	scd5.nombre as municipio,\n"
            + "	sdg.nit_denunciado as nit,\n"
            + "	sdg.nombre_denunciado as nombre,\n"
            + "	sdg.telefono_denunciado as telefono,\n"
            + "	sdg.direccion_est_denunciado as direDenunciado,\n"
            + "	sdg.observaciones as observaciones,\n"
            + "	sdg.id_proceso as tipo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_denuncia_grabada sdg\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sdg.estado\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "	scd2.codigo = sdg.id_motivo\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd3 on\n"
            + "	scd3.codigo = sdg.region\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd4 on\n"
            + "	scd4.codigo = sdg.departamento\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd5 on\n"
            + "	scd5.codigo = sdg.municipio\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd6 on\n"
            + "	 scd6.codigo = (cast(sdg.forma_pago as int))\n"
            + "where\n"
            + "	sdg.correlativo = :id", nativeQuery = true)
    List<DetalleDenunciasProjection> getDetailComplaint(@Param("id") String id);

    /**
     * @author lfvillag (Luis Villagrán)
     * @return
     */
    @Query(value = "	select \n"
            + "	scd.nombre as Nombre ,\n"
            + "	scd.codigo as Codigo\n"
            + "	from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "	where scd.codigo_catalogo = 9", nativeQuery = true)
    List<GerenciasProjection> getManagements();

}
