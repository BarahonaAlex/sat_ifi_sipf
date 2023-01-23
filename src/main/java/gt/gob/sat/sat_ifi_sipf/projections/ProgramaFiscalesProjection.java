/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author Luisf
 */
public interface ProgramaFiscalesProjection {

    /**
     * @autor alfvillag (Luis Villagran)
     * @creationDate 04/02/2022
     * @description obtiene el id y nombre de los programas fiscales, utilizado
     * en la elaboración de alcances
     * @return id y nombre de programa.
     */
    Integer getidPrograma();

    Integer getidTipoPrograma();

    Integer getidDireccionamientoAuditoria();

    Integer getidTipoAuditoria();

    Integer getidOrigenInsumo();

    Integer getidGerencia();

    String getimpuesto();

    Integer getanio();

    Integer getcorrelativo();

    Date getperiodoInicio();

    Date getperiodoFin();

    String getnombre();

    String getdescripcion();

    Integer getidDepartamento();

    Integer getidEstado();

    String getcodificacionPrograma();

    String getimpuestoNombres();

    String getusuarioModifica();

    String getipModifica();

    Date getfechaModifica();

    String getusuarioAgrega();
    
    Integer getidEstadoAnterior();
    

}
