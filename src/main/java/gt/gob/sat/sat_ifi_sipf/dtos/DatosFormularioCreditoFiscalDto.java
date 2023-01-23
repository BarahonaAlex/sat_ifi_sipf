/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author aaehernan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DatosFormularioCreditoFiscalDto {
    private Object resultadoContribuyente;
    private List<Object> resultadoRepresentante;
    private Object resultadoContador;
}
