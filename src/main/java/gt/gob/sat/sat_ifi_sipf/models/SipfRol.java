package gt.gob.sat.sat_ifi_sipf.models;
// Generated 18/03/2022 03:10:39 PM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SipfRol generated by hbm2java
 */
@Entity
@Table(name="sipf_rol"
    ,schema="sat_ifi_sipf"
)
public class SipfRol  implements java.io.Serializable {


     private int idRol;
     private String nombre;

    public SipfRol() {
    }

    public SipfRol(int idRol, String nombre) {
       this.idRol = idRol;
       this.nombre = nombre;
    }
   
     @Id 

    
    @Column(name="id_rol", unique=true, nullable=false)
    public int getIdRol() {
        return this.idRol;
    }
    
    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    
    @Column(name="nombre", nullable=false, length=200)
    public String getNombre() {
        return this.nombre;
    }
    
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }




}


