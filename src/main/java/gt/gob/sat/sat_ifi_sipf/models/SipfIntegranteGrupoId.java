package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SipfIntegranteGrupoId implements Serializable {

    private long idGrupo;
    private String nit;
    private Integer idPerfil;
}
