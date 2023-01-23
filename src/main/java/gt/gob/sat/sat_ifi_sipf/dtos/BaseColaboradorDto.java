/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author ruano
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BaseColaboradorDto extends BaseDto {

    private String nombresColaborador;
    private String idColaborador;
    private String loginColaborador;
    private String puestoTrabajo;
    private Integer idEstado;
    private Integer idGerencia;
    private String correo;
    private Date fechaInicio;
    private Date fechaFin;

}
