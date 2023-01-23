/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;

/**
 *
 * @author ruano
 */
@Entity
@Table(name = "sipf_historial_estados_colaborador",
        schema = "sat_ifi_sipf"
)
public class SipfHistorialEstadoColaborador implements Serializable {

    private int id;
    private String nitColaborador;
    private int idEstado;
    private Date fechaInicio;
    private Date fechaFin;
    private String usuarioCrea;
    private String ipCrea;
    private Date fechaCrea;

    public SipfHistorialEstadoColaborador() {
    }

    public SipfHistorialEstadoColaborador(int id, String nitColaborador, int idEstado, Date fechaInicio, Date fechaFin, String usuarioCrea, String ipCrea, Date fechaCrea) {
        this.id = id;
        this.nitColaborador = nitColaborador;
        this.idEstado = idEstado;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.usuarioCrea = usuarioCrea;
        this.ipCrea = ipCrea;
        this.fechaCrea = fechaCrea;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", nullable = false)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Column(name = "nit_colaborador", unique = true, nullable = false, length = 16)
    public String getNitColaborador() {
        return nitColaborador;
    }

    public void setNitColaborador(String nitColaborador) {
        this.nitColaborador = nitColaborador;
    }

    @Column(name = "id_estado", nullable = false)
    public int getIdEstado() {
        return idEstado;
    }

    public void setIdEstado(int idEstado) {
        this.idEstado = idEstado;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_inicio", nullable = false, length = 22)
    public Date getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(Date fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_fin", nullable = false, length = 22)
    public Date getFechaFin() {
        return fechaFin;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    @Column(name = "usuario_crea", nullable = false, length = 20)
    public String getUsuarioCrea() {
        return usuarioCrea;
    }

    public void setUsuarioCrea(String usuarioCrea) {
        this.usuarioCrea = usuarioCrea;
    }

    @Column(name = "ip_crea", nullable = false, length = 40)
    public String getIpCrea() {
        return ipCrea;
    }

    public void setIpCrea(String ipCrea) {
        this.ipCrea = ipCrea;
    }

    @Temporal(javax.persistence.TemporalType.DATE)
    @Column(name = "fecha_crea", nullable = false, length = 22)
    public Date getFechaCrea() {
        return fechaCrea;
    }

    public void setFechaCrea(Date fechaCrea) {
        this.fechaCrea = fechaCrea;
    }

}
