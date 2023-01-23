/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaAlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaParamDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaDetalleRechazadoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DetalleDenunciaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaAprobadaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaGuardadaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetalleDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GerenciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosMasivosProjections;
import gt.gob.sat.sat_ifi_sipf.repositories.DenunciaGrabadaRepository;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import static java.lang.Integer.parseInt;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author rabaraho
 */
@Service
@Transactional
@Slf4j
public class DenunciaGrabadaService {

    @Autowired
    DenunciaGrabadaRepository denunciaGrabadaRepository;
    @Autowired
    AlcancesService alcancesService;
    @Autowired
    GeneralServices generalServices;
    @Autowired
    PresenciasFiscalesService starProcess;
    @Autowired
    ContiendoACSService contiendoACSService;

    public List<DenunciaGuardadaProjection> getWhole() {
        return this.denunciaGrabadaRepository.findWhole();
    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @param nit
     * @return
     */
    @Transactional(readOnly = true)
    public List<DenunciaAprobadaProjection> getApplyComplaints(String nit) {
        //log.debug("Obtiene un listado de las denuncias en estado APLICA");
        return this.denunciaGrabadaRepository.getApplyComplaints(nit);
    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @param nit
     * @return
     */
    @Transactional(readOnly = true)
    public List<DenunciaAprobadaProjection> getRejectedComplaints(String nit) {
        //log.debug("Obtiene un listado de las denuncias en estado NO APLICA");
        return this.denunciaGrabadaRepository.getRejectedComplaints(nit);
    }

    /**
     * @author lfvillag (Luis Villagran)
     * @param dtodenuncia
     * @param id
     * @return
     */
    @Transactional
    public SipfDenunciaGrabada setApplyManagementsComplaints(DetalleDenunciaDto dtodenuncia, String id) {
        final SipfDenunciaGrabada denuncia = denunciaGrabadaRepository.findByCorrelativo(id).orElse(null);
        //Estado 957 se remplaza por Constante APPLY
        denuncia.setEstado(Catalog.Complaint.Status.APPLY);
        denuncia.setIdProceso(dtodenuncia.getIdproceso());
        denuncia.setDireccionFiscalDenunciado(dtodenuncia.getDireccion());
        denuncia.setRegion(dtodenuncia.getIdregion());
        return denunciaGrabadaRepository.save(denuncia);
    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @param dtodenuncia
     * @param id
     * @return
     */
    @Transactional
    public void setRejectedManagementsComplaints(DenunciaDetalleRechazadoDto dtoDenuncia, String id) {
        final SipfDenunciaGrabada denuncia = denunciaGrabadaRepository.findByCorrelativo(id).orElse(null);
        //log.debug("hola" + dtoDenuncia.getIdregion());
        //se remplaza estado "958" por Constante REJECTED
        denuncia.setEstado(Catalog.Complaint.Status.REJECTED);
        denuncia.setIdProceso(dtoDenuncia.getIdproceso());
        denuncia.setFechaRechazo(new Date());
        denuncia.setRegion(dtoDenuncia.getIdregion());
        denunciaGrabadaRepository.save(denuncia);
    }

    @Transactional
    public void setEditComplaints(DenunciaDetalleRechazadoDto dtoDenuncia, String id) {
        final SipfDenunciaGrabada denuncia = denunciaGrabadaRepository.findByCorrelativo(id).orElse(null);
        //log.debug("hola" + dtoDenuncia.getTipo());
        denuncia.setEstado(dtoDenuncia.getEstado());
        denuncia.setFechaRechazo(new Date());
        denuncia.setIdProceso(dtoDenuncia.getTipo());
        denunciaGrabadaRepository.save(denuncia);
    }

    ///metodo para subir el archivo a ACS
    @Transactional
    public int saveComplaintScope(DenunciaAlcanceDto dtoDenuncia, UserLogged user) {
        final SipfDenunciaGrabada denuncia = denunciaGrabadaRepository.findByCorrelativo(dtoDenuncia.getCorrelativo()).orElse(null);
        final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idMasivo", denuncia.getCorrelativo());
        object.put("idPresencia", denuncia.getCorrelativo());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "default");
        final StartProcessDto processId = contiendoACSService.startProces(3, body);
        //Se cambio el estado "18" por el estado "1091"
        denuncia.setEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
        denuncia.setIdProcesoAlcance(new Integer(processId.getId()));
        denunciaGrabadaRepository.save(denuncia);

        AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(dtoDenuncia.getIdTipoAlcance());
        alcance.setSecciones(dtoDenuncia.getSecciones());
        alcance = alcancesService.createAlcance(alcance, user);
        denuncia.setIdAlcance(alcance.getIdAlcance());
        return alcance.getIdAlcance();
    }

    @Transactional
    public Integer saveComplaintScopes(DenunciaAlcanceDto dtoDenuncia, List<MultipartFile> files, UserLogged user) {
        final SipfDenunciaGrabada denuncia = denunciaGrabadaRepository.findByCorrelativo(dtoDenuncia.getCorrelativo()).orElse(null);
        AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(dtoDenuncia.getIdTipoAlcance());
        alcance.setSecciones(dtoDenuncia.getSecciones());
        alcance = alcancesService.createAlcance(alcance, user);
        final Integer idAlcance = alcance.getIdAlcance();

        final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idMasivo", idAlcance.toString());
        object.put("idPresencia", idAlcance.toString());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "default");

        final StartProcessDto processId = contiendoACSService.startProces(3, body);

        if (dtoDenuncia.getCorrelativos() != null && !dtoDenuncia.getCorrelativos().isEmpty()) {
            dtoDenuncia.getCorrelativos().forEach((k) -> {
                Optional<SipfDenunciaGrabada> denunciaAnexada = denunciaGrabadaRepository.findByCorrelativo(k);
                if (denunciaAnexada.isPresent()) {
                    //Se cambio el estado "18" por el estado "1091"
                    denunciaAnexada.get().setEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
                    denunciaAnexada.get().setIdProcesoAlcance(new Integer(processId.getId()));
                    denunciaAnexada.get().setIdAlcance(idAlcance);
                    denunciaGrabadaRepository.save(denunciaAnexada.get());
                }
            });

        } else {
            //Se cambio el estado "18" por el estado "1091"
            denuncia.setEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
            denuncia.setIdProcesoAlcance(new Integer(processId.getId()));
            denuncia.setIdAlcance(alcance.getIdAlcance());
            denunciaGrabadaRepository.save(denuncia);
        }

        Map<String, Object> fileBody = new HashMap<>();
        fileBody.put("id", idAlcance);
        JSONObject jsonData = new JSONObject(fileBody);
        NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.ALCANCEMASIVO, jsonData);
        contiendoACSService.uploadFiles(nodos.getId(), files);

        return idAlcance;
    }

    @Transactional
    public DenunciaAlcanceDto editComplaintScopes(DenunciaAlcanceDto dtoDenuncia, List<MultipartFile> files, Integer idAlcance, UserLogged user) {

        final List<SipfDenunciaGrabada> denuncia = denunciaGrabadaRepository.findByIdAlcance(idAlcance);

        AlcanceDto alcance = new AlcanceDto();
        //Se cambio el estado "18" por el estado "1091"
        alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
        alcance.setSecciones(dtoDenuncia.getSecciones());
        alcance.setIdAlcance(idAlcance);
        alcance = alcancesService.updateAlcances(alcance, user);
        log.debug("Alcance a guardar:" + alcance);

        if (dtoDenuncia.getCorrelativos() != null && !dtoDenuncia.getCorrelativos().isEmpty()) {
            dtoDenuncia.getCorrelativos().forEach((k) -> {
                Optional<SipfDenunciaGrabada> denunciaAnexada = denunciaGrabadaRepository.findByCorrelativo(k);
                if (denunciaAnexada.isPresent()) {
                    //Se cambio el estado "18" por el estado "1091"
                    denunciaAnexada.get().setEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
                    denunciaAnexada.get().setIdProcesoAlcance(denuncia.get(0).getIdProcesoAlcance());
                    denunciaAnexada.get().setIdAlcance(idAlcance);
                    denunciaGrabadaRepository.save(denunciaAnexada.get());
                    log.debug("denuncia indexada:" + denunciaAnexada.get().getIdProcesoAlcance());
                }
            });

        } else {
            //Se cambio el estado "18" por el estado "1091"
            denuncia.get(0).setEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
            denuncia.get(0).setIdProcesoAlcance(denuncia.get(0).getIdProcesoAlcance());
            denuncia.get(0).setIdAlcance(alcance.getIdAlcance());
            log.debug("denuncia:" + denuncia.get(0));
            denunciaGrabadaRepository.saveAll(denuncia);
        }

        Map<String, Object> fileBody = new HashMap<>();
        fileBody.put("id", idAlcance);
        JSONObject jsonData = new JSONObject(fileBody);
        NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.ALCANCEMASIVO, jsonData);
        log.debug("ruta:" + nodos.getId());
        log.debug("Alcance" + idAlcance);
        this.contiendoACSService.uploadFiles(nodos.getId(), files);

       /* final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idMasivo", idAlcance.toString());
        object.put("idPresencia", idAlcance.toString());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "default");*/
        
        Map<String, Object> post = new HashMap<>();
        JSONObject formulario = new JSONObject();
        formulario.put("idMasivo",idAlcance.toString());
        formulario.put("idPresencia", idAlcance.toString());
        post.put("jsonFormulario", formulario.toJSONString());
        post.put("resultadoFormulario", "default");
        JSONObject json = new JSONObject(post);
        TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
        dtoComplete.setOutcome("\"default\"");
        dtoComplete.setValues(json);
        contiendoACSService.completaTaksByIntanceId(denuncia.get(0).getIdProcesoAlcance().toString(), dtoComplete);
        /* final StartProcessDto processId = contiendoACSService.startProces(3, body);*/

        return dtoDenuncia;
    }

    public List<DenunciaAprobadaProjection> getApplyComplaintsByGerencyProcess(Integer idR, Integer idP) {
        //log.debug("Obtiene el listado de denuncias en base a su gerencia y proceso masivo");
        return this.denunciaGrabadaRepository.getApplyComplaintsByGerencyProcess(idR, idP);
    }

    public List<DenunciaAprobadaProjection> getApplyComplaintsCabinet(Integer idR) {
        //log.debug("Obtiene el listado de denuncias gabinete en base a su gerencia");
        return this.denunciaGrabadaRepository.getApplyComplaintsCabinet(idR);
    }

    public List<ProcesosDenunciaProjection> getCatalogProcessComplaints(Integer idCatalogo) {
        //log.debug("Obtiene el listado de denuncias en base a su gerencia y proceso masivo");
        return this.denunciaGrabadaRepository.getCatalogProcessComplaints(idCatalogo);
    }

    public List<BandejaDenunciasProjection> getComplaintsGabineteProcess() {
        //log.debug("Obtiene el listado de denuncias que tengan un proceso masivo Gabinete.");
        return this.denunciaGrabadaRepository.getComplaintsGabineteProcess();
    }

    /**
     * @author lfvillag (Luis Villagran)
     * @description Servicio utilizado para obtener las denuncias rechazadas por
     * fecha.
     */
    public List<DenunciaAprobadaProjection> getRejectedComplaintsForDate(String nit, String fecha, String fechaFin) {
        //log.debug("Obtiene el listado de denuncias rechazadas en base a parametros de fechas.");
        return this.denunciaGrabadaRepository.getRejectedComplaintsForDate(nit, fecha, fechaFin);
    }

    /**
     * @author lfvillag (Luis Villagran)
     * @param pData
     * @return
     */
    public List<ProcesosMasivosProjections> getProcessMasive() {
        //log.debug("Obtiene el listado de procesos masivos que tiene una programacion de procesos Masivos");
        return this.denunciaGrabadaRepository.getProcessMasive();
    }

    public List<ProcesosMasivosProjections> getAllProcessMasive() {
        //log.debug("Obtiene el listado de procesos masivos que tiene una programacion de procesos Masivos");
        return this.denunciaGrabadaRepository.getAllProcessMassive();
    }

    public List<ProcesosMasivosProjections> getStateComplaints() {
        //log.debug("Obtiene el listado de estados que tiene una denuncia");
        return this.denunciaGrabadaRepository.getStateComplaints();
    }

    public List<DenunciaGrabadaDto> saveAll(List<DenunciaGrabadaDto> pData) {
   
        // se remplazo el estado 991 por Constante ENTRY
        pData.forEach(item -> item.setEstado(Catalog.Complaint.Status.ENTRY));
        List<SipfDenunciaGrabada> denuncias = new ObjectMapper().convertValue(pData, new TypeReference<List<SipfDenunciaGrabada>>() {
        });
        this.denunciaGrabadaRepository.saveAll(denuncias);
        return pData;
    }

    public List<SipfDenunciaGrabada> getComplaintByDateRange(DenunciaGrabadaParamDto pParam) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return this.denunciaGrabadaRepository.findByFechaGrabacionBetween(dateFormat.parse(pParam.getPFechaInicio()), dateFormat.parse(pParam.getPFechaFin()));
        } catch (ParseException ex) {
            Logger.getLogger(DenunciaGrabadaService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public List<DenunciaGuardadaProjection> getComplaintByDateRangeNotAssigned(DenunciaGrabadaParamDto pParam) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        try {

            Date endDate;
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(dateFormat.parse(pParam.getPFechaFin()));
            calendar.add(Calendar.HOUR_OF_DAY, 23);
            calendar.add(Calendar.MINUTE, 59);
            calendar.add(Calendar.SECOND, 59);
            endDate = calendar.getTime();
            // estado 991 es remplazado por Constante ENTRY 
            Integer vStatus = parseInt(Catalog.Complaint.Status.ENTRY);
            List<DenunciaGuardadaProjection> list = this.denunciaGrabadaRepository.findByEstadoAndFechaGrabacionBetween(vStatus, dateFormat.parse(pParam.getPFechaInicio()), endDate);

            return list;
        } catch (ParseException ex) {
            Logger.getLogger(DenunciaGrabadaService.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public SipfDenunciaGrabada saveNewComplaint(DenunciaGrabadaDto pDatos) {
        SipfDenunciaGrabada denounceToSave;
        denounceToSave = new ObjectMapper().convertValue(pDatos, new TypeReference<SipfDenunciaGrabada>() {
        });

        denounceToSave.setCorrelativo("D-" + pDatos.getCorrelativo() + "-" + String.format("%06d", this.generalServices.nextValSequence("sat_ifi_sipf.sipf_correlativo_denuncia")) + "-" + Calendar.getInstance().get(Calendar.YEAR));
        // estado inicial de creacion de denuncia 
        // estado 991 es remplazado por Constante ENTRY 
        Integer vStatus = parseInt(Catalog.Complaint.Status.ENTRY);
        denounceToSave.setEstado(vStatus);
        denounceToSave.setFechaGrabacion(new Date());
        return this.denunciaGrabadaRepository.save(denounceToSave);

    }

    /*
    * @author Luis Villagran (lfvillag)
    * Servicio que trae las denuncias que tiene un insumo asignado a un operador.
     */
    @Transactional(readOnly = true)
    public List<BandejaDenunciasProjection> getComplaints(String nit) {

        return denunciaGrabadaRepository.getComplaints(nit);
    }

    @Transactional(readOnly = true)
    public List<AlcanceDenunciaProjection> getScope(String nit) {
        return denunciaGrabadaRepository.getScope(nit);
    }

    /**
     * @author Luis Villagrán (lfvillag) Servicio para obtener el detalle de una
     * denuncia.
     */
    @Transactional(readOnly = true)
    public List<DetalleDenunciasProjection> getDetailComplaint(String id) {

        return denunciaGrabadaRepository.getDetailComplaint(id);
    }

    /**
     * @author Luis Villagran (lfvillag) Servicio para traer las gerencias
     * existentes.
     */
    @Transactional(readOnly = true)
    public List<GerenciasProjection> getManagements() {

        return denunciaGrabadaRepository.getManagements();
    }

}
