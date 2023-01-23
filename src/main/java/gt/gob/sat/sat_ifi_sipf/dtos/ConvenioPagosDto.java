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
public class ConvenioPagosDto {
    
    private String numeroFormulario;
    private String numeroExpediente;
    private String estadoConvenio;
    private Date fechaPresentacion;
    private BigDecimal totalAutorizado;
    private BigDecimal plazoOtorgado;
    private BigDecimal cuotasPagadas;
    private BigDecimal montoPagado;
    private BigDecimal saldo;
    
}
