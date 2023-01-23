/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author adftopvar
 */
public interface InconsistenciaProjections {
    
    Integer getIdInconsistencia();
    String getTipoInconsistencia();
    Date getPeriodoDesde();
    Date getPeriodoHasta();
    Integer getTipoRepetida();
    Integer getDeclaracionRepetida();
    String getFacturaProveedor();
    String getFacturaSerie();
    String getNoFactura();
    String getObservacion();
    Integer getIdEstado();
    String getEstado();
    
    
}
