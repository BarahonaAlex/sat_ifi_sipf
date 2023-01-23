/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.repositories.UtilsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author barah
 */
@Service
@Slf4j
public class GeneralServices {

    @Autowired
    private UtilsRepository utilsRepository;

    public Integer nextValSequence(String pSequenceName) {
        final int vNextVal = utilsRepository.nextValSequence(pSequenceName);
        return vNextVal;
    }

}
