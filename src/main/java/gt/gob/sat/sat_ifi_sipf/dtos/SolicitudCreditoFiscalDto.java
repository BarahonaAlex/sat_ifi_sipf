/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ajabarrer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudCreditoFiscalDto {
     private String numeroFormulario;
     private String nitContribuyente;
     private Date periodoInicio;
     private Date periodoFin;
     private String actividadEconomica;
     private String principalProducto;
     private String formularioIva;
     private String creditoSujetoDevolucion;
     private Integer monto;
     private Integer creditoNoSolicitado;
     private Integer montoDevolucion;
     private Integer multa;
     private Integer total;
     private String asignado;
     private Integer estado;
     private String usuarioModifica;
     private Date fechaModifica;
     private String ipModifica;
     private String documentosRespaldo;
     
}
