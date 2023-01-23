package gt.gob.sat.sat_ifi_sipf.models;
// Generated May 24, 2022 11:12:06 AM by Hibernate Tools 4.3.1


import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SipfTrasladoColaborador generated by hbm2java
 */
@Entity
@Table(name="sipf_traslado_colaborador"
    ,schema="sat_ifi_sipf"
)
public class SipfTrasladoColaborador  implements java.io.Serializable {


    private int idSolicitud;
    private String nitProfesional;
    private String idAutorizadorSolicitante;
    private long idGrupoAnterior;
    private String idAutorizadorAcepta;
    private long idGrupoNuevo;
    private long idEstado;
    private Date fechaEfectivaTraslado;
    private Date fechaNotificacionTraslado;
    private String motivo;
    private String usuarioModifica;
    private Date fechaModifica;
    private String ipModifica;
    private long idTipoTraslado;
    private Date fechaEfectivaRetorno;

    public SipfTrasladoColaborador() {
    }

	
    public SipfTrasladoColaborador(int idSolicitud, String nitProfesional, String idAutorizadorSolicitante, long idGrupoAnterior, String idAutorizadorAcepta, long idGrupoNuevo, long idEstado, String usuarioModifica, Date fechaModifica, String ipModifica) {
        this.idSolicitud = idSolicitud;
        this.nitProfesional = nitProfesional;
        this.idAutorizadorSolicitante = idAutorizadorSolicitante;
        this.idGrupoAnterior = idGrupoAnterior;
        this.idAutorizadorAcepta = idAutorizadorAcepta;
        this.idGrupoNuevo = idGrupoNuevo;
        this.idEstado = idEstado;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.ipModifica = ipModifica;
    }
    public SipfTrasladoColaborador(int idSolicitud, String nitProfesional, String idAutorizadorSolicitante, long idGrupoAnterior, String idAutorizadorAcepta, long idGrupoNuevo, long idEstado, Date fechaEfectivaTraslado, Date fechaNotificacionTraslado, String motivo, String usuarioModifica, Date fechaModifica, String ipModifica) {
       this.idSolicitud = idSolicitud;
       this.nitProfesional = nitProfesional;
       this.idAutorizadorSolicitante = idAutorizadorSolicitante;
       this.idGrupoAnterior = idGrupoAnterior;
       this.idAutorizadorAcepta = idAutorizadorAcepta;
       this.idGrupoNuevo = idGrupoNuevo;
       this.idEstado = idEstado;
       this.fechaEfectivaTraslado = fechaEfectivaTraslado;
       this.fechaNotificacionTraslado = fechaNotificacionTraslado;
       this.motivo = motivo;
       this.usuarioModifica = usuarioModifica;
       this.fechaModifica = fechaModifica;
       this.ipModifica = ipModifica;
    }
   
    @Id 
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_solicitud", unique=true, nullable=false)
    public int getIdSolicitud() {
        return this.idSolicitud;
    }
    
    public void setIdSolicitud(int idSolicitud) {
        this.idSolicitud = idSolicitud;
    }

    
    @Column(name="nit_profesional", nullable=false, length=16)
    public String getNitProfesional() {
        return this.nitProfesional;
    }
    
    public void setNitProfesional(String nitProfesional) {
        this.nitProfesional = nitProfesional;
    }

    
    @Column(name="id_autorizador_solicitante", nullable=false, length=16)
    public String getIdAutorizadorSolicitante() {
        return this.idAutorizadorSolicitante;
    }
    
    public void setIdAutorizadorSolicitante(String idAutorizadorSolicitante) {
        this.idAutorizadorSolicitante = idAutorizadorSolicitante;
    }

    
    @Column(name="id_grupo_anterior", nullable=false)
    public long getIdGrupoAnterior() {
        return this.idGrupoAnterior;
    }
    
    public void setIdGrupoAnterior(long idGrupoAnterior) {
        this.idGrupoAnterior = idGrupoAnterior;
    }

    
    @Column(name="id_autorizador_acepta", nullable=false, length=16)
    public String getIdAutorizadorAcepta() {
        return this.idAutorizadorAcepta;
    }
    
    public void setIdAutorizadorAcepta(String idAutorizadorAcepta) {
        this.idAutorizadorAcepta = idAutorizadorAcepta;
    }

    
    @Column(name="id_grupo_nuevo", nullable=false)
    public long getIdGrupoNuevo() {
        return this.idGrupoNuevo;
    }
    
    public void setIdGrupoNuevo(long idGrupoNuevo) {
        this.idGrupoNuevo = idGrupoNuevo;
    }

    
    @Column(name="id_estado", nullable=false)
    public long getIdEstado() {
        return this.idEstado;
    }
    
    public void setIdEstado(long idEstado) {
        this.idEstado = idEstado;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_efectiva_traslado", length=29)
    public Date getFechaEfectivaTraslado() {
        return this.fechaEfectivaTraslado;
    }
    
    public void setFechaEfectivaTraslado(Date fechaEfectivaTraslado) {
        this.fechaEfectivaTraslado = fechaEfectivaTraslado;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_notificacion_traslado", length=29)
    public Date getFechaNotificacionTraslado() {
        return this.fechaNotificacionTraslado;
    }
    
    public void setFechaNotificacionTraslado(Date fechaNotificacionTraslado) {
        this.fechaNotificacionTraslado = fechaNotificacionTraslado;
    }

    
    @Column(name="motivo", length=500)
    public String getMotivo() {
        return this.motivo;
    }
    
    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    
    @Column(name="usuario_modifica", nullable=false, length=20)
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }
    
    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="fecha_modifica", nullable=false, length=29)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }
    
    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    
    @Column(name="ip_modifica", nullable=false, length=40)
    public String getIpModifica() {
        return this.ipModifica;
    }
    
    public void setIpModifica(String ipModifica) {
        this.ipModifica = ipModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_efectiva_retorno", nullable = true, length = 29)
    /**
     * @return the fechaEfectivaRetorno
     */
    public Date getFechaEfectivaRetorno() {
        return fechaEfectivaRetorno;
    }

    /**
     * @param fechaEfectivaRetorno the fechaEfectivaRetorno to set
     */
    public void setFechaEfectivaRetorno(Date fechaEfectivaRetorno) {
        this.fechaEfectivaRetorno = fechaEfectivaRetorno;
    }

    @Column(name = "id_tipo_traslado", nullable = true)
    /**
     * id_tipo_traslado
     *
     * @return the idTipoTraslado
     */
    public long getIdTipoTraslado() {
        return idTipoTraslado;
    }

    /**
     * @param idTipoTraslado the idTipoTraslado to set
     */
    public void setIdTipoTraslado(long idTipoTraslado) {
        this.idTipoTraslado = idTipoTraslado;
    }

}