/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.List;

/**
 *
 * @author ajabarrer
 */
public class SdcfResProcesoNocturnoDto implements Serializable{
    private List<SdcfInconsistenciaDto> inconsistencias;
    private List<SdcfUpdateStatusDto> updateSolicitudes;

    public List<SdcfInconsistenciaDto> getInconsistencias() {
        return inconsistencias;
    }

    public void setInconsistencias(List<SdcfInconsistenciaDto> inconsistencias) {
        this.inconsistencias = inconsistencias;
    }

    public List<SdcfUpdateStatusDto> getUpdateSolicitudes() {
        return updateSolicitudes;
    }

    public void setUpdateSolicitudes(List<SdcfUpdateStatusDto> updateSolicitudes) {
        this.updateSolicitudes = updateSolicitudes;
    }

    public SdcfResProcesoNocturnoDto() {
    }

  
    
}
