/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.List;
import javax.validation.Valid;
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
public class MesPlanAnualDto {

    private Integer plan;
    @Range(min = 1, max = 12)
    private Integer month;
    private Integer type;
    @Valid
    private List<MetaPlanAnualDto> goals;
}
