/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaColaboradorId;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.repositories.DenunciaColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.DenunciaGrabadaRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author rabaraho
 */
@Service
@Slf4j
public class DenunciaColaboradorService {

    @Autowired
    private DenunciaColaboradorRepository denunciaColaboradorRepository;

    @Autowired
    private DenunciaGrabadaRepository denunciaGrabadaRepository;

    public List<?> getWhole() {

        return this.denunciaColaboradorRepository.findWhole();
    }

    public boolean assignList(List<String> pCorrelaviteList, String pResponsibleId, UserLogged pUser) {

        List<SipfDenunciaColaborador> assignmentsList;
        assignmentsList = new ArrayList<>();
        pCorrelaviteList.forEach(correlative -> {
            SipfDenunciaColaboradorId vId = new SipfDenunciaColaboradorId();
            vId.setNitResponsable(pResponsibleId);
            vId.setCorrelativoDenuncia(correlative);
            SipfDenunciaColaborador vAssignment = new SipfDenunciaColaborador();
            vAssignment.setId(vId);
            vAssignment.setUsuarioModifica(pUser.getLogin());
            vAssignment.setFechaModifica(new Date());
            vAssignment.setIpModifica(pUser.getIp());
            assignmentsList.add(vAssignment);
        });
        // el estado 956 es remplazado por la Consdtante ASSIGNED
        Integer vStatus = Catalog.Complaint.Status.ASSIGNED;
        List<SipfDenunciaGrabada> vList = denunciaGrabadaRepository.findBycorrelativoIn(pCorrelaviteList);
        vList.forEach(item -> item.setEstado(vStatus));
        this.denunciaGrabadaRepository.saveAll(vList);

        return this.denunciaColaboradorRepository.saveAll(assignmentsList).iterator().hasNext();
    }

}
