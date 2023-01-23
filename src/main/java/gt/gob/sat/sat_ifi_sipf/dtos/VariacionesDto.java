/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.dtos;

import java.math.BigDecimal;

/**
 *
 * @author crramosl
 */
public class VariacionesDto {

    private BigDecimal idRequest;
    private BigDecimal docType;
    private BigDecimal idDoc;

    public VariacionesDto() {
    } 

    public VariacionesDto(BigDecimal idRequest, BigDecimal docType, BigDecimal idDoc) {
        this.idRequest = idRequest;
        this.docType = docType;
        this.idDoc = idDoc;
    }

    public BigDecimal getIdRequest() {
        return idRequest;
    }

    public void setIdRequest(BigDecimal idRequest) {
        this.idRequest = idRequest;
    }

    public BigDecimal getDocType() {
        return docType;
    }

    public void setDocType(BigDecimal docType) {
        this.docType = docType;
    }

    public BigDecimal getIdDoc() {
        return idDoc;
    }

    public void setIdDoc(BigDecimal idDoc) {
        this.idDoc = idDoc;
    }
}
