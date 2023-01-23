package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author asacanoess
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class AlcancesMasivosDto extends AlcancesMasivosVersionesDto {

    private String descripcionAlcance;
    private Date fechaModifica;
    private String ipModifica;
    private String nombreAlcance;
    private String usuarioModifica;
}
