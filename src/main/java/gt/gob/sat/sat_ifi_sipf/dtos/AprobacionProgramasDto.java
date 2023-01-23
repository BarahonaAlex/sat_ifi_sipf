/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author josed
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AprobacionProgramasDto implements Serializable {

    private Integer idGerencia;
    private Integer idPrograma;
    private Integer cantidad;
    private String siglas;
    private Integer periodo;
    private String nombreGerencia;
    private String nombreJefe;
    private Integer idInsumo;
    private Integer idCaso;
    private String comentario;
    private Integer tipo;
    private Integer idAlcance;
    private Integer tipoCaso;
    private String idDenuncia;
}
