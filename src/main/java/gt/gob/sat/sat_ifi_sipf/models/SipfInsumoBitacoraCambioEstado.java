package gt.gob.sat.sat_ifi_sipf.models;
// Generated 13/12/2022 03:47:32 PM by Hibernate Tools 4.3.1

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SipfInsumoBitacoraCambioEstado generated by hbm2java
 */
@Entity
@Table(name = "sipf_insumo_bitacora_cambio_estado",
        schema = "sat_ifi_sipf"
)
public class SipfInsumoBitacoraCambioEstado implements java.io.Serializable {

    @JsonProperty(value = "id")
    private int id;
    @JsonProperty(value = "idInsumo")
    private int idInsumo;
    @JsonProperty(value = "idEstadoAnterior")
    private Integer idEstadoAnterior;
    @JsonProperty(value = "idEstadoNuevo")
    private int idEstadoNuevo;
    @JsonProperty(value = "usuarioModifica")
    private String usuarioModifica;
    @JsonProperty(value = "fechaModifica")
    private Date fechaModifica;

    public SipfInsumoBitacoraCambioEstado() {
    }

    public SipfInsumoBitacoraCambioEstado(int id, int idInsumo, int idEstadoNuevo, String usuarioModifica, Date fechaModifica) {
        this.id = id;
        this.idInsumo = idInsumo;
        this.idEstadoNuevo = idEstadoNuevo;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
    }

    public SipfInsumoBitacoraCambioEstado(int id, int idInsumo, Integer idEstadoAnterior, int idEstadoNuevo, String usuarioModifica, Date fechaModifica) {
        this.id = id;
        this.idInsumo = idInsumo;
        this.idEstadoAnterior = idEstadoAnterior;
        this.idEstadoNuevo = idEstadoNuevo;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "id_insumo", nullable = false)
    public int getIdInsumo() {
        return this.idInsumo;
    }

    public void setIdInsumo(int idInsumo) {
        this.idInsumo = idInsumo;
    }

    @Column(name = "id_estado_anterior")
    public Integer getIdEstadoAnterior() {
        return this.idEstadoAnterior;
    }

    public void setIdEstadoAnterior(Integer idEstadoAnterior) {
        this.idEstadoAnterior = idEstadoAnterior;
    }

    @Column(name = "id_estado_nuevo", nullable = false)
    public int getIdEstadoNuevo() {
        return this.idEstadoNuevo;
    }

    public void setIdEstadoNuevo(int idEstadoNuevo) {
        this.idEstadoNuevo = idEstadoNuevo;
    }

    @Column(name = "usuario_modifica", nullable = false)
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modifica", nullable = false, length = 29)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

}
