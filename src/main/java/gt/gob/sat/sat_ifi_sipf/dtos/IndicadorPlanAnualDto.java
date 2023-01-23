/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

/**
 *
 * @author crramosl
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndicadorPlanAnualDto {

    private Integer id;
    @Range(min = 0, max = 100)
    private Integer value;
}
