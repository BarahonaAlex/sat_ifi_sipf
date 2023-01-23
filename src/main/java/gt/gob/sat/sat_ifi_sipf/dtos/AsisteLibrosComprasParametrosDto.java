/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author adftopvar
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsisteLibrosComprasParametrosDto {

    private Integer anio;
    private String establecimiento;
    private String estado;
    private String mes;
    private String nitReceptor;
    private String tipo;
    private String tipoDocumento;
}
