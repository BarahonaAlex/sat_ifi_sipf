package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfAlcancesMasivos;
import gt.gob.sat.sat_ifi_sipf.projections.AlcancesMasivosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosAndAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetalleDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GerenciasProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author asacanoes
 */
public interface AlcancesMasivosRepository extends CrudRepository<SipfAlcancesMasivos, Integer> {

    @Query(value = "select distinct  sam.id_alcance_masivo \"idAlcanceMasivo\", sam.nombre_alcance \"nombreAlcanceMasivo\", sam.descripcion_alcance \"descripcionAlcanceMasivo\"\n"
            + "from sat_ifi_sipf.sipf_alcances_masivos_versiones samv\n"
            + "right join sat_ifi_sipf.sipf_alcances_masivos sam on sam.id_alcance_masivo = samv.id_tipo_alcance_masivo\n"
            + "where samv.id_estado = 1", nativeQuery = true)
    List<AlcancesMasivosProjection> findMassiveScope();

    //List<SipfAlcancesMasivos> findByIdEstado(Integer id);
    @Query(value = "select sc.id_caso \"idCaso\", sc.id_gerencia \"idGerencia\", sc.id_estado \"idEstado\",\n"
            + "sc.periodo_revision_inicio \"periodoInicio\" , sc.periodo_revision_fin \"periodoFin\", scm.objetivo_caso_masiva \"objetivo\", scd.nombre \"nombreEstado\",\n"
            + "spf.nombre \"nombrePrograma\", scm.id_tipo_alcance \"idAlcance\", scm.id_version_alcance \"idVersion\", sc.id_programa \"idPrograma\", scm.territorio_masivo \"territorio\",\n"
            + "scd2.nombre \"nombreGerencia\", sc2.nombres \"nombreColaborador\", sc2.correo \"correoColaborador\",\n"
            + "(select max(shc.comentarios) from sat_ifi_sipf.sipf_historial_comentarios shc where shc.id_tipo_comentario = 146 and cast(sc.id_caso as text) = shc.id_registro) \"comentariosSup\",\n"
            + "(select max(shc.comentarios) from sat_ifi_sipf.sipf_historial_comentarios shc where shc.id_tipo_comentario = 147 and cast(sc.id_caso as text) = shc.id_registro) \"comentariosJefe\"\n"
            + "from sat_ifi_sipf.sipf_casos sc \n"
            + "right join sat_ifi_sipf.sipf_casos_masivos scm on sc.id_caso = scm.id_caso \n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd on sc.id_estado = scd.codigo\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd2 on sc.id_gerencia = scd2.codigo\n"
            + "left join sat_ifi_sipf.sipf_programa_fiscales spf on sc.id_programa = spf.id_programa\n"
            + "right join sat_ifi_sipf.sipf_colaborador sc2 on sc.nit_contribuyente = sc2.nit\n"
            + "where sc.id_departamento = 120 and sc.id_estado in (130, 131, 132) and sc.nit_contribuyente = cast(:nit as text)", nativeQuery = true)
    List<CasosAndAlcanceProjection> findReachAndMassiveScope(@Param("nit") Integer nit);

    /**
     * @author lfvillag (Luis Villagrán)
     * @param nit
     * @return
     */


 
}
