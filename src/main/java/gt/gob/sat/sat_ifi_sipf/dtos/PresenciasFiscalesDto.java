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
 * @author jdaldana
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PresenciasFiscalesDto implements Serializable {

    private Integer idFormulario;
    private Integer idPresencia;
    private Date fechaInicio;
    private Date fechaFin;
    private String lugarDepartamental;
    private Integer idPrograma;
    private Integer meta;
    private String idProceso;
    private Integer idEstado;
    private String usuarioModifica;
    private Date fechaModifica;
    private String usuarioCreacion;
    private Integer idAlcance;
    private int idTipoAlcance;
    private List<AlcanceDetalleDto>secciones; 
    private String correlativoAprobacion;
    private Integer idGerencia;
    private String resultadoFormulario;
    private String lugarEjecucion;
}
