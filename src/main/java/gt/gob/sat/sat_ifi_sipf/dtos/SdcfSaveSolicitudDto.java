/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.List;

/**
 *
 * @author ajabarrer
 */
public class SdcfSaveSolicitudDto {
    private DatosSolicitudDto datosSolicitud;
    private List<DeclaracionDto> datoslistaDeclaracion;

    public DatosSolicitudDto getDatosSolicitud() {
        return datosSolicitud;
    }

    public void setDatosSolicitud(DatosSolicitudDto datosSolicitud) {
        this.datosSolicitud = datosSolicitud;
    }

    public List<DeclaracionDto> getDatoslistaDeclaracion() {
        return datoslistaDeclaracion;
    }

    public void setDatoslistaDeclaracion(List<DeclaracionDto> datoslistaDeclaracion) {
        this.datoslistaDeclaracion = datoslistaDeclaracion;
    }

    public SdcfSaveSolicitudDto(DatosSolicitudDto datosSolicitud, List<DeclaracionDto> datoslistaDeclaracion) {
        this.datosSolicitud = datosSolicitud;
        this.datoslistaDeclaracion = datoslistaDeclaracion;
    }

    public SdcfSaveSolicitudDto() {
    }

    
    
    
}
