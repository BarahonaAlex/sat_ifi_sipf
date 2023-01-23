/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfProgramaFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.ApproveAllCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraAprobacionProgramaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesComentarioProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesProjection;
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
public interface ProgramaFiscalesRepository extends CrudRepository<SipfProgramaFiscales, Integer> {

    /**
     *
     * @param pIdProgram
     * @return
     */
    Optional<ProgramaFiscalesProjection> findByIdPrograma(Integer pIdProgram);

    /**
     *
     * @param anio
     * @return el valor del maximo correlativo por año de los programas fiscales
     */
    @Query(value = "select max(p.correlativo) as correlativo from SipfProgramaFiscales p where p.anio=:anio")
    Integer findMaxCorrelativeByYear(@Param("anio") Integer anio);

    /**
     * @param pIdEstado id del estado por el que se desea filtrar
     * @description retorna listado de programas filtrados por estado
     * @return List
     */
    List<ProgramaFiscalesProjection> findByIdEstadoOrderByIdProgramaDesc(int pIdEstado);

    List<ProgramaFiscalesProjection> findByIdEstadoAndIdGerenciaOrderByIdPrograma(int pIdEstado, int pIdGerencia);

    @Query(value = "select t1.id_gerencia as idGerencia,t2.nombre as nombreGerencia, count(t1.id_gerencia) as casos\n"
            + "from sat_ifi_sipf.sipf_casos as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t2\n"
            + "on t1.id_gerencia =t2.codigo\n"
            + "group by t1.id_gerencia,t1.id_estado, t2.nombre\n"
            + "having t1.id_estado =133", nativeQuery = true)
    List<CarteraAprobacionProgramaProjection> walletApproval();

    @Query(value = "select\n"
            + "	t1.id_programa as idPrograma,\n"
            + "	t1.nombre as nombre,\n"
            + "	t3.id_gerencia as idGerencia,\n"
            + "	count (t3.id_programa)as Cantidad,\n"
            + "	t1.anio as periodo,\n"
            + "	t1.impuesto_nombres as nombreImpuesto\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_programa_fiscales as t1\n"
            + "inner join sat_ifi_sipf.sipf_casos as t3\n"
            + " on\n"
            + "	t3.id_programa = t1.id_programa\n"
            + "where\n"
            + "	t3.id_gerencia =:idGerencia\n"
            + "	and t3.id_estado = 133\n"
            + "group by\n"
            + "	t1.id_programa,\n"
            + "	t1.nombre,\n"
            + "	t3.id_gerencia,\n"
            + "	t1.anio,\n"
            + "	t1.impuesto_nombres", nativeQuery = true)
    List<CarteraAprobacionProgramaProjection> walletApprovalDetail(@Param("idGerencia") Integer idGerencia);

    @Query(value = "select\n"
            + "	t1.id_caso as idCaso,\n"
            + "	t1.id_estado as idEstado,\n"
            + "	t2.descripcion as siglas,\n"
            + " t1.id_gerencia as idGerencia,\n"
            + "	t3.anio as periodo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_casos as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t2\n"
            + "on t2.codigo =t1.id_gerencia \n"
            + "left join sat_ifi_sipf.sipf_programa_fiscales as t3\n"
            + "on t3.id_programa =t1.id_programa \n"
            + "where\n"
            + "	t1.id_estado = 133 and t1.id_caso=:idCaso", nativeQuery = true)
    ApproveAllCasesProjection getListCases(@Param("idCaso") Integer idCaso);

    @Query(value = "select\n"
            + "	t1.id_formulario as idCaso,\n"
            + "	t1.id_estado as idEstado,\n"
            + "	t2.descripcion as siglas,\n"
            + " t1.id_gerencia as idGerencia,\n"
            + "	t3.anio as periodo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_presencias_fiscales as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t2 on\n"
            + "	t2.codigo = t1.id_gerencia \n"
            + "left join sat_ifi_sipf.sipf_programa_fiscales as t3 on\n"
            + "	t3.id_programa = t1.id_programa\n"
            + "where\n"
            + "	t1.id_estado = 133\n"
            + "	and t1.id_formulario =:idCaso", nativeQuery = true)
    ApproveAllCasesProjection getListCasesPresencias(@Param("idCaso") Integer idCaso);

    @Query(value = "select\n"
            + "	t1.correlativo as idCaso,\n"
            + "	t1.estado as idEstado,\n"
            + "	t2.descripcion as siglas,\n"
            + "	t1.region as idGerencia\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_denuncia_grabada as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t2 on\n"
            + "	t2.codigo = t1.region\n"
            + "where\n"
            + "	t1.estado = 133\n"
            + "	and t1.correlativo =:idCaso", nativeQuery = true)
    ApproveAllCasesProjection getListCasesDenuncia(@Param("idCaso") String idCaso);

    @Query(value = "select\n"
            + "	t1.id_caso as idCaso,\n"
            + "	t1.id_estado as idEstado,\n"
            + "	t2.descripcion as siglas,\n"
            + " t1.id_gerencia as idGerencia,\n"
            + "	t3.anio as periodo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_casos as t1\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato as t2\n"
            + "on t2.codigo =t1.id_gerencia \n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales as t3\n"
            + "on t3.id_programa =t1.id_programa \n"
            + "where\n"
            + "	t1.id_estado = 133", nativeQuery = true)
    List<ApproveAllCasesProjection> getListCases();

    List<ProgramaFiscalesProjection> findByIdEstadoAndIdTipoProgramaOrderByIdPrograma(int pIdEstado, int pIdTipoPrograma);

    List<ProgramaFiscalesProjection> findByIdEstadoAndIdGerenciaAndIdTipoProgramaOrderByIdPrograma(int pIdEstado, int pIdGerencia, int pIdTipoPrograma);

    @Query(value = "select t.idPrograma as idPrograma, "
            + "    t.idTipoPrograma as idTipoPrograma, "
            + "    t.idDireccionamientoAuditoria as idDireccionamientoAuditoria, "
            + "    t.idTipoAuditoria as idTipoAuditoria, "
            + "    t.idOrigenInsumo as idOrigenInsumo, "
            + "    t.idGerencia as idGerencia, "
            + "    t.impuesto as impuesto, "
            + "    t.anio as anio, "
            + "    t.correlativo as correlativo, "
            + "    t.periodoInicio as periodoInicio,"
            + "    t.periodoFin as periodoFin, "
            + "    t.nombre as nombre, "
            + "    t.descripcion as descripcion, "
            + "    t.idDepartamento as idDepartamento, "
            + "    t.idEstado as idEstado, "
            + "    t.codificacionPrograma as codificacionPrograma, "
            + "    t.impuestoNombres as  impuestoNombres, "
            + "     ( "
            + "         select "
            + "          h.comentarios "
            + "         from SipfHistorialComentarios h "
            + "         where h.idHistorialComentario = ( "
            + "             select max(hh.idHistorialComentario) from SipfHistorialComentarios hh "
            + "                     where hh.idRegistro = text(t.idPrograma) "
            + "                     and hh.idTipoComentario = 156 "
            + "                                          ) "
            + "     ) as comentarios,  "
            + "     t.fechaModifica as fechaModifica,\n"
            + "     t.usuarioModifica as usuarioModifica,\n"
            + "     t.idEstadoAnterior as idEstadoAnterior\n"
            //+ "     c.login as usuarioModifica\n"
            + "from SipfProgramaFiscales t "
            + "where t.idEstado=:pIdEstado "
            + "And t.idGerencia = :pIdGerencia "
            + "And t.fechaCreacion Between :pDel and :pAl "
            + "Order By t.idPrograma ")
    List<ProgramaFiscalesComentarioProjection> getByIdEstadoAndIdGerenciaAndFechaCreacionBetweenOrderByIdPrograma(
            @Param("pIdEstado") int pIdEstado,
            @Param("pIdGerencia") int pIdGerencia,
            @Param("pDel") Date pDel,
            @Param("pAl") Date pAl);

    @Query(value = "select t.id_programa as idPrograma, \n"
            + "    t.id_tipo_programa as idTipoPrograma, \n"
            + "    t.id_direccionamiento_auditoria as idDireccionamientoAuditoria, \n"
            + "    t.id_tipo_auditoria as idTipoAuditoria, \n"
            + "    t.id_origen_insumo as idOrigenInsumo, \n"
            + "    t.id_gerencia as idGerencia, \n"
            + "    t.impuesto as impuesto, \n"
            + "    t.anio as anio, \n"
            + "    t.correlativo as correlativo, \n"
            + "    t.periodo_inicio as periodoInicio,\n"
            + "    t.periodo_fin as periodoFin, \n"
            + "    t.nombre as nombre, \n"
            + "    t.descripcion as descripcion, \n"
            + "    t.id_departamento as idDepartamento, \n"
            + "    t.id_estado as idEstado, \n"
            + "    t.codificacion_programa as codificacionPrograma, \n"
            + "    t.impuesto_nombres as  impuestoNombres,\n"
            + "         ( \n"
            + "         select \n"
            + "          h.comentarios \n"
            + "         from sat_ifi_sipf.sipf_historial_comentarios h \n"
            + "         where h.id_historial_comentario = ( \n"
            + "             select max(hh.id_historial_comentario) from sat_ifi_sipf.sipf_historial_comentarios hh \n"
            + "                     where hh.id_registro = text(t.id_programa) \n"
            + "                     and hh.id_tipo_comentario = 156 \n"
            + "                                          ) \n"
            + "     ) as comentarios,  \n"
            + " t.fecha_modifica as fechaModifica,\n"
            + " c.login as usuarioModifica,\n"
            + "     t.id_estado_anterior as idEstadoAnterior\n"
            + " from sat_ifi_sipf.sipf_programa_fiscales t \n"
            + " inner join sat_ifi_sipf.sipf_colaborador c \n"
            + " on c.nit =t.usuario_modifica \n"
            + "where t.id_estado=109\n"
            + "And t.fecha_creacion Between :pDel and :pAl \n"
            + "Order By t.id_programa ", nativeQuery = true)
    List<ProgramaFiscalesComentarioProjection> getProgramLocked(@Param("pDel") Date pDel, @Param("pAl") Date pAl);

    @Query(value = "select t.id_programa as idPrograma, "
            + "    t.id_tipo_programa as idTipoPrograma, "
            + "    t.id_direccionamiento_auditoria as idDireccionamientoAuditoria, "
            + "    t.id_tipo_auditoria as idTipoAuditoria, "
            + "    t.id_origen_insumo as idOrigenInsumo, "
            + "    t.id_gerencia as idGerencia, "
            + "    t.impuesto as impuesto, "
            + "    t.anio as anio, "
            + "    t.correlativo as correlativo, "
            + "    t.periodo_inicio as periodoInicio, "
            + "    t.periodo_fin as periodoFin, "
            + "    t.nombre as nombre, "
            + "    t.descripcion as descripcion, "
            + "    t.id_departamento as idDepartamento, "
            + "    t.id_estado as idEstado, "
            + "    t.codificacion_programa as codificacionPrograma, "
            + "    t.impuesto_nombres as  impuestoNombres "
            + "from sat_ifi_sipf.sipf_programa_fiscales t "
            + "where t.usuario_modifica = :pUsuario "
            + " Order By t.id_programa", nativeQuery = true)

    List<ProgramaFiscalesProjection> getByUser(@Param("pUsuario") String Usuario);

    @Query(value = "select t.id_Programa as idPrograma, \n"
            + "    t.id_Tipo_Programa as idTipoPrograma, \n"
            + "    t.id_Direccionamiento_Auditoria as idDireccionamientoAuditoria, \n"
            + "    t.id_Tipo_Auditoria as idTipoAuditoria, \n"
            + "    t.id_Origen_Insumo as idOrigenInsumo, \n"
            + "    t.id_Gerencia as idGerencia, \n"
            + "    t.impuesto as impuesto, \n"
            + "    t.anio as anio, \n"
            + "    t.correlativo as correlativo, \n"
            + "    t.periodo_Inicio as periodoInicio,\n"
            + "    t.periodo_Fin as periodoFin, \n"
            + "    t.nombre as nombre, \n"
            + "    t.descripcion as descripcion, \n"
            + "    t.id_Departamento as idDepartamento, \n"
            + "    t.id_Estado as idEstado, \n"
            + "    t.codificacion_Programa as codificacionPrograma, \n"
            + "    t.impuesto_Nombres as  impuestoNombres, \n"
            + "     ( \n"
            + "         select \n"
            + "          h.comentarios \n"
            + "         from sat_ifi_sipf.Sipf_Historial_Comentarios h \n"
            + "         where h.id_Historial_Comentario = ( \n"
            + "             select max(hh.id_Historial_Comentario) from sat_ifi_sipf.Sipf_Historial_Comentarios hh \n"
            + "                     where hh.id_Registro = text(t.id_Programa) \n"
            + "                     and hh.id_Tipo_Comentario = 156 \n"
            + "                                          ) \n"
            + "     ) as comentarios  ,\n"
            + "      ( \n"
            + "         select \n"
            + "          sc.nombres \n"
            + "         from sat_ifi_sipf.Sipf_Historial_Comentarios h \n"
            + "         inner join sat_ifi_sipf.sipf_colaborador sc on sc.nit = h.usuario_modifica \n"
            + "         where h.id_Historial_Comentario = ( \n"
            + "             select max(hh.id_Historial_Comentario) from sat_ifi_sipf.Sipf_Historial_Comentarios hh \n"
            + "                     where hh.id_Registro = text(t.id_Programa) \n"
            + "                     and hh.id_Tipo_Comentario = 156 \n"
            + "                                          ) \n"
            + "     ) as usuarioSolicitaCorreccion  , \n"
            + "     scd.nombre as nombreEstado,\n"
            + "     t.id_estado_anterior as idEstadoAnterior\n"
            + "from sat_ifi_sipf.Sipf_Programa_Fiscales t \n"
            + "INNER JOIN sat_ifi_sipf.sipf_cat_dato scd ON scd.codigo  =  t.id_estado \n"
            + "where t.id_Estado=:pIdEstado \n"
            + "And t.usuario_Agrega = :pUsuario \n"
            + "Order By t.id_Programa desc", nativeQuery = true)
    List<ProgramaFiscalesComentarioProjection> getByUserAndStatus(
            @Param("pIdEstado") int pIdEstado,
            @Param("pUsuario") String Usuario);

    @Query(value = "select t.idPrograma as idPrograma, "
            + "    t.idTipoPrograma as idTipoPrograma, "
            + "    t.idDireccionamientoAuditoria as idDireccionamientoAuditoria, "
            + "    t.idTipoAuditoria as idTipoAuditoria, "
            + "    t.idOrigenInsumo as idOrigenInsumo, "
            + "    t.idGerencia as idGerencia, "
            + "    t.impuesto as impuesto, "
            + "    t.anio as anio, "
            + "    t.correlativo as correlativo, "
            + "    t.periodoInicio as periodoInicio,"
            + "    t.periodoFin as periodoFin, "
            + "    t.nombre as nombre, "
            + "    t.descripcion as descripcion, "
            + "    t.idDepartamento as idDepartamento, "
            + "    t.idEstado as idEstado, "
            + "    t.codificacionPrograma as codificacionPrograma, "
            + "    t.impuestoNombres as  impuestoNombres, "
            + "     ( "
            + "         select "
            + "          h.comentarios "
            + "         from SipfHistorialComentarios h "
            + "         where h.idHistorialComentario = ( "
            + "             select max(hh.idHistorialComentario) from SipfHistorialComentarios hh "
            + "                     where hh.idRegistro = text(t.idPrograma) "
            + "                     and hh.idTipoComentario = 156 "
            + "                                          ) "
            + "     ) as comentarios,  "
            + "	("
            + " select "
            + "	b.usuarioModifica "
            + "from SipfBitacoraProgramasFiscales b "
            + "where b.id = ("
            + "	select max(bb.id) "
            + "	from SipfBitacoraProgramasFiscales bb "
            + "	where bb.idPrograma = t.idPrograma  "
            + "			and bb.idTipoOperacion = 412 )"
            + ") as usuarioAgrega,\n"
            + "     t.fechaModifica as fechaModifica,\n"
            + "     t.usuarioModifica as usuarioModifica,\n"
            + "     t.idEstadoAnterior as idEstadoAnterior\n"
            + "from SipfProgramaFiscales t \n"
            //   + "inner join sipfColaborador c \n"
            //  + " on c.nit =t.usuarioModifica \n"
            + "where t.idEstado=:pIdEstado "
            + "Order By t.idPrograma desc ")
    List<ProgramaFiscalesComentarioProjection> getByStatus(
            @Param("pIdEstado") int pIdEstado);

    @Query(value = "select spf.id_programa as idPrograma, \n"
            + "    spf.id_tipo_programa as idTipoPrograma, \n"
            + "    spf.id_direccionamiento_auditoria as idDireccionamientoAuditoria, \n"
            + "    spf.id_tipo_auditoria as idTipoAuditoria, \n"
            + "    spf.id_origen_insumo as idOrigenInsumo, \n"
            + "    spf.id_gerencia as idGerencia, \n"
            + "    spf.impuesto as impuesto, \n"
            + "    spf.anio as anio, \n"
            + "    spf.correlativo as correlativo, \n"
            + "    spf.periodo_inicio as periodoInicio,\n"
            + "    spf.periodo_fin as periodoFin, \n"
            + "    spf.nombre as nombre, \n"
            + "    spf.descripcion as descripcion, \n"
            + "    spf.id_departamento as idDepartamento, \n"
            + "    spf.id_estado as idEstado, \n"
            + "    spf.codificacion_programa as codificacionPrograma, \n"
            + "    spf.impuesto_nombres as  impuestoNombres \n"
            + "from sat_ifi_sipf.sipf_programa_fiscales spf  \n"
            + "where spf.id_estado= :estado and spf.anio = (select extract(year from current_date))\n"
            + "Order By spf.id_programa", nativeQuery = true)
    List<ProgramaFiscalesProjection> getByStatusAndCurrentYear(@Param("estado") int estado);
}
