/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author rabaraho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SipfInsumoBitacoraCambioEstadoDto {

    @JsonProperty(value = "id")
    private int id;
    @JsonProperty(value = "idInsumo")
    private int idInsumo;
    @JsonProperty(value = "idEstadoAnterior")
    private Integer idEstadoAnterior;
    @JsonProperty(value = "idEstadoNuevo")
    private int idEstadoNuevo;
    @JsonProperty(value = "usuarioModifica")
    private String usuarioModifica;
    @JsonProperty(value = "fechaModifica")
    private Date fechaModifica;

}
