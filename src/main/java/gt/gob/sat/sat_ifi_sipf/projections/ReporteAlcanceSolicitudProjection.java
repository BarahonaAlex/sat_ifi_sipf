/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author Jamier
 */
public interface ReporteAlcanceSolicitudProjection extends ReporteAlcanceBaseProjection {

    String getPlazo();

    String getEnte();

    String getAntecedentes();

    String getAuditorias();

    String getRubrosFiscalizar();

    String getObjetivo();

    String getProcedEspecificos();

}
