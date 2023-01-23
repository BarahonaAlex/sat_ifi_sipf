/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import io.swagger.annotations.ApiModel;
import lombok.Builder;
import lombok.Getter;

/**
 *
 * @author crramosl
 */
@ApiModel(value = "Error")
@Getter
@Builder
public class ErrorDto {

    private final String param;
    private final Object value;
    private final String description;
}
