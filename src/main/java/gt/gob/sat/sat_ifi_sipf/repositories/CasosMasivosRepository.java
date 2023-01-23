package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfCasosMasivos;
import gt.gob.sat.sat_ifi_sipf.projections.CasosOnlyProjection;
import gt.gob.sat.sat_ifi_sipf.projections.MassiveResumeProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author asacanoes
 */
public interface CasosMasivosRepository extends CrudRepository<SipfCasosMasivos, Integer> {

    @Query(value = "select "
            + "	sc.id_gerencia as idGerencia, "
            + "	gerencia.nombre as nombreGerencia, "
            + "	sdc.tipo_caso as idTipoCaso, "
            + "	tipo.nombre  as nombreTipoCaso, "
            + "	count(*) as cantidad "
            + "from "
            + "	sat_ifi_sipf.sipf_casos sc "
            + "inner join sat_ifi_sipf.sipf_detalle_caso sdc  "
            + "on "
            + "	sdc.id_caso = sc.id_caso "
            + "inner join sat_ifi_sipf.sipf_cat_dato gerencia "
            + "on "
            + "	gerencia.codigo = sc.id_gerencia "
            + "inner join sat_ifi_sipf.sipf_cat_dato tipo "
            + "on "
            + "	tipo.codigo = sdc.tipo_caso "
            + "	where sdc.tipo_caso  = :pIdTipoCaso "
            + " and sc.id_estado = :pIdEstado "
            + "group by "
            + "	sc.id_gerencia, "
            + "	sdc.tipo_caso , "
            + "	gerencia.nombre, "
            + "	tipo.nombre", nativeQuery = true)
    public List<MassiveResumeProjection> findResumeByStatusAndCaseType(@Param("pIdTipoCaso") Integer pIdTipoCaso, @Param("pIdEstado") Integer pIdEstado);

    @Query(value = "select\n"
            + "	sc.id_caso as  idCaso, "
            + "	sc.id_estado as idEstado,\n"
            + "	sc.id_origen as idOrigen,\n"
            + "	sc.id_gerencia as idGerencia,\n"
            + "	sc.id_proceso as idProceso,\n"
            + "	sc.monto_recaudado as idMontoRecaudo,\n"
            + "	sc.id_programa as idPrograma,\n"
            + "	sc.id_departamento as idDepartamento,\n"
            + "	sc.correlativo_aprobacion as correlativoAprobacion ,\n"
            + "	sc.usuario_modifica as usuarioModifica ,\n"
            + "	sc.fecha_modifica as fechaModifica ,\n"
            + "	sc.ip_modifica as ipModifica ,\n"
            + "	sc.id_insumo as idInsumo ,\n"
            + "	sc.periodo_revision_inicio as periodoRevisionInicio ,\n"
            + "	sc.periodo_revision_fin as periodoRevisionFin ,\n"
            + "	sc.nit_contribuyente as nitContribuyente ,\n"
            + "	sc.id_zona as idZona,\n"
            + "	sc.id_municipio as idMunicipio\n "
            + "from\n"
            + "	sat_ifi_sipf.sipf_casos sc\n"
            + "inner join sat_ifi_sipf.sipf_detalle_caso sdc \n"
            + "on\n"
            + "	sdc.id_caso = sc.id_caso\n"
            + "where\n"
            + "	sdc.tipo_caso = :pTipoCaso \n"
            + "	and sc.id_gerencia = :pIdGerencia \n"
            + "	and sc.id_estado = :pIdEstado \n"
            + "	fetch first :pCantidadCasos rows only",
            nativeQuery = true)

    List<CasosOnlyProjection> findTopNCasos(
            @Param(value = "pTipoCaso") Integer pTipoCaso,
            @Param(value = "pIdGerencia") Integer pIdGerencia,
            @Param(value = "pIdEstado") Integer pIdEstado,
            @Param(value = "pCantidadCasos") Integer pCantidadCasos
    );

}
