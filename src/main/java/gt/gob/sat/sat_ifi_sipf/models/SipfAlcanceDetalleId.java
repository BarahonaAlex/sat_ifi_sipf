package gt.gob.sat.sat_ifi_sipf.models;
// Generated 21-sep-2022 12:12:38 by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * SipfAlcanceDetalleId generated by hbm2java
 */
@Embeddable
public class SipfAlcanceDetalleId  implements java.io.Serializable {


     private int idAlcance;
     private int idSeccion;

    public SipfAlcanceDetalleId() {
    }

    public SipfAlcanceDetalleId(int idAlcance, int idSeccion) {
       this.idAlcance = idAlcance;
       this.idSeccion = idSeccion;
    }
   


    @Column(name="id_alcance", nullable=false)
    public int getIdAlcance() {
        return this.idAlcance;
    }
    
    public void setIdAlcance(int idAlcance) {
        this.idAlcance = idAlcance;
    }


    @Column(name="id_seccion", nullable=false)
    public int getIdSeccion() {
        return this.idSeccion;
    }
    
    public void setIdSeccion(int idSeccion) {
        this.idSeccion = idSeccion;
    }


   public boolean equals(Object other) {
         if ( (this == other ) ) return true;
		 if ( (other == null ) ) return false;
		 if ( !(other instanceof SipfAlcanceDetalleId) ) return false;
		 SipfAlcanceDetalleId castOther = ( SipfAlcanceDetalleId ) other; 
         
		 return (this.getIdAlcance()==castOther.getIdAlcance())
 && (this.getIdSeccion()==castOther.getIdSeccion());
   }
   
   public int hashCode() {
         int result = 17;
         
         result = 37 * result + this.getIdAlcance();
         result = 37 * result + this.getIdSeccion();
         return result;
   }   


}


