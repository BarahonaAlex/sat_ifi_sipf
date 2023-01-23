/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasesScopeMasiveDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasoComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ErrorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoManualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.MassiveAssignParamsDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParametroAccionInsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ReasignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ResRutaBaseACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SolicitudesAduanasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcance;
import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasosId;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasosDependencias;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfDetalleCaso;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoCaso;
import gt.gob.sat.sat_ifi_sipf.models.SipfImpuestoCasoId;
import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudesPosteriori;
import gt.gob.sat.sat_ifi_sipf.projections.AsignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraAllCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosOnlyProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetailWalletAppoint;
import gt.gob.sat.sat_ifi_sipf.projections.InsumoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.MassiveResumeProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosMasivosProjections;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ResponsableCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesAduanasProjections;
import gt.gob.sat.sat_ifi_sipf.projections.WalletAppointments;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcanceRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.AsignacionCasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosDependenciasRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosMasivosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.DetalleCasoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ImpuestoCasoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.InsumoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.SolicitudesAduanasRepository;
import gt.gob.sat.sat_ifi_sipf.utils.FileUtils;
import static gt.gob.sat.sat_ifi_sipf.utils.GeneralUtils.isSameOrNull;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import org.apache.poi.ss.usermodel.Cell;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ruarcuse
 */
@Transactional
@Service
@Slf4j
public class CasosService {

    @Autowired
    private CasosService casosService;

    @Autowired
    private InsumoRepository insumoRepository;

    @Autowired
    private ContribuyenteService taxPayerService;

    @Autowired
    private CorreoService correoServices;

    @Autowired
    private CasosRepository casosrepository;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ContiendoACSService contiendoACSService;

    @Autowired
    private CasosDependenciasRepository dependenciasRepository;

    @Autowired
    private GeneralServices generalService;

    @Autowired
    private AsignacionCasosRepository assignmentRepository;

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private HistorialComentariosRepository historialComentariosRepository;

    @Autowired
    private SolicitudesAduanasRepository solicitudesPosterioriRepository;

    @Autowired
    private ImpuestoCasoRepository impuestoCasoRepository;

    @Autowired
    private DetalleCasoRepository detalleCasoRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    Detector detector;

    @Autowired
    private ContiendoACSService contenidoACSService;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    @Autowired
    private CasosMasivosRepository casosMasivosRepository;

    @Autowired
    private SolicitudesAduanasRepository solicitudesAduanasRepository;

    @Autowired
    private AlcancesService alcancesService;

    @Autowired
    private AsignacionCasosRepository asignacionCaso;

    @Autowired
    private AlcanceRepository alcanceRepository;

    public List<AsignacionCasosProjection> findAssignmentCases(Integer cantidad, Integer idImpuesto, String nombreInconsistencia, Integer idGerencia) {
        return casosrepository.assignmentCases(cantidad, idImpuesto, idGerencia);
    }

    @Transactional(rollbackFor = Exception.class)
    public void createCaseMass(InsumoManualDto data, MultipartFile file, UserLogged userLogged) {
        data.setUsuarioModifica(userLogged.getLogin());

        final InsumoDto saveData = insumoService.saveInput(data, userLogged, true);
        final List<Map<String, Object>> newCases = extractCases(saveData, file, userLogged);
        final JSONObject body = new JSONObject();
        final List<SipfCasos> casesToValidate = new ArrayList<>();

        //se recorre la lista de maps, la cual contiene dentro el caso, el detalle y una lista con los impuestos del caso a ingresar
        newCases.forEach(item
                -> {
            SipfDetalleCaso detailNewCase = (SipfDetalleCaso) item.get("detalle");

            SipfCasos newCase = casosrepository.save((SipfCasos) item.get("caso"));
            detailNewCase.setIdCaso(newCase.getIdCaso());

            detalleCasoRepository.save(detailNewCase);
            List<String> taxes = (List<String>) item.get("taxesCase");
            //se mapean los impuestos y posteriormente se guardan
            List<SipfImpuestoCaso> caseTaxList = taxes.stream().map(t
                    -> {
                SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                caseTax.setId(new SipfImpuestoCasoId(newCase.getIdCaso(), Integer.parseInt(t)));
                caseTax.setUsuarioModifica(userLogged.getLogin());
                caseTax.setFechaModifica(new Date());
                caseTax.setIpModifica(userLogged.getIp());
                return caseTax;
            }).collect(Collectors.toList());

            impuestoCasoRepository.saveAll(caseTaxList);

            //se crea una coleccion de casos para validar si el insumo necesita algun archivo para publicarse
            casesToValidate.add(newCase);
        });

        //se usa la coleccion de casos para validar el estado del insumo
        saveData.setIdEstado(
                casesToValidate.stream().filter(item -> item.getIdEstado() == 178).count() > 0
                ? 178 : Catalog.Case.Status.PUBLISHED
        );

        body.put("jsonFormulario", saveData.toJson());
        body.put("resultadoFormulario", "interna");

        try {
            MultipartFile newFile = FileUtils.renameTo(
                    file,
                    "SAT-IFI-SIPF-I-"
                    + data.getIdInsumo()
                    + FileUtils.getExtension(file.getContentType())
            );
            this.contenidoACSService.uploadFiles(contenidoACSService.getNodosACSBySiteAndPath(TipoRuta.CARGA_MASIVAS, null).getId(), Arrays.asList(newFile));
        } catch (IOException ex) {
            Logger.getLogger(CasosService.class.getName()).log(Level.SEVERE, null, ex);
        }

        final StartProcessDto processId = contenidoACSService.startProces(1, body);
        saveData.setIdProceso(processId.getId());
        insumoService.modifyInput(saveData, saveData.getIdInsumo(), userLogged);
    }

    private List<Map<String, Object>> extractCases(InsumoDto data, MultipartFile file, UserLogged userLogged) {
        /*final List<SipfCasos> newCases = new ArrayList<>();*/
        final List<ErrorDto> errors = new ArrayList<>();
        //se instancia una lista de maps para guardar el caso, detalle e impuestos del caso
        final List<Map<String, Object>> casesList = new ArrayList<>();
        final List<String> collaborators = colaboradorRepository.getAllCollaboratorsLogin();

        try (InputStream document = file.getInputStream(); XSSFWorkbook myWorkBook = new XSSFWorkbook(document)) {
            // Return first sheet from the XLSX workbook
            XSSFSheet mySheet = myWorkBook.getSheetAt(0);

            // Get iterator to all the rows in current sheet
            Iterator<Row> rowIterator = mySheet.iterator();

            // Traversing over each row of XLSX file
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                boolean rowIsEmpty = checkIfRowIsEmpty(row);
                //log.debug("row number: " + row.getRowNum());
                //se crea un map que almacenara por cada caso su detalle e impuesto
                Map<String, Object> massiveDto = new HashMap<>();
                SipfCasos casoNuevo = new SipfCasos();
                SipfDetalleCaso detalleCasoNuevo = new SipfDetalleCaso();
                casoNuevo.setIdEstado(Catalog.Case.Status.PUBLISHED);
                detalleCasoNuevo.setRequiereDocumentacion(false);

                // For each row, iterate through each columns
                if (row.getRowNum() != 0 && !rowIsEmpty) {
                    Iterator<Cell> cellIterator = row.cellIterator();
                    while (cellIterator.hasNext()) {
                        Cell cell = cellIterator.next();

                        switch (cell.getColumnIndex()) {

                            case 0://nit_contribuyente
                                casoNuevo.setNitContribuyente(getValueAsString(cell));
                                break;
                            case 1://impuesto
                                /* casoNuevo.setIdImpuesto((int) getValueAsNumeric(cell)); */

                                //se almacenan los distintos impuestos que puede tener un caso, dentro de una lista
                                massiveDto.put("taxesCase", Arrays.asList(getValueAsString(cell).split("(,\\s*)")));
                                break;
                            case 2://periodo_revision_inicio
                                casoNuevo.setPeriodoRevisionInicio(cell.getDateCellValue());
                                break;
                            case 3://periodo_revision_fin
                                casoNuevo.setPeriodoRevisionFin(cell.getDateCellValue());
                                break;
                            case 4://monto_recaudado
                                casoNuevo.setMontoRecaudado(getValueAsNumeric(cell));
                                break;
                            case 5://login_profesional
                                detalleCasoNuevo.setLoginProfesional(getValueAsString(cell));
                                break;
                            case 6://requiere documentacion
                                if ("x".equals(getValueAsString(cell).toLowerCase(Locale.getDefault()))) {
                                    detalleCasoNuevo.setRequiereDocumentacion(true);
                                    casoNuevo.setIdEstado(178);
                                }
                                break;
                        }
                    }

                    casoNuevo.setFechaModifica(new Date());
                    casoNuevo.setUsuarioModifica(userLogged.getLogin());
                    casoNuevo.setIpModifica(userLogged.getIp());
                    casoNuevo.setIdDepartamento(data.getIdDepartamento());
                    casoNuevo.setIdGerencia(data.getIdGerencia());
                    casoNuevo.setIdInsumo(data.getIdInsumo());
                    detalleCasoNuevo.setTipoCaso(data.getIdTipoCaso());
                    detalleCasoNuevo.setFechaModifica(new Date());
                    detalleCasoNuevo.setUsuarioModifica(userLogged.getLogin());
                    detalleCasoNuevo.setIpModifica(userLogged.getIp());

                    if (collaborators.stream().filter(t -> t.equals(detalleCasoNuevo.getLoginProfesional())).collect(Collectors.toList()).isEmpty()) {
                        errors.add(
                                ErrorDto.builder()
                                        .param("Fila " + row.getRowNum())
                                        .value(detalleCasoNuevo.getLoginProfesional())
                                        .description("Login del profesional inválido.")
                                        .build()
                        );
                    }

                    if (casoNuevo.getNitContribuyente() != null) {

                        try {

                            taxPayerService.getGeneralTaxpayerInformation(
                                    casoNuevo.getNitContribuyente()
                            );

                            //se mapea el caso y el detalle 
                            massiveDto.put("caso", casoNuevo);
                            massiveDto.put("detalle", detalleCasoNuevo);
                            //se guarda el caso junto con la lista de impuestos y el detalle en la lista de cases
                            casesList.add(massiveDto);
                            //newCases.add(casoNuevo);
                        } catch (BusinessException e) {
                            errors.add(
                                    ErrorDto.builder()
                                            .param("Fila " + row.getRowNum())
                                            .value(casoNuevo.getNitContribuyente())
                                            .description("NIT del contribuyente inválido.")
                                            .build()
                            );
                        }
                    } else {
                        errors.add(
                                ErrorDto.builder()
                                        .param("Fila " + row.getRowNum())
                                        .value(casoNuevo.getNitContribuyente())
                                        .description("Columna del NIT vacía.")
                                        .build()
                        );
                    }

                }
            }
        } catch (Exception ex) {
            //log.debug("Error: ", ex);
            throw BusinessException.unprocessableEntity("El documento contiene errores de estructura. Por favor, verifique.");
        }
        if (!errors.isEmpty()) {
            throw BusinessException.badRequestError("El documento contiene errores. Por favor, verifique.", errors);
        }

        return casesList;
    }

    private String getValueAsString(Cell cell) {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return String.valueOf(Double.valueOf(cell.getNumericCellValue()).intValue());
        }
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        }
        return "";
    }

    private double getValueAsNumeric(Cell cell) {
        if (cell.getCellType().equals(CellType.NUMERIC)) {
            return cell.getNumericCellValue();
        }
        if (cell.getCellType().equals(CellType.STRING)) {
            return Double.parseDouble(cell.getStringCellValue());
        }
        return -1;
    }

    private boolean checkIfRowIsEmpty(Row row) {
        if (row == null) {
            return true;
        }
        if (row.getLastCellNum() <= 0) {
            return true;
        }
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK && StringUtils.isNotBlank(cell.toString())) {
                return false;
            }
        }
        return true;
    }

    public CasosDto ingresoArchivosRespaldo(CasosDto data, List<MultipartFile> archivo, UserLogged user) {
        Map<String, Object> json = new HashMap<>();
        json.put("caso", data.getIdCaso().toString());
        json.put("carpeta", "Archivos Respaldo");
        ResRutaBaseACSDto peticion = new ResRutaBaseACSDto(2, json);
        NodosACSDto key = this.contenidoACSService.getNodosACSBySiteAndPath(TipoRuta.CASOS, json);
        this.contenidoACSService.uploadFiles(key.getId(), archivo);
        data.setEstado(Catalog.Case.Status.DOCUMENTED);
        alterCase(data.getIdCaso(), data, user);
        return data;
    }

    public void replaceFile(String id, MultipartFile file) {

        this.contenidoACSService.replaceFile(id, file);
    }

    @Transactional(rollbackFor = Exception.class)
    public CasosDto createCase(CasosDto data, List<MultipartFile> archivo, UserLogged user) throws IOException {

        if (data.getTipoAlcance() == 117) {
            SipfCasos caso = casosrepository.save(
                    SipfCasos.builder()
                            .nitContribuyente(data.getNitContribuyente())
                            .montoRecaudado(data.getMontoRecaudado())
                            .periodoRevisionInicio(data.getPeriodoRevisionInicio())
                            .periodoRevisionFin(data.getPeriodoRevisionFin())
                            .idGerencia(data.getGerencia())
                            .idOrigen(data.getOrigen())
                            .idEstado(Catalog.Case.Status.PUBLISHED)
                            .idProceso(data.getProceso())
                            .idDepartamento(data.getTipoAlcance())
                            .usuarioModifica(detector.getLogin())
                            .fechaModifica(new Date())
                            .ipModifica(detector.getIp())
                            .idInsumo(data.getInsumo())
                            .build()
            );

            impuestoCasoRepository.saveAll(
                    data.getImpuestos().stream().map(t
                            -> {
                        SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                        caseTax.setId(new SipfImpuestoCasoId(caso.getIdCaso(), t));
                        caseTax.setUsuarioModifica(user.getLogin());
                        caseTax.setFechaModifica(new Date());
                        caseTax.setIpModifica(user.getIp());
                        return caseTax;
                    }).collect(Collectors.toList()));

            detalleCasoRepository.save(
                    SipfDetalleCaso.builder()
                            .idCaso(caso.getIdCaso())
                            .tipoCaso(969)
                            .usuarioModifica(user.getLogin())
                            .fechaModifica(new Date())
                            .ipModifica(user.getIp())
                            .build());
        } else {

            //log.debug("**************************** entra en el else");
            for (int i = 0; i < data.getArrayGeneral().size(); i++) {
                //data.getImpuestos().set(0, data.getImpuestoExterna());

                SipfCasos caso = casosrepository.save(
                        SipfCasos.builder()
                                .nitContribuyente(data.getArrayGeneral().get(i).getNit())
                                .montoRecaudado(data.getMontoRecaudado())
                                .periodoRevisionInicio(data.getArrayGeneral().get(i).getFechaDesde())
                                .periodoRevisionFin(data.getArrayGeneral().get(i).getFechaHasta())
                                .idGerencia(data.getArrayGeneral().get(i).getIdGerencia())
                                .idOrigen(data.getOrigen())
                                .idEstado(15)
                                //.idProceso(data.getProceso())
                                .idDepartamento(data.getTipoAlcance())
                                .usuarioModifica(detector.getLogin())
                                .fechaModifica(new Date())
                                .ipModifica(detector.getIp())
                                .idInsumo(data.getInsumo())
                                .build()
                );
                data.setIdCaso(caso.getIdCaso());

                Map<String, Object> post = new HashMap<>();
                JSONObject formulario = new JSONObject();
                formulario.put("idCaso", caso.getIdCaso());
                formulario.put("nitColaborador", data.getNitColaborador());
                post.put("jsonFormulario", formulario.toJSONString());
                post.put("resultadoFormulario", "externas");
                JSONObject json = new JSONObject(post);//starProcces
                //log.debug("->:" + json);
                //String url = "sat-gestor-app/process-instances/app-definition/" + consumosService.getAppId(2);
                StartProcessDto processId
                        = //consumosService.consume(json, url, StartProcessDto.class,                     HttpMethod.POST);
                        contiendoACSService.startProces(2, json);

                //log.debug("proceso stardeado *************************");
                //log.debug(processId.getId());
                data.setProceso(processId.getId());

                //casosService.alterCase(caso.getIdCaso(), CasosDto.builder().estado(15).proceso(processId.getId()).build(), user);

                /*   impuestoCasoRepository.saveAll(
                        data.getImpuestos().stream().map(t -> {
                            SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                            caseTax.setId(new SipfImpuestoCasoId(caso.getIdCaso(), t));
                            caseTax.setUsuarioModifica(user.getLogin());
                            caseTax.setFechaModifica(new Date());
                            caseTax.setIpModifica(user.getIp());
                            return caseTax;
                        }).collect(Collectors.toList())); */
                dependenciasRepository.save(
                        SipfCasosDependencias.builder()
                                .idCaso(caso.getIdCaso())
                                .idEntidadSolicitante(data.getEntidadSolicitante())
                                .detalleEntidadSolicitante(data.getDetalleEntidadSolicitante())
                                .idTipoSolicitud(data.getTipoSolicitud())
                                .nombreCuentaAuditar(data.getNombreCuentaAuditar())
                                .nitContribuyenteCruce(data.getNitContribuyenteCruce())
                                .numeroFacturaCruce(data.getNumeroFacturaCruce())
                                .fechaFacturaCruce(data.getFechaFacturaCruce())
                                .serieFacturaCruce(data.getSerieFacturaCruce())
                                .montoFacturaCruce(data.getMontoFacturaCruce())
                                .fechaSolicitud(data.getFechaSolicitud())
                                .fechaDocumento(data.getFechaDocumento())
                                .numeroDocumento(data.getNumeroDocumento())
                                .plazoEntrega(data.getPlazoEntrega())
                                .nombreContacto(data.getNombreContacto())
                                .correoContacto(data.getCorreoContacto())
                                .telefonoContacto(data.getTelefonoContacto())
                                .build()
                );

                Integer vIdGrupo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_id_grupo_asignacion");

                SipfAsignacionCasosId assingmentId = new SipfAsignacionCasosId(caso.getIdCaso(), data.getJefeUnidad());
                SipfAsignacionCasos assingment = new SipfAsignacionCasos();
                assingment.setIdEstado(15);
                assingment.setIdOrigen(140);
                assingment.setIpModifica(detector.getIp());
                assingment.setFechaModifica(new Date());
                assingment.setIdGrupoAsignacion(vIdGrupo);
                assingment.setUsuarioModifica(detector.getLogin());
                assingment.setId(assingmentId);

                data.setEstado(15);
                assignmentRepository.save(assingment);
                this.alterCase(caso.getIdCaso(), data, user);

                Map<String, Object> post2 = new HashMap<>();
                post2.put("caso", caso.getIdCaso());
                post2.put("carpeta", "Archivos Respaldo");
                JSONObject json2 = new JSONObject(post2);
                //log.debug("hola" + json2.toString());
                NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.CASOS, json2);
                //log.debug("nodos" + nodos.getId());
                contiendoACSService.uploadFiles(nodos.getId(), archivo);

            }

        }

        /* if (data.getTipoAlcance() == 120) {
            masivosRepository.save(
                    SipfCasosMasivos.builder()
                            .idCaso(caso.getIdCaso())
                            .objetivoCasoMasiva(data.getObjetivoCasoMasiva())
                            .build()
            );
            String post = "";
            String correlativo = "";
            String correo = "Se le ha asignado una solicitud para realizar alcance masivo";
            correoServices.envioCorreo(data.getCorreoContacto(), correo, "Solicitud Masiva");
        }*/

 /*if (Arrays.asList(120, 118).contains(data.getTipoAlcance())) {

            Integer vIdGrupo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_id_grupo_asignacion");

            SipfAsignacionCasosId assingmentId = new SipfAsignacionCasosId(caso.getIdCaso(), data.getNitColaborador());
            SipfAsignacionCasos assingment = new SipfAsignacionCasos();
            assingment.setIdEstado(130);
            assingment.setIdOrigen(140);
            assingment.setIpModifica(detector.getIp());
            assingment.setFechaModifica(new Date());
            assingment.setIdGrupoAsignacion(vIdGrupo);
            assingment.setUsuarioModifica(detector.getLogin());
            assingment.setId(assingmentId);

            caso.setIdEstado(16);
            assignmentRepository.save(assingment);
        }*/
        //log.debug("****************************************************");
        //log.debug(data.getIdCaso().toString());
        return data;
    }

    public List<CarteraCasosProjection> walletCase(String nitProfesional) {
        return casosrepository.walletCase(nitProfesional);
    }

    public CasoComentarioDto getCase(Integer id, UserLogged user) {
        List<Integer> comentario = new ArrayList<>();
        if (user.getRoles() == null) {
            comentario.add(970);
            comentario.add(408);
            comentario.add(409);
        } else {

            user.getRoles().forEach((String data)
                    -> {
                switch (data) {
                    case "AdministrativoSIPFOperador":
                        comentario.add(970);
                        break;
                    case "AdministrativoSIPFSolicitante":
                        comentario.add(408);
                        comentario.add(409);
                        comentario.add(189);
                        break;
                    case "AdministrativoSIPFAprobador":
                        comentario.add(409);
                        break;
                    default:
                        comentario.add(970);
                        comentario.add(408);
                        comentario.add(409);
                        break;

                }
            });
        }
        return new CasoComentarioDto(
                casosrepository.findByIdCase(id),
                historialComentariosRepository.findMaxIdHistorialComentario(id.toString(), comentario)
        );

    }

    public SipfDetalleCaso updateDetailCase(Integer idCaso, Integer idAlcance, UserLogged user) {
        Optional<SipfDetalleCaso> oldCase = detalleCasoRepository.findById(idCaso);
        if (oldCase.isPresent()) {
            SipfDetalleCaso tmp = oldCase.get();
            tmp.setIdAlcance(idAlcance);
            tmp.setUsuarioModifica(user.getLogin());
            tmp.setFechaModifica(new Date());
            tmp.setIpModifica(user.getIp());
            detalleCasoRepository.save(tmp);
            return tmp;
        }
        return null;
    }

    public ResponseEntity<SipfCasos> alterCase(Integer idCaso, CasosDto caseModify, UserLogged user) {

        Optional<SipfCasos> oldCase = casosrepository.findById(idCaso);
        List<SipfImpuestoCaso> oldTaxes = impuestoCasoRepository.findByIdIdCaso(idCaso);

        if (oldCase.isPresent()) {
            SipfCasos tmp = oldCase.get();
            int estadoAnterior = tmp.getIdEstado();
            tmp.setCorrelativoAprobacion(isSameOrNull(caseModify.getCorrelativoAprobacion(), tmp.getCorrelativoAprobacion()) ? tmp.getCorrelativoAprobacion() : caseModify.getCorrelativoAprobacion());
            tmp.setIdEstado(isSameOrNull(caseModify.getEstado(), tmp.getIdEstado()) ? tmp.getIdEstado() : caseModify.getEstado());
            tmp.setIdPrograma(isSameOrNull(caseModify.getPrograma(), tmp.getIdPrograma()) ? tmp.getIdPrograma() : caseModify.getPrograma());
            //log.debug("*********************************************");
            //log.debug(caseModify.getProceso());
            //log.debug(tmp.getIdProceso());
            tmp.setIdProceso(isSameOrNull(caseModify.getProceso(), tmp.getIdProceso()) ? tmp.getIdProceso() : caseModify.getProceso());
            tmp.setIdGerencia(isSameOrNull(caseModify.getGerencia(), tmp.getIdGerencia()) ? tmp.getIdGerencia() : caseModify.getGerencia());
            //tmp.setIdImpuesto(isSameOrNull(caseModify.getImpuesto(), tmp.getIdImpuesto()) ? tmp.getIdImpuesto() : caseModify.getImpuesto());
            tmp.setIdDepartamento(isSameOrNull(caseModify.getTipoAlcance(), tmp.getIdDepartamento()) ? tmp.getIdDepartamento() : caseModify.getTipoAlcance());
            tmp.setPeriodoRevisionInicio(isSameOrNull(caseModify.getPeriodoRevisionInicio(), tmp.getPeriodoRevisionInicio()) ? tmp.getPeriodoRevisionInicio() : caseModify.getPeriodoRevisionInicio());
            tmp.setPeriodoRevisionFin(isSameOrNull(caseModify.getPeriodoRevisionFin(), tmp.getPeriodoRevisionFin()) ? tmp.getPeriodoRevisionFin() : caseModify.getPeriodoRevisionFin());
            tmp.setUsuarioModifica(user.getLogin());
            tmp.setFechaModifica(new Date());
            tmp.setIpModifica(user.getIp());

            /*if (oldTaxes.isEmpty()) {
                throw new BusinessException(HttpStatus.NOT_FOUND, "No se encontraron impuestos relacionados a este caso");
            }*/
            if (caseModify.getImpuestos() != null) {

                List<SipfImpuestoCaso> deleteTaxes = oldTaxes.stream()
                        .filter((t) -> !caseModify.getImpuestos().stream().map((k) -> k).collect(toSet()).contains(t.getId().getIdImpuesto()))
                        .collect(Collectors.toList());
                List<SipfImpuestoCaso> newTaxes = caseModify.getImpuestos().stream().filter((t)
                        -> !oldTaxes.stream().map((k) -> k.getId().getIdImpuesto()).collect(toSet()).contains(t)).collect(Collectors.toList())
                        .stream().map((n)
                                -> {
                            SipfImpuestoCaso caseTax = new SipfImpuestoCaso();
                            caseTax.setId(new SipfImpuestoCasoId(tmp.getIdCaso(), n));
                            caseTax.setUsuarioModifica(user.getLogin());
                            caseTax.setFechaModifica(new Date());
                            caseTax.setIpModifica(user.getIp());
                            return caseTax;
                        }).collect(Collectors.toList());

                if (newTaxes.size() > 0) {
                    impuestoCasoRepository.saveAll(newTaxes);
                }
                if (deleteTaxes.size() > 0) {
                    //provisional
                    impuestoCasoRepository.deleteAll(deleteTaxes);
                }

            }

            casosrepository.save(tmp);
            if (caseModify.getEstado() == Catalog.Case.Status.DOCUMENTED && casosrepository.undocumentedCases(tmp.getIdInsumo(), Catalog.Case.Status.PENDING_DOCUMENTATION) == 0) {
                insumoService.changeInputStatus(tmp.getIdInsumo(), Catalog.Input.Status.PENDING_PUBLICATION, user);
            }
            if (caseModify.getEstado() == 179 || caseModify.getEstado() == 17) {
                if (tmp.getIdProceso() != null) {
                    alterStateCases(idCaso, 18, caseModify.getEstado(), user, "interno");
                }
            }
            if (caseModify.getEstado() == 15 && estadoAnterior == 17) {
                if (tmp.getIdProceso() != null && !tmp.getIdProceso().equalsIgnoreCase("0")) {
                    alterStateCases(idCaso, 15, caseModify.getEstado(), user, "corregido");
                }
            }
            if (caseModify.getEstado() == 1040) {
                if (tmp.getIdProceso() != null) {
                    alterStatecasesWithComentary(idCaso, 1040, caseModify.getEstado(), user, caseModify.getComentario(), 189, "cancelado");
                }
            }
            return ResponseEntity.ok(tmp);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    public void updateCaseByInsumo(Integer idInsumo, CasosDto caseM, UserLogged user) {
        List<SipfCasos> oldCase = casosrepository.findByidInsumo(idInsumo);
        oldCase.forEach((lista)
                -> {
            //log.debug("lista " + lista.getIdCaso() + ", caseM" + caseM.getEstado());
            alterCase(lista.getIdCaso(), caseM, user);
        });

    }

    public void updateCaseByInsumo(Integer idInsumo, UserLogged user) {
        List<SipfCasos> oldCase = casosrepository.findByidInsumo(idInsumo);

        //se guarda la lista de casos con su estado anterior al rechazo
        historialOperacionesRepository.save(SipfHistoricoOperaciones.builder()
                .data(new Gson().toJson(oldCase))
                .fechaModifica(new Date())
                .ipModifica(user.getIp())
                .usuarioModifica(user.getLogin())
                .idCambioRegistro(String.valueOf(idInsumo))
                .idTipoOperacion(405)
                .nombreTabla("sipf_insumo")
                .build());

        oldCase.forEach((lista)
                -> {
            //log.debug("lista " + lista.getIdCaso() + ", caseM");
            CasosDto casoNuevo = new CasosDto();
            casoNuevo.setEstado(1039);

            alterCase(lista.getIdCaso(), casoNuevo, user);
        });
    }

    public ResponseEntity<?> modifyCaseProgram(Integer idCaso) {
        //log.debug("" + idCaso);
        Integer p = idCaso;
        Optional<SipfCasos> caso = casosrepository.findById(p);
        Optional alterCases = Optional.empty();
        if (caso.isPresent()) {
            SipfCasos tmp = caso.get();
            //log.debug("" + tmp);
            tmp.setIdPrograma(null);
            alterCases = Optional.of(casosrepository.save(tmp));

        }
        return alterCases.isPresent() ? ResponseEntity.ok(alterCases) : ResponseEntity.notFound().build();
    }

    public List<SipfCasos> getCasesByPage(Integer pIdInput, Pageable pPage) {

        return this.casosrepository.findByIdInsumo(pIdInput, pPage);
    }

    public ResponseEntity<List<CasoProjection>> getCasesByInputAndStatus(Integer pIdInput, Integer pIdStatus) {
        return ResponseEntity.ok(this.casosrepository.findByIdInsumoAndIdEstado(pIdInput, pIdStatus));
    }

    public ResponseEntity<List<CasoProjection>> getCasesByInput(Integer pIdInput) {

        if (pIdInput == 0) {
            return ResponseEntity.ok(this.casosrepository.findByAll());
        }
        return ResponseEntity.ok(this.casosrepository.findByIdInsumo(pIdInput));
    }

    public ResponseEntity<List<CasoProjection>> getCasesByInputAndStatusAndIdFicha(Integer pIdInput) {
        if (pIdInput == 0) {
            return ResponseEntity.ok(this.casosrepository.findByAllAndIdEstadoAndIdFichaTecnica());
        }
        return ResponseEntity.ok(this.casosrepository.findByIdInsumoAndIdEstadoAndIdFichaTecnica(pIdInput, 178));
    }

    @Transactional(rollbackFor
            = {
                Exception.class
            })
    public ResponseEntity<?> assignmentCases(ParametroAccionInsumoDto pParamDto, UserLogged user, Integer id) {
        if (pParamDto.getPCantidad() > 0) {
            ResponseEntity<List<CasoProjection>> casosPendientes = this.casosService.getCasesByInputAndStatus(id, Catalog.Case.Status.PUBLISHED);
            if (casosPendientes.hasBody()) {
                if (!casosPendientes.getBody().isEmpty()) {
                    if (casosPendientes.getBody().size() >= pParamDto.getPCantidad()) {
                        Pageable pPage = PageRequest.of(0, pParamDto.getPCantidad());
                        final SipfColaborador collaborator = colaboradorRepository.findById(pParamDto.getPNit()).orElse(null);
                        List<SipfCasos> vListaCasosAsignar = this.casosrepository.findByIdInsumoAndIdEstado(id, Catalog.Case.Status.PUBLISHED, pPage);
                        Integer vIdGrupo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_id_grupo_asignacion");
                        List<SipfAsignacionCasos> vAsignacionCasos = new ArrayList<>();
                        vListaCasosAsignar.forEach(caso
                                -> {
                            log.debug("prueba asignacion" + caso.getIdCaso());
                            SipfAsignacionCasosId vIdAsignacionCasos = new SipfAsignacionCasosId();
                            SipfAsignacionCasos vAsignacion = new SipfAsignacionCasos();
                            vAsignacion.setIdEstado(15);
                            vAsignacion.setIdOrigen(140);
                            vAsignacion.setIpModifica(user.getIp());
                            vAsignacion.setFechaModifica(new Date());
                            vAsignacion.setIdGrupoAsignacion(vIdGrupo);
                            vAsignacion.setUsuarioModifica(user.getLogin());

                            vIdAsignacionCasos.setIdCaso(caso.getIdCaso());
                            vIdAsignacionCasos.setNitColaborador(pParamDto.getPNit());
                            vAsignacion.setId(vIdAsignacionCasos);

                            vAsignacionCasos.add(vAsignacion);
                            Map<String, Object> post = new HashMap<>();
                            JSONObject formulario = new JSONObject();
                            formulario.put("idCaso", caso.getIdCaso());
                            formulario.put("nitColaborador", pParamDto.getPNit());
                            post.put("jsonFormulario", formulario.toJSONString());
                            post.put("resultadoFormulario", "interna");
                            JSONObject json = new JSONObject(post);//starProcces
                            //log.debug("->:" + json);
                            String url = "sat-gestor-app/process-instances/app-definition/" + consumosService.getAppId(2);
                            StartProcessDto processId
                                    = //consumosService.consume(json, url, StartProcessDto.class,                     HttpMethod.POST);
                                    contiendoACSService.startProces(2, json);

                            casosService.alterCase(caso.getIdCaso(), CasosDto.builder().estado(15).proceso(processId.getId()).build(), user);

                            correoServices.envioCorreo(collaborator.getCorreo(), "Se le informa que al profesional " + collaborator.getNombres() + " se le asigno el caso " + caso.getIdCaso(), "Asignacion de caso de colaborador");

                        });
                        assignmentRepository.saveAll(vAsignacionCasos);
                        //falta terminar el analisis
                        //casosPendientes = this.casosService.getCasesByInputAndStatus(pParamDto.getPInput(), 177);
                        //if (casosPendientes.getBody().isEmpty()) {
                        //  Optional<SipfInsumo> vInsumo = this.insumoRepository.findById(pParamDto.getPInput());
                        //vInsumo.get().setIdEstado(473);
                        //insumoRepository.save(vInsumo.get());

                        /*Map<String, Object> post = new HashMap<>();
                            post.put("outcome", "default");
                            Map<String, Object> post2 = new HashMap<>();
                            post2.put("jsonFormulario", vAsignacionCasos.toString());
                            post2.put("resultadoFormulario", "");
                            JSONObject json2 = new JSONObject(post2);
                            post.put("values", json2);
                            JSONObject json = new JSONObject(post);
                            //log.debug("->:" + json);
                            contiendoACSService.completaTaksByIntanceId(vInsumo.get().getIdProceso(), json.toString());*/
                        //} 
                        // 1 que si se terminan los casos hay que terminar el proceso 
                        // el insumo a un nuevo estado de finalizado
                        casosPendientes = this.casosService.getCasesByInputAndStatus(id, Catalog.Case.Status.PUBLISHED);
                        if (casosPendientes.hasBody()) {
                            if (casosPendientes.getBody().isEmpty()) {
                                InsumoProjection vInsumo = this.insumoService.findByIdInsumo(id);

                                if (user.getRoles().contains("AdministrativoSIPFAutorizador")) {
                                    final JSONObject valuesBodyTaks = new JSONObject();
                                    valuesBodyTaks.put("idEstado", vInsumo.getIdEstado());
                                    final JSONObject bodyTaks = new JSONObject();
                                    bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
                                    bodyTaks.put("resultadoFormulario", "trasladar");
                                    contiendoACSService.completaTaksByIntanceId(String.valueOf(vInsumo.getIdProceso()), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
                                }

                                Map<String, Object> post = new HashMap<>();
                                JSONObject formulario = new JSONObject();
                                formulario.put("idInsumo", vInsumo.getIdInsumo());
                                formulario.put("nitColaborador", pParamDto.getPNit());
                                post.put("jsonFormulario", formulario.toJSONString());
                                post.put("resultadoFormulario", "asignado");
                                JSONObject json = new JSONObject(post);
                                //log.debug("->:" + json);
                                TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
                                dtoComplete.setOutcome("\"default\"");
                                dtoComplete.setValues(json);
                                contiendoACSService.completaTaksByIntanceId(vInsumo.getIdProceso().toString(), dtoComplete);
                                InsumoDto vInputDto = new InsumoDto();
                                vInputDto.setIdEstado(Catalog.Input.Status.NO_PENDIG_CASES);
                                this.insumoService.modifyInput(vInputDto, id, user);

                            }
                        }
                        return ResponseEntity.ok().build();
                    } else {
                        // aca tiene que ir el mensaje que  no se puede 
                        throw new BusinessException(HttpStatus.CONFLICT, "Casos insuficientes pendientes de asignar.");
                    }
                } else {
                    throw new BusinessException(HttpStatus.NOT_FOUND, "No existen casos pendientes de asignar.");
                }
            } else {
                throw new BusinessException(HttpStatus.NOT_FOUND, "No existen casos pendientes de asignar.");
            }

        } else {
            throw new BusinessException(HttpStatus.CONFLICT, "Casos insuficientes pendientes de asignar.");
        }

    }

    @Transactional(readOnly = true)
    public List<DesasignacionCasosProjection> getCasesForUnassign(String nit) {
        //log.debug("Obtiene informacion de los casos que tiene un profesional");
        return assignmentRepository.getCasesForUnassign(nit);
    }

    @Transactional(readOnly = true)
    public List<ReasignacionCasosProjection> getCasesForReassign(String nit) {
        //log.debug("Obtiene informacion de los casos que tiene un profesional");
        return assignmentRepository.getCasesForReassign(nit);
    }

    @Transactional(rollbackFor
            = {
                Exception.class
            })
    public boolean UnassignCases(ReasignacionCasosDto dto, List<Integer> idCaso) {

        try {
            for (int i = 0; i < idCaso.size(); i++) {
                SipfAsignacionCasos UnassignCases = new SipfAsignacionCasos();
                SipfAsignacionCasos UnassignCasesChange = assignmentRepository.findById(new SipfAsignacionCasosId(idCaso.get(i), dto.getNitAnterior())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
                if (UnassignCasesChange != null) {
                    UnassignCasesChange.setIdEstado(139);

                    UnassignCases.setId(new SipfAsignacionCasosId(idCaso.get(i), dto.getNit()));
                    UnassignCases.setIdEstado(182);
                    UnassignCases.setIdOrigen(UnassignCasesChange.getIdOrigen());
                    UnassignCases.setIdGrupoAsignacion(UnassignCasesChange.getIdGrupoAsignacion());
                    UnassignCases.setFechaModifica(new Date());
                    UnassignCases.setUsuarioModifica(detector.getLogin());
                    UnassignCases.setIpModifica(detector.getIp());
                    assignmentRepository.save(UnassignCases);

                }
            }
        } catch (Exception e) {
            log.error("Eror al consumir el servicico desasignar: " + e);
            return false;
        }
        return true;

    }

    public List<WalletAppointments> getWalletAppointments() {
        return casosrepository.walletCasesFinal();
    }

    public List<DetailWalletAppoint> getDetailWalletAppointments(Integer id) {
        return casosrepository.detailWalletAppointments(id);
    }

    /**
     * Se obtienen todos los casos en base a su estado
     *
     * @author Jose Aldana (jdaldana)
     * @since 04/11/2022
     * @return lista de todos los casos para su aprobacion en la programacion
     */
    public List<CarteraAllCasesProjection> casesAll() {
        return casosrepository.casesByStatesAll();
    }

    /**
     * Se obtienen todos los casos en base a su estado
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pStates Lista de estados
     * @since 23/07/2022
     * @return casos
     */
    @Transactional(readOnly = true)
    public List<CarteraCasosProjection> casesByStates(List<Integer> pStates) {

        return casosrepository.casesByStates(pStates);
    }

    /**
     * Metodo para actualizar el estado de un caso
     *
     * @author Rudy Culajay (ruarcuse)
     * @param idCase indentificador del caso
     * @param idState estado nuevo
     * @param idStateValid estado anterior
     * @param user datos del usuario
     * @since 23/07/2022
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean alterStateCases(Integer idCase, int idState, Integer idStateValid, UserLogged user, String outcome) {
        //log.debug("estado" + idState +  idStateValid);
        SipfCasos caso = casosrepository.findById(idCase).orElseThrow(() -> new BusinessException(HttpStatus.NOT_FOUND, "Caso no encontrado."));
        if (idStateValid != null && caso.getIdEstado() != idStateValid) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "La fase a la que se desea cambiar el caso no es la correcta");
        }
        if (idState == Catalog.Scope.Status.SCOPE_REVIEW_JD) {
            caso.setIdEstado(Catalog.Case.Status.ELABORATE_SCOPE);
        }
        if (idState == Catalog.Case.Status.PROGRAMMING_AUTHORIZATION) {
            caso.setIdEstado(Catalog.Case.Status.PROGRAMMING_AUTHORIZATION);
        } else {
            caso.setIdEstado(idState);
        }
        caso.setUsuarioModifica(user.getLogin());
        caso.setIpModifica(user.getIp());
        caso.setFechaModifica(new Date());
        casosrepository.save(caso);

        //log.debug("esto es el id proceso *********");
        //log.debug(caso.getIdProceso());
        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_casos")
                        .idCambioRegistro(String.valueOf(idCase))
                        .idTipoOperacion(405)
                        .data(new Gson().toJson(caso))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );

        /*if (idState == Catalog.Case.Status.ELABORATE_SCOPE) {
                alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
            } else {
                alcance.setIdEstado(idState);
            }*/
        // Se realiza el cambio de estado del alcance asociado
        //log.debug("idCase es" + idCase);
        SipfDetalleCaso alcanceCaso = detalleCasoRepository.findById(idCase).get();
        // log.debug(alcanceCaso.getIdAlcance() + " idAlcance");
        if (alcanceCaso.getIdAlcance() != null) {
            SipfAlcance alcance = alcanceRepository.findById(alcanceCaso.getIdAlcance()).get();
            //   log.debug( " entra" + idState);
            switch (idState) {
                case 18:
                    alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
                    // log.debug( " entra" + alcance.getIdEstado());
                    break;
                case 1092:
                    alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JD);
                    break;
                case 133:
                    alcance.setIdEstado(Catalog.Scope.Status.PROGRAMMING_AUTHORIZATION);
                    break;
            }
            alcance.setUsuarioModifica(user.getLogin());
            alcance.setFechaModifica(new Date());
            alcance.setIpModifica(user.getIp());
            alcanceRepository.save(alcance);
            //log.debug("llego al final");
        }

        if (!outcome.isEmpty()) {

            final JSONObject valuesBodyTaks = new JSONObject();
            valuesBodyTaks.put("idEstado", idState);
            final JSONObject bodyTaks = new JSONObject();
            bodyTaks.put("jsonFormulario", valuesBodyTaks.toJSONString());
            bodyTaks.put("resultadoFormulario", outcome);
            log.debug("llego al alfresco" + TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build() + " " + caso.getIdProceso());
            this.contenidoACSService.completaTaksByIntanceId(caso.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        }
        //log.debug("llego al final final");
        return true;
    }

    /**
     * Metodo para actualizar el estado de un caso
     *
     * @author Rudy Culajay (ruarcuse)
     * @param idCase indentificador del caso
     * @param idState estado nuevo
     * @param idStateValid estado anterior
     * @param user datos del usuario
     * @param comentary Comentario del usuario
     * @param typeComentary tipo de comentario a registar
     * @since 23/07/2022
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean alterStatecasesWithComentary(int idCase, int idState, Integer idStateValid, UserLogged user, String comentary, Integer typeComentary, String outcome) {
        SipfCasos caso = casosrepository.findById(idCase).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Caso no encontrado."));
        if (idStateValid != null && caso.getIdEstado() != idStateValid) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "La fase a la que se desea cambiar el caso no es la correcta");
        }
        final SipfColaborador collaborator = colaboradorRepository.findById(user.getNit()).orElse(null);
        final SipfAsignacionCasos collaborator1 = asignacionCaso.findByIdIdCaso(idCase).orElse(null);
        final SipfColaborador collaborator3 = colaboradorRepository.findById(collaborator1.getId().getNitColaborador()).orElse(null);
        caso.setIdEstado(idState);
        caso.setUsuarioModifica(user.getLogin());
        caso.setIpModifica(user.getIp());
        caso.setFechaModifica(new Date());
        casosrepository.save(caso);

        SipfHistorialComentarios comentaryF = new SipfHistorialComentarios();
        comentaryF.setComentarios(comentary);
        comentaryF.setIdRegistro(String.valueOf(idCase));
        comentaryF.setIdTipoComentario(typeComentary);
        comentaryF.setFechaModifica(new Date());
        comentaryF.setUsuarioModifica(user.getLogin());
        comentaryF.setIpModifica(user.getIp());
        historialComentariosRepository.save(comentaryF);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_casos")
                        .idCambioRegistro(String.valueOf(idCase))
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
            this.contenidoACSService.completaTaksByIntanceId(caso.getIdProceso(), TaskCompleteAPSDto.builder().outcome("default").values(bodyTaks).build());
        }

        SipfDetalleCaso alcanceCaso = detalleCasoRepository.findById(idCase).get();
        if (alcanceCaso.getIdAlcance() != null) {
            SipfAlcance alcance = alcanceRepository.findById(alcanceCaso.getIdAlcance()).get();
            switch (idState) {
                case 17://rechazado
                    alcance.setIdEstado(Catalog.Scope.Status.REJECTED);
                    break;
                case 181://correcion
                    alcance.setIdEstado(Catalog.Scope.Status.SCOPE_CORRECTION);
                    break;
            }
            alcance.setUsuarioModifica(user.getLogin());
            alcance.setFechaModifica(new Date());
            alcance.setIpModifica(user.getIp());
            alcanceRepository.save(alcance);
        }

        if (idState == 181) {
            correoServices.envioCorreo(collaborator3.getCorreo(), "Se le informa que el profesional " + collaborator3.getNombres() + " mando a corregir el alcance correspondiente al caso  " + caso.getIdCaso(), "Correcion de alcance");
        } else if (idState == 17) {
            correoServices.envioCorreo(collaborator3.getCorreo(), "Se le informa que el profesional " + collaborator3.getNombres() + " mando a rechazar el alcance correspondiente al caso  " + caso.getIdCaso() + " por el motivo de " + comentary, "Rechazo de alcance");
        }

        return true;
    }

    /**
     * Metodo paracrear solicitudes posterirori aduanas
     *
     * @author Gabriel Ruano (garuanom)
     * @param data lista de solicitudes
     * @param archivo archivos
     * @param user usuario loggeado
     * @since 08/09/2022
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public SolicitudesAduanasDto createSubsequentRequests(List<SolicitudesAduanasDto> data, List<MultipartFile> archivo, UserLogged user) throws IOException {
        //log.debug("hola" + data.size());
        Integer idAgrupacion = generalService.nextValSequence("sat_ifi_sipf.sipf_solicitudes_posteriori_id_agrupacion_seq");
        for (int i = 0; i < data.size(); i++) {
            SipfSolicitudesPosteriori solicitud = solicitudesPosterioriRepository.save(
                    SipfSolicitudesPosteriori.builder()
                            .idAgrupacion(idAgrupacion)
                            .idEstado(967)
                            .noSolicitud(data.get(i).getNo())
                            .audiencia(data.get(i).getAudiencia())
                            .fechaAudiencia(data.get(i).getFechaAudiencia())
                            .nit(data.get(i).getNIT())
                            .nombre(data.get(i).getNombre())
                            .noDuca(data.get(i).getNoDuca())
                            .noDucaOrden(data.get(i).getNoDucaOrden())
                            .fechaAceptacion(data.get(i).getFechaAceptacion())
                            .regimen(data.get(i).getRegimen())
                            .selectivo(data.get(i).getSelectivo())
                            .paisProveedor(data.get(i).getPaisProveedor())
                            .tipoAjuste(data.get(i).getTipoAjuste())
                            .expedientes(data.get(i).getExpedientes())
                            .certificadoEnsayo(data.get(i).getCertificadoensayo())
                            .dictamenArancelaria(data.get(i).getDictamenArancelaria())
                            .fechaDictamen(data.get(i).getFechaDictamen())
                            .descripcion(data.get(i).getDescripcion())
                            .capituloDeclarado(data.get(i).getCapituloDeclarado())
                            .partidaDeclarada(data.get(i).getPartidaDeclarada())
                            .incisosDeclarado(data.get(i).getIncisosDeclarado())
                            .tasaDai(data.get(i).getTasaDai())
                            .daiPagado(data.get(i).getDaiPagado())
                            .ivaPago(data.get(i).getIvaPago())
                            .tratoArancelario(data.get(i).getTratoArancelario())
                            .capituloDictaminado(data.get(i).getCapituloDictaminado())
                            .partidaDictaminado(data.get(i).getPartidaDictaminado())
                            .incisoDictaminado(data.get(i).getIncisoDictaminado())
                            .tasaDaiDictaminado(data.get(i).getTasaDaiaDictaminado())
                            .tipoAlertivo(data.get(i).getTipoAlertivo())
                            .opinionDace(data.get(i).getOpinionDace())
                            .montoTotal(data.get(i).getMontoTotal())
                            .observaciones(data.get(i).getObservaciones())
                            .unidadMedida(data.get(i).getUnidadMedida())
                            .cantidad(data.get(i).getCantidad())
                            .valorDeclarado(data.get(i).getValorDeclarado())
                            .valorAjustado(data.get(i).getValorAjustado())
                            .numeroOrden(data.get(i).getNumeroOrden())
                            .build()
            );

            Map<String, Object> post = new HashMap<>();
            post.put("carpeta", solicitud.getIdSolicitud());
            NodosACSDto nodo = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.POSTERIORI, post);
            List<MultipartFile> archivo2 = new ArrayList<MultipartFile>();
            archivo2.add(archivo.get(i));
            contiendoACSService.uploadFiles(nodo.getId(), archivo2);
        }
        return null;
    }

    public List<MassiveResumeProjection> getResumeBy(Integer pIdTipoCaso, Integer pIdEstado) {

        return this.casosMasivosRepository.findResumeByStatusAndCaseType(pIdTipoCaso, pIdEstado);
    }

    public List<CasosOnlyProjection> assignTopNToResponsible(UserLogged userLogged, MassiveAssignParamsDto pParams) {

        List<CasosOnlyProjection> vListCasesToAssign = this.casosMasivosRepository.findTopNCasos(pParams.getPTipoCaso(), pParams.getPIdGerencia(), Catalog.Case.Status.PUBLISHED, pParams.getPCantidadCasos());
        List<Integer> vCasesIdList = new ArrayList<>();

        if (!vListCasesToAssign.isEmpty()) {
            Integer vIdGrupo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_id_grupo_asignacion");

            List<SipfAsignacionCasos> vAssingmentList = new ArrayList<>();
            vListCasesToAssign.forEach(item
                    -> {
                vCasesIdList.add(item.getIdCaso());

                SipfAsignacionCasosId assingmentId = new SipfAsignacionCasosId(item.getIdCaso(), pParams.getPNitResponsable());
                SipfAsignacionCasos assingment = new SipfAsignacionCasos();
                assingment.setIdEstado(15);
                assingment.setIdOrigen(140);
                assingment.setIpModifica(userLogged.getIp());
                assingment.setFechaModifica(new Date());
                assingment.setIdGrupoAsignacion(vIdGrupo);
                assingment.setUsuarioModifica(userLogged.getNit());
                assingment.setId(assingmentId);
                vAssingmentList.add(assingment);
            });

            Iterable<SipfCasos> vCasesToModify
                    = this.casosrepository.findAllById(vCasesIdList);
            vCasesToModify.forEach(item
                    -> {
                item.setIdEstado(15);
                item.setUsuarioModifica(userLogged.getNit());
                item.setFechaModifica(new Date());
                item.setIpModifica(userLogged.getIp());
            });

            this.assignmentRepository.saveAll(vAssingmentList);

            this.casosrepository.saveAll(vCasesToModify);

        }

        return vListCasesToAssign;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean aproverDeclineCases(Integer idCaso, UserLogged user) {
        SipfCasos caso = casosrepository.casesId(idCaso);
        if (caso != null) {
            caso.setIdEstado(17);
            casosrepository.save(caso);

            Map<String, Object> post = new HashMap<>();
            JSONObject formulario = new JSONObject();
            formulario.put("idCaso", idCaso);
            post.put("jsonFormulario", formulario.toJSONString());
            post.put("resultadoFormulario", "Aprobar");
            JSONObject json = new JSONObject(post);
            TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
            dtoComplete.setOutcome("\"default\"");
            dtoComplete.setValues(json);
            contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean operatorDeclineCases(Integer idCaso, UserLogged user, String comentario) {
        SipfCasos caso = casosrepository.casesId(idCaso);
        if (caso != null) {
            caso.setIdEstado(16);
            casosrepository.save(caso);

            Map<String, Object> post = new HashMap<>();
            JSONObject formulario = new JSONObject();
            formulario.put("idCaso", idCaso);
            post.put("jsonFormulario", formulario.toJSONString());
            post.put("resultadoFormulario", "rechazar");
            JSONObject json = new JSONObject(post);
            TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
            dtoComplete.setOutcome("\"default\"");
            dtoComplete.setValues(json);
            contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);

            SipfHistorialComentarios history = new SipfHistorialComentarios();
            history.setIdRegistro(String.valueOf(idCaso));
            history.setIdTipoComentario(409);
            history.setComentarios(comentario);
            history.setFechaModifica(new Date());
            history.setIpModifica(user.getIp());
            history.setUsuarioModifica(user.getLogin());
            historialComentariosRepository.save(history);
            return true;
        }
        return false;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean finalRejection(Integer idCaso, CasosDto caso, UserLogged user) {
        caso.setEstado(Catalog.Case.Status.ULTIMATE_REJECTION);
        alterCase(idCaso, caso, user);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public Boolean notAprovee(Integer idCaso, UserLogged user) {
        SipfCasos caso = casosrepository.casesId(idCaso);
        if (caso != null) {
            caso.setIdEstado(15);
            casosrepository.save(caso);

            Map<String, Object> post = new HashMap<>();
            JSONObject formulario = new JSONObject();
            formulario.put("idCaso", idCaso);
            post.put("jsonFormulario", formulario.toJSONString());
            post.put("resultadoFormulario", "No Aprueba");
            JSONObject json = new JSONObject(post);
            TaskCompleteAPSDto dtoComplete = new TaskCompleteAPSDto();
            dtoComplete.setOutcome("\"default\"");
            dtoComplete.setValues(json);
            contiendoACSService.completaTaksByIntanceId(String.valueOf(caso.getIdProceso()), dtoComplete);
            return true;
        }
        return false;
    }

    public List<SolicitudesAduanasProjections> getSolitudesAduanas() {
        return solicitudesAduanasRepository.getSolitudesAduanas();
    }

    /**
     * OBTIENE LOS CASOS DE PUNTOS FIJOS PARA GENERAR ALCANCE.
     *
     * @author (lfvillag) Luis Villagrán
     * @return
     */
    @Transactional(readOnly = true)
    public List<CasosAlcanceProjection> getCasosForScopesFixedPoints(String nit) {
        return this.casosrepository.getCasesForScopesFixedPoints(nit);
    }

    @Transactional(readOnly = true)
    public List<ProcesosMasivosProjections> getProcesMasive() {
        return this.casosrepository.getProcesMasive();
    }

    /**
     * OBTIENE LOS CASOS DE GABINETE PARA GENERAR ALCANCE.
     *
     * @return
     */
    @Transactional(readOnly = true)
    public List<CasosAlcanceProjection> getCasesForScopesFiscal(String nit) {
        return this.casosrepository.getCasesForScopesFiscal(nit);
    }

    @Transactional
    public int saveCasesScope(CasesScopeMasiveDto dto, UserLogged user) {
        //Inicia el proceso en ALFRESCO
        final JSONObject body = new JSONObject();
        final JSONObject object = new JSONObject();
        object.put("idMasivo", dto.getIdCaso());
        object.put("idPresencia", dto.getIdCaso());
        body.put("jsonFormulario", object.toJSONString());
        body.put("resultadoFormulario", "default");
        final StartProcessDto processId = contiendoACSService.startProces(3, body);
        //Inicia el guardado de la información en la tabla de CASOS
        final SipfCasos cGabinete = casosrepository.findById(dto.getIdCaso()).orElse(null);
        cGabinete.setIdEstado(Catalog.Case.Status.ELABORATE_SCOPE);
        cGabinete.setFechaModifica(new Date());
        cGabinete.setIdProceso(processId.getId());
        casosrepository.save(cGabinete);
        //Inicia el guardado de la información en la tabla de ALCANCES Y DETALLE ALCANCE.
        AlcanceDto alcance = new AlcanceDto();
        alcance.setIdTipoAlcance(dto.getIdTipoAlcance());
        alcance.setSecciones(dto.getSecciones());
        alcance.setIdEstado(Catalog.Scope.Status.SCOPE_REVIEW_JU);
        alcance = alcancesService.createAlcance(alcance, user);
        if (dto.getIdTipoCaso() == 972) {
            SipfDetalleCaso dcGabinete = SipfDetalleCaso.builder()
                    .idAlcance(alcance.getIdAlcance())
                    .fechaModifica(new Date())
                    .loginProfesional(user.getLogin())
                    .ipModifica(user.getIp())
                    .tipoCaso(972)
                    .usuarioModifica(user.getLogin())
                    .idCaso(dto.getIdCaso())
                    .build();
            detalleCasoRepository.save(dcGabinete);
        } else if (dto.getIdTipoCaso() == 971) {
            SipfDetalleCaso dcGabinete = SipfDetalleCaso.builder()
                    .idAlcance(alcance.getIdAlcance())
                    .fechaModifica(new Date())
                    .loginProfesional(user.getLogin())
                    .ipModifica(user.getIp())
                    .tipoCaso(971)
                    .usuarioModifica(user.getLogin())
                    .idCaso(dto.getIdCaso())
                    .build();
            detalleCasoRepository.save(dcGabinete);
        }
        return alcance.getIdAlcance();
    }

    @Transactional(readOnly = true)
    public List<ResponsableCasosProjection> getResponsability(Integer idCaso) {
        return casosrepository.getDataResponsible(idCaso);
    }
}
