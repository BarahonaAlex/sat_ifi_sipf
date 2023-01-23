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
 * @author rabaraho
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SipfCatDatoDto implements Serializable {

    private int codigo;
    private int id;
    private int codigoDatoPadre;
    private String codigoIngresado;
    private String nombre;
    private String descripcion;
    private Integer estado;
    private String usuarioAgrega;
    private Date fechaAgrega;
    private String usuarioModifica;
    private Date fechaModifica;

}
