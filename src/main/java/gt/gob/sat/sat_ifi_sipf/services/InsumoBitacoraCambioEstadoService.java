/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gt.gob.sat.sat_ifi_sipf.dtos.SipfInsumoBitacoraCambioEstadoDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfInsumoBitacoraCambioEstado;
import gt.gob.sat.sat_ifi_sipf.repositories.InsumoBitacoraCambioEstadoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author rabaraho
 */
@Service
@Slf4j
public class InsumoBitacoraCambioEstadoService {

    @Autowired
    private InsumoBitacoraCambioEstadoRepository insumoBitacoraCambioEstadoRepository;

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<SipfInsumoBitacoraCambioEstadoDto> createRegistriChangeOfState(SipfInsumoBitacoraCambioEstadoDto pData) {
        SipfInsumoBitacoraCambioEstado toSave;
        toSave = new ObjectMapper().convertValue(pData, new TypeReference<SipfInsumoBitacoraCambioEstado>() {
        });
        this.insumoBitacoraCambioEstadoRepository.save(toSave);
        return ResponseEntity.ok(pData);
    }
}
