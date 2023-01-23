/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.dtos.BitacoraProgramasFiscalesDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfBitacoraProgramasFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.BitacoraProgramasFiscalesProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.BitacoraProgramasFiscalesRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author jdaldana
 */
@Transactional
@Service
@Slf4j
public class BitacoraProgramasFiscalesService {

    @Autowired
    private BitacoraProgramasFiscalesRepository bitacoraProgramasFiscalesRepository;

    public BitacoraProgramasFiscalesProjection getData(Integer tipoOperacion, Integer idPrograma) {

        return bitacoraProgramasFiscalesRepository.findMaxIdRegistroBitacoraByTypeByIdPrograma(tipoOperacion, idPrograma);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createLog(BitacoraProgramasFiscalesDto data) {

        bitacoraProgramasFiscalesRepository.save(
                SipfBitacoraProgramasFiscales.builder()
                        .idPrograma(data.getIdPrograma())
                        .idTipoOperacion(data.getIdTipoOperacion())
                        .idEstadoAnterior(data.getIdEstadoAnterior())
                        .idEstadoNuevo(data.getIdEstadoNuevo())
                        .usuarioModifica(data.getUsuarioModifica())
                        .fechaModifica(data.getFechaModifica())
                        .build()
        );
    }
}
