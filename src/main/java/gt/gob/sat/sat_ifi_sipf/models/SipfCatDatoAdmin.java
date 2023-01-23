package gt.gob.sat.sat_ifi_sipf.models;
// Generated 14/06/2022 12:21:19 PM by Hibernate Tools 4.3.1

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * SipfCatDatoAdmin generated by hbm2java
 */
@Entity
@Table(name = "sipf_cat_dato_admin",
         schema = "sat_ifi_sipf"
)
public class SipfCatDatoAdmin implements java.io.Serializable {

    private int codigo;
    private SipfCatDatoAdmin sipfCatDatoAdmin;
    private Integer codigoCatalogo;
    private String codigoIngresado;
    private String nombre;
    private String descripcion;
    private Integer estado;
    private String usuarioAgrega;
    private Date fechaAgrega;
    private String usuarioModifica;
    private Date fechaModifica;
    private Set<SipfCatDatoAdmin> sipfCatDatoAdmins = new HashSet<SipfCatDatoAdmin>(0);

    public SipfCatDatoAdmin() {
    }

    public SipfCatDatoAdmin(int codigo) {
        this.codigo = codigo;
    }

    public SipfCatDatoAdmin(int codigo, SipfCatDatoAdmin sipfCatDatoAdmin, Integer codigoCatalogo, String codigoIngresado, String nombre, String descripcion, Integer estado, String usuarioAgrega, Date fechaAgrega, String usuarioModifica, Date fechaModifica, Set<SipfCatDatoAdmin> sipfCatDatoAdmins) {
        this.codigo = codigo;
        this.sipfCatDatoAdmin = sipfCatDatoAdmin;
        this.codigoCatalogo = codigoCatalogo;
        this.codigoIngresado = codigoIngresado;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.estado = estado;
        this.usuarioAgrega = usuarioAgrega;
        this.fechaAgrega = fechaAgrega;
        this.usuarioModifica = usuarioModifica;
        this.fechaModifica = fechaModifica;
        this.sipfCatDatoAdmins = sipfCatDatoAdmins;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "codigo", unique = true, nullable = false)
    public int getCodigo() {
        return this.codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "codigo_dato_padre")
    public SipfCatDatoAdmin getSipfCatDatoAdmin() {
        return this.sipfCatDatoAdmin;
    }

    public void setSipfCatDatoAdmin(SipfCatDatoAdmin sipfCatDatoAdmin) {
        this.sipfCatDatoAdmin = sipfCatDatoAdmin;
    }

    @Column(name = "codigo_catalogo")
    public Integer getCodigoCatalogo() {
        return this.codigoCatalogo;
    }

    public void setCodigoCatalogo(Integer codigoCatalogo) {
        this.codigoCatalogo = codigoCatalogo;
    }

    @Column(name = "codigo_ingresado")
    public String getCodigoIngresado() {
        return this.codigoIngresado;
    }

    public void setCodigoIngresado(String codigoIngresado) {
        this.codigoIngresado = codigoIngresado;
    }

    @Column(name = "nombre")
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Column(name = "descripcion")
    public String getDescripcion() {
        return this.descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Column(name = "estado")
    public Integer getEstado() {
        return this.estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }

    @Column(name = "usuario_agrega")
    public String getUsuarioAgrega() {
        return this.usuarioAgrega;
    }

    public void setUsuarioAgrega(String usuarioAgrega) {
        this.usuarioAgrega = usuarioAgrega;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_agrega", length = 29)
    public Date getFechaAgrega() {
        return this.fechaAgrega;
    }

    public void setFechaAgrega(Date fechaAgrega) {
        this.fechaAgrega = fechaAgrega;
    }

    @Column(name = "usuario_modifica")
    public String getUsuarioModifica() {
        return this.usuarioModifica;
    }

    public void setUsuarioModifica(String usuarioModifica) {
        this.usuarioModifica = usuarioModifica;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "fecha_modifica", length = 29)
    public Date getFechaModifica() {
        return this.fechaModifica;
    }

    public void setFechaModifica(Date fechaModifica) {
        this.fechaModifica = fechaModifica;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sipfCatDatoAdmin")
    public Set<SipfCatDatoAdmin> getSipfCatDatoAdmins() {
        return this.sipfCatDatoAdmins;
    }

    public void setSipfCatDatoAdmins(Set<SipfCatDatoAdmin> sipfCatDatoAdmins) {
        this.sipfCatDatoAdmins = sipfCatDatoAdmins;
    }

}