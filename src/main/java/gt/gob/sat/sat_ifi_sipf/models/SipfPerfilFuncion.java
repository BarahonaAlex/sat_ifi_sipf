package gt.gob.sat.sat_ifi_sipf.models;
// Generated 21/03/2022 09:20:38 AM by Hibernate Tools 4.3.1


import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * SipfPerfilFuncion generated by hbm2java
 */
@Entity
@Table(name="sipf_perfil_funcion"
    ,schema="sat_ifi_sipf"
)
public class SipfPerfilFuncion  implements java.io.Serializable {


     private SipfPerfilFuncionId id;

    public SipfPerfilFuncion() {
    }

    public SipfPerfilFuncion(SipfPerfilFuncionId id) {
       this.id = id;
    }
   
     @EmbeddedId

    
    @AttributeOverrides( {
        @AttributeOverride(name="idPerfil", column=@Column(name="id_perfil", nullable=false) ), 
        @AttributeOverride(name="idFuncion", column=@Column(name="id_funcion", nullable=false) ) } )
    public SipfPerfilFuncionId getId() {
        return this.id;
    }
    
    public void setId(SipfPerfilFuncionId id) {
        this.id = id;
    }




}


