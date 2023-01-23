package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author asacanoes
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AlcancesMasivosVersionesDto {

    private Date fechaModifica;
    private int idEstado;
    private String ipModifica;
    private String justificacion;
    private String objetivo;
    private String procedimientosEspecificos;
    private String usuarioModifica;
    private int version;
    private int idTipoAlcanceMasivo;
    private String actividad;
}
