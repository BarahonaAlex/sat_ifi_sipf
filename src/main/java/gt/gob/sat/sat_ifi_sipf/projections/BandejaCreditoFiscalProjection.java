/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

/**
 *
 * @author adftopvar
 */
public interface BandejaCreditoFiscalProjection {
    Integer getIdSolicitud();
    Integer getIdEstado();
    String getNitContribuyente();
    String getNombreContribuyente();
    String getEstado();
    Integer getMulta();
    Integer getTotal();
    String getNumeroFormulario();
    String getNitRepresentante();
    String getNombreRepresentante();
    String getNitContador();
    String getNombreContador();
    String getComentario();            
}
