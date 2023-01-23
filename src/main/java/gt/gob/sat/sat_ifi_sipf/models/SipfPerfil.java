package gt.gob.sat.sat_ifi_sipf.models;
// Generated 18/03/2022 04:56:30 PM by Hibernate Tools 4.3.1

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SipfPerfil generated by hbm2java
 */
@Entity
@Table(name = "sipf_perfil",
         schema = "sat_ifi_sipf"
)
public class SipfPerfil implements java.io.Serializable {

    private int idPerfil;
    private String nombre;
    private int idRol;
    private int estado;

    public SipfPerfil() {
    }

    public SipfPerfil(int idPerfil, String nombre, int idRol, int estado) {
        this.idPerfil = idPerfil;
        this.nombre = nombre;
        this.idRol = idRol;
        this.estado = estado;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_perfil", unique = true, nullable = false)
    public int getIdPerfil() {
        return this.idPerfil;
    }

    public void setIdPerfil(int idPerfil) {
        this.idPerfil = idPerfil;
    }

    @Column(name = "nombre", nullable = false, length = 200)
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "id_rol", nullable = false)
    public int getIdRol() {
        return this.idRol;
    }

    public void setIdRol(int idRol) {
        this.idRol = idRol;
    }

    @Column(name = "estado", nullable = false)
    public int getEstado() {
        return this.estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }
}