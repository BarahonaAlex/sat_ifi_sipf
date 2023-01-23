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
 * @author agaruanom
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class solicitudExternaDto {
    
    private String  nit;
     private String nombre;
      private String domicilio;
      private String clasificacion;
      private Integer idGerencia;
      private String gerencia;
      private String entidad;
      private Date fechaDesde;
      private Date fechaHasta;
}
