/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author lfvillag
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ImportacionExportacionSivepaDto {
    @JsonProperty(value = "nit")
    private String nit;
    @JsonProperty(value = "fecha")
    private String fecha;
    @JsonProperty(value = "fechaDos")
    private String fechaDos;
}
