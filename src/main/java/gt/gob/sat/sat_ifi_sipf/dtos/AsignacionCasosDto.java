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
 * @author josed
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AsignacionCasosDto {

    private String nitColaborador;
    private int idGrupoAsignacion;
    private int idOrigen;
    private int cantidadCasos;
    private int idGerencia;
    private int idImpuesto;
    private String nombreCaso;
}
