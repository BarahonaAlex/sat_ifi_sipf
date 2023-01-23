/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 *
 * @author ajabarrer
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PlantillasDocumentosDto {
     private String nombre;
     private String catalogos;
     private String encabezado;
     private String piePagina;
     private String usuarioModifica;
     private String ipModificia;
     private Date fechaModifica;
     private List<String> varibalesPermitidas;
     private List<String> varibalesIngresadas;
}
