/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoManualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParametroAccionInsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SipfInsumoBitacoraCambioEstadoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfDetalleCaso;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoCaso;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoCasoId;
import gt.gob.sat.sat_ifi_sipf.models.SipfInsumo;
import gt.gob.sat.sat_ifi_sipf.projections.CasoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.InsumoProjecionStatus;
import gt.gob.sat.sat_ifi_sipf.projections.InsumoProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.DetalleCasoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ImpuestoCasoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.InsumoRepository;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author rabaraho
 */
@Service
@Slf4j
public class InsumoService {
    
    @Autowired
    private InsumoRepository insumoRepository;
    
    @Autowired
    private CasosRepository casosRepository;
    
    @Autowired
    private CasosService casosService;
    
    @Autowired
    private DetalleCasoRepository detalleCasoRepository;
    
    @Autowired
    private ImpuestoCasoRepository impuestoCasoRepository;
    
    @Autowired
    ContiendoACSService contiendoACSService;
    
    @Autowired
    private HistorialComentariosRepository historialComentariosRepository;
    
    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;
    
    @Autowired
    private InsumoBitacoraCambioEstadoService insumoBitacoraCambioEstadoService;
    
    @Transactional(rollbackFor = Exception.class)
    public InsumoDto saveInput(InsumoManualDto pInsumoDto, UserLogged user, boolean masivo) {
        SipfInsumo vInsumo = SipfInsumo.builder()
                .descripcion(masivo ? pInsumoDto.getDescripcion() : "Solicitud externa")
                .fechaModifica(new Date())
                .idDepartamento(masivo ? pInsumoDto.getIdDepartamento() : 172)
                .idEstado(masivo ? pInsumoDto.getIdEstado() : Catalog.Input.Status.PENDING_PUBLICATION)
                .idGerencia(masivo ? pInsumoDto.getIdGerencia() : 40)
                .idTipoInsumo(masivo ? pInsumoDto.getIdTipoInsumo() : null)
                .ipModifica(user.getIp())
                .nitEncargado(pInsumoDto.getNitEncargado())
                .nombreInsumo(masivo ? pInsumoDto.getNombreInsumo() : "Solicitud Externa")
                .usuarioModifica(user.getLogin())
                .build();
        
        this.insumoRepository.save(vInsumo);
        pInsumoDto.setIdInsumo(vInsumo.getIdInsumo());
        pInsumoDto.setIdEstado(vInsumo.getIdEstado());
        return pInsumoDto;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public InsumoDto createInput(InsumoManualDto dto, UserLogged user) {
        dto.setIdEstado(dto.getCasos().stream().filter(item -> "X".equals(item.getRequiereDocumentacion())).count() == 0 ? Catalog.Input.Status.PENDING_PUBLICATION : Catalog.Input.Status.PENDING_DOCUMENTATION);
        
        InsumoDto saveData = saveInput(dto, user, true);
        
        dto.getCasos().forEach(data
                -> {
            SipfCasos newCase = casosRepository.save(
                    SipfCasos.builder()
                            .nitContribuyente(data.getNitContribuyente())
                            .montoRecaudado(data.getMontoRecaudado())
                            .periodoRevisionInicio(data.getPeriodoRevisionInicio())
                            .periodoRevisionFin(data.getPeriodoRevisionFin())
                            .idGerencia(dto.getIdGerencia())
                            .idEstado("X".equals(data.getRequiereDocumentacion()) ? Catalog.Case.Status.PENDING_DOCUMENTATION : Catalog.Case.Status.PENDING_PUBLICATION)
                            .idDepartamento(dto.getIdDepartamento())
                            .usuarioModifica(user.getLogin())
                            .fechaModifica(new Date())
                            .ipModifica(user.getIp())
                            .idInsumo(saveData.getIdInsumo())
                            .build()
            );
            
            impuestoCasoRepository.saveAll(
                    data.getImpuestos().stream().map(t
                            -> {
                        SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                        caseTax.setId(new SipfImpuestoCasoId(newCase.getIdCaso(), t));
                        caseTax.setUsuarioModifica(user.getLogin());
                        caseTax.setFechaModifica(new Date());
                        caseTax.setIpModifica(user.getIp());
                        return caseTax;
                    }).collect(Collectors.toList()));
            
            detalleCasoRepository.save(
                    SipfDetalleCaso.builder()
                            .idCaso(newCase.getIdCaso())
                            .tipoCaso(dto.getIdTipoCaso())
                            .loginProfesional(data.getLoginProfesional())
                            .usuarioModifica(user.getLogin())
                            .fechaModifica(new Date())
                            .ipModifica(user.getIp())
                            .requiereDocumentacion("X".equals(data.getRequiereDocumentacion()))
                            .build());
            
        });

        /*final JSONObject body = new JSONObject();
        
        body.put("jsonFormulario", saveData.toJson());
        body.put("resultadoFormulario", "interna");
        
        final StartProcessDto processId = contiendoACSService.startProces(1, body);
        saveData.setIdProceso(processId.getId());
        modifyInput(saveData, saveData.getIdInsumo(), user);*/
        return saveData;
    }
    
    @Transactional(rollbackFor = Exception.class)
    public InsumoDto startInputProcess(InsumoManualDto dto, UserLogged user) {
        dto.setIdEstado(dto.getCasos().stream().filter(item -> "X".equals(item.getRequiereDocumentacion())).count() == 0 ? Catalog.Input.Status.PENDING_PUBLICATION : Catalog.Input.Status.PENDING_DOCUMENTATION);
        
        Optional<SipfInsumo> saveData = this.insumoRepository.findById(dto.getIdInsumo());
        
        saveData.get().setIdEstado(Catalog.Input.Status.PUBLISHED);
        final JSONObject body = new JSONObject();
        
        body.put("jsonFormulario", saveData.get());
        body.put("resultadoFormulario", "interna");
        
        final StartProcessDto processId = contiendoACSService.startProces(1, body);
        
        saveData.get().setIdProceso(processId.getId());
        
        this.insumoRepository.save(saveData.get());
        
        return dto;
    }

    //@Transactional(rollbackFor = Exception.class)
    public InsumoDto createInputExterna(InsumoManualDto dto, List<MultipartFile> archivo, UserLogged user) {
        
        InsumoDto saveData = saveInput(dto, user, false);
        //log.debug("saveData" + saveData);

        if (dto.getCasos() != null) {
            dto.getCasos().forEach(data
                    -> {
                SipfCasos newCase = casosRepository.save(
                        SipfCasos.builder()
                                .nitContribuyente(data.getNitContribuyente())
                                .montoRecaudado(data.getMontoRecaudado())
                                .periodoRevisionInicio(data.getPeriodoRevisionInicio())
                                .periodoRevisionFin(data.getPeriodoRevisionFin())
                                .idGerencia(data.getGerencia())
                                .idEstado("X".equals(data.getRequiereDocumentacion()) ? Catalog.Case.Status.PENDING_DOCUMENTATION : Catalog.Case.Status.PENDING_PUBLICATION)
                                .idDepartamento(174)
                                .usuarioModifica(user.getLogin())
                                .fechaModifica(new Date())
                                .ipModifica(user.getIp())
                                .idInsumo(saveData.getIdInsumo())
                                .idZona(data.getZona())
                                .idMunicipio(data.getMunicipioid())
                                .build()
                );
                if (data.getImpuestos() != null) {
                    impuestoCasoRepository.saveAll(
                            data.getImpuestos().stream().map(t
                                    -> {
                                SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                                caseTax.setId(new SipfImpuestoCasoId(newCase.getIdCaso(), t));
                                caseTax.setUsuarioModifica(user.getLogin());
                                caseTax.setFechaModifica(new Date());
                                caseTax.setIpModifica(user.getIp());
                                return caseTax;
                            }).collect(Collectors.toList()));
                }
                detalleCasoRepository.save(
                        SipfDetalleCaso.builder()
                                .idCaso(newCase.getIdCaso())
                                .tipoCaso(dto.getIdTipoCaso())
                                .usuarioModifica(user.getLogin())
                                .fechaModifica(new Date())
                                .ipModifica(user.getIp())
                                .build());
            });
            
        }

        //log.debug("data" + saveData.getIdInsumo());
        List<CasoProjection> casos = casosRepository.findByIdInsumo(saveData.getIdInsumo());
        //log.debug("casos" + casos.get(0).getidCaso());
        casos.forEach(data
                -> {
            Map<String, Object> post = new HashMap<>();
            post.put("caso", data.getidCaso());
            post.put("carpeta", "Archivos Respaldo");
            JSONObject json = new JSONObject(post);
            //log.debug("hola" + json.toString());
            NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.CASOS, json);
            //log.debug("nodos" + nodos.getId());
            contiendoACSService.uploadFiles(nodos.getId(), archivo);
        });
        final JSONObject body = new JSONObject();
        
        body.put("jsonFormulario", saveData.toJson());
        body.put("resultadoFormulario", "interna");
        
        final StartProcessDto processId = contiendoACSService.startProces(1, body);
        saveData.setIdProceso(processId.getId());
        modifyInput(saveData, saveData.getIdInsumo(), user);
        return saveData;
        
    }
    
    public InsumoProjection findByIdInsumo(Integer pIdInsumo) {
        return this.insumoRepository.findByIdInsumo(pIdInsumo);
    }
    
    public List<InsumoProjection> findByNitEncargado(String pNitEncargado) {
        
        return this.insumoRepository.findByNitEncargado(pNitEncargado);
    }
    
    @Transactional
    public ResponseEntity<InsumoDto> modifyInput(InsumoDto pInputDto, Integer pIdInput, UserLogged user) {
        Optional<SipfInsumo> vInputModify = this.insumoRepository.findById(pIdInput);
        if (vInputModify.isPresent()) {
            
            if (pInputDto.getIdEstado() == Catalog.Input.Status.PUBLISHED && vInputModify.get().getIdEstado() == Catalog.Input.Status.REFUSED) {
                
                List<SipfCasos> cases = new Gson().fromJson(historialOperacionesRepository.getCaseLastStateByInput(pIdInput).getData(), new TypeToken<List<SipfCasos>>() {
                }.getType());
                cases.forEach((caso) -> {
                    if (casosRepository.findById(caso.getIdCaso()).isPresent()) {
                        SipfCasos oldStateCase = casosRepository.findById(caso.getIdCaso()).get();
                        oldStateCase.setIdEstado(caso.getIdEstado());
                        oldStateCase.setFechaModifica(new Date());
                        oldStateCase.setUsuarioModifica(user.getLogin());
                        oldStateCase.setIpModifica(user.getIp());
                        casosRepository.save(oldStateCase);
                    }
                });
                
                final JSONObject valuesBodyTaks = new JSONObject();
                valuesBodyTaks.put("idEstado", pInputDto.getIdEstado());
                final JSONObject bodyTaks = new JSONObject();
                bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                bodyTaks.put("resultadoFormulario", "Corregido");
                
                contiendoACSService.completaTaksByIntanceId(vInputModify.get().getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
            }
            
            vInputModify.get().setFechaModifica(new Date());
            vInputModify.get().setIdEstado((pInputDto.getIdEstado() != null) ? pInputDto.getIdEstado() : vInputModify.get().getIdEstado());
            vInputModify.get().setIdDepartamento((pInputDto.getIdDepartamento() != null) ? pInputDto.getIdDepartamento() : vInputModify.get().getIdDepartamento());
            vInputModify.get().setIdGerencia((pInputDto.getIdGerencia() != null) ? pInputDto.getIdGerencia() : vInputModify.get().getIdGerencia());
            vInputModify.get().setIdTipoInsumo((pInputDto.getIdTipoInsumo() != null) ? pInputDto.getIdTipoInsumo() : vInputModify.get().getIdTipoInsumo());
            vInputModify.get().setIpModifica(user.getIp());
            vInputModify.get().setIdEstadoAnterior(pInputDto.getIdEstadoAnterior());
            //vInputModify.get().setNitEncargado((!StringUtils.isEmpty(pInputDto.getNitEncargado())) ? pInputDto.getNitEncargado() : vInputModify.get().getNitEncargado());
            vInputModify.get().setNombreInsumo(!StringUtils.isEmpty(pInputDto.getNombreInsumo()) ? pInputDto.getNombreInsumo() : vInputModify.get().getNombreInsumo());
            vInputModify.get().setDescripcion(!StringUtils.isEmpty(pInputDto.getDescripcion()) ? pInputDto.getDescripcion() : vInputModify.get().getDescripcion());
            vInputModify.get().setIdProceso(!StringUtils.isEmpty(pInputDto.getIdProceso()) ? pInputDto.getIdProceso() : vInputModify.get().getIdProceso());
            vInputModify.get().setIdUnidadAdministrativa(pInputDto.getIdUnidadAdministrativa() != null ? pInputDto.getIdUnidadAdministrativa() : null);
            vInputModify.get().setUsuarioModifica(user.getLogin());
            this.insumoRepository.save(vInputModify.get());
            
            if (pInputDto.getIdEstado() == 179) {//rechazo
                CasosDto casoNuevo = new CasosDto();
                casoNuevo.setGerencia(vInputModify.get().getIdGerencia());
                // casoNuevo.setImpuesto(vInputModify.get().getIdInsumo());
                casoNuevo.setNombreCaso(vInputModify.get().getNombreInsumo());
                casoNuevo.setTipoAlcance(vInputModify.get().getIdDepartamento());
                casoNuevo.setEstado(17);
                this.casosService.updateCaseByInsumo(pIdInput, user);
            }
            if (pInputDto.getIdEstado() == 440) { //Rechazo definitivo 
                alterStateInsumoWithComentary(pIdInput, 440, pInputDto.getIdEstado(), user, pInputDto.getComentario(), 189, "Cancelado");
                this.casosRepository.saveAll(this.casosRepository.findByidInsumoAndIdEstado(pIdInput, Catalog.Input.Status.PUBLISHED).stream().map(k -> {
                    k.setIdEstado(1040);
                    k.setIpModifica(user.getIp());
                    k.setFechaModifica(new Date());
                    k.setUsuarioModifica(user.getLogin());
                    if (!"".equals(k.getIdProceso())) {
                        this.contiendoACSService.cancelTaskByInstanceId(k.getIdProceso());
                    }
                    return k;
                }).collect(Collectors.toList()));
            }
            
            if (pInputDto.getIdEstado() == 439) {//suspension
                this.casosService.updateCaseByInsumo(pIdInput, user);
            }
            
            return ResponseEntity.ok(pInputDto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    public ResponseEntity<InsumoDto> declineInput(InsumoDto pInputDto, Integer pIdInput, UserLogged user) {
        pInputDto.setIdEstado(440);
        this.modifyInput(pInputDto, pIdInput, user);
        return ResponseEntity.ok(pInputDto);
    }
    
    public ResponseEntity<InsumoDto> correctInput(InsumoDto pInputDto, Integer pIdInput, UserLogged user) {
        pInputDto.setIdEstado(Catalog.Input.Status.PUBLISHED);
        this.modifyInput(pInputDto, pIdInput, user);
        return ResponseEntity.ok(pInputDto);
    }
    
    public List<InsumoProjecionStatus> findAllAuthorizer(List<Integer> pIdEstado, String pNit) {
        return this.insumoRepository.findByStatusAuthorizer(pIdEstado, pNit);
    }
    
    public List<InsumoProjecionStatus> findAllApprover(List<Integer> pIdEstado, String pNit) {
        return this.insumoRepository.findByStatusApprover(pIdEstado, pNit);
    }
    
    public List<InsumoProjecionStatus> findAll(List<Integer> pIdEstado) {
        return this.insumoRepository.findByStatus(pIdEstado);
    }
    
    @Transactional
    public ResponseEntity<InsumoDto> deleteInput(Integer pIdInput, Integer pIdStatus, Integer pIdTipoComentario, ParametroAccionInsumoDto pDatos, UserLogged user) {
        
        List<SipfCasos> vInputList = this.casosRepository.findByidInsumo(pIdInput);
        vInputList.forEach(item -> {
            item.setIdEstado(Catalog.Case.Status.DELETED);
            item.setFechaModifica(new Date());
            item.setUsuarioModifica(user.getNit());
        });
        this.casosRepository.saveAll(vInputList);
        return this.changeStatus(pIdInput, pIdStatus, pIdTipoComentario, pDatos, user);
    }
    
    @Transactional
    public ResponseEntity<InsumoDto> changeStatus(Integer pIdInput, Integer pIdStatus, Integer pIdTipoComentario, ParametroAccionInsumoDto pDatos, UserLogged user) {
        InsumoDto vTempDto = new InsumoDto();
        vTempDto.setIdEstado(pIdStatus);// estado de rechazo definitivo para un insumo

        SipfHistorialComentarios vComment = new SipfHistorialComentarios();
        vComment.setComentarios(pDatos.getComentario());
        vComment.setFechaModifica(new Date());
        vComment.setIdRegistro(pIdInput.toString());
        vComment.setIdTipoComentario(pIdTipoComentario);
        vComment.setUsuarioModifica(user.getLogin());
        vComment.setIpModifica(user.getIp());
        this.historialComentariosRepository.save(vComment);
        Optional<SipfInsumo> vInsumo = this.insumoRepository.findById(pIdInput);
        //log.debug("estado es" + pIdStatus);
        if (pIdStatus == 439) {
            vTempDto.setIdEstadoAnterior(vInsumo.get().getIdEstado());
            
        } else if (pIdStatus == 179) {
            
            vTempDto.setIdEstadoAnterior(179);
            final JSONObject valuesBodyTaks = new JSONObject();
            valuesBodyTaks.put("idEstado", vInsumo.get().getIdEstado());
            final JSONObject bodyTaks = new JSONObject();
            bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
            bodyTaks.put("resultadoFormulario", "rechazar");
            contiendoACSService.completaTaksByIntanceId(vInsumo.get().getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        } else if (pIdStatus == Catalog.Input.Status.DELETED) {
            // do nothing just put the input in status deleted
        } else {
            vTempDto.setIdEstadoAnterior(null);
            final JSONObject valuesBodyTaks = new JSONObject();
            valuesBodyTaks.put("idEstado", vInsumo.get().getIdEstado());
            final JSONObject bodyTaks = new JSONObject();
            bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
            bodyTaks.put("resultadoFormulario", "trasladar");
            contiendoACSService.completaTaksByIntanceId(vInsumo.get().getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        }
        
        return this.modifyInput(vTempDto, pIdInput, user);
    }
    
    public ResponseEntity<?> assignmentInsumo(InsumoDto pInputDto, Integer pIdInput, UserLogged user) {
        Optional<SipfInsumo> vInsumo = this.insumoRepository.findById(pIdInput);
        final JSONObject valuesBodyTaks = new JSONObject();
        valuesBodyTaks.put("idEstado", vInsumo.get().getIdEstado());
        final JSONObject bodyTaks = new JSONObject();
        bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
        bodyTaks.put("resultadoFormulario", "trasladar");
        contiendoACSService.completaTaksByIntanceId(vInsumo.get().getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        this.modifyInput(pInputDto, pIdInput, user);
        return ResponseEntity.ok().build();
    }
    
    public InsumoComentarioDto findByIdInsumoComentario(Integer pIdInsumo, UserLogged user) {
        List<Integer> comentario = new ArrayList<>();
        user.getRoles().forEach((String data) -> {
            switch (data) {
                
                case "AdministrativoSIPFSolicitante":
                    comentario.add(189);
                    comentario.add(190);
                    comentario.add(191);
                    comentario.add(192);
                    break;
                
            }
        });
        return new InsumoComentarioDto(
                insumoRepository.findByIdInsumobyCometario(pIdInsumo),
                historialComentariosRepository.findMaxIdHistorialComentario(pIdInsumo.toString(), comentario)
        );
    }
    
    @Transactional
    public void changeInputStatus(Integer pIdInsumo, Integer idStatus, UserLogged pUser) {
        Optional<SipfInsumo> vInputModify = this.insumoRepository.findById(pIdInsumo);
        
        vInputModify.get().setIdEstado(idStatus);
        vInputModify.get().setUsuarioModifica(pUser.getNit());
        vInputModify.get().setFechaModifica(new Date());
        this.insumoRepository.save(vInputModify.get());
        
    }
    
    @Transactional
    public void publish(Integer pIdInsumo, UserLogged pUser) {
        Optional<SipfInsumo> vInputModify = this.insumoRepository.findById(pIdInsumo);
        
        List<SipfCasos> listCases = this.casosRepository.findByidInsumo(pIdInsumo);
        listCases.forEach(item -> {
            item.setIdEstado(Catalog.Case.Status.PUBLISHED);
            item.setUsuarioModifica(pUser.getNit());
            item.setFechaModifica(new Date());
        });
        
        this.casosRepository.saveAll(listCases);
        
        SipfInsumoBitacoraCambioEstadoDto vChangeStateLogbook = new SipfInsumoBitacoraCambioEstadoDto();
        vChangeStateLogbook.setFechaModifica(new Date());
        vChangeStateLogbook.setIdEstadoAnterior(vInputModify.get().getIdEstado());
        vChangeStateLogbook.setIdInsumo(pIdInsumo);
        vChangeStateLogbook.setUsuarioModifica(pUser.getNit());
        
        vInputModify.get().setIdEstado(Catalog.Input.Status.PUBLISHED);
        vInputModify.get().setFechaPublicacion(new Date());
        vChangeStateLogbook.setIdEstadoNuevo(vInputModify.get().getIdEstado());
        this.insumoBitacoraCambioEstadoService.createRegistriChangeOfState(vChangeStateLogbook);
        
        final JSONObject body = new JSONObject();
        
        final JSONObject object = new JSONObject();
        object.put("idInsumo", vInputModify.get().getIdInsumo());
        object.put("idEstado", vInputModify.get().getIdEstado());
        
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "interna");
        
        final StartProcessDto processId = contiendoACSService.startProces(1, body);
        
        vInputModify.get().setIdProceso(processId.getId());
        
        this.insumoRepository.save(vInputModify.get());
        
    }

    /**
     * Metodo para actualizar el estado de un insumo
     *
     * @author Rudy Culajay (ruarcuse)
     * @param idInsumo identificador del insumo
     * @param idState estado nuevo
     * @param idStateValid estado anterior
     * @param user datos del usuario
     * @param outcome
     * @since 23/07/2022
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean alterStateInsumo(Integer idInsumo, int idState, Integer idStateValid, UserLogged user, String outcome) {
        SipfInsumo caso = insumoRepository.findById(idInsumo).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Insumo no encontrado"));
        if (idStateValid != null && !Objects.equals(caso.getIdEstado(), idStateValid)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "La fase a la que se desea cambiar el insumo no es la correcta");
        }
        
        SipfInsumoBitacoraCambioEstadoDto vChangeStateLogbook = new SipfInsumoBitacoraCambioEstadoDto();
        vChangeStateLogbook.setFechaModifica(new Date());
        vChangeStateLogbook.setIdEstadoAnterior(caso.getIdEstado());
        vChangeStateLogbook.setIdInsumo(idInsumo);
        vChangeStateLogbook.setUsuarioModifica(user.getNit());
        vChangeStateLogbook.setIdEstadoNuevo(idState);
        this.insumoBitacoraCambioEstadoService.createRegistriChangeOfState(vChangeStateLogbook);
        
        caso.setIdEstado(idState);
        caso.setUsuarioModifica(user.getLogin());
        caso.setIpModifica(user.getIp());
        caso.setFechaModifica(new Date());
        
        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_insumos")
                        .idCambioRegistro(String.valueOf(idInsumo))
                        .idTipoOperacion(405)
                        .data(new Gson().toJson(caso))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );
        if (!outcome.isEmpty()) {
            final JSONObject valuesBodyTaks = new JSONObject();
            valuesBodyTaks.put("idEstado", idState);
            final JSONObject bodyTaks = new JSONObject();
            bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
            bodyTaks.put("resultadoFormulario", outcome);
            contiendoACSService.completaTaksByIntanceId(caso.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        }
        
        return true;
    }

    /**
     * Metodo para actualizar el estado de un caso
     *
     * @author Rudy Culajay (ruarcuse)
     * @param idInsumo
     * @param idState estado nuevo
     * @param idStateValid estado anterior
     * @param user datos del usuario
     * @param comentary Comentario del usuario
     * @param outcome
     * @param typeComentary tipo de comentario a registar
     * @since 23/07/2022
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean alterStateInsumoWithComentary(int idInsumo, int idState, Integer idStateValid, UserLogged user, String comentary, Integer typeComentary, String outcome) {
        SipfInsumo caso = insumoRepository.findById(idInsumo).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Insumo no encontrado"));
        if (idStateValid != null && !Objects.equals(caso.getIdEstado(), idStateValid)) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "La fase a la que se desea cambiar el insumo no es la correcta");
        }
        
        SipfInsumoBitacoraCambioEstadoDto vChangeStateLogbook = new SipfInsumoBitacoraCambioEstadoDto();
        vChangeStateLogbook.setFechaModifica(new Date());
        vChangeStateLogbook.setIdEstadoAnterior(caso.getIdEstado());
        vChangeStateLogbook.setIdInsumo(idInsumo);
        vChangeStateLogbook.setUsuarioModifica(user.getNit());
        vChangeStateLogbook.setIdEstadoNuevo(idState);
        this.insumoBitacoraCambioEstadoService.createRegistriChangeOfState(vChangeStateLogbook);
        
        caso.setIdEstado(idState);
        caso.setUsuarioModifica(user.getLogin());
        caso.setIpModifica(user.getIp());
        caso.setFechaModifica(new Date());
        SipfHistorialComentarios comentaryF = new SipfHistorialComentarios();
        comentaryF.setComentarios(comentary);
        comentaryF.setIdRegistro(String.valueOf(idInsumo));
        comentaryF.setIdTipoComentario(typeComentary);
        comentaryF.setFechaModifica(new Date());
        comentaryF.setUsuarioModifica(user.getLogin());
        comentaryF.setIpModifica(user.getIp());
        historialComentariosRepository.save(comentaryF);
        
        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_insumos")
                        .idCambioRegistro(String.valueOf(idInsumo))
                        .idTipoOperacion(405)
                        .data(new Gson().toJson(caso))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );
        
        if (!outcome.isEmpty()) {
            final JSONObject valuesBodyTaks = new JSONObject();
            valuesBodyTaks.put("idEstado", idState);
            valuesBodyTaks.put("comentarios", comentary);
            final JSONObject bodyTaks = new JSONObject();
            bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
            bodyTaks.put("resultadoFormulario", outcome);
            this.contiendoACSService.completaTaksByIntanceId(caso.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        }
        return true;
    }
}
