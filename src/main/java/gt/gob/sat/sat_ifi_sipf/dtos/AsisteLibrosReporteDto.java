/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author adftopvar
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AsisteLibrosReporteDto {
    private String fecha;
    private String establecimiento_tipotransaccion;	
    private String tipotransaccion_tipofyduca;	
    private String tipofyduca_tipoduca;
    private String tipo_de_documento;
    private String estado;
    private String valor_iva_bienes;
    private String valor_iva_servicios;
    private String valor_gravado_bienes;
    private String valor_gravado_servicios;
    private String valor_exento_bienes;
    private String valor_exento_servicios;
    private String valoridp;
    private String valorith;
    private String valoritp;
    private String valortdp; 
    private String valorifb;
    private String valormun;
    private String valorcem;
    private String valoridb;
    private String valoribn;
    private String valortab;
    private String valortap;
    private String valor_total_documento;
}
