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
 * @author crramosl
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamsLineasDuasDucasDto {

    private String puertoEntrada;
    private String aduanaEntrada;
    private String anio;
    private Integer secuencia;
}
