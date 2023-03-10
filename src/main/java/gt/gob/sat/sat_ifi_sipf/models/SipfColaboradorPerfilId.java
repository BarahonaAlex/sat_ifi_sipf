package gt.gob.sat.sat_ifi_sipf.models;
// Generated 18/03/2022 05:35:48 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfColaboradorPerfilId generated by hbm2java
 */
@Embeddable
public class SipfColaboradorPerfilId  implements java.io.Serializable {


     private String nit;
     private int idPerfil;

    public SipfColaboradorPerfilId() {
    }

    public SipfColaboradorPerfilId(String nit, int idPerfil) {
       this.nit = nit;
       this.idPerfil = idPerfil;
    }
   


    @Column(name="nit", nullable=false, length=16)
    public String getNit() {
        return this.nit;
    }
    
    public void setNit(String nit) {
        this.nit = nit;
    }


    @Column(name="id_perfil", nullable=false)
    public int getIdPerfil() {
        return this.idPerfil;
    }
    
    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SipfColaboradorPerfilId) ) return false;
		 SipfColaboradorPerfilId castOther = ( SipfColaboradorPerfilId ) other; 
         
		 return ( (this.getNit()==castOther.getNit()) || ( this.getNit()!=null && castOther.getNit()!=null && this.getNit().equals(castOther.getNit()) ) )
 && (this.getIdPerfil()==castOther.getIdPerfil());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getNit() == null ? 0 : this.getNit().hashCode() );
         result = 37 * result + this.getIdPerfil();
         return result;
   }   


}


