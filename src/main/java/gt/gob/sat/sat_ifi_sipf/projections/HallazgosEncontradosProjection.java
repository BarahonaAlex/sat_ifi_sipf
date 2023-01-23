/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author josed
 */
public interface HallazgosEncontradosProjection {

    Integer getidHallazgos();

    Integer getidTipoHallazgo();

    String getdescripcionHallazgo();

    Integer getarrayDocs();

    String getidRubro();

    Date getfechaCreacion();

    Date getfechaModificacion();

    String getusuarioCreacion();

    String getusuarioModificacion();

    Integer getidEstado();

    Integer getidCaso();

    Integer getidAsignacionInsumos();

    String getnitColaborador();

    String getidPrograma();

    Integer getidImpuesto();

    Integer getidFormulario();

    Date getperiodo();

    Date getfechaInicio();

    Date getfechaFin();

    Date getperiodoEstadosFinancieros();

    Integer getidEstadosFinancieros();

    Integer getidFuente();

    String getsolicitudcambios();

    String getnombreImpuesto();

    String getnombreFormulario();

    String getnombreEstado();

    String getnombreEstadoFinancieros();

    String getnombreFuente();

    String getnombreRubro();
    Integer getestadoCaso();
}
