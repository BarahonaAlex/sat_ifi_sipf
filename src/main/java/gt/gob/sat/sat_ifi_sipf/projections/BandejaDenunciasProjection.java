/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author lfvillag
 */
public interface BandejaDenunciasProjection {
    String getCorrelativo();
    String getNit();
    String getNombre();
    String getProducto();
    Integer getCompra();
    Integer getTipo();
}
