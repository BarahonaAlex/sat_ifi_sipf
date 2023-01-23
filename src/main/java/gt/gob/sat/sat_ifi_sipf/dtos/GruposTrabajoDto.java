/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

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
public class GruposTrabajoDto {

    @Size(min = 1, max = 200)
    private String nombre;
    @Size(min = 1, max = 400)
    private String descripcion;
    private Integer estado;
    private Integer idUnidadAdministrativa;
    @NotEmpty(message = "Debe ingresar como minimo un integrante")
    private List<CollaboratorProfileDto> integrantes;
}
