/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.List;
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
public class CasesScopeMasiveDto {
    private Integer idCaso;
    private Integer estado;
    private Integer IdTipoAlcance;
    private List<AlcanceDetalleDto>secciones;   
    private Integer idTipoCaso;
}
