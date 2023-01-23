/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author crramosl
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SipfIndicadorPlanAnualId implements Serializable {

    private Integer idPlan;
    private Integer idIndicador;
}
