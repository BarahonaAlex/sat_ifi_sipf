package gt.gob.sat.sat_ifi_sipf.models;
// Generated 17-ene-2023 14:48:21 by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;

/**
 * SipfProgramaFiscales generated by hbm2java
 */
@Entity
@Builder
@Table(name = "sipf_programa_fiscales",
        schema = "sat_ifi_sipf"
)
public class SipfProgramaFiscales  implements java.io.Serializable {


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
     private String usuarioAgrega;
     private Date fechaCreacion;
     private Integer idEstadoAnterior;
     private String idProceso;

    public SipfProgramaFiscales() {
    }

	
    public SipfProgramaFiscales(int idPrograma, int idTipoPrograma, int idDireccionamientoAuditoria, int idTipoAuditoria, int idOrigenInsumo, int idGerencia, String impuesto, int anio, int correlativo, Date periodoInicio, Date periodoFin, String nombre, int idEstado, String codificacionPrograma, String impuestoNombres, String usuarioModifica, String ipModifica, Date fechaModifica, Date fechaCreacion) {
        this.idPrograma = idPrograma;
        this.idTipoPrograma = idTipoPrograma;
        this.idDireccionamientoAuditoria = idDireccionamientoAuditoria;
        this.idTipoAuditoria = idTipoAuditoria;
        this.idOrigenInsumo = idOrigenInsumo;
        this.idGerencia = idGerencia;
        this.impuesto = impuesto;
        this.anio = anio;
        this.correlativo = correlativo;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.nombre = nombre;
        this.idEstado = idEstado;
        this.codificacionPrograma = codificacionPrograma;
        this.impuestoNombres = impuestoNombres;
        this.usuarioModifica = usuarioModifica;
        this.ipModifica = ipModifica;
        this.fechaModifica = fechaModifica;
        this.fechaCreacion = fechaCreacion;
    }
    public SipfProgramaFiscales(int idPrograma, int idTipoPrograma, int idDireccionamientoAuditoria, int idTipoAuditoria, int idOrigenInsumo, int idGerencia, String impuesto, int anio, int correlativo, Date periodoInicio, Date periodoFin, String nombre, String descripcion, Integer idDepartamento, int idEstado, String codificacionPrograma, String impuestoNombres, String usuarioModifica, String ipModifica, Date fechaModifica, String usuarioAgrega, Date fechaCreacion, Integer idEstadoAnterior, String idProceso) {
       this.idPrograma = idPrograma;
       this.idTipoPrograma = idTipoPrograma;
       this.idDireccionamientoAuditoria = idDireccionamientoAuditoria;
       this.idTipoAuditoria = idTipoAuditoria;
       this.idOrigenInsumo = idOrigenInsumo;
       this.idGerencia = idGerencia;
       this.impuesto = impuesto;
       this.anio = anio;
       this.correlativo = correlativo;
       this.periodoInicio = periodoInicio;
       this.periodoFin = periodoFin;
       this.nombre = nombre;
       this.descripcion = descripcion;
       this.idDepartamento = idDepartamento;
       this.idEstado = idEstado;
       this.codificacionPrograma = codificacionPrograma;
       this.impuestoNombres = impuestoNombres;
       this.usuarioModifica = usuarioModifica;
       this.ipModifica = ipModifica;
       this.fechaModifica = fechaModifica;
       this.usuarioAgrega = usuarioAgrega;
       this.fechaCreacion = fechaCreacion;
       this.idEstadoAnterior = idEstadoAnterior;
       this.idProceso = idProceso;
    }
   
     @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_programa", unique = true, nullable = false)
    public int getIdPrograma() {
        return this.idPrograma;
    }
    
    public void setIdPrograma(int idPrograma) {
        this.idPrograma = idPrograma;
    }

    
    @Column(name="id_tipo_programa", nullable=false)
    public int getIdTipoPrograma() {
        return this.idTipoPrograma;
    }
    
    public void setIdTipoPrograma(int idTipoPrograma) {
        this.idTipoPrograma = idTipoPrograma;
    }

    
    @Column(name="id_direccionamiento_auditoria", nullable=false)
    public int getIdDireccionamientoAuditoria() {
        return this.idDireccionamientoAuditoria;
    }
    
    public void setIdDireccionamientoAuditoria(int idDireccionamientoAuditoria) {
        this.idDireccionamientoAuditoria = idDireccionamientoAuditoria;
    }

    
    @Column(name="id_tipo_auditoria", nullable=false)
    public int getIdTipoAuditoria() {
        return this.idTipoAuditoria;
    }
    
    public void setIdTipoAuditoria(int idTipoAuditoria) {
        this.idTipoAuditoria = idTipoAuditoria;
    }

    
    @Column(name="id_origen_insumo", nullable=false)
    public int getIdOrigenInsumo() {
        return this.idOrigenInsumo;
    }
    
    public void setIdOrigenInsumo(int idOrigenInsumo) {
        this.idOrigenInsumo = idOrigenInsumo;
    }

    
    @Column(name="id_gerencia", nullable=false)
    public int getIdGerencia() {
        return this.idGerencia;
    }
    
    public void setIdGerencia(int idGerencia) {
        this.idGerencia = idGerencia;
    }

    
    @Column(name="impuesto", nullable=false, length=50)
    public String getImpuesto() {
        return this.impuesto;
    }
    
    public void setImpuesto(String impuesto) {
        this.impuesto = impuesto;
    }

    
    @Column(name="anio", nullable=false)
    public int getAnio() {
        return this.anio;
    }
    
    public void setAnio(int anio) {
        this.anio = anio;
    }

    
    @Column(name="correlativo", nullable=false)
    public int getCorrelativo() {
        return this.correlativo;
    }
    
    public void setCorrelativo(int correlativo) {
        this.correlativo = correlativo;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="periodo_inicio", nullable=false, length=22)
    public Date getPeriodoInicio() {
        return this.periodoInicio;
    }
    
    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="periodo_fin", nullable=false, length=22)
    public Date getPeriodoFin() {
        return this.periodoFin;
    }
    
    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }

    
    @Column(name="nombre", nullable=false, length=150)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
    @Column(name="descripcion", length=250)
    public String getDescripcion() {
        return this.descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    
    @Column(name="id_departamento")
    public Integer getIdDepartamento() {
        return this.idDepartamento;
    }
    
    public void setIdDepartamento(Integer idDepartamento) {
        this.idDepartamento = idDepartamento;
    }

    
    @Column(name="id_estado", nullable=false)
    public int getIdEstado() {
        return this.idEstado;
    }
    
    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    
    @Column(name="codificacion_programa", nullable=false, length=70)
    public String getCodificacionPrograma() {
        return this.codificacionPrograma;
    }
    
    public void setCodificacionPrograma(String codificacionPrograma) {
        this.codificacionPrograma = codificacionPrograma;
    }

    
    @Column(name="impuesto_nombres", nullable=false, length=150)
    public String getImpuestoNombres() {
        return this.impuestoNombres;
    }
    
    public void setImpuestoNombres(String impuestoNombres) {
        this.impuestoNombres = impuestoNombres;
    }

    
    @Column(name="usuario_modifica", nullable=false, length=20)
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }
    
    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    
    @Column(name="ip_modifica", nullable=false, length=40)
    public String getIpModifica() {
        return this.ipModifica;
    }
    
    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_modifica", nullable=false, length=22)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }
    
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    
    @Column(name="usuario_agrega")
    public String getUsuarioAgrega() {
        return this.usuarioAgrega;
    }
    
    public void setUsuarioAgrega(String usuarioAgrega) {
        this.usuarioAgrega = usuarioAgrega;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_creacion", nullable=false, length=29)
    public Date getFechaCreacion() {
        return this.fechaCreacion;
    }
    
    public void setFechaCreacion(Date fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    
    @Column(name="id_estado_anterior")
    public Integer getIdEstadoAnterior() {
        return this.idEstadoAnterior;
    }
    
    public void setIdEstadoAnterior(Integer idEstadoAnterior) {
        this.idEstadoAnterior = idEstadoAnterior;
    }

    
    @Column(name="id_proceso", length=150)
    public String getIdProceso() {
        return this.idProceso;
    }
    
    public void setIdProceso(String idProceso) {
        this.idProceso = idProceso;
    }




}

