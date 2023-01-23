/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoSolicitudCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudCreditoFiscal;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author adftopvar
 */
public interface HistorialSolicitudCreditoFiscalRepository extends CrudRepository<SipfHistoricoSolicitudCreditoFiscal, Integer> {
    
    
    
    
}
