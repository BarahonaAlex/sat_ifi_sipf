/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PresenciasFiscalesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.services.AlcancesService;
import gt.gob.sat.sat_ifi_sipf.models.SipfPresenciasFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.PresenciasFiscalesProjetion;
import gt.gob.sat.sat_ifi_sipf.repositories.PresenciasFiscalesRepository;
import static gt.gob.sat.sat_ifi_sipf.utils.GeneralUtils.isSameOrNull;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author jdaldana
 */
@Transactional
@Service
@Slf4j
public class PresenciasFiscalesService {

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ContiendoACSService contenidoACSService;

    @Autowired
    AlcancesService alcancesService;

    @Autowired
    private GeneralServices generalService;

    @Autowired
    PresenciasFiscalesRepository presenciasFiscalesRepository;

    public List<PresenciasFiscalesProjetion> FindPresenciasAll(String nitP) {
        return presenciasFiscalesRepository.findAllPresencias(nitP);
    }

    public PresenciasFiscalesDto CreateFormulario(PresenciasFiscalesDto data, UserLogged user) {
        SipfPresenciasFiscales presencia = presenciasFiscalesRepository.save(
                SipfPresenciasFiscales.builder()
                        .lugarEjecucion(data.getLugarEjecucion())
                        .idEstado(177)
                        .idGerencia(data.getIdGerencia())
                        .fechaFin(data.getFechaFin())
                        .lugarDepartamental(data.getLugarDepartamental())
                        .meta(data.getMeta())
                        .idPrograma(data.getIdPrograma())
                        .fechaInicio(data.getFechaInicio())
                        .fechaModifica(new Date())
                        .usuarioCreacion(user.getNit())
                        .usuarioModifica(user.getNit())
                        .build());
        data.setIdFormulario(presencia.getIdFormulario());
        return data;
    }

    public PresenciasFiscalesProjetion FindIdPresencia(Integer id) {
        return presenciasFiscalesRepository.findByIdFormulario(id);
    }

    @Transactional(rollbackFor = Exception.class)
    public PresenciasFiscalesDto updatePresencia(Integer idPresencia, PresenciasFiscalesDto data) {
        Optional<SipfPresenciasFiscales> oldCase = presenciasFiscalesRepository.findById(idPresencia);
        if (oldCase.isPresent()) {
            SipfPresenciasFiscales tmp = oldCase.get();
            tmp.setIdEstado(isSameOrNull(data.getIdEstado(), tmp.getIdEstado()) ? tmp.getIdEstado() : data.getIdEstado());
            tmp.setIdGerencia(isSameOrNull(data.getIdGerencia(), tmp.getIdGerencia()) ? tmp.getIdGerencia() : data.getIdGerencia());
            tmp.setIdProceso(isSameOrNull(data.getIdProceso(), tmp.getIdProceso()) ? tmp.getIdProceso() : data.getIdProceso());
            tmp.setIdAlcance(isSameOrNull(data.getIdAlcance(), tmp.getIdAlcance()) ? tmp.getIdAlcance() : data.getIdAlcance());
            tmp.setLugarEjecucion(isSameOrNull(data.getLugarEjecucion(), tmp.getLugarEjecucion()) ? tmp.getLugarEjecucion() : data.getLugarEjecucion());
            tmp.setLugarDepartamental(isSameOrNull(data.getLugarDepartamental(), tmp.getLugarDepartamental()) ? tmp.getLugarDepartamental() : data.getLugarDepartamental());
            tmp.setCorrelativoAprobacion(isSameOrNull(data.getCorrelativoAprobacion(), tmp.getCorrelativoAprobacion()) ? tmp.getCorrelativoAprobacion() : data.getCorrelativoAprobacion());
            tmp.setMeta(isSameOrNull(data.getMeta(), tmp.getMeta()) ? tmp.getMeta() : data.getMeta());
            tmp.setIdPrograma(isSameOrNull(data.getIdPrograma(), tmp.getIdPrograma()) ? tmp.getIdPrograma() : data.getIdPrograma());
            presenciasFiscalesRepository.save(tmp);
            data.setIdProceso(tmp.getIdProceso());
            data.setIdFormulario(tmp.getIdFormulario());
        }
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    public PresenciasFiscalesDto startProcessPresencias(PresenciasFiscalesDto data, List<MultipartFile> file, UserLogged user) {
         AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(data.getIdTipoAlcance());
        alcance.setSecciones(data.getSecciones());
        alcance = alcancesService.createAlcance(alcance, user);
        final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idMasivo", alcance.getIdAlcance());
        object.put("idPresencia", data.getIdPresencia());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "default");

        final StartProcessDto processId = contenidoACSService.startProces(3, body);
       

        data.setIdProceso(processId.getId());
        data.setIdEstado(18);
        data.setIdAlcance(alcance.getIdAlcance());
        updatePresencia(data.getIdPresencia(), data);
        Map<String, Object> json = new HashMap<>();
        json.put("id", data.getIdAlcance().toString());
         log.debug(alcance.getIdAlcance()+"");
        log.debug(json.toString());
        this.contenidoACSService.uploadFiles(contenidoACSService.getNodosACSBySiteAndPath(TipoRuta.ALCANCEMASIVO, json).getId(), file);
        // this.alcancesService.deleteDocument();
        return data;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean updatePresencias(Integer id, PresenciasFiscalesDto data, UserLogged user) {
        //log.debug("id:" +id);
        updatePresencia(id, data);
        /* AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(data.getIdTipoAlcance());
        alcance.setSecciones(data.getSecciones());
        alcance.setIdAlcance(data.getIdAlcance());
        alcance.setIdEstado(data.getIdEstado());
        alcance = alcancesService.updateAlcances(alcance, user);process*/
        return true;
    }
     @Transactional(rollbackFor = Exception.class)
    public boolean updateProcessPresencias(Integer id, PresenciasFiscalesDto data,List<MultipartFile> file, UserLogged user) {
        //log.debug("id:" +id);
        data.setIdEstado(18);
        updatePresencia(data.getIdPresencia(), data);

        Optional<SipfPresenciasFiscales> oldCase = presenciasFiscalesRepository.findById(data.getIdPresencia());
        SipfPresenciasFiscales tmp = oldCase.get();
        if (tmp.getIdProceso() != null) {
            Map<String, Object> post = new HashMap<>();
            JSONObject formulario = new JSONObject();
            formulario.put("idMasivo", id);
            formulario.put("idPresencia", data.getIdPresencia());
            post.put("jsonFormulario", formulario.toJSONString());
            post.put("resultadoFormulario", "default");
            JSONObject json = new JSONObject(post);
            TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
            dtoComplete.setOutcome("\"default\"");
            dtoComplete.setValues(json);
            contenidoACSService.completaTaksByIntanceId(tmp.getIdProceso(), dtoComplete);
        }
        Map<String, Object> json = new HashMap<>();
        json.put("id", data.getIdAlcance().toString());
        this.contenidoACSService.uploadFiles(contenidoACSService.getNodosACSBySiteAndPath(TipoRuta.ALCANCEMASIVO, json).getId(), file);
        /* AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(data.getIdTipoAlcance());
        alcance.setSecciones(data.getSecciones());
        alcance.setIdAlcance(data.getIdAlcance());
        alcance.setIdEstado(data.getIdEstado());
        alcance = alcancesService.updateAlcances(alcance, user);process*/
        return true;
    }
}
