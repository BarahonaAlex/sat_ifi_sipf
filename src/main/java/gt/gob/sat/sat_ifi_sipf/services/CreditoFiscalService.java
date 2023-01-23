/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.itextpdf.html2pdf.HtmlConverter;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.DatosSolicitudDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfInconsistenciaDto;
import gt.gob.sat.sat_ifi_sipf.Config;
import gt.gob.sat.sat_ifi_sipf.controllers.EstadoArchivoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AprobedRejectedCedulaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CreditoFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfPeriodoNitParametros;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfResProcesoNocturnoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfSaveSolicitudDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfUpdateStatusDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SolicitudGuardadaCreditoFiscalDto;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.dtos.rejectApproveFiscalDocumentDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfArchivosCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfInconsistencia;
import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ArchivoDataCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.InconsistenciaProjections;
import gt.gob.sat.sat_ifi_sipf.repositories.ArchivosCreditoFiscalRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import gt.gob.sat.sat_ifi_sipf.repositories.CreditoFiscalRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.InconsistenciaRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.text.ParseException;
import java.util.Date;
import java.util.Optional;
import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.simple.JSONObject;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 * @author ajabarrer
 */
@Transactional
@Service
@Slf4j
public class CreditoFiscalService {

    @Autowired
    ContiendoACSService contiendoACSService;

    @Autowired
    private CreditoFiscalRepository creditoFiscal;

    @Autowired
    private ConsumosService consumoService;

    @Autowired
    private InconsistenciaRepository inconsistenciaRepository;

    @Autowired
    private CatDatoRepository catDatoRepository;

    @Autowired
    private ArchivosCreditoFiscalRepository archivosCreditoFiscalRepository;

    @Autowired
    private HistorialOperacionesRepository historicoOperacionesRepository;

    @Autowired
    private HistorialComentariosRepository historialComentario;

    @Autowired
    CorreoService correoService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    private Config config;

    @Transactional(readOnly = true)
    public List<SipfSolicitudCreditoFiscal> findAllSolicitudesCreditoFiscal() {
        return creditoFiscal.findAll();
    }

    @Transactional(readOnly = true)
    public SipfSolicitudCreditoFiscal findSolicitudesCreditoFiscalById(Integer id) {
        return creditoFiscal.findById(id).orElse(null);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateRequestsByNightProccess(Integer status, UserLogged logged) {

        //DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        //SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy"); 
        List<SipfSolicitudCreditoFiscal> solicitudes = creditoFiscal.findByEstado(status);

        List<BigDecimal> requests = solicitudes.stream().map(
                SipfSolicitudCreditoFiscal::getNumeroSolicitud
        ).collect(Collectors.toList());

        List<Integer> idRequests = solicitudes.stream().map(
                SipfSolicitudCreditoFiscal::getIdSolicitud
        ).collect(Collectors.toList());

        System.out.println("lista de solicitudes " + requests);
        GeneralResponseDto<List<DatosSolicitudDto>> result = consumoService.consumeCompleteUrlOracle(requests, "updated/requests", GeneralResponseDto.class, HttpMethod.POST);
        System.out.println("solicitudes actualizadas: " + result);

        List<DatosSolicitudDto> list = new ObjectMapper().convertValue(result.getData(), new TypeReference<List<DatosSolicitudDto>>() {
        });
        System.out.println("prueba" + new Gson().toJson(list));
        List<SipfSolicitudCreditoFiscal> solicitudesActualizadas = (List<SipfSolicitudCreditoFiscal>) creditoFiscal.saveAll(
                list.stream().map(
                        res -> {
                            SipfSolicitudCreditoFiscal findRequest = solicitudes.stream()
                                    .filter(solicitud -> res.getNumeroSolicitud()
                                    .equals(solicitud.getNumeroSolicitud())).findFirst().orElse(null);

                            findRequest.setActividadEconomica(res.getPrincipalProducto());

                            findRequest.setPeriodoInicio(res.getPeriodoDesde());

                            findRequest.setPeriodoFin(res.getPeriodoHasta());

                            findRequest.setMonto(res.getMontoSolicitud().intValue());
                            findRequest.setUsuarioModifica(logged.getLogin());
                            findRequest.setIpModifica(logged.getIp());
                            findRequest.setFechaModifica(new Date());
                            findRequest.setCorreoNotifica(res.getCorreoNotificacion());
                            findRequest.setPrincipalProducto(res.getProductoExportacion());
                            findRequest.setNumeroFormulario(res.getCodigoFormulario());
                            findRequest.setFormularioIva(res.getCodFormIvaGen());

                            if (res.getEstado().equals("NO ADMITIDO")) {
                                findRequest.setEstado(1075);
                            } else {
                                findRequest.setEstado(1076);
                            }
                            return findRequest;
                        }
                ).collect(Collectors.toList())
        );

        this.updateInconsistenciesRequest(requests, solicitudesActualizadas, logged);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateInconsistenciesRequest(List<BigDecimal> noSolicitudes, List<SipfSolicitudCreditoFiscal> requests, UserLogged logged) {
        System.out.println("listas de numero de solicitudes en el micro de inconsistencias:" + noSolicitudes);
        GeneralResponseDto<List<SdcfInconsistenciaDto>> result = consumoService.consumeCompleteUrlOracle(noSolicitudes, "inconsistencies/request", GeneralResponseDto.class, HttpMethod.POST);
        System.out.println("retorna " + result);
        List<SdcfInconsistenciaDto> inconsistencias = new ObjectMapper().convertValue(result.getData(), new TypeReference<List<SdcfInconsistenciaDto>>() {
        });

        inconsistenciaRepository.saveAll(inconsistencias.stream().map(res -> {

            SipfInconsistencia inconsistencia = SipfInconsistencia.builder()
                    .numeroSolicitud(requests.stream()
                            .filter(solicitud -> res.getNumeroSolicitud()
                            .equals(solicitud.getNumeroSolicitud())).findFirst().orElse(null).getIdSolicitud())
                    .declaracionRepetida(res.getDeclaracionRepetida().intValue())
                    .descripcion(res.getDescripcion())
                    .facturaNumero(res.getFacturaNumero())
                    .facturaProveedor(res.getFacturaProveedor())
                    .facturaSerie(res.getFacturaSerie())
                    .fechaGrabacion(new Date())
                    .numeroDeclaracion(res.getIdSolicitudDeclaracion().intValue())
                    .tipoInconsistencia(catDatoRepository.getCodigoByCatalogoAndCodIngresado(res.getIdTipoInconsistencia().toString(), 105))
                    .tipoRepetida(res.getTipoRepetida().intValue())
                    .usuarioGrabacion(logged.getLogin())
                    .estado(1079)
                    .build();
            return inconsistencia;
        }).collect(Collectors.toList()));
    }

    @Transactional
    public boolean uploadRespaldoPeriodo(UserLogged user, String periodo, List<MultipartFile> file) {
        Optional<SipfArchivosCreditoFiscal> oldFile = archivosCreditoFiscalRepository.findByNitContribuyenteAndPeriodoAndIdestadoAndNombre(user.getNit(), periodo, 1055, file.get(0).getOriginalFilename());
//validacion para que el contribuyente no pueda subir nuevos archivos, que esten pendientes de revison
        if (oldFile.isPresent()) {
            historicoOperacionesRepository.save(SipfHistoricoOperaciones.builder()
                    .data(new Gson().toJson(oldFile))
                    .nombreTabla("sipf_archivos_credito_fiscal")
                    .idCambioRegistro(Long.toString(oldFile.get().getIdArchivo()))
                    .idTipoOperacion(405)
                    .usuarioModifica(user.getLogin())
                    .fechaModifica(new Date())
                    .ipModifica(user.getIp())
                    .build());

            oldFile.get().setIdestado(1053);
            oldFile.get().setFechaModifica(new Date());
            oldFile.get().setIpModifica(user.getIp());
            oldFile.get().setUsuarioModifica(user.getLogin());

            archivosCreditoFiscalRepository.save(oldFile.get());
        } else {
            archivosCreditoFiscalRepository.save(SipfArchivosCreditoFiscal.builder()
                    .nombre(file.get(0).getOriginalFilename())
                    .idestado(1053)
                    .nitContribuyente(user.getNit())
                    .periodo(periodo)
                    .fechaModifica(new Date())
                    .ipModifica(user.getIp())
                    .usuarioModifica(user.getLogin())
                    .build());
        }

        Map<String, Object> post = new HashMap<>();
        post.put("nit", user.getNit());
        post.put("periodo", periodo);

        JSONObject json = new JSONObject(post);
        NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.ARCHIVOS_RESPALDO_CREDITO_FISCAL, json);
        contiendoACSService.uploadFiles(nodos.getId(), file);
        return true;
    }

    @Transactional(readOnly = true)
    public List<ArchivoDataCreditoFiscalProjection> getDataInformation(Integer idSolicitud, Integer idEstado) {
        return archivosCreditoFiscalRepository.findByIdSolicitud(idSolicitud, idEstado);
    }

    @Transactional
    public SipfArchivosCreditoFiscal rejectApproveDocument(rejectApproveFiscalDocumentDto dto, UserLogged user) {
        Optional<SipfArchivosCreditoFiscal> archivo = archivosCreditoFiscalRepository.findById(dto.getIdArchivo());

        if (archivo.isPresent()) {
            archivo.get().setIdestado(dto.getIdEstado());
            archivo.get().setFechaModifica(new Date());
            archivo.get().setIpModifica(user.getIp());
            archivo.get().setUsuarioModifica(user.getLogin());
            if (dto.getIdEstado() == 1055) {
                archivo.get().setComentario(dto.getComentario());
            }

            return archivosCreditoFiscalRepository.save(archivo.get());
        } else {
            return null;
        }
    }

    public List<BandejaCreditoFiscalProjection> findAllAssingByNitAndStatus(UserLogged logged) {
        return creditoFiscal.getByAsignadoAndEstado(logged.getNit());
    }

    @Transactional
    public boolean UpdateStatusCedulaRequest(String id, String estado, UserLogged logged, List<MultipartFile> file) {
        final SipfSolicitudCreditoFiscal solicitud = creditoFiscal.findById(Integer.parseInt(id)).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.UNKNOWN_ERROR.getText(id))
        );

        if (file.get(0).isEmpty()) {
            log.debug("archivo vacio linea 283");

        }

        solicitud.setEstado(Integer.parseInt(estado));
        solicitud.setFechaModifica(new Date());
        solicitud.setIpModifica(logged.getIp());
        solicitud.setUsuarioModifica(logged.getLogin());

        creditoFiscal.save(solicitud);
        Map<String, Object> post = new HashMap<>();
        post.put("idSolicitud", id);
        post.put("carpeta", "Cedulas");

        JSONObject json = new JSONObject(post);
        contiendoACSService.uploadFiles(contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.CEDULA_CREDITO, json).getId(), file);
        return true;
    }

    @Transactional(rollbackFor = Exception.class)
    public byte[] generacionPdf(String data, UserLogged user) throws FileNotFoundException, IOException {
        File f = new File("uploads/Cédula de No Admisión.pdf");
        HtmlConverter.convertToPdf(addStyleHtml(data), new FileOutputStream(f));
        FileInputStream input = new FileInputStream(f);
        byte[] archivo = IOUtils.toByteArray(input);
        input.close();
        return archivo;
    }

    public void deleteDocument() throws IOException {
        File f = new File("uploads/Cédula de No Admisión.pdf");
        if (Files.deleteIfExists(f.toPath())) {
            System.out.println("si lo borro");
        }
    }

    private static String addStyleHtml(String html) throws IOException {
        final StringBuilder htmlAppendable = new StringBuilder(html);
        htmlAppendable.append("<style>");
        htmlAppendable.append("content.min.css");
        htmlAppendable.append("</style>");
        return htmlAppendable.toString();
    }

    public List<InconsistenciaProjections> findInconsistencyByRequest(Integer idSolicitud) {
        return inconsistenciaRepository.findByRequest(idSolicitud);
    }

    @Transactional(readOnly = true)
    public List<SolicitudesCreditoFiscalProjection> getSolicitudesCreditoFiscal(String nit, List<Integer> status) {
        return creditoFiscal.getSolicitudesCreditoFiscal(nit, status);
    }

    @Transactional(rollbackFor = Exception.class)
    public void saveArchivoACS(List<MultipartFile> uploadFile, CreditoFiscalDto dto) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
        log.debug("" + uploadFile.size());
        //log.debug(""+ dto.size());
        Map<String, Object> post = new HashMap<>();
        post.put("idSolicitud", dto.getIdSolicitud());
        JSONObject json = new JSONObject(post);
        NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.CREDITO_FISCAL, json);
        //Se ingresa el "uploadFile" con archivos.
        contiendoACSService.uploadFiles(nodos.getId(), uploadFile);
        String url = config.getPingUrlWsOracle() + "/sdcf/load/files";
        final MultiValueMap<String, Object> newFiles = new LinkedMultiValueMap<>();
        uploadFile.forEach(file -> {
            newFiles.add("file", file.getResource());
        });
        newFiles.add("data", gson.toJson(dto));
        HttpHeaders headers = consumoService.getOracleHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        GeneralResponseDto<EstadoArchivoDto> resultado = consumoService.consume(newFiles, url, GeneralResponseDto.class, HttpMethod.POST, headers);
        System.out.println(resultado);
        if (resultado.getType().equals("ERROR")) {
            String message = resultado.getData() == null ? resultado.getMessage() : resultado.getData().getMensaje();
            throw BusinessException.unprocessableEntity(message);
        }

        if (resultado.getData() != null) {
            EstadoArchivoDto data = gson.fromJson(gson.toJson(resultado.getData()), EstadoArchivoDto.class);
            if (!data.getValido()) {
                throw BusinessException.unprocessableEntity(data.getMensaje());
            }
        }
    }
    
     public SdcfResProcesoNocturnoDto ResultadoProcesoNocturno() throws ParseException {
        
        GeneralResponseDto<String> resultado = this.consumosService.consumeCompleteUrlOracle(null,"/sdcf/iniciarProceso/", GeneralResponseDto.class, HttpMethod.GET);
        SdcfResProcesoNocturnoDto res = new Gson().fromJson(resultado.getData(), SdcfResProcesoNocturnoDto.class);
        
        List<SdcfInconsistenciaDto> listaIncosistencias = res.getInconsistencias();
          for (SdcfInconsistenciaDto inconsistencia : listaIncosistencias) {
                inconsistenciaRepository.save(SipfInconsistencia.builder()
                .numeroSolicitud(inconsistencia.getNumeroSolicitud().intValue())
                    .declaracionRepetida(inconsistencia.getDeclaracionRepetida().intValue())
                    .descripcion(inconsistencia.getDescripcion())
                    .facturaNumero(inconsistencia.getFacturaNumero())
                    .facturaProveedor(inconsistencia.getFacturaProveedor())
                    .facturaSerie(inconsistencia.getFacturaSerie())
                    .fechaGrabacion(new Date())
                    .numeroDeclaracion(inconsistencia.getIdSolicitudDeclaracion().intValue())
                    .tipoInconsistencia(catDatoRepository.getCodigoByCatalogoAndCodIngresado(inconsistencia.getIdTipoInconsistencia().toString(), 105))
                    .tipoRepetida(inconsistencia.getTipoRepetida().intValue())
                    .usuarioGrabacion("proceso nocturno")
                    .build()
                );
          }
        List<SdcfUpdateStatusDto> listaSolicitudes = res.getUpdateSolicitudes();
            for(SdcfUpdateStatusDto solicitud : listaSolicitudes){
                final SipfSolicitudCreditoFiscal updateSoli = creditoFiscal.findByNumeroSolicitud(solicitud.getNumeroSolicitud()).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.UNKNOWN_ERROR.getText(solicitud.getNumeroSolicitud().intValue())));
        
        if(solicitud.getNuevoStatus() == 3){
            updateSoli.setEstado(1076);
        }
        if(solicitud.getNuevoStatus() == 4){
            updateSoli.setEstado(1075);
        }
        updateSoli.setFechaModifica(new Date());
        updateSoli.setUsuarioModifica("proceso nocturno");

        creditoFiscal.save(updateSoli);
            }
        
        return res;
    }
     
    public Boolean saveSolicitudCreditoFiscal(SdcfSaveSolicitudDto dto, UserLogged user) throws ParseException {
        
        List<SipfSolicitudCreditoFiscal> request = creditoFiscal.findByNitContribuyenteAndPeriodoInicioAndPeriodoFin(
                dto.getDatosSolicitud().getNitContribuyente(), 
                dto.getDatosSolicitud().getPeriodoDesde(),
                dto.getDatosSolicitud().getPeriodoHasta());
        //System.out.println("solicitudes encontradas: "+request.get(0).getEstado());
        
        if( request.isEmpty() || (request.get(0).getEstado().equals(966) && request.get(0).getEstado().equals(1058))){
            GeneralResponseDto<String> resultado = this.consumosService.consumeCompleteUrlOracle(dto, "/sdcf/saveSolicitud", GeneralResponseDto.class, HttpMethod.POST);
            SolicitudGuardadaCreditoFiscalDto solicitud = new Gson().fromJson(resultado.getData(), SolicitudGuardadaCreditoFiscalDto.class);
            System.out.println(dto);
            creditoFiscal.save(SipfSolicitudCreditoFiscal.builder()
                    .numeroSolicitud(solicitud.getNumeroSolicitud())
                    .actividadEconomica(solicitud.getPrincipalProducto())
                    .monto(solicitud.getMontoSolicitud().intValue())
                    .numeroFormulario(solicitud.getNoFormIvaGen() == null ? "2125" : solicitud.getNoFormIvaGen().toString())
                    .nitContribuyente(solicitud.getNitContribuyente())
                    .periodoInicio(solicitud.getPeriodoDesde())
                    .periodoFin(solicitud.getPeriodoHasta())
                    .fechaModifica(new Date())
                    .ipModifica(user.getIp())
                    .usuarioModifica(user.getLogin())
                    .correoNotifica(solicitud.getCorreoNotificacion())
                    .direccionNotifica(solicitud.getDireccionNotificacion())
                    .estado(catDatoRepository.getCodigoByCatalogoAndCodIngresado(String.valueOf(solicitud.getEstado()), 84))
                    .formularioIva(solicitud.getCodigoFormulario())
                    .principalProducto(solicitud.getProductoExportacion())
                    .build());
            System.out.println("se crea la solicitud");
            return true;
        }else{
            System.out.println("Entra aqui");
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<SolicitudesCreditoFiscalProjection> getSolicitudesCreditoFiscalAdmitidas() {
        return creditoFiscal.getSolicitudesCreditoFiscalAdmitidas();
    }

    @Transactional
    public SipfSolicitudCreditoFiscal asignarSolicitudProfesional(Integer id, String nit, UserLogged logged) {
        final SipfSolicitudCreditoFiscal solicitud = creditoFiscal.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.UNKNOWN_ERROR.getText(id))
        );

        solicitud.setAsignado(nit);
        solicitud.setEstado(1056);
        solicitud.setFechaModifica(new Date());
        solicitud.setIpModifica(logged.getIp());
        solicitud.setUsuarioModifica(logged.getLogin());

        return creditoFiscal.save(solicitud);
    }

    //Microservicio de Correo Elecronico -- RECHAZO
    @Transactional
    public void SendEmailRejected(UserLogged user, AprobedRejectedCedulaDto dto) {
        final SipfSolicitudCreditoFiscal dataSolicitud = creditoFiscal.findById(dto.getIdSolicitud()).orElse(null);
        log.debug("", dataSolicitud.getAsignado());
        final SipfColaborador dataColaborador = colaboradorRepository.findById(dataSolicitud.getAsignado()).orElse(null);
        if (dataSolicitud.getEstado() == 965) {
            dataSolicitud.setEstado(1077);
            final SipfHistorialComentarios comentario = new SipfHistorialComentarios();
            comentario.setIdRegistro(dto.getIdSolicitud().toString());
            comentario.setIdTipoComentario(1080);
            comentario.setComentarios(dto.getComentario());
            comentario.setFechaModifica(new Date());
            comentario.setUsuarioModifica(user.getLogin());
            comentario.setIpModifica(user.getIp());
            historialComentario.save(comentario);
            correoService.envioCorreo(dataColaborador.getCorreo(), "Se rechaza por revisión de Jefe", "Información sobre Credito Fiscal");
        } else if (dataSolicitud.getEstado() == 1057) {
            dataSolicitud.setEstado(1077);
            final SipfHistorialComentarios comentario = new SipfHistorialComentarios();
            comentario.setIdRegistro(dto.getIdSolicitud().toString());
            comentario.setIdTipoComentario(1080); 
            comentario.setComentarios(dto.getComentario());
            comentario.setFechaModifica(new Date());
            comentario.setUsuarioModifica(user.getLogin());
            comentario.setIpModifica(user.getIp());
            historialComentario.save(comentario);
            correoService.envioCorreo(dataColaborador.getCorreo(), "Denegar rechazo al Operador", "Información sobre Credito Fiscal");
        }

        List<SipfArchivosCreditoFiscal> oldFile = archivosCreditoFiscalRepository.findByNitContribuyenteAndPeriodo(
                dataSolicitud.getNitContribuyente(), archivosCreditoFiscalRepository.findByRequest(dto.getIdSolicitud()).getPeriodo());

        archivosCreditoFiscalRepository.saveAll(
                oldFile.stream().map(u -> {
                    historicoOperacionesRepository.save(SipfHistoricoOperaciones.builder()
                            .data(new Gson().toJson(oldFile))
                            .nombreTabla("sipf_archivos_credito_fiscal")
                            .idCambioRegistro(Long.toString(u.getIdArchivo()))
                            .idTipoOperacion(405)
                            .usuarioModifica(user.getLogin())
                            .fechaModifica(new Date())
                            .ipModifica(user.getIp())
                            .build());

                    u.setIdestado(1053);
                    u.setFechaModifica(new Date());
                    u.setIpModifica(user.getIp());
                    u.setUsuarioModifica(user.getLogin());

                    return u;
                }).collect(Collectors.toList()));

    }

    //Microservicio de Correo Elecronico -- APROBAR
    @Transactional
    public void SendEmailAccept(Integer idSolicitud) {
        final SipfSolicitudCreditoFiscal dataSolicitud = creditoFiscal.findById(idSolicitud).orElse(null);
        if (dataSolicitud.getEstado() == 965) {
            dataSolicitud.setEstado(1058);
            correoService.envioCorreo(dataSolicitud.getCorreoNotifica(), "Se aprueba solicitud por revisión de jefe", "Información sobre Credito Fiscal");
        } else if (dataSolicitud.getEstado() == 1057) {
            dataSolicitud.setEstado(966);
            correoService.envioCorreo(dataSolicitud.getCorreoNotifica(), "Se rechaza solicitud por revisión de jefe", "Información sobre Credito Fiscal");
        }
    }

    public BandejaCreditoFiscalProjection getExtraDataRequest(Integer idSolicitud) {
        return creditoFiscal.getExtraDataRequest(idSolicitud);
    }

    @Transactional
    public boolean updateInconsistency(Integer id, Integer idEstado, UserLogged logged) {
        final SipfInconsistencia inconsistencia = inconsistenciaRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.UNKNOWN_ERROR.getText(id))
        );
        inconsistencia.setEstado(idEstado);
        inconsistencia.setUsuarioGrabacion(logged.getLogin());
        return true;
    }
    
    public List<CatalogDataProjection> getFileNames(String periodo, String nit, List<Integer> estados) {
        return archivosCreditoFiscalRepository.getFileNames(periodo, nit, estados);
    }
    
    public List<SipfSolicitudCreditoFiscal> obtenerDeclaraciones(SdcfPeriodoNitParametros dto){
        //List<Object> empty = new ArrayList<Object>();
        List<SipfSolicitudCreditoFiscal> request = creditoFiscal.findByNitContribuyenteAndPeriodoInicioAndPeriodoFin(dto.getNit(),new Date(dto.getPeriodoDesde()), new Date(dto.getPeriodoHasta()));
        /*System.out.println("solicitudes existentes"+ request);
        if(request.isEmpty()){
            GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/obtenerDeclaracionesPost", GeneralResponseDto.class, HttpMethod.POST);
            return resultado.getData();
        }else{
           return empty;
        }*/
       return request;
    }
}
