/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.math.BigDecimal;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ajsbatzmo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetenIVAResponseDto {

        private String nitAgenteRetenedor;
        private String fechaInicio;
        private String fechaFin;
        private String nitRetenido;
        private String estadoRetencion;
        private String codigoEstado;
        private String numeroConstancia;
        private String limite;
        private String claveResumen;
        private String usuario;
        private String concepto;
        private String fr;
        private Integer tipo;
        private Integer codigoRenta;  
}
