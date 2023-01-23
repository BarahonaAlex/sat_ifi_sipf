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
@NoArgsConstructor
@AllArgsConstructor
public class ProcessVariableDto {

    private String name;
    private String type;
    private Object value;
}
