/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.Date;
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
public class BitacoraProgramasFiscalesDto implements Serializable {

    private int idPrograma;
    private int idTipoOperacion;
    private String usuarioModifica;
    private Date fechaModifica;
    private int idEstadoAnterior;
    private int idEstadoNuevo;

}
