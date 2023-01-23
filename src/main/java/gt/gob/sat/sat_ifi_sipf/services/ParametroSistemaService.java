/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfParametroSistema;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ParametroSistemaRepository;
import java.util.List;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author adftopvar
 */
@Transactional
@Service
@Slf4j
public class ParametroSistemaService {

    @Autowired
    private ParametroSistemaRepository parametroSistemaRepository;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    @Transactional(readOnly = true)
    public List<SipfParametroSistema> getParams() {
        return parametroSistemaRepository.findAllByOrderByIdAsc();
    }

    public SipfParametroSistema updateParam(Integer id, String value, UserLogged logged) {
        final SipfParametroSistema param = parametroSistemaRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.PARAM_NOT_FOUND.getText(id))
        );
        final JsonObject dataParam = new JsonObject();
        dataParam.add("valorAnterior", new Gson().toJsonTree(param));

        param.setValue(value.replaceAll("\"", " "));
        param.setModifiedBy(logged.getLogin());
        param.setModifiedAt(new Date());
        param.setModifiedFrom(logged.getIp());

        parametroSistemaRepository.save(param);

        dataParam.add("valorNuevo", new Gson().toJsonTree(param));

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_parametro_sistema")
                        .idCambioRegistro(String.valueOf(param.getId()))
                        .idTipoOperacion(405)
                        .data(dataParam.toString())
                        .fechaModifica(new Date())
                        .usuarioModifica(logged.getLogin())
                        .ipModifica(logged.getIp())
                        .build()
        );

        return param;

    }

}
