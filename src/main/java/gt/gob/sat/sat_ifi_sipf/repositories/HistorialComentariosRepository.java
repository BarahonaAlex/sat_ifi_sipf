/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.projections.HistorialComentariosProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author josed
 */
public interface HistorialComentariosRepository extends CrudRepository<SipfHistorialComentarios, Integer> {

    @Query(value = "select shc.comentarios as comentarios ,\n"
            + "shc.id_historial_comentario as idHistorialComentario, \n"
            + "shc.id_registro as idRegistro, \n"
            + "shc.id_tipo_comentario as idTipoComentario \n"
            + "from sat_ifi_sipf.sipf_historial_comentarios shc \n"
            + "where shc.id_historial_comentario = (select max(t.id_historial_comentario) from sat_ifi_sipf.sipf_historial_comentarios t \n"
            + "where t.id_registro = :pIdRegistro \n"
            + "and t.id_tipo_comentario in(:pIdTipoComentario ))", nativeQuery = true)
    public HistorialComentariosProjection findMaxIdHistorialComentario(@Param("pIdRegistro") String pIdRegistro, @Param("pIdTipoComentario") List<Integer> pIdTipoComentario);

}
