/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.projections.GruposTrabajoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.IntegrantesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.UnidadAdministrativaProjection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author crramosl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GruposTrabajoDetalleDto {

    private GruposTrabajoProjection equipoTrabajo;
    private List<IntegrantesProjection> integrantes;
    private List<UnidadAdministrativaProjection> unidades;
}
