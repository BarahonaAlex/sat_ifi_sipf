/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class DenunciaGrabadaDto {

    @JsonProperty(value = "correlativo")
    private String correlativo;
    @JsonProperty(value = "nitDenunciante")
    private String nitDenunciante;
    @JsonProperty(value = "nombreDenunciante")
    private String nombreDenunciante;
    @JsonProperty(value = "telefonoDenunciante")
    private String telefonoDenunciante;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "nitDenunciado")
    private String nitDenunciado;
    @JsonProperty(value = "nombreDenunciado")
    private String nombreDenunciado;
    @JsonProperty(value = "establecimientoDenunciado")
    private String establecimientoDenunciado;
    @JsonProperty(value = "direccionFiscalDenunciado")
    private String direccionFiscalDenunciado;
    @JsonProperty(value = "direccionEstDenunciado")
    private String direccionEstDenunciado;
    @JsonProperty(value = "telefonoDenunciado")
    private String telefonoDenunciado;
    @JsonProperty(value = "idMotivo")
    private Integer idMotivo;
    @JsonProperty(value = "otroMotivo")
    private String otroMotivo;
    @JsonProperty(value = "formaPago")
    private String formaPago;
    @JsonProperty(value = "fechaCompra")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaCompra;
    @JsonProperty(value = "productoServicio")
    private String productoServicio;
    @JsonProperty(value = "valorCompraServicio")
    private Integer valorCompraServicio;
    @JsonProperty(value = "observaciones")
    private String observaciones;
    @JsonProperty(value = "estado")
    private String estado;
    @JsonProperty(value = "auditor")
    private String auditor;
    @JsonProperty(value = "razonRechazo")
    private String razonRechazo;
    @JsonProperty(value = "departamento")
    private Integer departamento;
    @JsonProperty(value = "municipio")
    private Integer municipio;
    @JsonProperty(value = "fechaGrabacion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaGrabacion;
    @JsonProperty(value = "justificacion")
    private String justificacion;
    @JsonProperty(value = "fechaAsignacion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaAsignacion;
    @JsonProperty(value = "fechaAsignacionJs")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaAsignacionJs;
    @JsonProperty(value = "fechaCargaPlan")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaCargaPlan;
    @JsonProperty(value = "fechaRechazo")
    @JsonFormat(pattern = "dd/MM/yyyy")
    private Date fechaRechazo;
    @JsonProperty(value = "linea")
    private Integer linea;
    @JsonProperty(value = "nitAp")
    private String nitAp;
    @JsonProperty(value = "nitAuditorRechazo")
    private String nitAuditorRechazo;
    @JsonProperty(value = "nitJs")
    private String nitJs;
    @JsonProperty(value = "nitSn")
    private String nitSn;
    @JsonProperty(value = "nombrePlan")
    private String nombrePlan;
    @JsonProperty(value = "region")
    private Integer region;
    @JsonProperty(value = "urlImagen")
    private String urlImagen;
    @JsonProperty(value = "latitud")
    private String latitud;
    @JsonProperty(value = "longitud")
    private String longitud;
    @JsonProperty(value = "idInsumo")
    private Integer idInsumo;
    @JsonProperty(value = "idProceso")
    private Integer idProceso;
    @JsonProperty(value = "idZona")
    private Integer idZona;
    @JsonProperty(value = "idGrupoHabitacional")
    private Integer idGrupoHabitacional;
    @JsonProperty(value = "nombreColonia")
    private String nombreColonia;
    @JsonProperty(value = "idVialidad")
    private Integer idVialidad;
    @JsonProperty(value = "idSubConceptoDenuncia")
    private Integer idSubConceptoDenuncia;
    @JsonProperty(value = "idAlcance")
    private Integer idAlcance;
    @JsonProperty(value = "idProcesoAlcance")
    private Integer idProcesoAlcance;
    @JsonProperty(value = "correlativoAprobacion")
    private String correlativoAprobacion;

}
