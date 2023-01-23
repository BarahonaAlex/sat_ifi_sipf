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
 * @author rabaraho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuditDto {

    private String numeroExpediente;
    private String ubicacion;
    private String estadoExpediente;
    private String nombramiento;
    private Date fechaPeriodoDel;
    private Date fechaPeriodoAl;
    private String estadoNombramiento;
    private String nombrePlan;
    private BigDecimal multa;
    private BigDecimal ajuste;
    
}
