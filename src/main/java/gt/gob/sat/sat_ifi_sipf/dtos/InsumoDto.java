/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.simple.JSONObject;

/**
 *
 * @author rabaraho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class InsumoDto {

    private Integer idInsumo;
    private String nombreInsumo;
    //private Integer cantidadCasos;
    private Integer idGerencia;
    private String nitEncargado;
    private Integer idEstado;
    private Integer idEstadoAnterior;
    //private Double montoRecaudo;
    private Integer idDepartamento;
    private Integer idTipoInsumo;
    private String usuarioModifica;
    private Integer casosAasignar;
    private Integer idUnidadAdministrativa;
    private String descripcion;
    private String idProceso;
    private Integer idTipoCaso;
    private String comentario;

    public String toJson() {
        final JSONObject object = new JSONObject();
        object.put("idInsumo", getIdInsumo());
        object.put("idEstado", getIdEstado());
        return object.toJSONString();
    }
}
