/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasosId;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacionCasosProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author josed
 */
public interface AsignacionCasosRepository extends CrudRepository<SipfAsignacionCasos, SipfAsignacionCasosId> {

    public Optional<SipfAsignacionCasos> findByIdIdCaso(int idCaso);

    @Query(value = "select distinct \n"
            + "sc.id_estado as IdEstado,\n"
            + "sc2.nombres as NombreContribuyente,\n"
            + "sig.nit as Nit,\n"
            + "sac.id_caso as IdCaso\n"
            + "from\n"
            + "sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_colaborador sc2 on\n"
            + "sc2.nit = sig.nit \n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos sac on\n"
            + "sac.nit_colaborador = sig.nit \n"
            + "inner join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sac.id_caso \n"
            + "where \n"
            + "sc.id_estado = 15 and sac.id_estado = 15 and sig.nit = :nit", nativeQuery = true)
    List<DesasignacionCasosProjection> getCasesForUnassign(@Param("nit") String nit);

    @Query(value = "select distinct \n"
            + "sc.nit_contribuyente as NitContribuhente,\n"
            + "sc.nombre_caso as NombreCaso,\n"
            + "si.id_departamento as Departamento,\n"
            + "sac.id_caso as IdCaso,\n"
            + "si.id_impuesto as Impuesto\n"
            + "from\n"
            + "sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "inner join sat_ifi_sipf.sipf_asignacion_casos sac on\n"
            + "sac.nit_colaborador = sig.nit \n"
            + "inner join sat_ifi_sipf.sipf_casos sc on\n"
            + "sc.id_caso = sac.id_caso \n"
            + "inner join sat_ifi_sipf.sipf_insumo si on\n"
            + "sc.id_insumo = si.id_insumo \n"
            + "where \n"
            + "sc.id_estado = 15 and sac.id_estado = 138 and sig.nit = :nit", nativeQuery = true)
    List<ReasignacionCasosProjection> getCasesForReassign(@Param("nit") String nit);
}
