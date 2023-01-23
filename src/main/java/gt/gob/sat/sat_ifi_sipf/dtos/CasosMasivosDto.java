package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author asacanoes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class CasosMasivosDto {

    private int idCaso;
    private Integer idTipoAlcance;
    private String territorioMasivo;
    private Integer idVersionAlcance;
    private int idEstado;
    private Date periodoRevisionInicio;
    private Date periodoRevisionFin;
    private Integer idPrograma;
    private String nombrePrograma;
    private String nombreAlcance;
    private String nombreGerencia;
    private String nombreColaborador;
    private String correoColaborador;
}
