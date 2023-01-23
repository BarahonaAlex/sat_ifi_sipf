/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.controllers.CreditoFiscalController;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
/**
 *
 * @author lfvillag
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditoFiscalArchivosDto {
    private List<CreditoFiscalController> libros;
}
