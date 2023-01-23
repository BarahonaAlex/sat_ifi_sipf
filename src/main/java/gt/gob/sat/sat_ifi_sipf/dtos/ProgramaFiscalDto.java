/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
public class ProgramaFiscalDto implements Serializable {

    private int idPrograma;
    private int idTipoPrograma;
    private int idDireccionamientoAuditoria;
    private int idTipoAuditoria;
    private int idOrigenInsumo;
    private int idGerencia;
    private String impuesto;
    private int anio;
    private int correlativo;
    private Date periodoInicio;
    private Date periodoFin;
    private String nombre;
    private String descripcion;
    private Integer idDepartamento;
    private int idEstado;
    private String codificacionPrograma;
    private String impuestoNombres;
    private String usuarioModifica;
    private String ipModifica;
    private Date fechaModifica;
    List<ImpuestoProgramaFiscalDto> impuestos;
    private String comentarios;
    private String usuarioAgrega;
    private int idEstadoAnterior;
    private String idProceso;

}
