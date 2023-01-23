/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;


import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jdaldana
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AlcanceDto {
    
     private int idAlcance;
     private int idTipoAlcance;
     private int idEstado;
     private List<AlcanceDetalleDto>secciones;    
}
