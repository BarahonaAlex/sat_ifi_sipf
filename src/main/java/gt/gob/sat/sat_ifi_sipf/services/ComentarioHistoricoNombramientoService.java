/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

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
 * @author rabaraho
 */
@Service
@Slf4j
public class ComentarioHistoricoNombramientoService {
    
    @Autowired
    private ComentarioHistoricoNombramientoBitacoraRepository comentatioHistoricoBitacoraRepository;
    @Autowired
    private ComentarioHistoricoNombramientoRepository comentatioHistoricoRepository;

    public ComentarioHistoricoNombramientoDto saveCommnetAudit(ComentarioHistoricoNombramientoDto pDatos, UserLogged logged) {
        SipfComentarioHistoricoNombramientoBitacora tabla2save = new SipfComentarioHistoricoNombramientoBitacora();
        SipfComentarioHistoricoNombramiento vCommentSave
                = new SipfComentarioHistoricoNombramiento(
                        new SipfComentarioHistoricoNombramientoId(
                                pDatos.getNombrameinto(),
                                pDatos.getExpediente()));
        vCommentSave.setComentario(pDatos.getComentario());
        vCommentSave.setFechaModificacion(new Date());
        vCommentSave.setUsuarioModificacion(logged.getLogin());
        vCommentSave.setIpModificacion(logged.getIp());
        this.comentatioHistoricoRepository.save(vCommentSave);
        
        tabla2save.setNombrameinto(pDatos.getNombrameinto());
        tabla2save.setExpediente(pDatos.getExpediente());
        tabla2save.setComentario(pDatos.getComentario());
        tabla2save.setFechaModificacion(new Date());
        tabla2save.setUsuarioModificacion(logged.getLogin());
        tabla2save.setIpModificacion(logged.getIp());
         this.comentatioHistoricoBitacoraRepository.save(tabla2save);
        return pDatos;
        
        
    }

    public String getCommentByNombramientoExpediente(String pNombramieto, String pExpediente) {

        SipfComentarioHistoricoNombramientoId id = new SipfComentarioHistoricoNombramientoId(pNombramieto, pExpediente);
        Optional<SipfComentarioHistoricoNombramiento> comment = this.comentatioHistoricoRepository.findById(id);
        if (comment.isPresent()) {
            return comment.get().getComentario();
        } else {
            return "";
        }

    }
}
