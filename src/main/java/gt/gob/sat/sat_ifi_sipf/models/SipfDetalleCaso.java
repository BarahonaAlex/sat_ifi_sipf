package gt.gob.sat.sat_ifi_sipf.models;
// Generated 14/12/2022 01:51:50 PM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;

/**
 * SipfDetalleCaso generated by hbm2java
 */
@Entity
@Table(name = "sipf_detalle_caso",
         schema = "sat_ifi_sipf"
)
@Builder
public class SipfDetalleCaso  implements java.io.Serializable {

    private int idCaso;
    private int tipoCaso;
    private String loginProfesional;
    private String usuarioModifica;
    private Date fechaModifica;
    private String ipModifica;
    private Integer idAlcance;
    private Boolean requiereDocumentacion;

    public SipfDetalleCaso() {
    }

    public SipfDetalleCaso(int idCaso, int tipoCaso, String usuarioModifica, Date fechaModifica, String ipModifica) {
        this.idCaso = idCaso;
        this.tipoCaso = tipoCaso;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
    }

    public SipfDetalleCaso(int idCaso, int tipoCaso, String loginProfesional, String usuarioModifica, Date fechaModifica, String ipModifica, Integer idAlcance, Boolean requiereDocumentacion) {
        this.idCaso = idCaso;
        this.tipoCaso = tipoCaso;
        this.loginProfesional = loginProfesional;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
        this.idAlcance = idAlcance;
        this.requiereDocumentacion = requiereDocumentacion;
    }

    @Id

    @Column(name = "id_caso", unique = true, nullable = false)
    public int getIdCaso() {
        return this.idCaso;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    @Column(name = "tipo_caso", nullable = false)
    public int getTipoCaso() {
        return this.tipoCaso;
    }

    public void setTipoCaso(int tipoCaso) {
        this.tipoCaso = tipoCaso;
    }

    @Column(name = "login_profesional", length = 20)
    public String getLoginProfesional() {
        return this.loginProfesional;
    }

    public void setLoginProfesional(String loginProfesional) {
        this.loginProfesional = loginProfesional;
    }

    @Column(name = "usuario_modifica", nullable = false, length = 20)
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

    @Column(name = "ip_modifica", nullable = false, length = 40)
    public String getIpModifica() {
        return this.ipModifica;
    }

    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }

    @Column(name = "id_alcance")
    public Integer getIdAlcance() {
        return this.idAlcance;
    }

    public void setIdAlcance(Integer idAlcance) {
        this.idAlcance = idAlcance;
    }

    @Column(name = "requiere_documentacion")
    public Boolean getRequiereDocumentacion() {
        return this.requiereDocumentacion;
    }

    public void setRequiereDocumentacion(Boolean requiereDocumentacion) {
        this.requiereDocumentacion = requiereDocumentacion;
    }

}
