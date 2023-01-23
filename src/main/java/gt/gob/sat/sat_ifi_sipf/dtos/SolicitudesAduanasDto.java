/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author garuanom
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class SolicitudesAduanasDto {

    private Integer no;
    private String audiencia;
    private Date fechaAudiencia;
    private String NIT;
    private String nombre;
    private String noDuca;
    private String noDucaOrden;
    private String fechaAceptacion;
    private String regimen;
    private String selectivo;
    private String paisProveedor;
    private String tipoAjuste;
    private String expedientes;
    private String certificadoensayo;
    private String dictamenArancelaria;
    private Date fechaDictamen;
    private String Descripcion;
    private Integer capituloDeclarado;
    private Integer partidaDeclarada;
    private Integer incisosDeclarado;
    private Double tasaDai;
    private Double daiPagado;
    private Double ivaPago;
    private Integer capituloDictaminado;
    private Integer partidaDictaminado;
    private Integer incisoDictaminado;
    private Double tasaDaiaDictaminado;
    private String tipoAlertivo;
    private String opinionDace;
    private Double montoTotal;
    private String observaciones;
    private String unidadMedida;
    private Integer cantidad;
    private Double valorDeclarado;
    private Double valorAjustado;
    private String numeroOrden;
    private String tratoArancelario;

}
