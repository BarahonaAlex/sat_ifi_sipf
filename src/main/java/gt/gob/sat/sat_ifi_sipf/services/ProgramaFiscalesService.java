/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AprobacionProgramasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.BitacoraProgramasFiscalesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ImpuestoProgramaFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PresenciasFiscalesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ProgramaFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoPrograma;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoProgramaId;
import gt.gob.sat.sat_ifi_sipf.models.SipfProgramaFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.ApproveAllCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraAprobacionProgramaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesComentarioProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ProgramaFiscalesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.SipfImpuestoProgramaRepository;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author ruarcuse
 */
@Transactional
@Service
@Slf4j
public class ProgramaFiscalesService {

    @Autowired
    private ProgramaFiscalesRepository fiscalProgramRepository;

    @Autowired
    private HistorialComentariosRepository historialComentariosRepository;

    @Autowired
    private SipfImpuestoProgramaRepository fiscalProgramTaxesRepository;

    @Autowired
    private GeneralServices generalService;

    @Autowired
    private ContiendoACSService contiendoACSService;

    @Autowired
    private CasosService casosService;

    @Autowired
    private CasosRepository casosRepository;

    @Autowired
    private Detector detector;

    @Autowired
    private AlcancesService alcancesService;

    @Autowired
    private PresenciasFiscalesService presenciasFiscalesService;

    @Autowired
    private BitacoraProgramasFiscalesService bitacoraProgramasFiscalesService;

    public Optional<ProgramaFiscalesProjection> findFiscalProgramById(Integer pIdFiscalProgram) {
        return this.fiscalProgramRepository.findByIdPrograma(pIdFiscalProgram);
    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ProgramaFiscalDto> createFiscalProgram(ProgramaFiscalDto pFiscalProgram) {
        log.error(detector.getLogin());
        SipfProgramaFiscales vFiscalProgram = SipfProgramaFiscales.builder()
                .anio(pFiscalProgram.getAnio())
                .codificacionPrograma(pFiscalProgram.getCodificacionPrograma())
                .correlativo(pFiscalProgram.getCorrelativo())
                .descripcion(pFiscalProgram.getDescripcion())
                .idDepartamento(pFiscalProgram.getIdDepartamento())
                .idDireccionamientoAuditoria(pFiscalProgram.getIdDireccionamientoAuditoria())
                .idEstado(pFiscalProgram.getIdEstado())
                .idGerencia(pFiscalProgram.getIdGerencia())
                .idOrigenInsumo(pFiscalProgram.getIdOrigenInsumo())
                .idTipoAuditoria(pFiscalProgram.getIdTipoAuditoria())
                .idTipoPrograma(pFiscalProgram.getIdTipoPrograma())
                .impuesto(pFiscalProgram.getImpuesto())
                .impuestoNombres(pFiscalProgram.getImpuestoNombres())
                .ipModifica(pFiscalProgram.getIpModifica())
                .nombre(pFiscalProgram.getNombre())
                .periodoFin(pFiscalProgram.getPeriodoFin())
                .periodoInicio(pFiscalProgram.getPeriodoInicio())
                .usuarioModifica(pFiscalProgram.getUsuarioModifica())
                .fechaModifica(new Date())
                .usuarioAgrega(pFiscalProgram.getUsuarioAgrega())
                .fechaCreacion(new Date())
                .build();
        Integer vCorrelative = this.fiscalProgramRepository.findMaxCorrelativeByYear(pFiscalProgram.getAnio());

        vFiscalProgram.setCorrelativo((vCorrelative != null) ? Integer.sum(vCorrelative, 1) : 1);

        Optional vOptionaFiscalProgram;

        vOptionaFiscalProgram = Optional.of(this.fiscalProgramRepository.save(vFiscalProgram));

        if (vOptionaFiscalProgram.isPresent()) {

            pFiscalProgram.setCorrelativo(vFiscalProgram.getCorrelativo());
            pFiscalProgram.setIdPrograma(vFiscalProgram.getIdPrograma());
            this.relateFiscalProgramTaxes(pFiscalProgram);
        }

        BitacoraProgramasFiscalesDto bitacoraDto = new BitacoraProgramasFiscalesDto();
        bitacoraDto.setIdPrograma(vFiscalProgram.getIdPrograma());
        bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.CREATE);
        bitacoraDto.setIdEstadoNuevo(Catalog.FiscalPrograms.Status.REVISION);
        bitacoraDto.setIdEstadoAnterior(Catalog.FiscalPrograms.Status.REVISION);
        bitacoraDto.setFechaModifica(new Date());
        bitacoraDto.setUsuarioModifica(pFiscalProgram.getUsuarioModifica());
        bitacoraProgramasFiscalesService.createLog(bitacoraDto);

        //Inicia el proceso en ALFRESCO
        final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idPrograma", vFiscalProgram.getIdPrograma());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "creado-revision");
        final StartProcessDto processId = contiendoACSService.startProces(4, body);
        vFiscalProgram.setIdProceso(processId.getId());
        this.fiscalProgramRepository.save(vFiscalProgram);
        return vOptionaFiscalProgram.isPresent() ? ResponseEntity.ok(pFiscalProgram) : ResponseEntity.notFound().build();

    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ProgramaFiscalDto> updateFiscalProgram(Integer pIdFiscalProgram, ProgramaFiscalDto pFiscalProgram, UserLogged user) {

        Optional<SipfProgramaFiscales> vOptionalFiscalProgramFound = this.fiscalProgramRepository.findById(pIdFiscalProgram);
        SipfProgramaFiscales vFiscalProgramToModify;
        BitacoraProgramasFiscalesDto bitacoraDto = new BitacoraProgramasFiscalesDto();
        Optional vOptionalFiscalProgramModified = Optional.empty();
        final JSONObject valuesBodyTaks = new JSONObject();
        final JSONObject bodyTaks = new JSONObject();
        if (vOptionalFiscalProgramFound.isPresent()) {

            vFiscalProgramToModify = vOptionalFiscalProgramFound.get();
            bitacoraDto.setIdEstadoAnterior(vFiscalProgramToModify.getIdEstado());
            vFiscalProgramToModify.setCodificacionPrograma((!StringUtils.isEmpty(pFiscalProgram.getCodificacionPrograma()) && !pFiscalProgram.getCodificacionPrograma().equalsIgnoreCase(vFiscalProgramToModify.getCodificacionPrograma())) ? pFiscalProgram.getCodificacionPrograma() : vFiscalProgramToModify.getCodificacionPrograma());
            vFiscalProgramToModify.setDescripcion((!StringUtils.isEmpty(pFiscalProgram.getDescripcion()) && !pFiscalProgram.getDescripcion().equalsIgnoreCase(vFiscalProgramToModify.getDescripcion())) ? pFiscalProgram.getDescripcion() : vFiscalProgramToModify.getDescripcion());
            vFiscalProgramToModify.setIdDepartamento((pFiscalProgram.getIdDepartamento() != null && !pFiscalProgram.getIdDepartamento().equals(vFiscalProgramToModify.getIdDepartamento())) ? pFiscalProgram.getIdDepartamento() : vFiscalProgramToModify.getIdDepartamento());
            vFiscalProgramToModify.setIdDireccionamientoAuditoria((pFiscalProgram.getIdDireccionamientoAuditoria() != 0 && pFiscalProgram.getIdDireccionamientoAuditoria() != vFiscalProgramToModify.getIdDireccionamientoAuditoria()) ? pFiscalProgram.getIdDireccionamientoAuditoria() : vFiscalProgramToModify.getIdDireccionamientoAuditoria());
            vFiscalProgramToModify.setIdGerencia((pFiscalProgram.getIdGerencia() != 0 && pFiscalProgram.getIdGerencia() != vFiscalProgramToModify.getIdGerencia()) ? pFiscalProgram.getIdGerencia() : vFiscalProgramToModify.getIdGerencia());
            vFiscalProgramToModify.setIdOrigenInsumo((pFiscalProgram.getIdOrigenInsumo() != 0 && pFiscalProgram.getIdOrigenInsumo() != vFiscalProgramToModify.getIdOrigenInsumo()) ? pFiscalProgram.getIdOrigenInsumo() : vFiscalProgramToModify.getIdOrigenInsumo());
            vFiscalProgramToModify.setIdTipoAuditoria((pFiscalProgram.getIdTipoAuditoria() != 0 && pFiscalProgram.getIdTipoAuditoria() != vFiscalProgramToModify.getIdTipoAuditoria()) ? pFiscalProgram.getIdTipoAuditoria() : vFiscalProgramToModify.getIdTipoAuditoria());
            vFiscalProgramToModify.setIdTipoPrograma((pFiscalProgram.getIdTipoPrograma() != 0 && pFiscalProgram.getIdTipoPrograma() != vFiscalProgramToModify.getIdTipoPrograma()) ? pFiscalProgram.getIdTipoPrograma() : vFiscalProgramToModify.getIdTipoPrograma());
            vFiscalProgramToModify.setImpuesto((!StringUtils.isEmpty(pFiscalProgram.getImpuesto()) && !pFiscalProgram.getImpuesto().equalsIgnoreCase(vFiscalProgramToModify.getImpuesto())) ? pFiscalProgram.getImpuesto() : vFiscalProgramToModify.getImpuesto());
            vFiscalProgramToModify.setImpuestoNombres((!StringUtils.isEmpty(pFiscalProgram.getImpuestoNombres()) && !pFiscalProgram.getImpuestoNombres().equalsIgnoreCase(vFiscalProgramToModify.getImpuestoNombres())) ? pFiscalProgram.getImpuestoNombres() : vFiscalProgramToModify.getImpuestoNombres());
            vFiscalProgramToModify.setIpModifica((!StringUtils.isEmpty(pFiscalProgram.getIpModifica()) && !pFiscalProgram.getIpModifica().equalsIgnoreCase(vFiscalProgramToModify.getIpModifica())) ? pFiscalProgram.getIpModifica() : vFiscalProgramToModify.getIpModifica());
            vFiscalProgramToModify.setNombre((!StringUtils.isEmpty(pFiscalProgram.getNombre()) && !pFiscalProgram.getNombre().equalsIgnoreCase(vFiscalProgramToModify.getNombre())) ? pFiscalProgram.getNombre() : vFiscalProgramToModify.getNombre());
            vFiscalProgramToModify.setPeriodoFin((pFiscalProgram.getPeriodoFin() != null && !pFiscalProgram.getPeriodoFin().equals(vFiscalProgramToModify.getPeriodoFin())) ? pFiscalProgram.getPeriodoFin() : vFiscalProgramToModify.getPeriodoFin());
            vFiscalProgramToModify.setPeriodoInicio((pFiscalProgram.getPeriodoInicio() != null && !pFiscalProgram.getPeriodoInicio().equals(vFiscalProgramToModify.getPeriodoInicio())) ? pFiscalProgram.getPeriodoInicio() : vFiscalProgramToModify.getPeriodoInicio());
            vFiscalProgramToModify.setUsuarioModifica((!StringUtils.isEmpty(pFiscalProgram.getUsuarioModifica()) && !pFiscalProgram.getUsuarioModifica().equalsIgnoreCase(vFiscalProgramToModify.getUsuarioModifica())) ? pFiscalProgram.getUsuarioModifica() : vFiscalProgramToModify.getUsuarioModifica());
            vFiscalProgramToModify.setFechaModifica(new Date());

            if (pFiscalProgram.getIdEstado() == Catalog.FiscalPrograms.Status.LOCKED) {//para bloquear
                vFiscalProgramToModify.setIdEstadoAnterior(vFiscalProgramToModify.getIdEstado());
                vFiscalProgramToModify.setIdEstado((pFiscalProgram.getIdEstado() != 0 && pFiscalProgram.getIdEstado() != vFiscalProgramToModify.getIdEstado()) ? pFiscalProgram.getIdEstado() : vFiscalProgramToModify.getIdEstado());
            } else if (pFiscalProgram.getIdEstado() == Catalog.FiscalPrograms.Status.UNLOCKED) {//para desbloquear
                vFiscalProgramToModify.setIdEstado(vFiscalProgramToModify.getIdEstadoAnterior());
                vFiscalProgramToModify.setIdEstadoAnterior(Catalog.FiscalPrograms.Status.UNLOCKED);

            } else {//los demas estados
                if (pFiscalProgram.getIdEstado() == Catalog.FiscalPrograms.Status.CORRECTION_REQUEST && vFiscalProgramToModify.getIdEstado() == Catalog.FiscalPrograms.Status.REVISION
                        || pFiscalProgram.getIdEstado() == Catalog.FiscalPrograms.Status.CORRECTION_REQUEST && vFiscalProgramToModify.getIdEstado() == Catalog.FiscalPrograms.Status.APPROVAL) {
                    valuesBodyTaks.put("idEstado", pFiscalProgram.getIdEstado());
                    bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                    bodyTaks.put("resultadoFormulario", "modificar");
                    this.contiendoACSService.completaTaksByIntanceId(vFiscalProgramToModify.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
                }
                vFiscalProgramToModify.setIdEstado((pFiscalProgram.getIdEstado() != 0 && pFiscalProgram.getIdEstado() != vFiscalProgramToModify.getIdEstado()) ? pFiscalProgram.getIdEstado() : vFiscalProgramToModify.getIdEstado());
                if (pFiscalProgram.getIdEstado() == 0) {
                    pFiscalProgram.setIdEstado(vFiscalProgramToModify.getIdEstado());
                }
            }
            bitacoraDto.setIdPrograma(vFiscalProgramToModify.getIdPrograma());
            bitacoraDto.setIdEstadoNuevo(vFiscalProgramToModify.getIdEstado());
            bitacoraDto.setFechaModifica(new Date());
            bitacoraDto.setUsuarioModifica(user.getLogin());

            switch (pFiscalProgram.getIdEstado()) {
                case 107:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.CORRECTION_REQUEST);
                    break;
                case 111:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.APROVAL);
                     valuesBodyTaks.put("idEstado", pFiscalProgram.getIdEstado());
                    bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                    bodyTaks.put("resultadoFormulario", "aprobar");
                    this.contiendoACSService.completaTaksByIntanceId(vFiscalProgramToModify.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
                    break;
                case 108:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.AUTHORIZED);
                    valuesBodyTaks.put("idEstado", pFiscalProgram.getIdEstado());
                    bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                    bodyTaks.put("resultadoFormulario", "autorizar");
                    this.contiendoACSService.completaTaksByIntanceId(vFiscalProgramToModify.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
                    break;
                case 110:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.REVISION);
                     valuesBodyTaks.put("idEstado", pFiscalProgram.getIdEstado());
                    bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                    bodyTaks.put("resultadoFormulario", "revision");
                    this.contiendoACSService.completaTaksByIntanceId(vFiscalProgramToModify.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
                    break;
                case 109:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.LOCKED);
                    break;
                case 187:
                    bitacoraDto.setIdTipoOperacion(Catalog.FiscalPrograms.Binnacle.UNLOCKED);
                    break;
            }
            bitacoraProgramasFiscalesService.createLog(bitacoraDto);

            vOptionalFiscalProgramModified = Optional.of(this.fiscalProgramRepository.save(vFiscalProgramToModify));

            try {
                ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
                String json = ow.writeValueAsString(vFiscalProgramToModify);
            } catch (Exception ex) {

            }
            if (vOptionalFiscalProgramModified.isPresent()) {
                this.relateFiscalProgramTaxes(pFiscalProgram);
            }

            if (pFiscalProgram.getComentarios() != null) {
                SipfHistorialComentarios vComment = new SipfHistorialComentarios();
                vComment.setComentarios(pFiscalProgram.getComentarios());
                vComment.setFechaModifica(new Date());
                vComment.setIdRegistro(pIdFiscalProgram.toString());
                vComment.setIdTipoComentario(156);// codigo de tipo de comentario para modificaciones de 
                vComment.setUsuarioModifica(pFiscalProgram.getUsuarioModifica());
                vComment.setIpModifica(pFiscalProgram.getIpModifica());
                this.historialComentariosRepository.save(vComment);
            }

        }

        return vOptionalFiscalProgramModified.isPresent() ? ResponseEntity.ok(pFiscalProgram) : ResponseEntity.notFound().build();

    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByStatus(int pIdStatus) {
        //log.debug("***************************** order by desc");
        return this.fiscalProgramRepository.findByIdEstadoOrderByIdProgramaDesc(pIdStatus);
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByStatusAndRegional(int pIdStatus, int pIdRegional) {
        return this.fiscalProgramRepository.findByIdEstadoAndIdGerenciaOrderByIdPrograma(pIdStatus, pIdRegional);
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByStatusAndProgramType(int pIdStatus, int pIdProgramType) {
        return this.fiscalProgramRepository.findByIdEstadoAndIdTipoProgramaOrderByIdPrograma(pIdStatus, pIdProgramType);
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByIdGerenciaAndProgramType(int pIdStatus, int pIdProgramType) {
        return this.fiscalProgramRepository.findByIdEstadoAndIdTipoProgramaOrderByIdPrograma(pIdStatus, pIdProgramType);
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByStatusAndRegionalAndProgramType(int pIdStatus, int pIdRegional, int pIdProgramType) {
        return this.fiscalProgramRepository.findByIdEstadoAndIdGerenciaAndIdTipoProgramaOrderByIdPrograma(pIdStatus, pIdRegional, pIdProgramType);
    }

    public List<ProgramaFiscalesComentarioProjection> getFiscalProgramsByStatusAndRegionalAndDateRange(int pIdStatus, int pIdRegional, Date pDel, Date pAl) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pAl);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return this.fiscalProgramRepository.getByIdEstadoAndIdGerenciaAndFechaCreacionBetweenOrderByIdPrograma(pIdStatus, pIdRegional, pDel, calendar.getTime());
    }

    public List<ProgramaFiscalesComentarioProjection> getFiscalProgramsByStatusAndDateRange(Date pDel, Date pAl) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(pAl);
        calendar.add(Calendar.HOUR_OF_DAY, 23);
        calendar.add(Calendar.MINUTE, 59);
        return this.fiscalProgramRepository.getProgramLocked(pDel, calendar.getTime());
    }

    public List<ProgramaFiscalesComentarioProjection> getFiscalProgramsByUserAndStatus(int pIdStatus, UserLogged user) {

        return this.fiscalProgramRepository.getByUserAndStatus(pIdStatus, user.getNit());
    }

    public List<ProgramaFiscalesComentarioProjection> getFiscalProgramsByStatusProgram(int pIdStatus) {
        return this.fiscalProgramRepository.getByStatus(pIdStatus);
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByUser(UserLogged user) {

        return this.fiscalProgramRepository.getByUser(user.getLogin());
    }

    public void relateFiscalProgramTaxes(ProgramaFiscalDto pFiscalProgram) {
        /*for (ImpuestoProgramaProjection item : this.fiscalProgramTaxesRepository.findByPrograma(pFiscalProgram.getIdPrograma())) {
            this.fiscalProgramTaxesRepository.deleteById(item.getId());
        }
         */
        this.fiscalProgramTaxesRepository.deleteAll(this.fiscalProgramTaxesRepository.findByPrograma(pFiscalProgram.getIdPrograma()));

        for (ImpuestoProgramaFiscalDto item : pFiscalProgram.getImpuestos()) {
            this.fiscalProgramTaxesRepository.save(
                    new SipfImpuestoPrograma(
                            new SipfImpuestoProgramaId(pFiscalProgram.getIdPrograma(), item.getIdImpuesto()),
                            pFiscalProgram.getUsuarioModifica(),
                            pFiscalProgram.getIpModifica(),
                            new Date()));
        }

    }

    public List<CarteraAprobacionProgramaProjection> walletApproval() {

        return fiscalProgramRepository.walletApproval();
    }

    public List<CarteraAprobacionProgramaProjection> walletDetail(Integer idGerencia) {
        List<CarteraAprobacionProgramaProjection> carteraDetalle = fiscalProgramRepository.walletApprovalDetail(idGerencia);
        return carteraDetalle;
    }

    public List<ApproveAllCasesProjection> approvalAllCase(UserLogged user) {
        List<ApproveAllCasesProjection> listaInsumos = fiscalProgramRepository.getListCases();
        if (!listaInsumos.isEmpty()) {
            String correlativo = "";
            CasosDto caso = new CasosDto();
            for (int i = 0; i < listaInsumos.size(); i++) {

                correlativo = "APRO-" + listaInsumos.get(i).getSiglas() + "-" + Secuencia(listaInsumos.get(i).getIdGerencia()) + "-" + listaInsumos.get(i).getPeriodo();
                caso.setEstado(133);
                caso.setCorrelativoAprobacion(correlativo);
                casosService.alterCase(listaInsumos.get(i).getIdCaso(), caso, user);
            }
            return listaInsumos;
        } else {
            return null;
        }

    }

    @Transactional(rollbackFor = Exception.class)
    public List<AprobacionProgramasDto> approvalCases(List<AprobacionProgramasDto> dto, UserLogged user) {
        for (int i = 0; i < dto.size(); i++) {
            //log.debug("idcase:" + dto.get(i).getIdCaso());
            //log.debug("tipoCaso:" + dto.get(i).getTipoCaso());
            Date fecha = new Date();
            ApproveAllCasesProjection listaInsumos;
            if (dto.get(i).getTipoCaso() == 973) {
                if (dto.get(i).getIdDenuncia() != null) {
                    listaInsumos = fiscalProgramRepository.getListCasesDenuncia(dto.get(i).getIdDenuncia());
                } else {
                    listaInsumos = fiscalProgramRepository.getListCasesPresencias(dto.get(i).getIdCaso());
                }
            } else {
                if (dto.get(i).getIdDenuncia() != null) {
                    listaInsumos = fiscalProgramRepository.getListCasesDenuncia(dto.get(i).getIdDenuncia());
                } else {
                    listaInsumos = fiscalProgramRepository.getListCases(dto.get(i).getIdCaso());
                }

            }
            //log.debug("lista de casos: " + listaInsumos);
            if (listaInsumos != null) {
                String correlativo = "";
                String correo = "Estimado Lic. \n"
                        + "Nombre del Jefe del departamento \n"
                        + "Jefe Departamento de Fiscalización \n"
                        + "Intendencia de Fiscalización \n"
                        + "Atentamente se le informa, se ha aprobado la programación de fiscalización que corresponde a las siguientes gerencias:" + dto.get(i).getNombreGerencia() + ", con:" + dto.get(i).getCantidad() + "\n"
                        + "Este es un correo automático por lo que no deberá dar respuesta.";

                if (listaInsumos.getPeriodo() == null) {
                    correlativo = "APRO-" + listaInsumos.getSiglas() + "-" + Secuencia(listaInsumos.getIdGerencia()) + "-" + fecha.getYear();
                } else {
                    correlativo = "APRO-" + listaInsumos.getSiglas() + "-" + Secuencia(listaInsumos.getIdGerencia()) + "-" + listaInsumos.getPeriodo();
                }
                //Modificacion de alcances
                AlcanceDto alcance = new AlcanceDto();
                alcance.setIdAlcance(dto.get(i).getIdAlcance());
                alcance.setIdEstado(Catalog.Scope.Status.PROGRAMMED);
                alcancesService.updateAlcances(alcance, user);
                if (dto.get(i).getTipoCaso() == 973) {
                    if (dto.get(i).getIdDenuncia() != null) {
                        //log.debug(dto.get(i).getIdDenuncia());
                        DenunciaGrabadaDto denuncia = new DenunciaGrabadaDto();
                        denuncia.setEstado("182");
                        denuncia.setCorrelativoAprobacion(correlativo);
                        denuncia.setCorrelativo(dto.get(i).getIdDenuncia());
                        denuncia.setIdAlcance(dto.get(i).getIdAlcance());
                        alcancesService.autorizarGerencial(denuncia);
                    } else {
                        PresenciasFiscalesDto presencia = new PresenciasFiscalesDto();
                        presencia.setCorrelativoAprobacion(correlativo);
                        presencia.setIdEstado(Catalog.Scope.Status.PROGRAMMED);
                        presencia.setIdFormulario(dto.get(i).getIdCaso());
                        presencia.setResultadoFormulario("autoriza");
                        //presenciasFiscalesService.updatePresencia(dto.get(i).getIdCaso(), presencia);
                        List<MultipartFile> file = new ArrayList<MultipartFile>();
                        presenciasFiscalesService.updateProcessPresencias(dto.get(i).getIdCaso(), presencia, file, user);
                    }
                } else {
                    if (dto.get(i).getIdDenuncia() != null) {
                        //log.debug(dto.get(i).getIdDenuncia());
                        DenunciaGrabadaDto denuncia = new DenunciaGrabadaDto();
                        denuncia.setEstado("182");
                        denuncia.setCorrelativoAprobacion(correlativo);
                        denuncia.setCorrelativo(dto.get(i).getIdDenuncia());
                        denuncia.setIdAlcance(dto.get(i).getIdAlcance());
                        alcancesService.autorizarGerencial(denuncia);
                    } else {
                        CasosDto caso = new CasosDto();
                        caso.setEstado(182);
                        caso.setCorrelativoAprobacion(correlativo);
                        casosService.alterStateCases(listaInsumos.getIdCaso(), 182, 133, user, "autoriza");
                        casosService.alterCase(listaInsumos.getIdCaso(), caso, user);
                    }

                }

                //  correoService.envioCorreo("jodaales@gmail.com", correo, "Aprovacion de programacion");
            } else {
                return null;

            }

        }
        return dto;

    }

    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<?> declineCases(List<AprobacionProgramasDto> dto, UserLogged user) {

        for (int i = 0; i < dto.size(); i++) {
            //log.debug("idcase:" + dto.get(i).getIdCaso());
            //log.debug("tipoCaso:" + dto.get(i).getTipoCaso());

            AlcanceDto alcance = new AlcanceDto();
            alcance.setIdAlcance(dto.get(i).getIdAlcance());
            alcance.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);
            alcancesService.updateAlcances(alcance, user);
            if (dto.get(i).getTipoCaso() == 973) {
                if (dto.get(i).getIdDenuncia() != null) {
                    DenunciaGrabadaDto denuncia = new DenunciaGrabadaDto();
                    denuncia.setEstado("181");
                    denuncia.setCorrelativo(dto.get(i).getIdDenuncia());
                    denuncia.setIdAlcance(dto.get(i).getIdAlcance());
                    alcancesService.RechazoGerencial(denuncia);
                } else {
                    PresenciasFiscalesDto presencia = new PresenciasFiscalesDto();
                    /*  if (dto.get(i).getTipo() == 1) {
                    presencia.setIdEstado(17);//estado en rechazo
                    presencia.setResultadoFormulario("autoriza");
                    presenciasFiscalesService.updateProcessPresencias(dto.get(i).getIdCaso(), presencia, user);
                    //casosService.alterStatecasesWithComentary(dto.get(i).getIdCaso(), 17, 133, user, dto.get(i).getComentario(), 409, "rechazar");
                } else {*/
                    presencia.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);//Correccion
                    presencia.setResultadoFormulario("cambios");
                    List<MultipartFile> file = new ArrayList<MultipartFile>();
                    presenciasFiscalesService.updateProcessPresencias(dto.get(i).getIdCaso(), presencia, file, user);
                }
                //casosService.alterStatecasesWithComentary(dto.get(i).getIdCaso(), 181, 133, user, dto.get(i).getComentario(), 408, "corregir");
                //}
                SipfHistorialComentarios vComment = new SipfHistorialComentarios();
                vComment.setComentarios(dto.get(i).getComentario());
                vComment.setFechaModifica(new Date());
                if (dto.get(i).getIdDenuncia() != null) {
                    vComment.setIdRegistro(dto.get(i).getIdDenuncia());
                } else {
                    vComment.setIdRegistro(dto.get(i).getIdCaso().toString());
                }
                vComment.setIdTipoComentario(408);
                vComment.setUsuarioModifica(user.getNit());
                vComment.setIpModifica(user.getIp());
                this.historialComentariosRepository.save(vComment);
            } else {
                if (dto.get(i).getIdDenuncia() != null) {
                    DenunciaGrabadaDto denuncia = new DenunciaGrabadaDto();
                    denuncia.setEstado("181");
                    denuncia.setCorrelativo(dto.get(i).getIdDenuncia());
                    denuncia.setIdAlcance(dto.get(i).getIdAlcance());
                    alcancesService.RechazoGerencial(denuncia);
                } else {
                    if (dto.get(i).getTipoCaso() == 969 || dto.get(i).getTipoCaso() == 117) {
                        CasosDto caso = new CasosDto();
                        if (dto.get(i).getTipo() == 1) {
                            caso.setEstado(17);//estado en rechazo
                            casosService.alterStatecasesWithComentary(dto.get(i).getIdCaso(), Catalog.Case.Status.REJECTED, Catalog.Case.Status.PROGRAMMING_AUTHORIZATION, user, dto.get(i).getComentario(), 409, "rechazar");
                        } else {
                            caso.setEstado(181);//Correccion
                            casosService.alterStatecasesWithComentary(dto.get(i).getIdCaso(), Catalog.Case.Status.SCOPE_CORRECTION, Catalog.Case.Status.PROGRAMMING_AUTHORIZATION, user, dto.get(i).getComentario(), 408, "corregir");
                        }
                    } else {
                        alcancesService.rechazarAlcance(dto.get(i).getIdAlcance(), user, dto.get(i).getComentario());
                    }

                }
            }

        }

        return ResponseEntity.ok().build();

    }

    public Integer Secuencia(Integer idGerencia) {
        Integer correlativo = 0;
        switch (idGerencia) {

            case 40:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_central");
                break;
            case 41:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_sur");
                break;
            case 42:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_occidente");
                break;
            case 43:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_nororiente");
                break;
            case 44:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_medianos_especiales");
                break;
            case 45:
                correlativo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_aprobacion_grandes_especiales");
                break;

        }
        return correlativo;
    }

    public List<ProgramaFiscalesProjection> getFiscalProgramsByStatusAndCurrentYear(Integer status) {
        return this.fiscalProgramRepository.getByStatusAndCurrentYear(status.intValue());
    }

    public SipfCasos assignFiscalProgram(Integer idProgram, Integer idCaso, UserLogged logged) {
        final SipfCasos caso = casosRepository.findById(idCaso).orElse(null);
        caso.setIdPrograma(idProgram);
        caso.setFechaModifica(new Date());
        caso.setIpModifica(logged.getIp());
        caso.setUsuarioModifica(logged.getLogin());
        casosRepository.save(caso);

        return caso;

    }
}
