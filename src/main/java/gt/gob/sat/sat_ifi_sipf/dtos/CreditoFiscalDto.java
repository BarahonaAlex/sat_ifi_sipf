/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author lfvillag
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditoFiscalDto {

    private Integer idSolicitud;
    private Date periodoDesde;
    private Date periodoHasta;
    private String nit;
    private HashMap<String, BigDecimal> idDocs;
}
