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
public interface ReporteAlcanceSelectivoProjection extends ReporteAlcanceBaseProjection {

    String getAntecedentes();

    String getAuditorias();

    String getInfoEspecial();

    String getInconsistencias();

    String getHallazgosProgramacion();

    String getObjetivo();

    String getProcedEspecificos();

}
