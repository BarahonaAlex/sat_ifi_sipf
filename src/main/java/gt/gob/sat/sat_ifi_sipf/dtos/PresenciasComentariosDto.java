/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.projections.AlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.HistorialComentariosProjection;
import lombok.AllArgsConstructor;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jdaldana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PresenciasComentariosDto {
    
       private List<AlcanceProjection> masivo;
       private HistorialComentariosProjection comentario;
}
