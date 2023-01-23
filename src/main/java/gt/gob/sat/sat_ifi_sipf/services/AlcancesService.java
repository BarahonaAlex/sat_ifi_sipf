package gt.gob.sat.sat_ifi_sipf.services;

import com.itextpdf.html2pdf.ConverterProperties;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcancesMasivosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcancesMasivosVersionesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosMasivosDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcancesMasivos;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcancesMasivosVersiones;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcancesMasivosVersionesId;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasosMasivos;
import gt.gob.sat.sat_ifi_sipf.models.SipfSeccionesCaso;
import gt.gob.sat.sat_ifi_sipf.projections.AlcancesMasivosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosAndAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SeccionesCasoProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcancesMasivosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcancesMasivosVersionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosMasivosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.SeccionesCasoRepository;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import com.itextpdf.html2pdf.HtmlConverter;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciasPuntosFijosPresenciasComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GenerationPdfDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PresenciasComentariosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcance;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcanceDetalle;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcanceDetalleId;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.models.SipfDetalleCaso;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfPresenciasFiscales;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraCasosProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcanceDetalleRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcanceRepository;
import static gt.gob.sat.sat_ifi_sipf.utils.GeneralUtils.isSameOrNull;
import gt.gob.sat.sat_ifi_sipf.repositories.DenunciaGrabadaRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.DetalleCasoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.PresenciasFiscalesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ruarcuse
 */
@Service
@Slf4j
public class AlcancesService {

    @Autowired
    private AlcancesMasivosRepository alcancesMasivosRepository;

    @Autowired
    private AlcanceRepository alcanceRepository;

    @Autowired
    private AlcanceDetalleRepository alcanceDetalleRepository;

    @Autowired
    private AlcancesMasivosVersionesRepository alcancesMasivosVersionesRepository;

    @Autowired
    private CasosMasivosRepository casosMasivosRepository;

    @Autowired
    AlcancesService alcancesService;

    @Autowired
    private CasosRepository casosRepository;

    @Autowired
    private SeccionesCasoRepository seccionesCasoRepository;

    @Autowired
    private DetalleCasoRepository detalleCasoRepository;

    @Autowired
    private DenunciaGrabadaRepository denunciaGrabadaRepository;

    @Autowired
    private PresenciasFiscalesRepository presenciasFiscalesRepository;

    @Autowired
    Detector detector;

    @Autowired
    private CorreoService correoService;

    @Autowired
    private ContiendoACSService contiendoACSService;

    @Autowired
    private CasosService claseService;

    @Autowired
    private HistorialComentariosRepository historialComentariosRepository;

    @Transactional
    public SipfAlcancesMasivosVersiones createScope(AlcancesMasivosDto dto) {
        final SipfAlcancesMasivos scope = alcancesMasivosRepository.save(
                SipfAlcancesMasivos.builder().
                        nombreAlcance(dto.getNombreAlcance())
                        .descripcionAlcance(dto.getDescripcionAlcance())
                        .usuarioModifica(detector.getLogin())
                        .fechaModifica(new Date())
                        .ipModifica(detector.getIp())
                        .build()
        );

        final SipfAlcancesMasivosVersionesId id = new SipfAlcancesMasivosVersionesId();
        id.setVersion(1);
        id.setIdTipoAlcanceMasivo(scope.getIdAlcanceMasivo());

        final SipfAlcancesMasivosVersiones version = new SipfAlcancesMasivosVersiones();
        version.setId(id);
        version.setJustificacion(dto.getJustificacion());
        version.setObjetivo(dto.getObjetivo());
        version.setProcedimientosEspecificos(dto.getProcedimientosEspecificos());
        version.setActividad(dto.getActividad());
        version.setIdEstado(1);
        version.setUsuarioModifica(detector.getLogin());
        version.setFechaModifica(new Date());
        version.setIpModifica(detector.getIp());

        return alcancesMasivosVersionesRepository.save(version);
    }

    @Transactional
    public SipfAlcancesMasivosVersiones modifyScopeVersion(AlcancesMasivosVersionesDto dto) {
        final SipfAlcancesMasivosVersiones version = new SipfAlcancesMasivosVersiones();
        final SipfAlcancesMasivosVersionesId id = new SipfAlcancesMasivosVersionesId();
        id.setVersion((dto.getVersion() + 1));
        id.setIdTipoAlcanceMasivo(dto.getIdTipoAlcanceMasivo());
        version.setId(id);
        version.setIdEstado(1);
        version.setJustificacion(dto.getJustificacion());
        version.setObjetivo(dto.getObjetivo());
        version.setProcedimientosEspecificos(dto.getProcedimientosEspecificos());
        version.setActividad(dto.getActividad());
        version.setUsuarioModifica(detector.getLogin());
        version.setFechaModifica(new Date());
        version.setIpModifica(detector.getIp());
        return alcancesMasivosVersionesRepository.save(version);
    }

    @Transactional
    public SipfAlcancesMasivosVersiones changeVersionStatus(Integer ver, Integer idMassiveScope) {
        SipfAlcancesMasivosVersionesId id = new SipfAlcancesMasivosVersionesId();
        id.setVersion(ver);
        id.setIdTipoAlcanceMasivo(idMassiveScope);
        final SipfAlcancesMasivosVersiones status = alcancesMasivosVersionesRepository.findById(id).orElse(null);
        if (status.getIdEstado() == 1) {
            status.setIdEstado(2);
        } else if (status.getIdEstado() == 2) {
            status.setIdEstado(1);
        } else {
            //log.debug("Error");
        }
        return alcancesMasivosVersionesRepository.save(status);
    }

    @Transactional(readOnly = true)
    public List<AlcancesMasivosProjection> getMassiveScope() {
        return alcancesMasivosRepository.findMassiveScope();
    }

    /* @Transactional(readOnly = true)
    public List<SipfAlcancesMasivos> getMassiveScopeByStatus(Integer id) {
        return alcancesMasivosRepository.findByIdEstado(id);
    }*/
    @Transactional(readOnly = true)
    public List<SipfAlcancesMasivosVersiones> getVersionsByMassiveScope(Integer id) {
        return alcancesMasivosVersionesRepository.findByIdIdTipoAlcanceMasivo(id);
    }

    @Transactional
    public SipfCasosMasivos assignModifyAndDeleteMassiveScope(CasosMasivosDto dto, Integer op) {
        SipfCasosMasivos assignScope = casosMasivosRepository.findById(dto.getIdCaso()).orElse(null);
        SipfCasos cases = casosRepository.findById(dto.getIdCaso()).orElse(null);

        if (op == 1) {
            assignScope.setIdTipoAlcance(dto.getIdTipoAlcance());
            assignScope.setIdVersionAlcance(dto.getIdVersionAlcance());
            assignScope.setTerritorioMasivo(dto.getTerritorioMasivo());
            cases.setPeriodoRevisionInicio(dto.getPeriodoRevisionInicio());
            cases.setPeriodoRevisionFin(dto.getPeriodoRevisionFin());
            cases.setIdPrograma(dto.getIdPrograma());
            cases.setIdEstado(131);
            cases.setUsuarioModifica(detector.getLogin());
            cases.setFechaModifica(new Date());
            cases.setIpModifica(detector.getIp());

            String post = "";
            String correlativo = "";
            String correo = "Estimado Lic.\n"
                    + dto.getNombreColaborador() + "\n"
                    + "Supervisor de Fiscalizaci�n\n"
                    + "Intendencia de Fiscalizaci�n\n"
                    + "Atentamente se le informa, que se le ha trasladado el alcance masivo para la \n"
                    + "fiscalizaci�n de " + dto.getNombreAlcance() + "\n "
                    + ", el cual ha sido asociado al programa " + dto.getNombrePrograma() + " \n"
                    + ", que corresponde a la gerencia " + dto.getNombreGerencia() + ",\n"
                    + "para que proceda a efectuar la revisi�n correspondiente.\n"
                    + "Este es un correo autom�tico por lo que no deber� dar respuesta.";

            correoService.envioCorreo(dto.getCorreoColaborador(), correo, "Asignacion Alcance Masivo");

        } else if (op == 2) {
            assignScope.setIdTipoAlcance(null);
            assignScope.setIdVersionAlcance(null);
            assignScope.setTerritorioMasivo(null);
            cases.setPeriodoRevisionInicio(null);
            cases.setPeriodoRevisionFin(null);
            cases.setIdPrograma(null);
            cases.setIdEstado(130);
            cases.setUsuarioModifica(detector.getLogin());
            cases.setFechaModifica(new Date());
            cases.setIpModifica(detector.getIp());
        }

        casosRepository.save(cases);
        return casosMasivosRepository.save(assignScope);
    }

    @Transactional
    public List<CasosAndAlcanceProjection> findReachAndMassiveScope(Integer nit) {
        return alcancesMasivosRepository.findReachAndMassiveScope(nit);
    }

    /**
     * Metodo para crear una seccion de los alcances para insumos (casos)
     *
     * @author Luis Villagran (alfvillag)
     * @param section modelo de la seccion
     * @since 18/02/2022
     * @return seccion creada
     */
    @Transactional(rollbackFor = Exception.class)
    public SipfSeccionesCaso createSectionCase(SipfSeccionesCaso section) {
        section.setUsuarioModifica(detector.getLogin());
        section.setIpModifica(detector.getIp());
        section.setFechaModifica(new Date());
        return seccionesCasoRepository.save(section);
    }

    /**
     * @author Luis Villagran (alfvillag)
     * @param idCaso
     * @since 18/02/2022
     * @return seccion creada
     */
    @Transactional(readOnly = true)
    public SipfSeccionesCaso getSectionCase(Integer idCaso) {
        SipfSeccionesCaso sectionCase = seccionesCasoRepository.findById(idCaso).orElse(null);
        return sectionCase;
    }

    @Transactional(readOnly = true)
    public List<SeccionesCasoProjection> getSectionAndComentary(Integer idCaso) {
        return seccionesCasoRepository.getSectionAndComentary(idCaso);
    }

    /**
     * @author Jose Aldana (jdaldana)
     * @param data
     * @param user
     * @since 01/09/2022
     * @return archivo tipo blod
     */
    @Transactional(rollbackFor = Exception.class)
    public byte[] generacionPdf(GenerationPdfDto data, UserLogged user) throws FileNotFoundException, IOException {
        File f = new File("uploads/Alcances.pdf");
        FileOutputStream file2 = new FileOutputStream(f);
        ConverterProperties flush = new ConverterProperties();
        flush.setImmediateFlush(false);
        HtmlConverter.convertToPdf(addStyleHtml(data.getDatos()), file2, flush);
        log.debug("se muere");
        FileInputStream input = new FileInputStream(f);
        log.debug("se muere2");
        byte[] archivo = IOUtils.toByteArray(input);
        log.debug("se muere3");
        input.close();
        return archivo;
    }

    public boolean changeState(Integer idCase, GenerationPdfDto data, UserLogged user) throws IOException {
        log.debug("entra al metodo");
        deleteDocument();
        Optional<SipfDetalleCaso> caseDetail = detalleCasoRepository.findById(idCase);
        AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(Catalog.Case.Type.SELECTIVE);
        alcance.setSecciones(data.getSecciones());
        //alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
        log.debug("entra" + data.getIdEstado() + Catalog.Case.Status.SCOPE_CORRECTION);
        if (data.getIdEstado() == Catalog.Case.Status.ASSIGNED) {
            log.debug("entra al primer if");
            if (data.getCambios() == 1) {
                log.debug("entra al segundo if");
                alcance.setIdEstado(410);
                if (caseDetail.get().getIdAlcance() == null) {
                    log.debug("entra al tercer if");
                    alcance = alcancesService.createAlcance(alcance, user);
                    claseService.updateDetailCase(idCase, alcance.getIdAlcance(), user);
                } else {
                    log.debug("entra al primer else");
                    alcance.setIdAlcance(caseDetail.get().getIdAlcance());
                    alcance = alcancesService.updateAlcances(alcance, user);
                }
                return true;
            } else {
                log.debug("entra al segundo else");
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);

                if (caseDetail.get().getIdAlcance() == null) {
                    alcance = alcancesService.createAlcance(alcance, user);
                    claseService.updateDetailCase(idCase, alcance.getIdAlcance(), user);
                } else {
                    alcance = alcancesService.updateAlcances(alcance, user);
                    claseService.updateDetailCase(idCase, caseDetail.get().getIdAlcance(), user);
                }
                
                log.debug("entra al else 1");
                return claseService.alterStateCases(idCase, Catalog.Case.Status.ELABORATE_SCOPE, Catalog.Case.Status.ASSIGNED, user, "interno");

            }

        }else if (data.getIdEstado().equals(Catalog.Case.Status.SCOPE_CORRECTION)) {
            log.debug("entra al tercer else" + data.getCambios());
            if (data.getCambios() == 1) {
                alcance.setIdAlcance(caseDetail.get().getIdAlcance());
                alcance.setIdEstado(410);
                alcance = alcancesService.updateAlcances(alcance, user);
                return true;
            } else {
                alcance.setIdAlcance(caseDetail.get().getIdAlcance());
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
                alcance = alcancesService.updateAlcances(alcance, user);
                log.debug("entra al cuarto else");
                return claseService.alterStateCases(idCase,Catalog.Case.Status.ELABORATE_SCOPE, Catalog.Case.Status.SCOPE_CORRECTION, user, "interno");
                
            }

        }
        return false;
    }

    public void deleteDocument() throws IOException {
        File f = new File("uploads/Alcances.pdf");
        if (Files.deleteIfExists(f.toPath())) {
            //log.debug("si lo borro");
        }

    }

    private static String addStyleHtml(String html) throws IOException {
        final StringBuilder htmlAppendable = new StringBuilder(html);
        htmlAppendable.append("<style>");
        htmlAppendable.append("content.min.css");
        htmlAppendable.append("</style>");
        return htmlAppendable.toString();
    }

    /**
     * @author Jose Aldana (jdaldana)
     * @param data
     * @param userLogged
     * @return data Servicio para crear alcance.
     */
    public AlcanceDto createAlcance(AlcanceDto data, UserLogged userLogged) {
        SipfAlcance alcance = alcanceRepository.save(
                SipfAlcance.builder()
                        .idTipoAlcance(data.getIdTipoAlcance())
                        .idEstado(data.getIdEstado())
                        .fechaModifica(new Date())
                        .ipModifica(userLogged.getIp())
                        .usuarioModifica(userLogged.getLogin())
                        .build());
        for (AlcanceDetalleDto seccione : data.getSecciones()) {

            SipfAlcanceDetalle detalle = new SipfAlcanceDetalle();
            SipfAlcanceDetalleId detalleId = new SipfAlcanceDetalleId();

            detalleId.setIdAlcance(alcance.getIdAlcance());
            detalleId.setIdSeccion(seccione.getIdSeccion());
            detalle.setId(detalleId);
            detalle.setDetalle(seccione.getDetalle());
            detalle.setFechaModifica(new Date());
            detalle.setUsuarioModifica(userLogged.getLogin());
            detalle.setIpModifica(userLogged.getIp());

            alcanceDetalleRepository.save(detalle);
        }
        data.setIdAlcance(alcance.getIdAlcance());
        return data;
    }

    /**
     * @author Gabriel Ruano (garuanom) Obtiene los alcances de Gabinete.
     */
    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getPointsPresenceCabinet(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Precencia");
        return alcanceRepository.getPointsPresenceCabinet(idalcance);
    }

    public PresenciasComentariosDto getAlcancesbyType(Integer idAlcance, Integer tipo) {
        List<Integer> comentario = new ArrayList<>();
        comentario.add(970);
        switch (tipo) {
            case 973:
                return new PresenciasComentariosDto(
                        alcanceRepository.getAlcancebyIdPresencias(idAlcance),
                        historialComentariosRepository.findMaxIdHistorialComentario(idAlcance.toString(), comentario)
                );
            case 117:
                return new PresenciasComentariosDto(
                        alcanceRepository.getAlcancebyIdSelectiva(idAlcance),
                        historialComentariosRepository.findMaxIdHistorialComentario(idAlcance.toString(), comentario)
                );
        }
        return null;
    }

    public AlcanceDto updateAlcances(AlcanceDto data, UserLogged user) {

        Optional<SipfAlcance> oldAlcance = alcanceRepository.findById(data.getIdAlcance());
        if (oldAlcance.isPresent()) {
            SipfAlcance tmp = oldAlcance.get();
            tmp.setIdEstado(isSameOrNull(data.getIdEstado(), tmp.getIdEstado()) ? tmp.getIdEstado() : data.getIdEstado());
            tmp.setUsuarioModifica(user.getLogin());
            tmp.setFechaModifica(new Date());
            tmp.setIpModifica(user.getIp());
            alcanceRepository.save(tmp);

            if (data.getSecciones() != null) {
                for (int i = 0; i < data.getSecciones().size(); i++) {

                    SipfAlcanceDetalleId detalleId = new SipfAlcanceDetalleId();
                    detalleId.setIdAlcance(data.getIdAlcance());
                    detalleId.setIdSeccion(data.getSecciones().get(i).getIdSeccion());
                    Optional<SipfAlcanceDetalle> oldAlcanceDetalle = alcanceDetalleRepository.findById(detalleId);
                    if (oldAlcanceDetalle.isPresent()) {
                        SipfAlcanceDetalle tmp2 = oldAlcanceDetalle.get();
                        tmp2.setDetalle(isSameOrNull(data.getSecciones().get(i).getDetalle(), tmp2.getDetalle()) ? tmp2.getDetalle() : data.getSecciones().get(i).getDetalle());
                        tmp2.setUsuarioModifica(user.getLogin());
                        tmp2.setFechaModifica(new Date());
                        tmp2.setIpModifica(user.getIp());
                        alcanceDetalleRepository.save(tmp2);
                    }
                }
            }

        }
        return data;
    }

    /**
     * @author Gabriel Ruano (garuanom) Obtiene los alcances de Gabinete
     */
    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getGabinet(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Gabinete");
        return alcanceRepository.getGabinet(idalcance);
    }

    /**
     * @author Gabriel Ruano (garuanom) Obtiene los alcances de Puntos fijos
     */
    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getPoint(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Puntos fijos");
        return alcanceRepository.getPoint(idalcance);
    }

    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getPresence(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Puntos fijos");
        return alcanceRepository.getPresence(idalcance);
    }

    /**
     * @author Gabriel Ruano (garuanom) Obtiene los alcances de Gabinete
     */
    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getGabinetJd(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Gabinete");
        return alcanceRepository.getGabinetJd(idalcance);
    }

    /**
     * @author Gabriel Ruano (garuanom) Obtiene los alcances de Puntos fijos
     */
    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getPointJd(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Puntos fijos");
        return alcanceRepository.getPointJd(idalcance);
    }

    @Transactional(readOnly = true)
    public List<BandejaAlcanceProjection> getPresenceJd(Integer idalcance) {
        //log.debug("Obtiene los Alcances de Puntos fijos");
        return alcanceRepository.getPresenceJd(idalcance);
    }

    @Transactional(readOnly = true)
    public DenunciasPuntosFijosPresenciasComentarioDto getAlcancePuntosPresencias(Integer idalcance) {
        List<Integer> comentario = new ArrayList<>();
        comentario.add(970);
        //log.debug("Obtiene los Alcances de Puntos fijos");
        return new DenunciasPuntosFijosPresenciasComentarioDto(
                alcanceRepository.getPuntosFijosPresencias(idalcance),
                historialComentariosRepository.findMaxIdHistorialComentario(idalcance.toString(), comentario)
        );
    }

    @Transactional
    public Boolean rechazarAlcance(Integer idalcance, UserLogged user, String comentario) {
        List<SipfDetalleCaso> rechazoCaso = detalleCasoRepository.findByIdAlcance(idalcance);
        List<SipfDenunciaGrabada> rechazoDenuncia = denunciaGrabadaRepository.findByIdAlcance(idalcance);
        List<SipfPresenciasFiscales> rechazoPresencia = presenciasFiscalesRepository.findByIdAlcance(idalcance);

        if (!rechazoCaso.isEmpty()) {
            rechazoCaso.forEach((t) -> {

                SipfCasos caso = casosRepository.casesId(t.getIdCaso());
                caso.setIdEstado(Catalog.Case.Status.SCOPE_CORRECTION);//181
                casosRepository.save(caso);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idCaso", t.getIdCaso());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "cambios");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);

                SipfHistorialComentarios history = new SipfHistorialComentarios();
                history.setIdRegistro(String.valueOf(caso.getIdCaso()));
                history.setIdTipoComentario(970);
                history.setComentarios(comentario);
                history.setFechaModifica(new Date());
                history.setIpModifica(user.getIp());
                history.setUsuarioModifica(user.getLogin());
                historialComentariosRepository.save(history);

            });
            return true;
        }

        if (!rechazoDenuncia.isEmpty()) {
            rechazoDenuncia.forEach(t -> {
                SipfDenunciaGrabada rechazo = denunciaGrabadaRepository.findByCorrelativo(t.getCorrelativo()).orElse(null);
                rechazo.setEstado(Catalog.Case.Status.SCOPE_CORRECTION);
                denunciaGrabadaRepository.save(rechazo);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getCorrelativo());
                formulario.put("idPresencia", t.getCorrelativo());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "cambios");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(rechazo.getIdProcesoAlcance()), dtoComplete);

                SipfHistorialComentarios history = new SipfHistorialComentarios();
                history.setIdRegistro(String.valueOf(rechazo.getIdAlcance()));
                history.setIdTipoComentario(970);
                history.setComentarios(comentario);
                history.setFechaModifica(new Date());
                history.setIpModifica(user.getIp());
                history.setUsuarioModifica(user.getLogin());
                historialComentariosRepository.save(history);
            });
            return true;
        }

        if (!rechazoPresencia.isEmpty()) {
            rechazoPresencia.forEach((t) -> {

                SipfPresenciasFiscales rechazo = presenciasFiscalesRepository.IdFormulario(t.getIdFormulario());
                rechazo.setIdFormulario(t.getIdFormulario());
                rechazo.setIdEstado(Catalog.Case.Status.SCOPE_CORRECTION);
                presenciasFiscalesRepository.save(rechazo);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getIdFormulario());
                formulario.put("idPresencia", t.getIdFormulario());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "cambios");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(t.getIdProceso(), dtoComplete);

                SipfHistorialComentarios history = new SipfHistorialComentarios();
                history.setIdRegistro(String.valueOf(rechazo.getIdAlcance()));
                history.setIdTipoComentario(970);
                history.setComentarios(comentario);
                history.setFechaModifica(new Date());
                history.setIpModifica(user.getIp());
                history.setUsuarioModifica(user.getLogin());
                historialComentariosRepository.save(history);
            });
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean autorizarrAlcanceJU(Integer idalcance) {
        List<SipfDetalleCaso> aprobarCaso = detalleCasoRepository.findByIdAlcance(idalcance);
        List<SipfDenunciaGrabada> aprobarDenuncia = denunciaGrabadaRepository.findByIdAlcance(idalcance);
        List<SipfPresenciasFiscales> aprobarPresencia = presenciasFiscalesRepository.findByIdAlcance(idalcance);

        if (!aprobarCaso.isEmpty()) {
            aprobarCaso.forEach((t) -> {

                SipfCasos caso = casosRepository.casesId(t.getIdCaso());
                caso.setIdEstado(19);
                casosRepository.save(caso);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(19);
                alcanceRepository.save(alcance);

                //log.debug("hola" + t.getIdCaso());
                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idCaso", t.getIdCaso());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "aprueba");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);
            });
            return true;
        }

        if (!aprobarDenuncia.isEmpty()) {
            aprobarDenuncia.forEach(t -> {
                SipfDenunciaGrabada aprobar = denunciaGrabadaRepository.findByCorrelativo(t.getCorrelativo()).orElse(null);
                aprobar.setEstado(19);
                denunciaGrabadaRepository.save(aprobar);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(19);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getCorrelativo());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "aprueba");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(aprobar.getIdProcesoAlcance()), dtoComplete);
            });
            return true;
        }

        if (!aprobarPresencia.isEmpty()) {
            aprobarPresencia.forEach((t) -> {

                SipfPresenciasFiscales rechazo = presenciasFiscalesRepository.IdFormulario(t.getIdFormulario());
                rechazo.setIdEstado(19);
                presenciasFiscalesRepository.save(rechazo);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(19);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getIdFormulario());
                formulario.put("idPresencia", t.getIdFormulario());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "aprueba");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(t.getIdProceso(), dtoComplete);
            });
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean autorizarrAlcanceJD(Integer idalcance) {
        List<SipfDetalleCaso> rechazoCaso = detalleCasoRepository.findByIdAlcance(idalcance);
        List<SipfDenunciaGrabada> rechazoDenuncia = denunciaGrabadaRepository.findByIdAlcance(idalcance);
        List<SipfPresenciasFiscales> rechazoPresencia = presenciasFiscalesRepository.findByIdAlcance(idalcance);

        if (!rechazoCaso.isEmpty()) {
            rechazoCaso.forEach((t) -> {
                SipfCasos caso = casosRepository.casesId(t.getIdCaso());
                caso.setIdEstado(133);
                casosRepository.save(caso);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(133);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getIdCaso());
                formulario.put("idPresencia", t.getIdCaso());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "autoriza");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);
            });
            return true;
        }

        if (!rechazoDenuncia.isEmpty()) {
            rechazoDenuncia.forEach(t -> {
                SipfDenunciaGrabada aprobar = denunciaGrabadaRepository.findByCorrelativo(t.getCorrelativo()).orElse(null);
                aprobar.setEstado(133);
                denunciaGrabadaRepository.save(aprobar);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(133);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getCorrelativo());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "autoriza");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(String.valueOf(aprobar.getIdProcesoAlcance()), dtoComplete);
            });
            return true;
        }

        if (!rechazoPresencia.isEmpty()) {
            rechazoPresencia.forEach((t) -> {

                SipfPresenciasFiscales rechazo = presenciasFiscalesRepository.IdFormulario(t.getIdFormulario());
                rechazo.setIdEstado(133);
                presenciasFiscalesRepository.save(rechazo);

                SipfAlcance alcance = alcanceRepository.findById(t.getIdAlcance()).orElse(null);
                alcance.setIdEstado(133);
                alcanceRepository.save(alcance);

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idMasivo", t.getIdFormulario());
                formulario.put("idPresencia", t.getIdFormulario());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "autoriza");
                JSONObject json = new JSONObject(post);
                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                dtoComplete.setOutcome("\"default\"");
                dtoComplete.setValues(json);
                contiendoACSService.completaTaksByIntanceId(t.getIdProceso(), dtoComplete);
            });
            return true;
        }
        return false;
    }

    @Transactional
    public Boolean autorizarGerencial(DenunciaGrabadaDto dto) {

        SipfDenunciaGrabada aprobar = denunciaGrabadaRepository.findByCorrelativo(dto.getCorrelativo()).orElse(null);
        if (aprobar == null) {
            return false;
        }
        aprobar.setEstado(Integer.parseInt(dto.getEstado()));
        aprobar.setCorrelativoAprobacion(dto.getCorrelativoAprobacion());
        denunciaGrabadaRepository.save(aprobar);
        Map<String, Object> post = new HashMap<>();
        JSONObject formulario = new JSONObject();
        formulario.put("idMasivo", dto.getCorrelativo());
        post.put("jsonFormulario", formulario.toJSONString());
        post.put("resultadoFormulario", "autoriza");
        JSONObject json = new JSONObject(post);
        TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
        dtoComplete.setOutcome("\"default\"");
        dtoComplete.setValues(json);
        contiendoACSService.completaTaksByIntanceId(String.valueOf(aprobar.getIdProcesoAlcance()), dtoComplete);

        return true;
    }

    @Transactional
    public Boolean RechazoGerencial(DenunciaGrabadaDto dto) {

        SipfDenunciaGrabada rechazo = denunciaGrabadaRepository.findByCorrelativo(dto.getCorrelativo()).orElse(null);
        if (rechazo == null) {
            return false;
        }
        rechazo.setEstado(Integer.parseInt(dto.getEstado()));
        rechazo.setCorrelativoAprobacion(dto.getCorrelativoAprobacion());
        denunciaGrabadaRepository.save(rechazo);
        SipfAlcance alcance = alcanceRepository.findById(dto.getIdAlcance()).orElse(null);
        alcance.setIdEstado(181);
        alcanceRepository.save(alcance);
        Map<String, Object> post = new HashMap<>();
        JSONObject formulario = new JSONObject();
        formulario.put("idMasivo", dto.getCorrelativo());
        post.put("jsonFormulario", formulario.toJSONString());
        post.put("resultadoFormulario", "cambios");
        JSONObject json = new JSONObject(post);
        TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
        dtoComplete.setOutcome("\"default\"");
        dtoComplete.setValues(json);
        contiendoACSService.completaTaksByIntanceId(String.valueOf(rechazo.getIdProcesoAlcance()), dtoComplete);

        return true;
    }

    /**
     * @author Luis Villagran
     * @description Servicio para obtener la información del caso en base al
     * estado del alcance y el tipo de alcance selecivo
     * @since 1/19/2023
     * @param states
     * @return
     */
    @Transactional(readOnly = true)
    public List<CarteraCasosProjection> getSelectScope(List<Integer> states) {
        return alcanceRepository.getSelectScope(states);
    }
}
