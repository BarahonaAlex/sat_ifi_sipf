package gt.gob.sat.sat_ifi_sipf.dtos;

import java.io.Serializable;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author crramosl
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class HallazgosEncontradosDto implements Serializable {

    @Size(max = 300)
    @NotEmpty
    private String nombre;
    @NotEmpty
    private String descripcion;
    private List<Integer> rubros;
    private int caso;
    private String correlativo;
}
