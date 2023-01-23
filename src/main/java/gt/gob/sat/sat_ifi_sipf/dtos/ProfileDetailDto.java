/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import gt.gob.sat.sat_ifi_sipf.projections.PerfilProjections;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author crramosl
 */
@Builder
@Getter
@Setter
public class ProfileDetailDto {
    private ColaboradorDetalleDto user;
    private List<PerfilProjections> profiles;
}
