/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.projections.CasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.HistorialComentariosProjection;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author jdaldana
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CasoComentarioDto implements Serializable {

    private CasosProjection caso;
    private HistorialComentariosProjection comentario;
}
