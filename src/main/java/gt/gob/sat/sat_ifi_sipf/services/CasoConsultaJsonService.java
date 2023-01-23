/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.dtos.CasoConsultaJsonDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfConsultaCasoJson;
import gt.gob.sat.sat_ifi_sipf.repositories.CasoConsultaJsonRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import gt.gob.sat.sat_ifi_sipf.projections.CasoConsultaJsonProjection;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author rabaraho
 */
@Service
@Slf4j
public class CasoConsultaJsonService {

    @Autowired
    private CasoConsultaJsonRepository casoConsultaJsonRepository;

    public List<CasoConsultaJsonProjection> getList(Integer pIdCaso, Integer pIdTipoConsulta) {
        return this.casoConsultaJsonRepository.findByIdCasoAndIdTipoConsulta(pIdCaso, pIdTipoConsulta);
    }

    public void createCasoConsultaJson(CasoConsultaJsonDto pDatos) throws SQLException {

        SipfConsultaCasoJson vSave = SipfConsultaCasoJson.builder()
                .fechaModifica(new Date())
                .idCaso(pDatos.getIdCaso())
                .ipModifica("10.10.10.10")
                .idTipoConsulta(pDatos.getIdTipoConsulta())
                .json(pDatos.getJson().toString())
                .build();
        this.casoConsultaJsonRepository.save(vSave);
    }

}
