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
public class DetalleConvenioPagoDto {

    private BigDecimal cuota;
    private String numeroFormulario;
    private Date fechaRecaudo;
    private BigDecimal impuesto;
    private BigDecimal interes;
    private BigDecimal mora;
    private BigDecimal multaOmision;
    private BigDecimal multaFormal;
    private BigDecimal multaRectificativa;
    private BigDecimal cuotaPagar;
    private BigDecimal saldoPagar;
    private BigDecimal recargoInteres;
    private BigDecimal recargoMora;
    private BigDecimal totalRecargo;
    private BigDecimal totalPagado;

}
