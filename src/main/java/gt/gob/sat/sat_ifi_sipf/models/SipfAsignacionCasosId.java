package gt.gob.sat.sat_ifi_sipf.models;
// Generated 1/02/2022 09:22:58 AM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfAsignacionCasosId generated by hbm2java
 */
@Embeddable
public class SipfAsignacionCasosId implements java.io.Serializable {

    private int idCaso;
    private String nitColaborador;

    public SipfAsignacionCasosId() {
    }

    public SipfAsignacionCasosId(int idCaso, String nitColaborador) {
        this.idCaso = idCaso;
        this.nitColaborador = nitColaborador;
    }

    @Column(name = "id_caso", nullable = false)
    public int getIdCaso() {
        return this.idCaso;
    }

    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }

    @Column(name = "nit_colaborador", nullable = false, length = 16)
    public String getNitColaborador() {
        return this.nitColaborador;
    }

    public void setNitColaborador(String nitColaborador) {
        this.nitColaborador = nitColaborador;
    }
     @Override
    public String toString() {
    return "nitColaborador:"+getNitColaborador()+", id_caso:"+getIdCaso();
          
    }
}
