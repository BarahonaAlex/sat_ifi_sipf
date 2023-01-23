package gt.gob.sat.sat_ifi_sipf.dtos;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author ajsbatzmo
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExcelMasivoDto {

        private String cantSelec;
        private String tipoDeUsuarioE;
        private String unidadE;
        private String tipoAgenteE;
        private String codigoEstadoE;
        private String nitAgentenRetencionE;
        private String nombreAgenteRetencionE;
        private String usuarioE;
        private String fechaDelEx;
        private String fechaAlEx;
        private String estadoE;
        private String concepto;
        private String totalTotalFactura;
        private String totalImporteNeto;
        private String totalAfectoRetencion;
        private String totalRetencion;
        private String totalTotalRentaImponible;
        private String usuario;
        private Integer registrosSinFEL;
        private AgenteExcelMasivoDto agente;
        private String tipoExcel;
        private Integer varModoReg;
        private String[] constanciasGE;
        private Boolean usrAdmin ;
}
