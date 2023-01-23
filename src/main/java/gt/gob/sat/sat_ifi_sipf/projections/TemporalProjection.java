/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author josed
 */
public interface TemporalProjection {
    String getnitContribuyente(); 
    Integer getimpuesto();
    String getnombreImpuesto();
    String getidProceso();
    String getperiodo();
    Integer getid();
    String getnitColaborador();
    String getnombreColaborador();
    Integer getidTemporal();
}
