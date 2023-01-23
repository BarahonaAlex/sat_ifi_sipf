/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.util.Date;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author ajabarrer
 */
@Entity
@Table(name="sipf_comentario_historico_nombramiento_bitacora",schema="sat_ifi_sipf")
public class SipfComentarioHistoricoNombramientoBitacora implements java.io.Serializable {
    
     private int id;
     private String nombrameinto;
     private String expediente;
     private String comentario;
     private String usuarioModificacion;
     private Date fechaModificacion;
     private String ipModificacion;
     
    public SipfComentarioHistoricoNombramientoBitacora() {
    }

    public SipfComentarioHistoricoNombramientoBitacora(int id, String nombrameinto, String expediente, String comentario, String usuarioModificacion, Date fechaModificacion, String ipModificacion) {
        this.id = id;
        this.nombrameinto = nombrameinto;
        this.expediente = expediente;
        this.comentario = comentario;
        this.usuarioModificacion = usuarioModificacion;
        this.fechaModificacion = fechaModificacion;
        this.ipModificacion = ipModificacion;
    }
    
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id", unique=true, nullable=false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    @Column(name="nombrameinto")
    public String getNombrameinto() {
        return nombrameinto;
    }

    public void setNombrameinto(String nombrameinto) {
        this.nombrameinto = nombrameinto;
    }
    
    @Column(name="expediente")
    public String getExpediente() {
        return expediente;
    }

    public void setExpediente(String expediente) {
        this.expediente = expediente;
    }

     @Column(name="comentario")
    public String getComentario() {
        return this.comentario;
    }
    
    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    
    @Column(name="usuario_modificacion")
    public String getUsuarioModificacion() {
        return this.usuarioModificacion;
    }
    
    public void setUsuarioModificacion(String usuarioModificacion) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Temporal(TemporalType.DATE)
    @Column(name="fecha_modificacion", length=13)
    public Date getFechaModificacion() {
        return this.fechaModificacion;
    }
    
    public void setFechaModificacion(Date fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    
    @Column(name="ip_modificacion")
    public String getIpModificacion() {
        return this.ipModificacion;
    }
    
    public void setIpModificacion(String ipModificacion) {
        this.ipModificacion = ipModificacion;
    }   
   
    
}
