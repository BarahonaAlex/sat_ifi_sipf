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
 * @author ajsbatzmo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgenteExcelMasivoDto {
        private String nit;
        private Integer id_tipo;
        private String nombre_agente;
        private String codigo;
        private Integer id_clasificacion;
        private String nombre_unidad;
        private String usuario;
        private String estado;
        private String uc;
        private String fr;
        private String independiente;
        private String manual;
        private String nombre_tipo;
        private String fecha_ini_retencion;
        private String clasiRtu;
    
}
