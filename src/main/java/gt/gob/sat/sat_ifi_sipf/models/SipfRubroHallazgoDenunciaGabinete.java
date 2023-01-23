/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Builder;

/**
 *
 * @author adftopvar
 */

@Entity
@Builder
@Table(name="sipf_rubro_hallazgo_denuncia_gabinete"
    ,schema="sat_ifi_sipf"
)
@IdClass(SipfRubroHallazgoDenunciaGabineteId.class)
public class SipfRubroHallazgoDenunciaGabinete implements Serializable{
    
    private Integer idRubro;
    private Integer idHallazgo;
    private String usuarioModifica;
    private Date fechaModifica;
    private String ipModifica;

    public SipfRubroHallazgoDenunciaGabinete() {
    }

    public SipfRubroHallazgoDenunciaGabinete(Integer idRubro, Integer idHallazgo) {
        this.idRubro = idRubro;
        this.idHallazgo = idHallazgo;
    }

    public SipfRubroHallazgoDenunciaGabinete(Integer idRubro, Integer idHallazgo, String usuarioModifica, Date fechaModifica, String ipModifica) {
        this.idRubro = idRubro;
        this.idHallazgo = idHallazgo;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
    }

    @Id
    @Column(name = "id_rubro", nullable = false)
    public Integer getIdRubro() {
        return idRubro;
    }

    public void setIdRubro(Integer idRubro) {
        this.idRubro = idRubro;
    }

    @Id
    @Column(name = "id_hallazgo", nullable = false)
    public Integer getIdHallazgo() {
        return idHallazgo;
    }

    public void setIdHallazgo(Integer idHallazgo) {
        this.idHallazgo = idHallazgo;
    }

    @Column(name = "usuario_modifica", nullable = false)
    public String getUsuarioModifica() {
        return usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Column(name = "fecha_modifica", nullable = false)
    public Date getFechaModifica() {
        return fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    @Column(name = "ip_modifica", nullable = false)
    public String getIpModifica() {
        return ipModifica;
    }

    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }
    
    
    
    
    
}
