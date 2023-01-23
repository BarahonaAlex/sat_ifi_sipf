package gt.gob.sat.sat_ifi_sipf.models;
// Generated 1/02/2022 09:22:58 AM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;

/**
 * SipfAsignacionCasos generated by hbm2java
 */
@Entity
@Builder
@Table(name = "sipf_asignacion_casos",
        schema = "sat_ifi_sipf"
)
public class SipfAsignacionCasos implements java.io.Serializable {

    private SipfAsignacionCasosId id;
    private Date fechaModifica;
    private int idEstado;
    private int idGrupoAsignacion;
    private int idOrigen;
    private String ipModifica;
    private String usuarioModifica;

    public SipfAsignacionCasos() {
    }

    public SipfAsignacionCasos(SipfAsignacionCasosId id, Date fechaModifica, int idEstado, int idGrupoAsignacion, int idOrigen, String ipModifica, String usuarioModifica) {
        this.id = id;
        this.fechaModifica = fechaModifica;
        this.idEstado = idEstado;
        this.idGrupoAsignacion = idGrupoAsignacion;
        this.idOrigen = idOrigen;
        this.ipModifica = ipModifica;
        this.usuarioModifica = usuarioModifica;
    }

    @EmbeddedId

    @AttributeOverrides({
        @AttributeOverride(name = "idCaso", column = @Column(name = "id_caso", nullable = false))
        , 
        @AttributeOverride(name = "nitColaborador", column = @Column(name = "nit_colaborador", nullable = false, length = 16))})
    public SipfAsignacionCasosId getId() {
        return this.id;
    }

    public void setId(SipfAsignacionCasosId id) {
        this.id = id;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modifica", nullable = false, length = 22)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    @Column(name = "id_estado", nullable = false)
    public int getIdEstado() {
        return this.idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    @Column(name = "id_grupo_asignacion", nullable = false)
    public int getIdGrupoAsignacion() {
        return this.idGrupoAsignacion;
    }

    public void setIdGrupoAsignacion(int idGrupoAsignacion) {
        this.idGrupoAsignacion = idGrupoAsignacion;
    }

    @Column(name = "id_origen", nullable = false)
    public int getIdOrigen() {
        return this.idOrigen;
    }

    public void setIdOrigen(int idOrigen) {
        this.idOrigen = idOrigen;
    }

    @Column(name = "ip_modifica", nullable = false, length = 40)
    public String getIpModifica() {
        return this.ipModifica;
    }

    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }

    @Column(name = "usuario_modifica", nullable = false, length = 20)
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Override
    public String toString() {
        return "SipfAsignacionCasos{" + "id=" + id + ", fechaModifica=" + fechaModifica + ", idEstado=" + idEstado + ", idGrupoAsignacion=" + idGrupoAsignacion + ", idOrigen=" + idOrigen + ", ipModifica=" + ipModifica + ", usuarioModifica=" + usuarioModifica + '}';
    }

}
