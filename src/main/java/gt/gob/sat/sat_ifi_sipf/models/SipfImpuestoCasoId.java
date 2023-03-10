package gt.gob.sat.sat_ifi_sipf.models;
// Generated Sep 19, 2022 10:43:06 AM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfImpuestoCasoId generated by hbm2java
 */
@Embeddable
public class SipfImpuestoCasoId  implements java.io.Serializable {


     private int idCaso;
     private int idImpuesto;

    public SipfImpuestoCasoId() {
    }

    public SipfImpuestoCasoId(int idCaso, int idImpuesto) {
       this.idCaso = idCaso;
       this.idImpuesto = idImpuesto;
    }
   


    @Column(name="id_caso", nullable=false)
    public int getIdCaso() {
        return this.idCaso;
    }
    
    public void setIdCaso(int idCaso) {
        this.idCaso = idCaso;
    }


    @Column(name="id_impuesto", nullable=false)
    public int getIdImpuesto() {
        return this.idImpuesto;
    }
    
    public void setIdImpuesto(int idImpuesto) {
        this.idImpuesto = idImpuesto;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SipfImpuestoCasoId) ) return false;
		 SipfImpuestoCasoId castOther = ( SipfImpuestoCasoId ) other; 
         
		 return (this.getIdCaso()==castOther.getIdCaso())
 && (this.getIdImpuesto()==castOther.getIdImpuesto());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdCaso();
         result = 37 * result + this.getIdImpuesto();
         return result;
   }   


}


