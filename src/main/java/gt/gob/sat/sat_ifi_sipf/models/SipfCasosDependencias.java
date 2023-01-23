package gt.gob.sat.sat_ifi_sipf.models;
// Generated 04-feb-2022 10:37:19 by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;

/**
 * SipfCasosDependencias generated by hbm2java
 */
@Entity
@Builder
@Table(name = "sipf_casos_dependencias",
        schema = "sat_ifi_sipf"
)
public class SipfCasosDependencias implements java.io.Serializable {

    private int idCaso;
    private Integer idEntidadSolicitante;
    private Integer idTipoSolicitud;
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

    public SipfCasosDependencias() {
    }

    public SipfCasosDependencias(int idCaso) {
        this.idCaso = idCaso;
    }

    public SipfCasosDependencias(int idCaso, Integer idEntidadSolicitante, Integer idTipoSolicitud, String nombreCuentaAuditar, String nitContribuyenteCruce, String numeroFacturaCruce, String serieFacturaCruce, Date fechaFacturaCruce, Float montoFacturaCruce, Date fechaSolicitud, Date fechaDocumento, Integer numeroDocumento, Date plazoEntrega, String nombreContacto, String telefonoContacto, String correoContacto, String detalleEntidadSolicitante) {
        this.idCaso = idCaso;
        this.idEntidadSolicitante = idEntidadSolicitante;
        this.idTipoSolicitud = idTipoSolicitud;
        this.nombreCuentaAuditar = nombreCuentaAuditar;
        this.nitContribuyenteCruce = nitContribuyenteCruce;
        this.numeroFacturaCruce = numeroFacturaCruce;
        this.serieFacturaCruce = serieFacturaCruce;
        this.fechaFacturaCruce = fechaFacturaCruce;
        this.montoFacturaCruce = montoFacturaCruce;
        this.fechaSolicitud = fechaSolicitud;
        this.fechaDocumento = fechaDocumento;
        this.numeroDocumento = numeroDocumento;
        this.plazoEntrega = plazoEntrega;
        this.nombreContacto = nombreContacto;
        this.telefonoContacto = telefonoContacto;
        this.correoContacto = correoContacto;
        this.detalleEntidadSolicitante = detalleEntidadSolicitante;
    }

    @Id

    @Column(name = "id_caso", unique = true, nullable = false)
    public int getIdCaso() {
        return this.idCaso;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    @Column(name = "id_entidad_solicitante")
    public Integer getIdEntidadSolicitante() {
        return this.idEntidadSolicitante;
    }

    public void setIdEntidadSolicitante(Integer idEntidadSolicitante) {
        this.idEntidadSolicitante = idEntidadSolicitante;
    }

    @Column(name = "id_tipo_solicitud")
    public Integer getIdTipoSolicitud() {
        return this.idTipoSolicitud;
    }

    public void setIdTipoSolicitud(Integer idTipoSolicitud) {
        this.idTipoSolicitud = idTipoSolicitud;
    }

    @Column(name = "nombre_cuenta_auditar", length = 150)
    public String getNombreCuentaAuditar() {
        return this.nombreCuentaAuditar;
    }

    public void setNombreCuentaAuditar(String nombreCuentaAuditar) {
        this.nombreCuentaAuditar = nombreCuentaAuditar;
    }

    @Column(name = "nit_contribuyente_cruce", length = 16)
    public String getNitContribuyenteCruce() {
        return this.nitContribuyenteCruce;
    }

    public void setNitContribuyenteCruce(String nitContribuyenteCruce) {
        this.nitContribuyenteCruce = nitContribuyenteCruce;
    }

    @Column(name = "numero_factura_cruce", length = 15)
    public String getNumeroFacturaCruce() {
        return this.numeroFacturaCruce;
    }

    public void setNumeroFacturaCruce(String numeroFacturaCruce) {
        this.numeroFacturaCruce = numeroFacturaCruce;
    }

    @Column(name = "serie_factura_cruce", length = 15)
    public String getSerieFacturaCruce() {
        return this.serieFacturaCruce;
    }

    public void setSerieFacturaCruce(String serieFacturaCruce) {
        this.serieFacturaCruce = serieFacturaCruce;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_factura_cruce", length = 22)
    public Date getFechaFacturaCruce() {
        return this.fechaFacturaCruce;
    }

    public void setFechaFacturaCruce(Date fechaFacturaCruce) {
        this.fechaFacturaCruce = fechaFacturaCruce;
    }

    @Column(name = "monto_factura_cruce", precision = 8, scale = 8)
    public Float getMontoFacturaCruce() {
        return this.montoFacturaCruce;
    }

    public void setMontoFacturaCruce(Float montoFacturaCruce) {
        this.montoFacturaCruce = montoFacturaCruce;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_solicitud", length = 22)
    public Date getFechaSolicitud() {
        return this.fechaSolicitud;
    }

    public void setFechaSolicitud(Date fechaSolicitud) {
        this.fechaSolicitud = fechaSolicitud;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_documento", length = 22)
    public Date getFechaDocumento() {
        return this.fechaDocumento;
    }

    public void setFechaDocumento(Date fechaDocumento) {
        this.fechaDocumento = fechaDocumento;
    }

    @Column(name = "numero_documento")
    public Integer getNumeroDocumento() {
        return this.numeroDocumento;
    }

    public void setNumeroDocumento(Integer numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "plazo_entrega", length = 22)
    public Date getPlazoEntrega() {
        return this.plazoEntrega;
    }

    public void setPlazoEntrega(Date plazoEntrega) {
        this.plazoEntrega = plazoEntrega;
    }

    @Column(name = "nombre_contacto", length = 200)
    public String getNombreContacto() {
        return this.nombreContacto;
    }

    public void setNombreContacto(String nombreContacto) {
        this.nombreContacto = nombreContacto;
    }

    @Column(name = "telefono_contacto", length = 10)
    public String getTelefonoContacto() {
        return this.telefonoContacto;
    }

    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    @Column(name = "correo_contacto", length = 100)
    public String getCorreoContacto() {
        return this.correoContacto;
    }

    public void setCorreoContacto(String correoContacto) {
        this.correoContacto = correoContacto;
    }

    @Column(name = "detalle_entidad_solicitante", length = 100)
    public String getDetalleEntidadSolicitante() {
        return this.detalleEntidadSolicitante;
    }

    public void setDetalleEntidadSolicitante(String detalleEntidadSolicitante) {
        this.detalleEntidadSolicitante = detalleEntidadSolicitante;
    }

}
