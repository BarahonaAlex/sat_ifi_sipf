/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceMasivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CedulaVerificacionProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceMasivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceSelectivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReporteAlcanceSolicitudProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author Jamier
 */
public interface ReportesAlcancesRepository extends CrudRepository<SipfCasos, Integer> {

    @Query(value
            = "select\n"
            + "		sic.nit_contribuyente \"nit\",\n"
            + "		sic.periodo_revision_inicio \"periodoDel\",\n"
            + "		sic.periodo_revision_fin \"periodoAl\",\n"
            + "		spf.nombre \"programa\",\n"
            + "		sic.id_departamento  \"tipoAlcance\"\n"
            + "from sat_ifi_sipf.sipf_casos sic\n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales spf on sic.id_programa = spf.id_programa\n"
            + "where sic.nit_contribuyente = :nit",
            nativeQuery = true
    )
    public List<AlcanceProjection> getAlcance(@Param("nit") String nit);

    @Query(value
            = "select  samv.actividad,\n"
            + "        sc.periodo_revision_inicio as plazoDel,\n"
            + "	       sc.periodo_revision_fin as plazoAl,\n"
            + "        spf.nombre as programa,\n"
            + "        sc.id_departamento as tipoAlcance,\n"
            + "        sc.id_caso as idCaso\n"
            + "from sat_ifi_sipf.sipf_casos sc\n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales spf on sc.id_programa = spf.id_programa\n"
            + "inner join sat_ifi_sipf.sipf_casos_masivos scm on sc.id_caso = scm.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_alcances_masivos_versiones samv on scm.id_version_alcance = samv.version and scm.id_departamento = samv.id_tipo_alcance_masivo\n"
            + "where sc.id_caso = :id",
            nativeQuery = true
    )
    public List<AlcanceMasivoProjection> getAlcanceMasivo(@Param("id") Integer id);

    @Query(value
            = "select\n"
            + "       sic.nit_contribuyente as nit,\n"
            + "	      sic.periodo_revision_inicio as periodoDel,\n"
            + "	      sic.periodo_revision_fin as periodoAl,\n"
            + "	      spf.impuesto_nombres as impuesto,\n"
            + "	      spf.nombre as programa,\n"
            + "       sic.usuario_modifica as iniciales,\n"
            + "       string_agg(t.value ->> 'antecedentes', '') as antecedentes,\n"
            + "       string_agg(t.value ->> 'rAuditorias', '') as auditorias,\n"
            + "       string_agg(t.value ->> 'iEspecial', '') as infoEspecial,\n"
            + "       string_agg(t.value ->> 'inconsistencia', '') as inconsistencias,\n"
            + "       string_agg(t.value ->> 'hProgramacion', '') as hallazgosProgramacion,\n"
            + "       string_agg(t.value ->> 'objetivo', '') as objetivo,\n"
            + "       string_agg(t.value ->> 'pEspecificos', '') as procedEspecificos\n"
            + "from (\n"
            + "         select json_array_elements(cast(ssc.json_seccion as json)) as value,\n"
            + "                ssc.id_caso\n"
            + "         from sat_ifi_sipf.sipf_secciones_caso ssc\n"
            + "         inner join sat_ifi_sipf.sipf_casos sic on ssc.id_caso = sic.id_caso\n"
            + "         where sic.nit_contribuyente = :nit\n"
            + "     ) t\n"
            + "inner join sat_ifi_sipf.sipf_casos sic on sic.id_caso = t.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales spf on sic.id_programa = spf.id_programa\n"
            + "where sic.nit_contribuyente = :nit\n"
            + "group by sic.nit_contribuyente,\n"
            + "         sic.periodo_revision_inicio,\n"
            + "         sic.periodo_revision_fin,\n"
            + "         spf.impuesto_nombres,\n"
            + "         spf.nombre,\n"
            + "         sic.usuario_modifica",
            nativeQuery = true
    )
    public List<ReporteAlcanceSelectivoProjection> getReporteAlcanceSelectivo(@Param("nit") String nit);

    @Query(value
            = "select\n"
            + "	      sic.nit_contribuyente as nit,\n"
            + "	      sic.periodo_revision_inicio as periodoDel,\n"
            + "	      sic.periodo_revision_fin as periodoAl,\n"
            + "	      spf.impuesto_nombres as impuesto,\n"
            + "       scd.plazo_entrega as plazo,\n"
            + "       scd2.nombre as ente,\n"
            + "       spf.nombre as programa,\n"
            + "       sic.usuario_modifica as iniciales,\n"
            + "       string_agg(t.value ->> 'antecedentes', '') as antecedentes,\n"
            + "       string_agg(t.value ->> 'rAuditorias', '') as auditorias,\n"
            + "       string_agg(t.value ->> 'rFiscalizar', '') as rubrosFiscalizar,\n"
            + "       string_agg(t.value ->> 'objetivo', '') as objetivo,\n"
            + "       string_agg(t.value ->> 'pEspecificos', '') as procedEspecificos\n"
            + "from (\n"
            + "         select json_array_elements(cast(ssc.json_seccion as json)) as value,\n"
            + "                ssc.id_caso\n"
            + "         from sat_ifi_sipf.sipf_secciones_caso ssc\n"
            + "         inner join sat_ifi_sipf.sipf_casos sic on ssc.id_caso = sic.id_caso\n"
            + "         where sic.nit_contribuyente = :nit\n"
            + "     ) t\n"
            + "inner join sat_ifi_sipf.sipf_casos sic on sic.id_caso = t.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales spf on sic.id_programa = spf.id_programa\n"
            + "inner join sat_ifi_sipf.sipf_casos_dependencias scd on sic.id_caso = scd.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd2 on scd.id_entidad_solicitante = scd2.codigo\n"
            + "where sic.nit_contribuyente = :nit\n"
            + "group by sic.nit_contribuyente,\n"
            + "         sic.periodo_revision_inicio,\n"
            + "         sic.periodo_revision_fin,\n"
            + "         spf.impuesto_nombres,\n"
            + "         scd.plazo_entrega,\n"
            + "         scd2.nombre,\n"
            + "         spf.nombre,\n"
            + "         sic.usuario_modifica",
            nativeQuery = true
    )
    public List<ReporteAlcanceSolicitudProjection> getReporteAlcanceSolicitud(@Param("nit") String nit);

    @Query(value
            = "select  samv.actividad,\n"
            + "        sc.periodo_revision_inicio \"plazoDel\",\n"
            + "		sc.periodo_revision_fin \"plazoAl\",\n"
            + "        spf.nombre \"programa\",\n"
            + "        samv.justificacion,\n"
            + "        samv.objetivo,\n"
            + "        samv.procedimientos_especificos \"procedEspesificos\",\n"
            + "        scm.territorio_masivo \"territorio\",\n"
            + "        sc.usuario_modifica \"iniciales\"\n"
            + "from sat_ifi_sipf.sipf_casos sc\n"
            + "inner join sat_ifi_sipf.sipf_programa_fiscales spf on sc.id_programa = spf.id_programa\n"
            + "inner join sat_ifi_sipf.sipf_casos_masivos scm on sc.id_caso = scm.id_caso\n"
            + "inner join sat_ifi_sipf.sipf_alcances_masivos_versiones samv on scm.id_version_alcance = samv.version and scm.id_tipo_alcance = samv.id_tipo_alcance_masivo\n"
            + "where sc.id_caso = :id",
            nativeQuery = true
    )
    public List<ReporteAlcanceMasivoProjection> getReporteAlcanceMasivo(@Param("id") Integer id);
    
    @Query(value
            = "select \n"
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
            + "	    ) as contribuyente,\n"
            + "	    to_char( sscf.periodo_inicio, 'dd-mm-yyyy') as periodoInicio,\n"
            + "	    to_char( sscf.periodo_fin, 'dd-mm-yyyy') as periodoFin,\n"
            + "	    upper('x') as marcaFel,\n"
            + "	    upper('x') as marcaRegimen,\n"
            + "	    upper('x') as marcaEnvio,\n"
            + "	    upper('x') as marcaDocs,\n"
            + "	    upper('x') as marcaLibros\n"
            + "from sat_ifi_sipf.sipf_solicitud_credito_fiscal sscf  \n"
            + "where sscf.id_solicitud = :idSolicitud",
            nativeQuery = true
    )
    public List<CedulaVerificacionProjection> getCedulaVerificacion(@Param("idSolicitud") Integer id);

}
