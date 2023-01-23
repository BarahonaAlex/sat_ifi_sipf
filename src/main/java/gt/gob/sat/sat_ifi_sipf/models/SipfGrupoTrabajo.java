package gt.gob.sat.sat_ifi_sipf.models;
// Generated 1/02/2022 09:29:14 AM by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Builder;

/**
 * SipfGrupoTrabajo generated by hbm2java
 */
@Entity
@Builder
@Table(name = "sipf_grupo_trabajo",
        schema = "sat_ifi_sipf"
)
public class SipfGrupoTrabajo implements java.io.Serializable {

    private long id;
    private String nombre;
    private String descripcion;
    private String usuarioModifica;
    private Date fechaModifica;
    private String ipModifica;
    private int idEstado;
    private Integer idGerencia;
    
    public SipfGrupoTrabajo() {
    }

    public SipfGrupoTrabajo(long id, String nombre, String usuarioModifica, Date fechaModifica, String ipModifica, int idEstado, Integer idGerencia) {
        this.id = id;
        this.nombre = nombre;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
        this.idEstado = idEstado;
        this.idGerencia = idGerencia;
    }

    public SipfGrupoTrabajo(long id, String nombre, String descripcion, String usuarioModifica, Date fechaModifica, String ipModifica, int idEstado, Integer idGerencia) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
        this.idEstado = idEstado;
        this.idGerencia = idGerencia;
    }

    
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @Column(name = "nombre", nullable = false, length = 200)
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "descripcion", length = 400)
    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "usuario_modifica", nullable = false, length = 20)
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modifica", nullable = false, length = 29)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    @Column(name = "ip_modifica", nullable = false, length = 15)
    public String getIpModifica() {
        return this.ipModifica;
    }

    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }

    @Column(name = "id_estado", nullable = false)
    public int getIdEstado() {
        return this.idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }
    
    @Column(name = "id_gerencia")
    public Integer getIdGerencia() {
        return idGerencia;
    }

    public void setIdGerencia(Integer idGerencia) {
        this.idGerencia = idGerencia;
    }  
    
}