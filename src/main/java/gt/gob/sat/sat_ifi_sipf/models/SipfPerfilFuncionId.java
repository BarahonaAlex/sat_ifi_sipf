package gt.gob.sat.sat_ifi_sipf.models;
// Generated 21/03/2022 09:20:38 AM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfPerfilFuncionId generated by hbm2java
 */
@Embeddable
public class SipfPerfilFuncionId  implements java.io.Serializable {


     private int idPerfil;
     private int idFuncion;

    public SipfPerfilFuncionId() {
    }

    public SipfPerfilFuncionId(int idPerfil, int idFuncion) {
       this.idPerfil = idPerfil;
       this.idFuncion = idFuncion;
    }
   


    @Column(name="id_perfil", nullable=false)
    public int getIdPerfil() {
        return this.idPerfil;
    }
    
    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }


    @Column(name="id_funcion", nullable=false)
    public int getIdFuncion() {
        return this.idFuncion;
    }
    
    public void setIdFuncion(int idFuncion) {
        this.idFuncion = idFuncion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SipfPerfilFuncionId) ) return false;
		 SipfPerfilFuncionId castOther = ( SipfPerfilFuncionId ) other; 
         
		 return (this.getIdPerfil()==castOther.getIdPerfil())
 && (this.getIdFuncion()==castOther.getIdFuncion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdPerfil();
         result = 37 * result + this.getIdFuncion();
         return result;
   }   


}


