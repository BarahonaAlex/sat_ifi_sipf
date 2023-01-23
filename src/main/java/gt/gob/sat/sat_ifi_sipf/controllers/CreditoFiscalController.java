/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import gt.gob.sat.sat_ifi_sipf.dtos.AprobedRejectedCedulaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CreditoFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DatosContribuyenteCreditoFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DatosFormularioCreditoFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfPeriodoNitParametros;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfRectificadaParametros;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfResProcesoNocturnoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfSatEnLineaBitacoraParametros;
import gt.gob.sat.sat_ifi_sipf.dtos.SdcfSaveSolicitudDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.dtos.rejectApproveFiscalDocumentDto;
import gt.gob.sat.sat_ifi_sipf.dtos.VariacionesDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfSolicitudCreditoFiscal;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.InconsistenciaProjections;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesCreditoFiscalProjection;
import gt.gob.sat.sat_ifi_sipf.services.CreditoFiscalService;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.utils.HeaderUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 *
 * @author ajabarrer
 */
@Api(tags = {"Credito Fiscal"})
@Validated
@RestController
@RequestMapping("/fiscal/credit")
@Slf4j
public class CreditoFiscalController {

    @Autowired
    private CreditoFiscalService creditoFiscal;

    @Autowired
    ConsumosService consumosService;

    @GetMapping(path = "/findAllFiscalCreditRequests", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las solicitudes de credito fiscal")
    public List<SipfSolicitudCreditoFiscal> findAllFiscalCreditRequests() {
        return creditoFiscal.findAllSolicitudesCreditoFiscal();
    }

    @GetMapping(path = "/findFiscalCreditRequests/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene una solicitud de credito fiscal en base al id ")
    public SipfSolicitudCreditoFiscal findCreditFiscalRequestsById(
            @ApiParam(value = "Id de la solicitud")
            @PathVariable Integer id) {

        return creditoFiscal.findSolicitudesCreditoFiscalById(id);
    }

    @GetMapping(path = "/findDatosSolicitudes/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene solicitudes de un contribuyente ", notes = "")
    public ResponseEntity<List<Object>> findDatosSolicitudes(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/findDatosSolicitudes/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/obtenerSecuencia", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene una secuencia", notes = "")
    public ResponseEntity<Long> obtenerSecuencia() {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Long> resultado = consumosService.consumeCompleteUrlOracle(null, "/sdcf/obtenerSecuencia", GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/findDatosContribuyente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene datos del contribuyente", notes = "")
    public ResponseEntity<Object> findDatosContribuyente(
            @ApiIgnore UserLogged user,
            @RequestParam(value = "idSolicitud", required = false, defaultValue = "") Integer idSolicitud) {
        SipfSolicitudCreditoFiscal solicitud = new SipfSolicitudCreditoFiscal();

        if (idSolicitud != null) {
            solicitud = this.creditoFiscal.findSolicitudesCreditoFiscalById(idSolicitud);
        }
        String nit = idSolicitud != null ? solicitud.getNitContribuyente() : user.getNit();

        HttpHeaders headers = HeaderUtils.createHeaders();
        DatosFormularioCreditoFiscalDto respuesta = new DatosFormularioCreditoFiscalDto();
        GeneralResponseDto<Object> resultadoContribuyente = consumosService.consumeCompleteUrlOracle(nit, "/sdcf/findDatosContribuyente/" + nit, GeneralResponseDto.class, HttpMethod.GET);
        String contribuyenteJson = new Gson().toJson(resultadoContribuyente.getData());
        DatosContribuyenteCreditoFiscalDto contribuyente = new Gson().fromJson(contribuyenteJson, DatosContribuyenteCreditoFiscalDto.class);
        GeneralResponseDto<List<Object>> resultadoRepresentante = consumosService.consumeCompleteUrlOracle(nit, "/sdcf/ObtenerRepresentantes/" + nit, GeneralResponseDto.class, HttpMethod.GET);

        if (contribuyente.getNitContador() != null) {
            GeneralResponseDto<Object> resultadoContador = consumosService.consumeCompleteUrlOracle(contribuyente.getNitContador(), "/sdcf/obtenerDatosCondador/" + contribuyente.getNitContador(), GeneralResponseDto.class, HttpMethod.GET);
            respuesta.setResultadoContador(resultadoContador.getData());
        }

        respuesta.setResultadoContribuyente(contribuyente);
        respuesta.setResultadoRepresentante(resultadoRepresentante.getData());
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping(path = "/findEmail/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene email en base a un nit", notes = "")
    public ResponseEntity<List<Object>> findEmail(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/findEmail/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/funcionConstruirDIreccion/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "contruye la direccion del contribuyente n base al nit", notes = "")
    public ResponseEntity<List<Object>> funcionConstruirDIreccion(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/funcionConstruirDIreccion/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/ObtenerRepresentantes/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los representantes legales de un contribuyente", notes = "")
    public ResponseEntity<List<Object>> ObtenerRepresentantes(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/ObtenerRepresentantes/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/obtenerDatosCondador/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos de un contador", notes = "")
    public ResponseEntity<Object> obtenerDatosCondador(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Object> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/obtenerDatosCondador/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/buscarRatificado/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "consulta ratificado ", notes = "")
    public ResponseEntity<Object> buscarRatificado(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Object> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/buscarRatificado/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/funcionVerificaOmiso/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<Boolean> funcionVerificaOmiso(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Boolean> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/funcionVerificaOmiso/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @PostMapping(path = "obtenerDeclaracionesPost", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene infromacion de declaraciones de un contribuyente en base al nit y a un periodo dd/mm/yyyy", notes = "")
    public ResponseEntity<List<Object>> obtenerDeclaracionesPost(@RequestBody SdcfPeriodoNitParametros dto) {
       
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/obtenerDeclaracionesPost", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "guardarBitacoraAgenciaVirtual", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "guarda datos en la bitacora de agencia virtual", notes = "")
    public void guardarBitacoraAgenciaVirtual(@RequestBody SdcfSatEnLineaBitacoraParametros dto) {
        this.consumosService.consumeCompleteUrlOracle(dto, "/sdcf/guardarBitacoraAgenciaVirtual", GeneralResponseDto.class, HttpMethod.POST);

    }

    @PostMapping(path = "verificarConvenioPost", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los convenios de un contribuyente en base al nit y a un periodo dd/mm/yyyy", notes = "")
    public ResponseEntity<List<Object>> verificarConvenioPost(@RequestBody SdcfPeriodoNitParametros dto) {
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/verificarConvenioPost", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "existeSolicitud2125Post", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los convenios de un contribuyente en base al nit y a un periodo dd/mm/yyyy", notes = "")
    public ResponseEntity<Boolean> existeSolicitud2125Post(@RequestBody SdcfPeriodoNitParametros dto) {
        GeneralResponseDto<Boolean> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/existeSolicitud2125Post", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "existeMesEn2251Post", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los convenios de un contribuyente en base al nit y a un periodo dd/mm/yyyy", notes = "")
    public ResponseEntity<Object> existeMesEn2251Post(@RequestBody SdcfPeriodoNitParametros dto) {
        GeneralResponseDto<String> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/existeMesEn2251Post", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @GetMapping(path = "funcionVerificaOmisoPorPerido/{pNit}/{pPeriodo}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<Boolean> funcionVerificaOmisoPorPerido(
            @ApiParam(value = "pNit")
            @PathVariable String pNit,
            @PathVariable String pPeriodo) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Boolean> resultado = consumosService.consumeCompleteUrlOracle(pNit + pPeriodo, "/sdcf/funcionVerificaOmisoPorPerido/" + pNit + "/" + pPeriodo, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @PostMapping(path = "buscarRectificadaPorCasillaPost", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los convenios de un contribuyente en base al nit y a un periodo dd/mm/yyyy", notes = "")
    public ResponseEntity<String> buscarRectificadaPorCasillaPost(@RequestBody SdcfRectificadaParametros dto) {
        GeneralResponseDto<String> resultado = consumosService.consumeCompleteUrlOracle(dto, "/sdcf/buscarRectificadaPorCasillaPost", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @GetMapping(path = "funcionVerificaCualquierOmiso/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<Boolean> funcionVerificaCualquierOmiso(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Boolean> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/funcionVerificaCualquierOmiso/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "verificarCualquierConvenio/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<List<Object>> verificarCualquierConvenio(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/verificarCualquierConvenio/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @PostMapping(path = "saveSolicitud", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "guarda datos en la bitacora de agencia virtual", notes = "")
    public ResponseEntity<Boolean> saveSolicitud(@RequestBody SdcfSaveSolicitudDto dto, @ApiIgnore UserLogged user) throws ParseException {
        
        
//        dto.getDatosSolicitud().setPeriodoDesde(new SimpleDateFormat("yyyy-MM-dd").parse("01-07-2014"));
//        dto.getDatosSolicitud().setPeriodoHasta(new SimpleDateFormat("yyyy-MM-dd").parse("31-07-2014"));
//        
        String Body = new Gson().toJson(dto);
        System.out.println("body controlador"+Body);

        return ResponseEntity.ok(creditoFiscal.saveSolicitudCreditoFiscal(dto, user));
    }
    
    @GetMapping(path = "procesoNocturno", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Retorna los resultados del proceso nocturno", notes = "")
    public ResponseEntity<SdcfResProcesoNocturnoDto> ResultadoProcesoNocturno() throws ParseException {
       
        return ResponseEntity.ok(creditoFiscal.ResultadoProcesoNocturno());
    }

    @GetMapping(path = "validarNitEnFEL/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<Object> validarNitEnFEL(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Object> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/validarNitEnFEL/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "validarNitEnRegElectronico/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<Object> validarNitEnRegElectronico(
            @ApiParam(value = "pNit")
            @PathVariable String pNit) {

        HttpHeaders headers = HeaderUtils.createHeaders();

        GeneralResponseDto<Object> resultado = consumosService.consumeCompleteUrlOracle(pNit, "/sdcf/validarNitEnRegElectronico/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        return ResponseEntity.ok(resultado.getData());
    }

    @PostMapping(path = "update/request/night/process", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "actualiza las solicitudes despu�s de pasar por el proceso nocturno", notes = "")
    public void updateRequestFromNigthProcess(
            @RequestParam Integer idStatus,
            @ApiIgnore UserLogged logged) {

        creditoFiscal.updateRequestsByNightProccess(idStatus, logged);
    }

    @PostMapping(path = "save/inconsitencies/request/night/process", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "actualiza las solicitudes despu�s de pasar por el proceso nocturno", notes = "")
    public void saveInconsistenciesRequestFromNigthProcess(
            @RequestBody List<SipfSolicitudCreditoFiscal> requests,
            @RequestBody List<BigDecimal> solicitudes,
            @ApiIgnore UserLogged logged) {

        creditoFiscal.updateInconsistenciesRequest(solicitudes, requests, logged);
    }

    @PostMapping(path = "upload/file/period", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Inserta en base datos la informaci�n correspondiente a un caso", notes = "ingreso de casos")
    public ResponseEntity<?> uploadFilesPeriods(@RequestPart("data") String period,
            @RequestPart("file") List<MultipartFile> files, @ApiIgnore UserLogged userLogged) throws IOException {
        return ResponseEntity.ok(creditoFiscal.uploadRespaldoPeriodo(
                userLogged,
                new Gson().fromJson(period, String.class),
                files
        ));
    }

    @GetMapping(path = "has/variations/{idRequest}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Inserta en base datos la informaci�n correspondiente a un caso", notes = "ingreso de casos")
    public boolean hasVariations(@PathVariable BigDecimal idRequest, @RequestParam Integer docType, @RequestParam BigDecimal idDoc) {
        VariacionesDto body = new VariacionesDto(idRequest, new BigDecimal(docType), idDoc);
        GeneralResponseDto<Boolean> resultado = consumosService.consumeCompleteUrlOracle(body, "sdcf/has/variations", GeneralResponseDto.class, HttpMethod.POST);
        if (resultado.getData()) {
            throw BusinessException.unprocessableEntity(Message.VARIATIONS_FOUND.getText());
        }
        return resultado.getData();
    }

    @GetMapping(path = "file/information/{idSolicitud}/{idEstado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los datos ", notes = "")
    public ResponseEntity<?> getFilesInformation(@PathVariable Integer idSolicitud, @PathVariable Integer idEstado) {
        return ResponseEntity.ok(creditoFiscal.getDataInformation(idSolicitud, idEstado));
    }

    @PostMapping(path = "reject/approve/file", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "aprueba o rechaza un documento desde la vista del operador", notes = "")
    public ResponseEntity<?> rejectApproveFiscalDocument(@RequestBody rejectApproveFiscalDocumentDto dto, @ApiIgnore UserLogged user) {

        return ResponseEntity.ok(creditoFiscal.rejectApproveDocument(dto, user));
    }

    @GetMapping(path = "/find/profetional/request", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las solicitudes de credito fiscal en base al profesional y el estado")
    public List<BandejaCreditoFiscalProjection> findAllFiscalCreditRequests(
            @ApiIgnore UserLogged logged) {
        return creditoFiscal.findAllAssingByNitAndStatus(logged);
    }

    @PostMapping(path = "/update/status", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Actualiza el estado de la solicitud de credito fiscal")
    public boolean updateStatusCedulaRequest(
            @RequestPart("data1") String idSolicitud,
            @RequestPart("data2") String idEstado,
            @RequestPart("file") List<MultipartFile> file,
            @ApiIgnore UserLogged userLogged) {

        return creditoFiscal.UpdateStatusCedulaRequest(new Gson().fromJson(idSolicitud, String.class),
                new Gson().fromJson(idEstado, String.class),
                userLogged,
                file);
    }

    @PostMapping(path = "/generation/cedula",
            consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Crear archivo pdf")
    public ResponseEntity<  byte[]> createPdf(
            @RequestBody(required = true) String data, @ApiIgnore UserLogged user) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(creditoFiscal.generacionPdf(data, user));
    }

    @DeleteMapping(path = "delete/generation/cedula")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Elimina archivo pdf")
    public ResponseEntity<?> deletePdf() throws IOException {
        creditoFiscal.deleteDocument();
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/inconsistency/{idSolicitud}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las inconsitencias de una solicitud de credito fiscal ")
    public List<InconsistenciaProjections> findInconsistencyByRequest(
            @ApiParam(value = "Id de la solicitud")
            @PathVariable Integer idSolicitud) {

        return creditoFiscal.findInconsistencyByRequest(idSolicitud);
    }

    @GetMapping(path = "solicitudes/contribuyente", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Obtiene las solicitudes en base a un nit de contribuyente.")
    public ResponseEntity<List<SolicitudesCreditoFiscalProjection>> getSolicitudesCreditoFiscal(@ApiIgnore UserLogged user) {
        return ResponseEntity.ok(this.creditoFiscal.getSolicitudesCreditoFiscal(user.getNit(), Arrays.asList(1058,1070,966)));
    }

    @PostMapping(path = "/save/libros",
            consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "guarda los archivos en ACS")
    public void saveLibrosCreditoFiscal(@RequestPart("file") List<MultipartFile> file, @RequestPart("data") String data, @ApiIgnore UserLogged user) {
        //CREACION DEL PERSONALIZADO CON EL "TYPE TOKEN"
        CreditoFiscalDto libros = new Gson().fromJson(data, CreditoFiscalDto.class);
        libros.setNit(user.getNit());
        this.creditoFiscal.saveArchivoACS(file, libros);
    }

    @GetMapping(path = "solicitudes/admited", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Obtiene las solicitudes en base a un nit de contribuyente.")
    public ResponseEntity<List<SolicitudesCreditoFiscalProjection>> getSolicitudesCreditoFiscalAdmitidas(@ApiIgnore UserLogged user) {
        return ResponseEntity.ok(this.creditoFiscal.getSolicitudesCreditoFiscalAdmitidas());
    }

    @PutMapping(path = "/assing/solicitud")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Asigna solicitud a un profesional ")
    public SipfSolicitudCreditoFiscal asignarSolicitudProfesional(
            @ApiParam(value = "Identifificador de la solicitud")
            @RequestParam Integer id,
            @RequestParam String nit,
            @ApiIgnore UserLogged logged) {

        return creditoFiscal.asignarSolicitudProfesional(id, nit, logged);
    }

    @PutMapping(path = "/email/rejected")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Rechaza Solicitud y envia correo electronio")
    public void SendEmailRejected(@ApiParam(value = "Id de la solicitud")
            @RequestBody AprobedRejectedCedulaDto dto,
            @ApiIgnore UserLogged user ){
        creditoFiscal.SendEmailRejected(user, dto);
    }
    
    @PutMapping(path = "/email/accept/{idSolicitud}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Aprueba Solicitud y envia correo electronio")
    public void SendEmailAccept(@ApiParam(value = "Id de la solicitud")
            @PathVariable Integer idSolicitud){
        creditoFiscal.SendEmailAccept(idSolicitud);
    }
    
    @GetMapping(path = "extra/data/{idRequest}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Obtiene las solicitudes en base a un nit de contribuyente.")
    public ResponseEntity<BandejaCreditoFiscalProjection> getSolicitudesCreditoFiscal(
            @PathVariable Integer idRequest) {
        return ResponseEntity.ok(this.creditoFiscal.getExtraDataRequest(idRequest));
    }
    
    @PutMapping(path = "/update/inconsistency")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Asigna solicitud a un profesional ")
    public boolean updateInconsistency(
            @ApiParam(value = "Identifificador de la solicitud")
            @RequestParam Integer id,
            @RequestParam Integer status,
            @ApiIgnore UserLogged logged) {

        return creditoFiscal.updateInconsistency(id, status, logged);
    }
    
    @GetMapping(path = "/filenames")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value ="Obtiene el nombre de los archivos de respaldo")
    public ResponseEntity<List<CatalogDataProjection>> getFileNames(
            @RequestParam String periodo,
            @RequestParam List<Integer> status,
            @ApiIgnore UserLogged logged) {
        return ResponseEntity.ok(creditoFiscal.getFileNames(periodo, logged.getNit(), status));
    }
}
 