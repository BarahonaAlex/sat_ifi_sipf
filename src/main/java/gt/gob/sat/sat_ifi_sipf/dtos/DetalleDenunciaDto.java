/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author lfvillag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetalleDenunciaDto {
    private Integer estado;
    private String direccion;
    private Integer idproceso;
    private Integer idregion;
}
