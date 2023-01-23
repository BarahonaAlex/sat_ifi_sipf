/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

//import org.apache.poi.hpsf.Blob;
import java.sql.Blob;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import javax.sql.rowset.serial.SerialBlob;
import java.util.Date;
import javax.websocket.Session;
import org.hibernate.Hibernate;

/**
 *
 * @author adftopvar
 */

public class EfaDto {
    private String id;
    private String nit;
    private String periodoDesde;
    private String periodoHasta;
    private String nombreArchivo;
    private Blob dataArchivo;
    private String tipoArchivo;
    private String estado;
    private Date fechaPresentacion;
    private Date fechaAutorizacion;
    private String anio;
    private String docFirmado;
    private String tipoObligacion;

    public EfaDto() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public String getPeriodoDesde() {
        return periodoDesde;
    }

    public void setPeriodoDesde(String periodoDesde) {
        this.periodoDesde = periodoDesde;
    }

    public String getPeriodoHasta() {
        return periodoHasta;
    }

    public void setPeriodoHasta(String periodoHasta) {
        
        this.periodoHasta = periodoHasta;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public Blob getDataArchivo() {
        return dataArchivo;
    }

    public void setDataArchivo(byte[] dataArchivo) throws SQLException{
        this.dataArchivo = new SerialBlob(dataArchivo);
    }

    public String getTipoArchivo() {
        return tipoArchivo;
    }

    public void setTipoArchivo(String tipoArchivo) {
        this.tipoArchivo = tipoArchivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public Date getFechaPresentacion() {
        return fechaPresentacion;
    }

    public void setFechaPresentacion(Date fechaPresentacion){
        this.fechaPresentacion = fechaPresentacion;
    }

    public Date getFechaAutorizacion() {
        return fechaAutorizacion;
    }

    public void setFechaAutorizacion(Date fechaAutorizacion){
        this.fechaAutorizacion = fechaAutorizacion;
    }

    public String getAnio() {
        return anio;
    }

    public void setAnio(String anio) {
        this.anio = anio;
    }

    public String getDocFirmado() {
        return docFirmado;
    }

    public void setDocFirmado(String docFirmado) {
        this.docFirmado = docFirmado;
    }

    public String getTipoObligacion() {
        return tipoObligacion;
    }

    public void setTipoObligacion(String tipoObligacion) {
        this.tipoObligacion = tipoObligacion;
    }
    
    
}
