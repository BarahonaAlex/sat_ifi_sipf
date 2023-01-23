/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfIntegranteGrupo;
import gt.gob.sat.sat_ifi_sipf.models.SipfIntegranteGrupoId;
import gt.gob.sat.sat_ifi_sipf.projections.GrupoColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.IntegrantesPerfilProjection;
import gt.gob.sat.sat_ifi_sipf.projections.IntegrantesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.OperatorProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignCasesProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudTrasladoColaboradorProjection;

/**
 * @author crramosl
 */
public interface IntegranteEquipoRepository extends CrudRepository<SipfIntegranteGrupo, SipfIntegranteGrupoId> {

    @Override
    List<SipfIntegranteGrupo> findAll();

    List<SipfIntegranteGrupo> findByIdGrupo(Long idGrupo);

    List<SipfIntegranteGrupo> findByIdGrupoAndIdRol(Long idGrupo, Integer idRol);

    @Query(value
            = "select\n"
            + "     cl.nit, cl.nombres as nombre, cl.login, pt.id as idPuesto,"
            + "     pt.nombre as puesto, cd.codigo as idEstado, cd.nombre as estado, ig.id_perfil as idPerfil"
            + " from sat_ifi_sipf.sipf_integrante_grupo ig"
            + " left join sat_ifi_sipf.sipf_colaborador cl on ig.nit = cl.nit"
            + " left join sat_ifi_sipf.sipf_cat_dato cd on cl.id_estado = cd.codigo"
            + " left join sat_ifi_sipf.sipf_puesto_trabajo pt on cl.id_puesto_trabajo = pt.id"
            + " where ig.id_grupo = :groupId",
            nativeQuery = true
    )
    List<IntegrantesProjection> getByGroupId(@Param("groupId") Long groupId);

    @Query(value = "select\n"
            + "	sig2.nit as NitContribuyente,\n"
            + "	sig.id_grupo as IdGrupo,\n"
            + "	sc.nombres as NombreContribuyente,\n"
            + "	sig2.id_rol as idRol,\n"
            + "	scd.nombre as NombreEstado,\n"
            + "	sig2.id_grupo as Grupo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on\n"
            + "	sig.id_grupo = sig2.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sig.id_estado\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	sc.nit = sig2.nit\n"
            + "where\n"
            + "	sig.nit = :nit\n"
            + "	and sig.id_rol in(:idRol)\n"
            + "	and sig2.id_rol = 3\n"
            + "	and sig2.id_estado = 170", nativeQuery = true)
    List<GrupoColaboradoresProjection> teamMembers(@Param("nit") String nit, @Param("idRol") List<Integer> idRol);

    //esta query se utiliza para obtener los miembros con su perfil activo
    @Query(value = "select \n"
            + "	distinct sc.nit, sc.nombres as nombre, sig.id_perfil as perfil, sig.id_rol as rol, sc.puesto_trabajo as puesto, scd.nombre as estado\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sig.nit = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on scd.codigo = sc.id_estado\n"
            + "where sig.id_grupo = :id", nativeQuery = true)
    List<IntegrantesProjection> getMemberAndProfileByGroupId(@Param("id") Integer groupId);

    @Query(value = "select sig.nit as nit, sig.id_perfil as perfilColaborador \n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "where sig.nit = :nit and sig.id_perfil = :idPerfil and sig.id_estado = 170", nativeQuery = true)
    List<IntegrantesPerfilProjection> getRepeatedMember(@Param("nit") String nit, @Param("idPerfil") Integer pIdProfile);

    Optional<SipfIntegranteGrupo> findByIdGrupoAndNitAndIdEstadoAndIdRol(long id, String pNit, Integer pStatus, Integer pRol);

    //
    @Query(value = "select \n"
            + "	stc.id_solicitud as idSolicitud,\n"
            + "	stc.nit_profesional as nitColaborador,\n"
            + "	sc2.nombres as nombreColaborador,\n"
            + "	stc.id_autorizador_solicitante as nitSolicitante,\n"
            + "	sc3.nombres as nombreAprobador,\n"
            + "	stc.id_grupo_nuevo as idGrupoNuevo,\n"
            + "	sgt.nombre as nombreGrupoNuevo,\n"
            + "	sua.nombre as nombreUnidadAdmin,\n"
            + "	stc.motivo,\n"
            + "	scd.nombre as estado \n"
            + "from sat_ifi_sipf.sipf_colaborador sc  \n"
            + "inner join sat_ifi_sipf.sipf_traslado_colaborador stc on stc.id_autorizador_acepta = sc.nit  \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on stc.nit_profesional = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on stc.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on stc.id_grupo_nuevo = sgt.id \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sgt.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc3 on stc.id_autorizador_solicitante = sc3.nit \n"
            + "where stc.id_estado =193 and sc.login =:login", nativeQuery = true)
    List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaborator(@Param("login") String login);

    //
    @Query(value = "select \n"
            + "	stc.id_solicitud as idSolicitud,\n"
            + "	stc.nit_profesional as nitColaborador,\n"
            + "	sc2.nombres as nombreColaborador,\n"
            + "	stc.id_autorizador_solicitante as nitSolicitante,\n"
            + "	sc3.nombres as nombreAprobador,\n"
            + "	stc.id_grupo_nuevo as idGrupoNuevo,\n"
            + "	sgt.nombre as nombreGrupoNuevo,\n"
            + "	sua.nombre as nombreUnidadAdmin,\n"
            + "	stc.motivo,\n"
            + "	scd.nombre as estado \n"
            + "from sat_ifi_sipf.sipf_traslado_colaborador stc  \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on stc.nit_profesional = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on stc.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on stc.id_grupo_nuevo = sgt.id \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sgt.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc3 on stc.id_autorizador_solicitante = sc3.nit \n"
            + "where stc.id_estado =193", nativeQuery = true)
    List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorAdministrator();

    @Query(value = "select \n"
            + "	stc.id_solicitud as idSolicitud,\n"
            + "	stc.nit_profesional as nitColaborador,\n"
            + "	sc2.nombres as nombreColaborador,\n"
            + "	stc.id_autorizador_solicitante as nitSolicitante,\n"
            + "	sc3.nombres as nombreAprobador,\n"
            + "	stc.id_grupo_nuevo as idGrupoNuevo,\n"
            + "	sgt.nombre as nombreGrupoNuevo,\n"
            + "	sua.nombre as nombreUnidadAdmin,\n"
            + "	stc.motivo,\n"
            + "	scd.nombre as estado,\n"
            + "	scd2.nombre as tipoTraslado\n"
            + "from sat_ifi_sipf.sipf_colaborador sc  \n"
            + "inner join sat_ifi_sipf.sipf_traslado_colaborador stc on stc.id_autorizador_acepta = sc.nit  \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on stc.nit_profesional = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on stc.id_estado = scd.codigo\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on stc.id_grupo_nuevo = sgt.id \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sgt.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc3 on stc.id_autorizador_solicitante = sc3.nit\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on stc.id_tipo_traslado = scd2.codigo \n"
            + "where stc.id_solicitud =:idSolicitud", nativeQuery = true)
    List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorbyIdS(@Param("idSolicitud") Integer idSolicitud);

    @Query(value = "select\n"
            + "	sig.nit as nit, \n"
            + "	sig.id_rol as rol,\n"
            + "	sgt.id_gerencia as unidad\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sig.id_grupo = sgt.id \n"
            + "where sig.id_rol = 168;", nativeQuery = true)
    List<OperatorProjection> getOperatorTeamsAndUnits();

    @Query(value = "select\n"
            + "	sig.nit as nit, \n"
            + "	sig.id_rol as rol\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sig.id_grupo = sgt.id \n"
            + "where sig.id_rol = 168 and sig.nit =:nit", nativeQuery = true)
    List<OperatorProjection> getTeamsAndUnitsByNitOperator(@Param("nit") String nit);

    //Query para traer solicitudes de traslado de colaborador mediante nit de autorizador
    @Query(value = "select \n"
            + "	stc.id_solicitud as idSolicitud,\n"
            + "	stc.nit_profesional as nitColaborador,\n"
            + "	sc2.nombres as nombreColaborador,\n"
            + "	stc.id_autorizador_solicitante as nitSolicitante,\n"
            + "	sc3.nombres as nombreAprobador,\n"
            + "	stc.id_grupo_nuevo as idGrupoNuevo,\n"
            + "	sgt.nombre as nombreGrupoNuevo,\n"
            + "	sua.nombre as nombreUnidadAdmin,\n"
            + "	stc.motivo,\n"
            + "	scd.nombre as estado \n"
            + "from sat_ifi_sipf.sipf_colaborador sc  \n"
            + "inner join sat_ifi_sipf.sipf_traslado_colaborador stc on stc.id_autorizador_acepta = sc.nit  \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on stc.nit_profesional = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on stc.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on stc.id_grupo_nuevo = sgt.id \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sgt.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc3 on stc.id_autorizador_solicitante = sc3.nit \n"
            + "where stc.id_estado =193 and stc.id_autorizador_acepta =:nit", nativeQuery = true)
    List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorByNitAuthorizer(@Param("nit") String nit);

    @Query(value = "select\n"
            + "	ig.nit, sc.nombres as nombre, count(ig.nit)\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo ig\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on ig.nit = sc.nit\n"
            + "where \n"
            + "	id_perfil in (select sp.id_perfil from sat_ifi_sipf.sipf_perfil sp where id_rol = 3)\n"
            + "	and ig.id_estado = 170\n"
            + "	and ig.nit in (:nits)\n"
            + "group by ig.nit, sc.nombres", nativeQuery = true)
    List<IntegrantesProjection> getRepeatedMembers(@Param("nits") List<String> nits);

    @Query(value = "select\n"
            + "	count(*) = 1\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo ig\n"
            + "where ig.nit = :nit and ig.id_perfil = :idPerfil and id_estado = 170", nativeQuery = true)
    boolean canDeleteProfiles(@Param("nit") String nit, @Param("idPerfil") Integer idPerfil);

    List<SipfIntegranteGrupo> findByNitAndIdEstado(String nit, Integer state);

    @Query(value = "select distinct \n"
            + "	sig2.nit as NitContribuyente,\n"
            //+ "	sig.id_grupo as IdGrupo,\n"
            + "	sc.nombres as NombreContribuyente,\n"
            + "	sig2.id_rol as idRol,\n"
            + "	scd.nombre as NombreEstado \n"
            // + "	,sig2.id_grupo as Grupo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on\n"
            + "	sig.id_grupo = sig2.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = sig.id_estado\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	sc.nit = sig2.nit\n"
            + "where\n"
            + "	sig2.id_rol = 3\n"
            + "	and sig.id_estado = 170", nativeQuery = true)
    List<GrupoColaboradoresProjection> getMembers();

    @Query(value = "select distinct sig.nit as nit,\n"
            + "sc.login as login,\n"
            + "sc.nombres as nombre,\n"
            + "sig.id_grupo as idGrupo\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on sig2.id_grupo = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sc.nit = sig.nit \n"
            + "where sig.id_rol = 3\n"
            + "and sig.id_estado = 170\n"
            + "and sig2.id_estado = 170\n"
            + "and sgt.id_estado = 163", nativeQuery = true)
    List<GrupoColaboradoresProjection> getAllCollaboratorsToReassign();

    @Query(value = "select distinct sig.nit as nit,\n"
            + "sc.login as login,\n"
            + "sc.nombres as nombre,\n"
            + "sig.id_grupo as idGrupo\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on sig2.id_grupo = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sc.nit = sig.nit \n"
            + "where sig2.id_rol in (:pRol)\n"
            + "and sig.id_rol = 3\n"
            + "and sig.id_estado = 170\n"
            + "and sig2.id_estado = 170\n"
            + "and sgt.id_estado = 163\n"
            + "and sig2.nit = :pNit", nativeQuery = true)
    List<GrupoColaboradoresProjection> getCollaboratorsToReassign(@Param("pRol") List<Integer> pRoles, @Param("pNit") String pNit);

    @Query(value = "select distinct sc.id_caso as idCaso,\n"
            + "sc.nit_contribuyente as nitContribuyente,\n"
            + "sac.nit_colaborador as nitProfesional,\n"
            + "sc2.nombres as nombreProfesional,\n"
            + "scd.nombre as nombreEstado,\n"
            + "scd.codigo as idEstado,\n"
            + "	 coalesce(\n"
            + "    (select rci.primer_nombre \n"
            + "    		|| ' ' \n"
            + "    		|| rci.segundo_nombre \n"
            + "    		|| coalesce(' ' || rci.tercer_nombre , '') \n"
            + "    		|| ' ' \n"
            + "    		|| rci.primer_apellido \n"
            + "    		|| ' ' \n"
            + "    		|| rci.segundo_apellido \n"
            + "    		|| coalesce(' DE ' || rci.apellido_casada , '')           \n"
            + "        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "        where rci.nit = sc.nit_contribuyente)\n"
            + "       ,\n"
            + "     ( select rcj.razon_social\n"
            + "     from sat_rtu.sat_rtu.rtu_contrib_juridico rcj \n"
            + "     where rcj.nit = sc.nit_contribuyente)\n"
            + "     ) as nombreContribuyente \n"
            + "from sat_ifi_sipf.sipf_casos sc \n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_asignacion_casos sac  \n"
            + "        on\n"
            + "    sac.id_caso = sc.id_caso\n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "            on\n"
            + "    sig.nit = sac.nit_colaborador\n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_grupo_trabajo sgt\n"
            + "              on\n"
            + "    sgt.id = sig.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on sc2.nit = sac.nit_colaborador \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on scd.codigo = sc.id_estado \n"
            + "where  \n"
            + "sac.nit_colaborador = cast(:pNitProfesional as varchar) \n"
            + "or sac.id_caso = :pIdCaso \n"
            + "or sc.nit_contribuyente = cast(:pNitContribuyente as varchar)\n"
            + "and sac.nit_colaborador = (select distinct sig.nit \n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on sig2.id_grupo = sig.id_grupo \n"
            + "where sig2.id_rol in (:pRol)\n"
            + "and sig.id_rol = 3\n"
            + "and sig.id_estado = 170\n"
            + "and sig2.id_estado = 170\n"
            + "and sgt.id_estado = 163\n"
            + "and sig2.nit = :pNitSuperior\n"
            + "and sig.nit = sac.nit_colaborador)\n"
            + "and sgt.id_estado = 163\n"
            + "and sc.id_estado <> 1040 and sac.id_estado = 138", nativeQuery = true)
    List<ReasignCasesProjection> getCasesForUnassign(@Param("pNitProfesional") String pNitProfesional, @Param("pIdCaso") Integer pIdCaso, @Param("pNitContribuyente") String pNitContribuyente, @Param("pRol") List<Integer> pRoles, @Param("pNitSuperior") String pNitSuperior);

    @Query(value = "select distinct sc.id_caso as idCaso,\n"
            + "sc.nit_contribuyente as nitContribuyente,\n"
            + "sac.nit_colaborador as nitProfesional,\n"
            + "sc2.nombres as nombreProfesional,\n"
            + "scd.nombre as nombreEstado,\n"
            + "scd.codigo as idEstado,\n"
            + "	 coalesce(\n"
            + "    (select rci.primer_nombre \n"
            + "    		|| ' ' \n"
            + "    		|| rci.segundo_nombre \n"
            + "    		|| coalesce(' ' || rci.tercer_nombre , '') \n"
            + "    		|| ' ' \n"
            + "    		|| rci.primer_apellido \n"
            + "    		|| ' ' \n"
            + "    		|| rci.segundo_apellido \n"
            + "    		|| coalesce(' DE ' || rci.apellido_casada , '')           \n"
            + "        from sat_rtu.sat_rtu.rtu_contrib_individual rci\n"
            + "        where rci.nit = sc.nit_contribuyente)\n"
            + "       ,\n"
            + "     ( select rcj.razon_social\n"
            + "     from sat_rtu.sat_rtu.rtu_contrib_juridico rcj \n"
            + "     where rcj.nit = sc.nit_contribuyente)\n"
            + "     ) as nombreContribuyente \n"
            + "from sat_ifi_sipf.sipf_casos sc \n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_asignacion_casos sac  \n"
            + "        on\n"
            + "    sac.id_caso = sc.id_caso\n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "            on\n"
            + "    sig.nit = sac.nit_colaborador\n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_grupo_trabajo sgt\n"
            + "              on\n"
            + "    sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on sc2.nit = sac.nit_colaborador \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on scd.codigo = sc.id_estado \n"
            + "where sac.nit_colaborador = cast(:pNitProfesional as varchar)\n"
            + "or sac.id_caso = :pIdCaso \n"
            + "or sc.nit_contribuyente = cast(:pNitContribuyente as varchar) \n"
            + "and sgt.id_estado = 163\n"
            + "and sc.id_estado <> 1040 and sac.id_estado = 138", nativeQuery = true)
    List<ReasignCasesProjection> getAllCasesForUnassign(@Param("pNitProfesional") String pNitProfesional, @Param("pIdCaso") Integer pIdCaso, @Param("pNitContribuyente") String pNitContribuyente);

    @Query(value = "select \n"
            + "	stc.id_solicitud as idSolicitud\n"
            + "from sat_ifi_sipf.sipf_traslado_colaborador stc   \n"
            + "where stc.id_estado =193 and stc.nit_profesional = :nit\n"
            + "limit 1", nativeQuery = true)
    Integer transferRequestExist(@Param("nit") String nit);

}
