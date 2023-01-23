
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



/**
 *
 * @author ajdaldana
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class IngresoCasosDto implements Serializable {
    private Integer estado;
    private Integer origen;
    private Integer gerencia;
    private Long proceso;
    private Double montoRecaudado;
    private Integer clasificacionRiesgo;
    private Integer tipoInsumo;
    private Integer programa;
    private Integer impuesto;
    private Integer tipoAlcance;
    private String nombreCaso;
    private Integer cargaMasiva;
    private Date periodoRevisionInicio;
    private Date periodoRevisionFin;
    private String nitContribuyente;
    private Integer tipoSolicitud;
    private String descripcion;
    private Date fechaModifica;
    private String usuarioModifica;
    private String ipModifica;
    
}
