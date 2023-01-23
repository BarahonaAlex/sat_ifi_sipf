/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ajdaldana
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResRutaBaseACSDto {
    Integer nodeType;
    Map<String, Object> properties;
}
