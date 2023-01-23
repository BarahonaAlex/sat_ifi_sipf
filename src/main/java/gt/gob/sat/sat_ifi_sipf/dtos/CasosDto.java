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
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

/**
 *
 * @author josed
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CasosDto implements Serializable {

    private Integer estado;
    private Integer origen;
    private Integer gerencia;
    private String proceso;
    private Double montoRecaudado;
    private Integer tipoInsumo;
    private Integer programa;
    private Integer tipoAlcance;
    private String correlativoAprobacion;
    private String nombreCaso;
    private Integer insumo;
    private Date periodoRevisionInicio;
    private Date periodoRevisionFin;
    private String nitContribuyente;
    private Integer entidadSolicitante;
    private Integer tipoSolicitud;
    private String nombreCuentaAuditar;
    private String nitContribuyenteCruce;
    private String numeroFacturaCruce;
    private String serieFacturaCruce;
    private Date fechaFacturaCruce;
    private Float montoFacturaCruce;
    private Date fechaSolicitud;
    private Date fechaDocumento;
    private Integer numeroDocumento;
    private Date plazoEntrega;
    private String nombreContacto;
    private String telefonoContacto;
    private String correoContacto;
    private String detalleEntidadSolicitante;
    private String objetivoCasoMasiva;
    private String nitColaborador;
    private String descripcion;
    private String requiereDocumentacion;
    private String jefeUnidad;
    private Integer zona;
    private Integer municipioid;
    private Integer idCaso;
    private List<solicitudExternaDto> arrayGeneral;
    private List<Integer> impuestos;
    //campo utilizado para obtener el impuesto de las solicitudes externas de manera provisional
    private Integer impuestoExterna;
    private String comentario;
    private String loginProfesional;
}
