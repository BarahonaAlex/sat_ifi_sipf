/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Builder;

/**
 *
 * @author crramosl
 */
@Entity
@Builder
@Table(name = "sipf_rubro_hallazgo",
        schema = "sat_ifi_sipf"
)
@IdClass(SipfRubroHallazgoId.class)
public class SipfRubroHallazgo implements Serializable {

    private Integer idRubro;
    private Integer idHallazgo;

    public SipfRubroHallazgo() {
    }

    public SipfRubroHallazgo(Integer idRubro, Integer idHallazgo) {
        this.idRubro = idRubro;
        this.idHallazgo = idHallazgo;
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
}
