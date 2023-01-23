package gt.gob.sat.sat_ifi_sipf.models;
// Generated 16/06/2022 06:51:22 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfComentarioHistoricoNombramientoId generated by hbm2java
 */
@Embeddable
public class SipfComentarioHistoricoNombramientoId  implements java.io.Serializable {


     private String nombrameinto;
     private String expediente;

    public SipfComentarioHistoricoNombramientoId() {
    }

    public SipfComentarioHistoricoNombramientoId(String nombrameinto, String expediente) {
       this.nombrameinto = nombrameinto;
       this.expediente = expediente;
    }
   


    @Column(name="nombrameinto", nullable=false)
    public String getNombrameinto() {
        return this.nombrameinto;
    }
    
    public void setNombrameinto(String nombrameinto) {
        this.nombrameinto = nombrameinto;
    }


    @Column(name="expediente", nullable=false)
    public String getExpediente() {
        return this.expediente;
    }
    
    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SipfComentarioHistoricoNombramientoId) ) return false;
		 SipfComentarioHistoricoNombramientoId castOther = ( SipfComentarioHistoricoNombramientoId ) other; 
         
		 return ( (this.getNombrameinto()==castOther.getNombrameinto()) || ( this.getNombrameinto()!=null && castOther.getNombrameinto()!=null && this.getNombrameinto().equals(castOther.getNombrameinto()) ) )
 && ( (this.getExpediente()==castOther.getExpediente()) || ( this.getExpediente()!=null && castOther.getExpediente()!=null && this.getExpediente().equals(castOther.getExpediente()) ) );
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + ( getNombrameinto() == null ? 0 : this.getNombrameinto().hashCode() );
         result = 37 * result + ( getExpediente() == null ? 0 : this.getExpediente().hashCode() );
         return result;
   }   


}


