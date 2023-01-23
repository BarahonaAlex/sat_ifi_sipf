package gt.gob.sat.sat_ifi_sipf.models;
// Generated Sep 6, 2022 6:02:57 PM by Hibernate Tools 4.3.1


import java.math.BigDecimal;
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
import org.hibernate.annotations.GeneratorType;
import org.hibernate.tuple.ValueGenerator;

/**
 * SipfSolicitudCreditoFiscal generated by hbm2java
 */
@Builder
@Entity
@Table(name="sipf_solicitud_credito_fiscal"
    ,schema="sat_ifi_sipf"
)
public class SipfSolicitudCreditoFiscal  implements java.io.Serializable {


     private int idSolicitud;
     private String numeroFormulario;
     private String nitContribuyente;
     private Date periodoInicio;
     private Date periodoFin;
     private String actividadEconomica;
     private String principalProducto;
     private String formularioIva;
     private String creditoSujetoDevolucion;
     private Integer monto;
     private Integer creditoNoSolicitado;
     private Integer montoDevolucion;
     private Integer multa;
     private Integer total;
     private String asignado;
     private Integer estado;
     private String usuarioModifica;
     private Date fechaModifica;
     private String ipModifica;
     private String documentosRespaldo;
     private String direccionNotifica;
     private String correoNotifica;
     private BigDecimal numeroSolicitud;
     private String nitRepresentante;

    public SipfSolicitudCreditoFiscal() {
    }

	
    public SipfSolicitudCreditoFiscal(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    public SipfSolicitudCreditoFiscal(int idSolicitud, String numeroFormulario, String nitContribuyente, Date periodoInicio, Date periodoFin, String actividadEconomica, String principalProducto, String formularioIva, String creditoSujetoDevolucion, Integer monto, Integer creditoNoSolicitado, Integer montoDevolucion, Integer multa, Integer total, String asignado, Integer estado, String usuarioModifica, Date fechaModifica, String ipModifica, String documentosRespaldo, String direccionNotifica, String correoNotifica, BigDecimal numeroSolicitud, String nitRepresentante) {
        this.idSolicitud = idSolicitud;
        this.numeroFormulario = numeroFormulario;
        this.nitContribuyente = nitContribuyente;
        this.periodoInicio = periodoInicio;
        this.periodoFin = periodoFin;
        this.actividadEconomica = actividadEconomica;
        this.principalProducto = principalProducto;
        this.formularioIva = formularioIva;
        this.creditoSujetoDevolucion = creditoSujetoDevolucion;
        this.monto = monto;
        this.creditoNoSolicitado = creditoNoSolicitado;
        this.montoDevolucion = montoDevolucion;
        this.multa = multa;
        this.total = total;
        this.asignado = asignado;
        this.estado = estado;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
        this.documentosRespaldo = documentosRespaldo;
        this.direccionNotifica = direccionNotifica;
        this.correoNotifica = correoNotifica;
        this.numeroSolicitud = numeroSolicitud;
        this.nitRepresentante = nitRepresentante;
    }

    
   
    
   
     @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solicitud", unique=true, nullable=false)
    public int getIdSolicitud() {
        return this.idSolicitud;
    }
    
    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    
    @Column(name="numero_formulario")
    public String getNumeroFormulario() {
        return this.numeroFormulario;
    }
    
    public void setNumeroFormulario(String numeroFormulario) {
        this.numeroFormulario = numeroFormulario;
    }

    
    @Column(name="nit_contribuyente")
    public String getNitContribuyente() {
        return this.nitContribuyente;
    }
    
    public void setNitContribuyente(String nitContribuyente) {
        this.nitContribuyente = nitContribuyente;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="periodo_inicio", length=13)
    public Date getPeriodoInicio() {
        return this.periodoInicio;
    }
    
    public void setPeriodoInicio(Date periodoInicio) {
        this.periodoInicio = periodoInicio;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="periodo_fin", length=13)
    public Date getPeriodoFin() {
        return this.periodoFin;
    }
    
    public void setPeriodoFin(Date periodoFin) {
        this.periodoFin = periodoFin;
    }

    
    @Column(name="actividad_economica")
    public String getActividadEconomica() {
        return this.actividadEconomica;
    }
    
    public void setActividadEconomica(String actividadEconomica) {
        this.actividadEconomica = actividadEconomica;
    }

    
    @Column(name="principal_producto")
    public String getPrincipalProducto() {
        return this.principalProducto;
    }
    
    public void setPrincipalProducto(String principalProducto) {
        this.principalProducto = principalProducto;
    }

    
    @Column(name="formulario_iva")
    public String getFormularioIva() {
        return this.formularioIva;
    }
    
    public void setFormularioIva(String formularioIva) {
        this.formularioIva = formularioIva;
    }

    
    @Column(name="credito_sujeto_devolucion")
    public String getCreditoSujetoDevolucion() {
        return this.creditoSujetoDevolucion;
    }
    
    public void setCreditoSujetoDevolucion(String creditoSujetoDevolucion) {
        this.creditoSujetoDevolucion = creditoSujetoDevolucion;
    }

    
    @Column(name="monto")
    public Integer getMonto() {
        return this.monto;
    }
    
    public void setMonto(Integer monto) {
        this.monto = monto;
    }

    
    @Column(name="credito_no_solicitado")
    public Integer getCreditoNoSolicitado() {
        return this.creditoNoSolicitado;
    }
    
    public void setCreditoNoSolicitado(Integer creditoNoSolicitado) {
        this.creditoNoSolicitado = creditoNoSolicitado;
    }

    
    @Column(name="monto_devolucion")
    public Integer getMontoDevolucion() {
        return this.montoDevolucion;
    }
    
    public void setMontoDevolucion(Integer montoDevolucion) {
        this.montoDevolucion = montoDevolucion;
    }

    
    @Column(name="multa")
    public Integer getMulta() {
        return this.multa;
    }
    
    public void setMulta(Integer multa) {
        this.multa = multa;
    }

    
    @Column(name="total")
    public Integer getTotal() {
        return this.total;
    }
    
    public void setTotal(Integer total) {
        this.total = total;
    }

    
    @Column(name="asignado")
    public String getAsignado() {
        return this.asignado;
    }
    
    public void setAsignado(String asignado) {
        this.asignado = asignado;
    }

    
    @Column(name="estado")
    public Integer getEstado() {
        return this.estado;
    }
    
    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    
    @Column(name="usuario_modifica")
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }
    
    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_modifica", length=29)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }
    
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    
    @Column(name="ip_modifica")
    public String getIpModifica() {
        return this.ipModifica;
    }
    
    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }
    
    @Column(name="documentos_respaldo")
    public String getDocumentosRespaldo() {
        return this.documentosRespaldo;
    }

    public void setDocumentosRespaldo(String documentosRespaldo) {
        this.documentosRespaldo = documentosRespaldo;
    }

    
    @Column(name="direccion_notifica")
    public String getDireccionNotifica() {
        return this.direccionNotifica;
    }
    public void setDireccionNotifica(String direccionNotifica) {
        this.direccionNotifica = direccionNotifica;
    }

    @Column(name="correo_notifica")
    public String getCorreoNotifica() {
        return this.correoNotifica;
    }
    public void setCorreoNotifica(String correoNotifica) {
        this.correoNotifica = correoNotifica;
    }

    @Column(name="numero_solicitud", precision=32, scale=0)
    public BigDecimal getNumeroSolicitud() {
        return this.numeroSolicitud;
    }
    public void setNumeroSolicitud(BigDecimal numeroSolicitud) {
        this.numeroSolicitud = numeroSolicitud;
    }
    
    @Column(name="nit_representante")
    public String getNitRepresentante() {
        return nitRepresentante;
    }

    public void setNitRepresentante(String nitRepresentante) {
        this.nitRepresentante = nitRepresentante;
    }
    
}


