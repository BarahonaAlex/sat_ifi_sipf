/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfHallazgoDenunciaGabinete;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgoDetalleProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author adftopvar
 */
public interface HallazgoDenunciaGabineteRepository extends CrudRepository<SipfHallazgoDenunciaGabinete, Integer>{
    
    @Query(value ="select \n" +
            "	shdg.id_hallazgo as id, shdg.nombre, shdg.descripcion, \n" +
            "	cast((\n" +
            "	select array_to_json(array_agg(row_to_json(so))) from (\n" +
            "		select\n" +
            "			rb.id,\n" +
            "			(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_rubro) rubro,\n" +
            "			(select nombre from sat_ifi_sipf.sipf_cat_dato where codigo = rb.id_impuesto) impuesto\n" +
            "		from sat_ifi_sipf.sipf_rubro_hallazgo_denuncia_gabinete srhdg\n" +
            "		inner join sat_ifi_sipf.sipf_rubro rb on srhdg.id_rubro = rb.id\n" +
            "		where srhdg.id_hallazgo = shdg.id_hallazgo\n" +
            "	 ) so\n" +
            " ) as text) as rubros\n" +
            "from sat_ifi_sipf.sipf_hallazgo_denuncia_gabinete shdg \n" +
            "inner join sat_ifi_sipf.sipf_denuncia_colaborador sdc on shdg.correlativo = sdc.correlativo_denuncia \n" +
            "where shdg.correlativo = :correlativo and sdc.nit_responsable = :nit",
            nativeQuery = true)
    List<HallazgoDetalleProjection> getDetailFindings(@Param("nit") String nit, @Param("correlativo") String correlativo);
    
}
