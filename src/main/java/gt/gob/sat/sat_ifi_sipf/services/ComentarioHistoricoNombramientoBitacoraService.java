/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.dtos.ComentarioHistoricoNombramientoBitacoraDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ComentarioHistoricoNombramientoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfComentarioHistoricoNombramiento;
import gt.gob.sat.sat_ifi_sipf.models.SipfComentarioHistoricoNombramientoBitacora;
import gt.gob.sat.sat_ifi_sipf.models.SipfComentarioHistoricoNombramientoId;
import gt.gob.sat.sat_ifi_sipf.repositories.ComentarioHistoricoNombramientoBitacoraRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ComentarioHistoricoNombramientoRepository;
import java.util.Date;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author ajabarrer
 */
@Service
@Slf4j
public class ComentarioHistoricoNombramientoBitacoraService {
    
     @Autowired
    private ComentarioHistoricoNombramientoBitacoraRepository comentatioHistoricoBitacoraRepository;
      @Autowired
    private ComentarioHistoricoNombramientoRepository comentatioHistoricoRepository;
     
     public ComentarioHistoricoNombramientoDto saveCommnetAuditBitacora(ComentarioHistoricoNombramientoDto pDatos, UserLogged logged) {
        SipfComentarioHistoricoNombramiento vCommentSave
                = this.comentatioHistoricoRepository.findById(
                        new SipfComentarioHistoricoNombramientoId(
                                pDatos.getNombrameinto(),
                                pDatos.getExpediente())).orElse(null);
        SipfComentarioHistoricoNombramientoBitacora tabla2save = new SipfComentarioHistoricoNombramientoBitacora();
       
        tabla2save.setNombrameinto(pDatos.getNombrameinto());
        tabla2save.setExpediente(pDatos.getExpediente());
        tabla2save.setComentario(vCommentSave.getComentario());
        tabla2save.setFechaModificacion(new Date());
        tabla2save.setUsuarioModificacion(logged.getLogin());
        tabla2save.setIpModificacion(logged.getIp());
        
        this.comentatioHistoricoRepository.deleteById(vCommentSave.getId());
        if(this.comentatioHistoricoRepository.findById(
                        new SipfComentarioHistoricoNombramientoId(
                                pDatos.getNombrameinto(),
                                pDatos.getExpediente())).orElse(null)==null){
            this.comentatioHistoricoBitacoraRepository.save(tabla2save);
        }
        return pDatos;
    }
     

}
