/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
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
public class CollaboratorProfileDto {
    @Size(min = 4, max = 16)
    private String nit;
    @Positive
    private Integer profile;
    private Integer rol;
}
