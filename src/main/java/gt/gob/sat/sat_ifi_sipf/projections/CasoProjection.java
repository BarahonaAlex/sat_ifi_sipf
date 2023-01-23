/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author rabaraho
 */
public interface CasoProjection {

    int getidCaso();

    int getidEstado();

    Integer getidOrigen();

    Integer getidGerencia();

    Long getidProceso();

    Double getmontoRecaudado();


    Integer getidTipoInsumo();

    Integer getidPrograma();

    String getImpuesto();

    int getidTipoAlcance();

    String getcorrelativoAprobacion();

    String getusuarioModifica();

    Date getfechaModifica();

    String getipModifica();

    String getnombreInsumo();

    Integer getidInsumo();

    Date getperiodoRevisionInicio();

    Date getperiodoRevisionFin();

    String getnitContribuyente();

    String getdescripcion();

    String getidFichaTecnica();

    String getnombreGerencia();

    String getnombreEstado();

    String getnombreDepartamento();
    
    String getnombreContribuyente();
    
    String getImpuestos();
    
    Boolean getRequiereDocumentacion();
}
