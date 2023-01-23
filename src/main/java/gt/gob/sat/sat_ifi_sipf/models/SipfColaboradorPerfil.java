package gt.gob.sat.sat_ifi_sipf.models;
// Generated 18/03/2022 05:35:48 PM by Hibernate Tools 4.3.1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SipfColaboradorPerfil generated by hbm2java
 */
@Entity
@Table(name="sipf_colaborador_perfil"
    ,schema="sat_ifi_sipf"
)
public class SipfColaboradorPerfil  implements java.io.Serializable {


     private SipfColaboradorPerfilId id;
     private int estado;

    public SipfColaboradorPerfil() {
    }

    public SipfColaboradorPerfil(SipfColaboradorPerfilId id, int estado) {
       this.id = id;
       this.estado = estado;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="nit", column=@Column(name="nit", nullable=false, length=16) ), 
        @AttributeOverride(name="idPerfil", column=@Column(name="id_perfil", nullable=false) ) } )
    public SipfColaboradorPerfilId getId() {
        return this.id;
    }
    
    public void setId(SipfColaboradorPerfilId id) {
        this.id = id;
    }

    
    @Column(name="estado", nullable=false)
    public int getEstado() {
        return this.estado;
    }
    
    public void setEstado(int estado) {
        this.estado = estado;
    }




}


