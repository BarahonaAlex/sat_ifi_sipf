/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.Date;

/**
 *
 * @author rabaraho
 */
public interface DenunciaGuardadaProjection {

    String getcorrelativo();

    String getnitDenunciante();

    String getnombreDenunciante();

    String gettelefonoDenunciante();

    String getemail();

    String getnitDenunciado();

    String getnombreDenunciado();

    String getestablecimientoDenunciado();

    String getdireccionFiscalDenunciado();

    String getdireccionEstDenunciado();

    String gettelefonoDenunciado();

    Integer getidMotivo();

    String getotroMotivo();

    String getformaPago();

    Date getfechaCompra();

    String getproductoServicio();

    Integer getvalorCompraServicio();

    String getobservaciones();

    Integer getestado();

    String getauditor();

    String getrazonRechazo();

    Integer getdepartamento();

    Integer getmunicipio();

    @JsonProperty(value = "fechaGrabacion")
    @JsonFormat(pattern = "dd/MM/yyyy")
    String getfechaGrabacion();

    String getjustificacion();

    Date getfechaAsignacion();

    Date getfechaAsignacionJs();

    Date getfechaCargaPlan();

    Date getfechaRechazo();

    Integer getlinea();

    String getnitAp();

    String getnitAuditorRechazo();

    String getnitJs();

    String getnitSn();

    String getnombrePlan();

    Integer getregion();

    String geturlImagen();

    String getlatitud();

    String getlongitud();

    Integer getIdInsumo();

    Integer getIdProceso();

    Integer getIdVialidad();

    String getNombreColonia();

    Integer getIdGrupoHabitacional();

    Integer getIdZona();

    Integer getIdSubConceptoDenuncia();

    String getCorrelativoAprobacion();

}
