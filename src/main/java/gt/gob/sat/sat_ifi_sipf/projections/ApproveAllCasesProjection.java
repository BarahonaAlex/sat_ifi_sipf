/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author jdaldana
 */
public interface ApproveAllCasesProjection {
    Integer getIdCaso();
    Integer getIdGerencia();
    Integer getIdEstado();
    String getSiglas();
    Integer getPeriodo();
}
