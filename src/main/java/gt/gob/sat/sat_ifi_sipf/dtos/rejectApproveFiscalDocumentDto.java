/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author adftopvar
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class rejectApproveFiscalDocumentDto {
    
    private Long idArchivo;
    private Integer idEstado;
    private String comentario;
    
}
