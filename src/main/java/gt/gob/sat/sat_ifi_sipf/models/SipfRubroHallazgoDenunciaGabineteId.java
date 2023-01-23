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
 * @author adftopvar
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SipfRubroHallazgoDenunciaGabineteId implements Serializable {
    
    private int idRubro;
    private int idHallazgo;
    
}
