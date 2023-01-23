/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author rabaraho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MassiveAssignParamsDto {

    private String pNitResponsable;
    private Integer pCantidadCasos;
    private Integer pTipoCaso;
    private Integer pIdGerencia;

}
