/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ajsbatzmo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ColaboratorTransferDto extends CollaboratorProfileDto{
   
    private long idGrupoT;//T de trasnferencia 
    private int idSolicitud;//Id de Solicitud de transferencia 
   
}
