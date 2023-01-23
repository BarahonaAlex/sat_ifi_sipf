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
public class HisotrialEstadoColaboradorDto {

    private String nitColaborador;
    private Integer idEstado;
    private Date fechaInicio;
    private Date fechaFin;
    private String usuarioCrea;
    private String ipCrea;
    private Date fechaCrea;

}
