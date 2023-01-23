/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresGerenciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignarCasosDetalleProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignarCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.EquiposYUnidadesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacioCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SupervidoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.TrasladoColaboradorProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author ruano
 */
public interface ColaboradorRepository extends CrudRepository<SipfColaborador, String> {

    //Query para obtener todo los colaboradores creados
    @Query(value = "select\n"
            + "	sc.nit,\n"
            + "	sc.nombres,\n"
            + "	sc.login,\n"
            + "	sc.correo,\n"
            + "	sc.id_estado,\n"
            + "	sc.puesto_trabajo,\n"
            + "	id_gerencia,\n"
            + "	scd.nombre as nombreEstado,\n"
            + "	sua.id_tipo_programacion as idTipoProgramacion,\n"
            + "	tp.nombre as nombreTipoProgramacion,\n"
            + "	sua.nombre as nombreUnidadAdministrativa ,\n"
            + "	shec.fecha_inicio as fechaInicio,\n"
            + "	shec.fecha_fin as fechaFin\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "left outer join sat_ifi_sipf.sipf_historial_estados_colaborador shec    \n"
            + "    on\n"
            + "	shec.nit_colaborador = sc.nit\n"
            + "	and shec.id =(\n"
            + "	select\n"
            + "		max(id)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_historial_estados_colaborador shec2\n"
            + "	where\n"
            + "		shec2.nit_colaborador = sc.nit)\n"
            + "inner join\n"
            + "        sat_ifi_sipf.sipf_unidad_administrativa sua  \n"
            + "            on\n"
            + "	sua.id = id_gerencia\n"
            + "left join\n"
            + "        sat_ifi_sipf.sipf_cat_dato tp \n"
            + "            on\n"
            + "	tp.codigo = sua.id_tipo_programacion\n"
            + "left join\n"
            + "        sat_ifi_sipf.sipf_cat_dato scd \n"
            + "            on\n"
            + "	sc.id_estado = scd.codigo\n"
            + "where\n"
            + "	sc.id_estado <> :pEstadoEliminado\n"
            + "	and scd.codigo_catalogo = 3\n"
            + "	and sc.nit in (\n"
            + "	select\n"
            + "		distinct sc3.nit\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_colaborador sc3\n"
            + "	where\n"
            + "		sc3.nit in (\n"
            + "		select\n"
            + "			distinct scp2.nit\n"
            + "		from\n"
            + "			sat_ifi_sipf.sipf_colaborador_perfil scp2\n"
            + "		where\n"
            + "			scp2.id_perfil in (\n"
            + "			select\n"
            + "				distinct sp.id_perfil\n"
            + "			from\n"
            + "				sat_ifi_sipf.sipf_perfil sp\n"
            + "			where\n"
            + "				sp.id_perfil_padre in(\n"
            + "				select\n"
            + "					distinct scp.id_perfil\n"
            + "				from\n"
            + "					sat_ifi_sipf.sipf_colaborador_perfil scp\n"
            + "				where\n"
            + "					scp.nit = :pUsuarioLogueado\n"
            + "					and scp.estado = :pIdEstadoColaboradoPerfil)\n"
            + "	)\n"
            + "			and scp2.estado = :pIdEstadoColaboradoPerfil)\n"
            + "		and sc3.id_gerencia in (\n"
            + "		select\n"
            + "			sc2.id_gerencia\n"
            + "		from\n"
            + "			sat_ifi_sipf.sipf_colaborador sc2\n"
            + "		where\n"
            + "			sc2.nit = :pUsuarioLogueado\n"
            + "			and sc2.id_estado = :pIdEstadoColaborador\n"
            + "	)	\n"
            + "	)\n"
            + "order by\n"
            + "	sc.nit ", nativeQuery = true)
    List<ColaboradoresProjection> getAll(@Param("pIdEstadoColaborador") Integer pIdEstadoColaborador,
            @Param("pIdEstadoColaboradoPerfil") Integer pIdEstadoColaboradoPerfil,
            @Param("pEstadoEliminado") Integer pEstadoEliminado,
            @Param("pUsuarioLogueado") String pUsuarioLogueado);

    @Query(value = "select\n"
            + "login\n"
            + "from sat_ifi_sipf.sipf_colaborador sc\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on sc.id_estado  = scd.codigo \n"
            + "where sc.id_estado <> 407 and scd.codigo_catalogo = 3", nativeQuery = true)
    List<String> getAllCollaboratorsLogin();

    //Query para traer una lista de colaboradores en base al Idgerencia
    @Query(value = "select sc.nombres, sc.nit, scd.nombre as gerencia, scd.codigo as Idgerencia, sc.correo as correo \n"
            + "from sat_ifi_sipf.sipf_colaborador sc \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on scd.codigo = sc.id_gerencia \n"
            + "where sc.id_gerencia =:id_gerencia and sc.id_estado  ='4'", nativeQuery = true)
    List<ColaboradoresGerenciaProjection> managementCollaborator(@Param("id_gerencia") Integer id_gerencia);

    //Query para obtener un colaborador en base a su NIT
    @Query(value = "select nit,\n"
            + "nombres,\n"
            + "login,\n"
            + "correo,\n"
            + "sc.id_estado,\n"
            + "puesto_trabajo,\n"
            + "id_gerencia,\n"
            + "scd.nombre as nombreEstado\n"
            + "from sat_ifi_sipf.sipf_colaborador sc\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on sc.id_estado  = scd.codigo \n"
            + "where sc.id_estado <> 407 and sc.nit = :nit and scd.codigo_catalogo = 3", nativeQuery = true)
    ColaboradoresProjection getByNit(@Param("nit") String nit);

    //Query para la tabla de anexo de reasignacion de casos
    @Query(value = "select sc2.nit_contribuyente as nitContribuyente,\n"
            + "scd.nombre as nombreEstado,\n"
            + "scd2.nombre as nombreOrigen,\n"
            + "sac.id_caso \n"
            + "from sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos sac on sc.nit = sac.nit_colaborador \n"
            + "inner join sat_ifi_sipf.sipf_casos sc2 on sac.id_caso = sc2.id_caso \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on sc2.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on sc2.id_origen = scd2.codigo \n"
            + "where sac.id_estado = 138 and sc.nit =:nit", nativeQuery = true)
    List<ReasignacioCasosProjection> annexOne(@Param("nit") String nit);

    //Query de listado de colaboradores con puesto supervisor y estado activo
    @Query(value = "select nit, nombres from sat_ifi_sipf.sipf_colaborador where puesto_trabajo = 2 and id_estado = 4", nativeQuery = true)
    List<SupervidoresProjection> supervisorList();

    public Optional<SipfColaborador> findByLogin(String login);

    //Query que trae colaboradores en base a su autorizador
    @Query(value = "select 	\n"
            + "		sig.nit as nitColaborador,		\n"
            + "		sc2.nombres as nombreColaborador,\n"
            + "		sig2.nit as nitAprobador,\n"
            + "		sc.nombres as  nombreAprobador,\n"
            + "		sig.id_grupo as grupo,\n"
            + "		sgt.nombre as nombreGrupo,\n"
            + "		sgt.id_gerencia as idUnidadAdmin,\n"
            + "		sua.nombre as unidadAdmin,\n"
            + "		scd.nombre as estado,\n"
            + "		sig.id_perfil as idPerfil,\n"
            + "		sig.id_rol as idRol,\n"
            + "		sgt.id_estado as estadoGrupo,\n"
            + "		sig.id_estado as estadoColaboradorGrupo\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on sig2.id_grupo = sig.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sig2.nit = sc.nit \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on sig.nit = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on sc.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sc.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on scd2.codigo = sgt.id_estado\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd3 on scd3.codigo = sig.id_estado \n"
            + "where sig.nit = :nit \n"
            + "and sig2.id_rol =:rol \n"
            + "and sig.id_rol =:rol1  \n"
            + "and sig.id_estado = 170 \n"
            + "and sgt.id_estado = 163\n"
            + "and sig.nit in(\n"
            + "	select\n"
            + "	sc.nit as nit\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sc.nit = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo\n"
            + "where sig.id_rol = 3 \n"
            + "and sig.id_estado = 170\n"
            + "and sig.id_grupo in(\n"
            + "	select\n"
            + "		id_grupo as grupo\n"
            + "	from sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "	where sig.id_estado = 170 \n"
            + "	and sig.id_rol = 1\n"
            + "	and sig.nit =:cNit)\n"
            + ")", nativeQuery = true)
    public List<TrasladoColaboradorProjection> getColaboratorByTraslate(@Param("nit") String nit, @Param("rol") Integer rol, @Param("rol1") Integer rol1, @Param("cNit") String cNit);

    //Query que trae colaboradores para realizar traslado utilizando rol de administrador
    @Query(value = "select 	\n"
            + "		sig.nit as nitColaborador,		\n"
            + "		sc2.nombres as nombreColaborador,\n"
            + "		sig2.nit as nitAprobador,\n"
            + "		sc.nombres as  nombreAprobador,\n"
            + "		sig.id_grupo as grupo,\n"
            + "		sgt.nombre as nombreGrupo,\n"
            + "		sgt.id_gerencia as idUnidadAdmin,\n"
            + "		sua.nombre as unidadAdmin,\n"
            + "		scd.nombre as estado,\n"
            + "		sig.id_perfil as idPerfil,\n"
            + "		sig.id_rol as idRol,\n"
            + "		sgt.id_estado as estadoGrupo,\n"
            + "		sig.id_estado as estadoColaboradorGrupo\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig2 on sig2.id_grupo = sig.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on sig2.nit = sc.nit \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on sig.nit = sc2.nit \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on sc.id_estado = scd.codigo \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sc.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on scd2.codigo = sgt.id_estado\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd3 on scd3.codigo = sig.id_estado \n"
            + "where sig.nit = :nit \n"
            + "and sig2.id_rol =:rol \n"
            + "and sig.id_rol =:rol1 ", nativeQuery = true)
    public List<TrasladoColaboradorProjection> getColaboratorByTraslateAdministrator(@Param("nit") String nit, @Param("rol") Integer rol, @Param("rol1") Integer rol1);

    /**
     * Se obtiene a todos los usuarios del grupo al que pertenece
     *
     * @author Jose Aldana (jdaldana)
     * @param pNit identificador nit del jefe
     * @param pPerfilJefe identificador perfil del jefe
     * @param pPerfil identificador perfil a buscar
     * @since 23/05/2022
     * @return usuarios todos los usuarios del grupo que pertenece
     */
    @Query(value = "select\n"
            + "	sc.nit ,\n"
            + "	sc.nombres\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig on\n"
            + "	sig.nit = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on\n"
            + "	sgt.id = sig.id_grupo\n"
            + "where\n"
            + " sgt.id in (select\n"
            + "		sgt.id \n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_grupo_trabajo sgt\n"
            + "	inner join sat_ifi_sipf.sipf_integrante_grupo sig on\n"
            + "		sig.id_grupo = sgt.id\n"
            + "	where\n"
            + "		sig.nit = :pNit\n"
            + "		and sig.id_perfil = :pPerfilJefe"
            + "         and sig.id_estado = 170) and sig.id_perfil in(:pPerfil)", nativeQuery = true)
    public List<ColaboradoresProjection> findByNitPerfilJefeandPerfil(@Param("pNit") String pNit, @Param("pPerfilJefe") Integer pPerfilJefe, @Param("pPerfil") Integer pPerfil);

    /**
     * Se obtiene a todos los usuarios segun el cargo del jefe
     *
     * @author Jose Aldana (jdaldana)
     * @param pNit identificador nit del jefe
     * @param pPerfilJefe identificador perfil del jefe
     * @param pPerfil identificador perfil a buscar
     * @since 23/05/2022
     * @return unidades todos los usuarios de cualquier grupo segun el perfil
     */
    @Query(value = "select\n"
            + "	sc.nit ,\n"
            + "	sc.nombres\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_colaborador sc\n"
            + "inner join sat_ifi_sipf.sipf_integrante_grupo sig on\n"
            + "	sig.nit = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on\n"
            + "	sgt.id = sig.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa suap on\n"
            + "	suap.id = sgt.id_gerencia\n"
            + "where\n"
            + "	suap.id_padre in (select\n"
            + "		sua.id_padre \n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_grupo_trabajo sgt\n"
            + "	inner join sat_ifi_sipf.sipf_integrante_grupo sig on\n"
            + "		sig.id_grupo = sgt.id\n"
            + "	inner join sat_ifi_sipf.sipf_unidad_administrativa sua on\n"
            + "		sua.id = sgt.id_gerencia\n"
            + "	where\n"
            + "		sig.nit = :pNit\n"
            + "		and sig.id_perfil = :pPerfilJefe) and sig.id_perfil in(:pPerfil)", nativeQuery = true)
    public List<ColaboradoresProjection> findByNitAndRolGeneral(@Param("pNit") String pNit, @Param("pPerfilJefe") Integer pPerfilJefe, @Param("pPerfil") Integer pPerfil);

    /**
     * Se obtienen unidades administrativas y equipos de trabajo en base a nit
     * de Autorizador
     *
     * @author Jamier Batz (ajsbatzmo)
     * @param nit identificador nit del Autorizador
     * @param rol identificador rol del Autorizador
     * @param estado identificador estado deel equipo de trabajo
     * @since 23/05/2022
     * @return unidades administrativas
     */
    @Query(value = "select  sig.nit nitAprobador,\n"
            + "		sig.id_grupo as idGrupo, \n"
            + "		sgt.nombre as nombreGrupo,\n"
            + "		sgt.id_gerencia idUnidad,\n"
            + "		sua.nombre as nombreUnidad,\n"
            + "		scd.nombre as estado\n"
            + "from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on sgt.id = sig.id_grupo \n"
            + "inner join sat_ifi_sipf.sipf_unidad_administrativa sua on sgt.id_gerencia = sua.id \n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on scd.codigo = sgt.id_estado \n"
            + "where sig.nit = :nit and sig.id_rol =:rol and sgt.id_estado =:estado", nativeQuery = true)
    List<EquiposYUnidadesProjection> getTeamsAndUnits(@Param("nit") String nit, @Param("rol") Integer rol, @Param("estado") Integer estado);

    /**
     * Script SQL para obtener la informacion general del profesional
     *
     * @since 6/14/2022
     * @author Luis Villagran (lfvillag)
     * @param nit
     * @return
     */
    @Query(value = "select distinct \n"
            + "	sig.nit as nitProfesional,\n"
            + "	sc.nombres as nombreProfesional,\n"
            + "	si.nombre_insumo as nombreInsumo,\n"
            + "	scd.nombre as nombreEstado\n"
            + "	from sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "	inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	sig.nit = sc.nit \n"
            + "	inner join sat_ifi_sipf.sipf_insumo si on\n"
            + "	si.nit_encargado = sig.nit \n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	scd.codigo = si.id_estado \n"
            + "	where sig.nit =:nit", nativeQuery = true)
    public List<DesasignarCasosProjection> fingByNitProfetional(@Param("nit") String nit);

    /**
     * Script SQL para obtener la informacion para desasignar
     *
     * @since 6/14/2022
     * @author Luis Villagrán (lfvillag)
     * @return
     */
    @Query(value = "select distinct\n"
            + "sc.nit_contribuyente as NitContribuyente,\n"
            + "sig.nit as Nit,\n"
            + "scd.nombre as NombreEstado,\n"
            + "sac.id_caso as IdCaso\n"
            + "from\n"
            + "sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on\n"
            + "sc2.nit = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos sac on\n"
            + "sac.nit_colaborador = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sac.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "scd.codigo = sc.id_estado\n"
            + "where\n"
            + "sc.id_estado = 15 and sac.id_estado = 15 and not exists (select null from sat_ifi_sipf.sipf_hallazgos_encontrados she \n"
            + "where she.id_caso = sc.id_caso) and sig.nit = :nit", nativeQuery = true)
    List<SupervidoresProjection> getProfetionalDesasignar(@Param("nit") String Nit);

    /**
     * Script SQL para obtener la informacion para reasignar
     *
     * @since 6/15/2022
     * @author Luis Villagran (lfvillag)
     * @param Nit
     * @return
     */
    @Query(value = "select\n"
            + "	distinct \n"
            + "    sc.nit_contribuyente as NitContribuhente,\n"
            + "	scd2.nombre as Departamento,\n"
            + "	sac.id_caso as IdCaso,\n"
            + "	cast((\n"
            + "	select array_to_json(array_agg(row_to_json(so))) from \n"
            + "	(\n"
            + "	select scd.nombre nombreImpuesto,\n"
            + "	sic.id_impuesto idImpuesto\n"
            + "	from sat_ifi_sipf.sipf_casos sc2 \n"
            + "	inner join sat_ifi_sipf.sipf_impuesto_caso sic\n"
            + "	on sic.id_caso = sc2.id_caso \n"
            + "	inner join sat_ifi_sipf.sipf_cat_dato scd \n"
            + "	on scd.codigo = sic.id_impuesto \n"
            + "	where sc2.id_caso = sc.id_caso and scd.codigo_catalogo = 10\n"
            + "	)\n"
            + "	so\n"
            + " ) as varchar) as impuesto\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos sac on\n"
            + "	sac.nit_colaborador = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_casos sc on\n"
            + "	sc.id_caso = sac.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_insumo si on\n"
            + "	sc.id_insumo = si.id_insumo\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on\n"
            + "	si.id_departamento = scd2.codigo\n"
            + "inner join sat_ifi_sipf.sipf_hallazgos_encontrados she on\n"
            + "	she.id_caso = sc.id_caso\n"
            + "where\n"
            + "	sc.id_estado in (181, 18, 19, 133)\n"
            + "	and sac.id_estado = 15\n"
            + "	and sig.nit = :nit", nativeQuery = true)
    List<DesasignarCasosDetalleProjection> getPrfoetionalReasignar(@Param("nit") String Nit);

    @Query(value = "select\n"
            + "	distinct sig.nit as nit,\n"
            + "	sc.nombres as nombres,\n"
            + "	p.nombre as nombrePerfil,\n"
            + "	p.id_perfil as idPerfil\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo gt on\n"
            + "	gt.id = sig.id_grupo\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	sc.nit = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_perfil p on\n"
            + "	p.id_perfil = sig.id_perfil\n"
            + "where\n"
            + "	sc.id_estado =  4\n"
            + "	and \n"
            + "	gt.id_estado = 163\n"
            + "	and \n"
            + "	sig.id_grupo in (\n"
            + "	select\n"
            + "		sig.id_grupo\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "	where\n"
            + "		sig.nit = :pNit \n"
            + ")\n"
            + "	and sig.id_perfil in (  \n"
            + "with recursive perfil_recursivo as (\n"
            + "	select\n"
            + "		id_perfil ,\n"
            + "		id_perfil_padre ,\n"
            + "		nombre ,\n"
            + "		1 as depth\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_perfil\n"
            + "	where\n"
            + "		id_perfil_padre in (\n"
            + "		select\n"
            + "			id_perfil\n"
            + "		from\n"
            + "			sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "		inner join sat_ifi_sipf.sipf_grupo_trabajo gt on\n"
            + "			gt.id = sig.id_grupo\n"
            + "		where\n"
            + "			gt.id_estado = 163\n"
            + "			and \n"
            + "			sig.nit = :pNit )\n"
            + "union\n"
            + "	select\n"
            + "		e.id_perfil ,\n"
            + "		e. id_perfil_padre ,\n"
            + "		e.nombre ,\n"
            + "		s.depth + 1\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_perfil e\n"
            + "	inner join perfil_recursivo s on\n"
            + "		s.id_perfil = e.id_perfil_padre )\n"
            + "	select\n"
            + "		id_perfil\n"
            + "	from\n"
            + "		perfil_recursivo\n"
            + "	where\n"
            + "		depth = :pLevel \n"
            + "	)", nativeQuery = true)
    public List<ColaboradorProjection> findSubColaboratoresBySuperior(@Param(value = "pNit") String pNit, @Param(value = "pLevel") Integer pLevel);

    //Query para obtener todo los operadores de un grupo de trabajo
    @Query(value = "select\n"
            + "	sc.nit as nit,\n"
            + "	sc.nombres as nombres\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc on\n"
            + "	sc.nit = sig.nit\n"
            + "inner join sat_ifi_sipf.sipf_grupo_trabajo sgt on\n"
            + "	sgt.id = sig.id_grupo\n"
            + "where\n"
            + "	sig.id_rol = 3\n"
            + "	and sig.id_estado = 170\n"
            + "	and sig.id_grupo in(:grup)\n"
            + "	", nativeQuery = true)
    List<ColaboradoresProjection> getOperadorGrups(@Param(value = "grup") List<Integer> grup);

    //Query para obtener todo los operadores de un grupo de trabajo
    @Query(value = "select\n"
            + "	id_grupo as grupo\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_integrante_grupo sig\n"
            + "where\n"
            + "	sig.id_estado = 170 and sig.id_rol = 1\n"
            + "	and sig.nit =:nit", nativeQuery = true)
    List<Integer> getIdGrups(@Param("nit") String nit);

    @Query(value = "select count(*) > 0 from sat_ifi_sipf.sipf_colaborador sc\n"
            + "left join sat_ifi_sipf.sipf_asignacion_casos sac on sac.nit_colaborador = sc.nit\n"
            + "left join sat_ifi_sipf.sipf_presencias_fiscales spf on spf.usuario_creacion = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_alcance sap on sap.id_alcance = spf.id_alcance\n"
            + "left join sat_ifi_sipf.sipf_denuncia_colaborador sdc on sdc.nit_responsable = sc.nit\n"
            + "inner join sat_ifi_sipf.sipf_denuncia_grabada sdg on sdg.correlativo = sdc.correlativo_denuncia\n"
            + "inner join sat_ifi_sipf.sipf_alcance sa on sa.id_alcance = sdg.id_alcance\n"
            + "where sac.id_estado != 182 and sap.id_estado != 18 and sa.id_estado != 18 and sc.nit = :nit", nativeQuery = true)
    boolean hasWorkload(@Param("nit") String nit);

}
