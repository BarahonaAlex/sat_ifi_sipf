/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfSeccionesCaso;
import gt.gob.sat.sat_ifi_sipf.projections.SeccionesCasoProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Es un contrato que define el comportamiento que alguna clase debe implementar
 * mediante un conjunto de metodos relacionados.
 * <p>
 * Este repositorio utiliza un {@code CrudRepository} para acceder a la ENTITY y
 * poder ejecutar los querys necesarios. Debe seguir el estandar de Spring JPA
 * para el nombrado de las consultas y envio de parametros.
 *
 * @author Luis Villagran (alfvillag)
 * @since 12/02/2022
 * @version 1.0
 */
public interface SeccionesCasoRepository extends CrudRepository<SipfSeccionesCaso, Integer> {

    // public List<SipfSeccionesCaso> findSectionCase (Integer idCaso);
    @Query(value = "select ssc.json_seccion \"jsonSection\",shc.id_tipo_comentario \"comentarioTipo\", shc.comentarios \"comentario\", shc.id_registro \"registro\"\n"
            + "     from (select json_array_elements(cast(json_seccion as json)) as value from sat_ifi_sipf.sipf_secciones_caso where id_caso = :idCaso) ssjc\n"
            + "     inner join sat_ifi_sipf.sipf_secciones_caso ssc on cast(ssc.id_caso as text) = ssjc.value->>'idCaso'\n"
            + "     inner join sat_ifi_sipf.sipf_historial_comentarios shc on concat(ssc.id_caso,',',ssjc.value->>'tipo') = shc.id_registro\n"
            + "     where shc.id_historial_comentario = (select max(id_historial_comentario) from sat_ifi_sipf.sipf_historial_comentarios where concat(ssc.id_caso,',',ssjc.value->>'tipo') = id_registro)",
            nativeQuery = true)
    public List<SeccionesCasoProjection> getSectionAndComentary(@Param("idCaso") Integer idCaso);
}
