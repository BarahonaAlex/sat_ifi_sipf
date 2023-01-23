package gt.gob.sat.sat_ifi_sipf.projections;

import java.util.Date;

/**
 *
 * @author asacanoes
 */
public interface CasosAndAlcanceProjection {

    int getIdCaso();

    int getIdEstado();

    int getIdGerencia();

    Date getPeriodoInicio();

    Date getPeriodoFin();

    String getObjetivo();

    String getNombrePrograma();

    String getNombreEstado();

    Integer getIdAlcance();

    Integer getIdVersion();

    Integer getIdPrograma();

    String getTerritorio();

    String getNombreGerencia();

    String getNombreColaborador();

    String getCorreoColaborador();

    String getComentariosSup();

    String getComentariosJefe();
}
