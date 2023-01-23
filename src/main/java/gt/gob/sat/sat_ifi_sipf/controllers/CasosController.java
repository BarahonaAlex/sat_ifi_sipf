/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.CaseMultipartDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasesScopeMasiveDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasoComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasoConsultaJsonDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ComentarioHistoricoNombramientoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoManualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.MassiveAssignParamsDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ReasignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SolicitudesAduanasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraAllCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasoProjection;
import gt.gob.sat.sat_ifi_sipf.services.CasoConsultaJsonService;
import gt.gob.sat.sat_ifi_sipf.services.CasosService;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
import gt.gob.sat.sat_ifi_sipf.projections.CasoConsultaJsonProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetailWalletAppoint;
import gt.gob.sat.sat_ifi_sipf.projections.MassiveResumeProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosMasivosProjections;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ResponsableCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudesAduanasProjections;
import gt.gob.sat.sat_ifi_sipf.projections.WalletAppointments;
import gt.gob.sat.sat_ifi_sipf.services.ComentarioHistoricoNombramientoService;
import gt.gob.sat.sat_ifi_sipf.services.ComentarioHistoricoNombramientoBitacoraService;
import java.lang.reflect.Type;
import java.sql.SQLException;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author crramosl
 */
@Api(tags = {"Casos"})
@Validated
@RestController
@Slf4j
@RequestMapping("/cases")
public class CasosController {

    @Autowired
    CasosService claseService;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    CasoConsultaJsonService casoConsultaJsonService;

    @Autowired
    ComentarioHistoricoNombramientoService comentarioHistoricoNombramientoService;

    @Autowired
    ComentarioHistoricoNombramientoBitacoraService comentarioHistoricoNombramientoBitacoraService;

    @Autowired
    Detector detector;

    @PostMapping(path = "data/file", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Inserta en base datos la información correspondiente a un caso", notes = "ingreso de casos")
    public ResponseEntity<?> createCase(@RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> files, @ApiIgnore UserLogged userLogged) throws IOException {
        return ResponseEntity.ok(claseService.createCase(
                new Gson().fromJson(data, CasosDto.class), files, userLogged
        ));
    }

    @PostMapping(path = "/massive")
    @ApiOperation(value = "Recibe el archivo con los datos del insumo", notes = "ingreso de casos por archivo")
    public ResponseEntity<?> createCasesFile(@RequestPart("data") String data,
            @RequestPart("file") MultipartFile file, @ApiIgnore UserLogged userLogged) {
        claseService.createCaseMass(new Gson().fromJson(data, InsumoManualDto.class), file, userLogged);
        return ResponseEntity.ok().build();
    }

    @GetMapping(path = "/wallet/{nitProfesional}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos asignados al colaborador", notes = "Retorna una lista de casos asignados")
    public ResponseEntity<List<CarteraCasosProjection>> walletCase(
            @ApiParam(value = "Nit del colaborador")
            @PathVariable String nitProfesional, @ApiIgnore UserLogged logged) {
        List<CarteraCasosProjection> insumos = claseService.walletCase("def".equals(nitProfesional) ? logged.getNit() : nitProfesional);
        return ResponseEntity.ok(insumos);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtener datos de un caso en especifico", notes = "Obtiene los datos del usuario especificado en el path")
    public ResponseEntity<CasoComentarioDto> findCaseById(
            @ApiParam(value = "Id de caso")
            @PathVariable Integer id,
            @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.getCase(id, user));
    }

    @PutMapping(path = "/{idCaso}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Modificar el caso", notes = "")
    public ResponseEntity<?> alterCase(@PathVariable Integer idCaso, @RequestBody CasosDto insumo, @ApiIgnore UserLogged user) {

        return ResponseEntity.ok(claseService.alterCase(idCaso, insumo, user));
    }

    @PutMapping(path = "multipart/{idCaso}",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "actualiza los archivos de un caso", notes = "sustituye archivos previamente cargados a un caso dado")
    @Transactional
    public ResponseEntity saveNew(@PathVariable Integer idCaso, @RequestPart("file") List<MultipartFile> file,
            @RequestPart("data") String data, @ApiIgnore UserLogged user) {

        CaseMultipartDto params = new Gson().fromJson(data, CaseMultipartDto.class);
        params.getListFileData().forEach(item -> {

            this.claseService.replaceFile(item.getIdFile(), file.get(item.getIndex()));

        });
        params.getCaseData().setEstado(15);
        params.getCaseData().setIdCaso(idCaso);
        return ResponseEntity.ok(claseService.alterCase(idCaso, params.getCaseData(), user));
    }

    @PutMapping(path = "/program/{idCaso}", produces = MediaType.APPLICATION_JSON_VALUE)//consumes = MediaType.APPLICATION_JSON_VALUE,
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "elimina el programa de un caso", notes = "")
    public void alterCaseProgram(@PathVariable Integer idCaso) {//, @RequestBody(required = false) CasosDto insumo) {

        claseService.modifyCaseProgram(idCaso);
    }

    @GetMapping(path = "/input/status/{idInput}/{idStatus}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los casos filtrados por insumo y estado ", notes = "")
    public ResponseEntity<List<CasoProjection>> obtainCasesByInputAndStatus(
            @ApiParam(value = "Id del insumo")
            @PathVariable Integer idInput,
            @ApiParam(value = "Id del estado ")
            @PathVariable Integer idStatus) {

        return this.claseService.getCasesByInputAndStatus(idInput, idStatus);
    }

    @GetMapping(path = "/input/{idInput}", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los casos filtrados por insumo", notes = "")
    public ResponseEntity<List<CasoProjection>> obtainCasesByInput(
            @ApiParam(value = "Id del insumo")
            @PathVariable Integer idInput) {

        return this.claseService.getCasesByInput(idInput);
    }

    @GetMapping(path = "/input/status/documents/{idInput}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene los casos filtrados por insumo y estado ", notes = "")
    public ResponseEntity<List<CasoProjection>> obtainCasesByInputAndStatusAndIdFichaTecnica(
            @ApiParam(value = "Id del insumo")
            @PathVariable Integer idInput) {

        return this.claseService.getCasesByInputAndStatusAndIdFicha(idInput);
    }

    @GetMapping(path = "/input/stored/json/{idInput}/{idQueryType}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene las consultas almacenadas de un caso por id de caso y tipo de consulta ", notes = "")
    public ResponseEntity<List<CasoConsultaJsonProjection>> obtainJsonStored(
            @ApiParam(value = "Id del insumo")
            @PathVariable Integer idInput,
            @ApiParam(value = "Id del tipo de consulta ")
            @PathVariable Integer idQueryType) {

        return ResponseEntity.ok(this.casoConsultaJsonService.getList(idInput, idQueryType));
    }

    @PostMapping(path = "/input/stored/json",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un insumo ")

    public ResponseEntity<CasoConsultaJsonDto> createInsumo(@RequestBody(required = true) CasoConsultaJsonDto pDatos) throws SQLException {

        //log.debug(pDatos.toString());
        //@Nullable Object body, @NotNull @NotBlank String url, @NotNull Class<T> klass, @NotNull HttpMethod type, HttpHeaders headers
        this.casoConsultaJsonService.createCasoConsultaJson(pDatos);
        //log.debug(resultado.getData().toString());
        return ResponseEntity.ok(pDatos);
    }

    @PostMapping(path = "/audit/comment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crear comentario de un nombramiento", notes = "crea un comentario sobre un nombramiento")
    public ComentarioHistoricoNombramientoDto putFiscalProgram(@RequestBody ComentarioHistoricoNombramientoDto pComment, @ApiIgnore UserLogged logged) {
        return this.comentarioHistoricoNombramientoService.saveCommnetAudit(pComment, logged);
    }

    @PostMapping(path = "/audit/comment/delete",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Elimina comentario de un nombramiento", notes = "lo elimina y lo guarda en la tabla de bitacora")
    public ComentarioHistoricoNombramientoDto putFiscalProgramBitacora(@RequestBody ComentarioHistoricoNombramientoDto pComment, @ApiIgnore UserLogged logged) {
        return this.comentarioHistoricoNombramientoBitacoraService.saveCommnetAuditBitacora(pComment, logged);
    }

    ///obtiene los casos para desasignar
    @GetMapping(path = "/unassign/{nit}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos de un profesional para desasignar")
    public ResponseEntity<List<DesasignacionCasosProjection>> getCasesForUnassign(
            @ApiParam(value = "Nit del usuario profesional")
            @PathVariable String nit) {
        List<DesasignacionCasosProjection> getCasesUnassign = claseService.getCasesForUnassign(nit);
        return ResponseEntity.ok(getCasesUnassign);
    }

    ////
    @GetMapping(path = "/reassign/{nit}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos de un profesional para reasignar")
    public ResponseEntity<List<ReasignacionCasosProjection>> getCasesForReassign(
            @ApiParam(value = "Nit del usuario profesional")
            @PathVariable String nit) {
        List<ReasignacionCasosProjection> getCasesReassign = claseService.getCasesForReassign(nit);
        return ResponseEntity.ok(getCasesReassign);
    }

    @PutMapping(path = "/unassign",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Desasignar el caso a un colaborador.")
    public boolean UnassignCase(@RequestParam List<Integer> idCaso, @RequestBody ReasignacionCasosDto dto) {
        return claseService.UnassignCases(dto, idCaso);
    }

    @GetMapping(path = "/wallet/appointments")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Regresa lista de casos para nombramiento")
    public ResponseEntity<List<WalletAppointments>> getWalletAppointments() {
        return ResponseEntity.ok(claseService.getWalletAppointments());
    }

    @GetMapping(path = "/detail/wallet/appointments/{idInsumo}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Regresa lista de casos para nombramiento")
    public ResponseEntity<List<DetailWalletAppoint>> getWalletAppointments(@PathVariable Integer idInsumo) {
        return ResponseEntity.ok(claseService.getDetailWalletAppointments(idInsumo));
    }

    @GetMapping(path = "/states",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos en base a estados", notes = "Retorna los casos en base a estado")
    public ResponseEntity<List<CarteraCasosProjection>> casesByStates(
            @ApiParam(value = "Estados de los casos")
            @RequestParam List<Integer> pStates) {
        List<CarteraCasosProjection> insumos = claseService.casesByStates(pStates);
        return ResponseEntity.ok(insumos);
    }

    @PutMapping(path = "/scope/approve/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se aprueba el alcance del caso", notes = "Se aprueba el alcance del caso")
    public ResponseEntity<Boolean> approveScopeCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.alterStateCases(id, Catalog.Scope.Status.SCOPE_REVIEW_JD, Catalog.Case.Status.ELABORATE_SCOPE, user, "aprueba"));
    }

    @PutMapping(path = "/approver/decline/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> approverDeclineCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(claseService.alterStatecasesWithComentary(id, Catalog.Case.Status.REJECTED,  Catalog.Case.Status.ELABORATE_SCOPE, user, comentario, 408, "rechazar"));
    }

    @PutMapping(path = "/approver/request/fixes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> approverRequestFixesCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(claseService.alterStatecasesWithComentary(id,  Catalog.Case.Status.SCOPE_CORRECTION, Catalog.Case.Status.ELABORATE_SCOPE, user, comentario, 409, "corregir"));
    }

    @PutMapping(path = "/scope/authorizer/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se autoriza el alcance del caso", notes = "Se autoriza el alcance del caso")
    public ResponseEntity<Boolean> authorizerScopeCases(
            @ApiParam(value = "dentificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.alterStateCases(id, Catalog.Case.Status.PROGRAMMING_AUTHORIZATION, Catalog.Scope.Status.SCOPE_REVIEW_JU, user, "autoriza"));
    }

    @PutMapping(path = "/authorizer/decline/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> authorizerDeclineCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(claseService.alterStatecasesWithComentary(id, Catalog.Case.Status.REJECTED, Catalog.Case.Status.ELABORATE_SCOPE, user, comentario, 970, "rechazar"));
    }

    @PutMapping(path = "/authorizer/request/fixes/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> authorizerRequestFixesCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(claseService.alterStatecasesWithComentary(id,  Catalog.Case.Status.SCOPE_CORRECTION, Catalog.Case.Status.ELABORATE_SCOPE, user, comentario, 409, "corregir"));
    }

    @PostMapping(path = "subsequent/requests", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Inserta en base datos la información correspondiente a un caso", notes = "ingreso de casos")
    public ResponseEntity<?> createSubsequentRequests(@RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> files, @ApiIgnore UserLogged userLogged) throws IOException {
        Type tipoLista = new TypeToken<ArrayList<SolicitudesAduanasDto>>() {
        }.getType();
        ArrayList<SolicitudesAduanasDto> solicitudes = new Gson().fromJson(data, tipoLista);
        return ResponseEntity.ok(claseService.createSubsequentRequests(
                solicitudes, files, userLogged
        ));
    }

    @GetMapping(path = "/massive/resume", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el resumen de los casos agrupados por tipo de caso y gerencia", notes = "")
    public ResponseEntity<List<MassiveResumeProjection>> getResumeBy(@ApiParam(value = "id del tipo tipo de caso a buscar")
            @RequestParam(required = false) Integer pIdTipoCaso/*, 
            @ApiParam(value = "id del estado a buscar")
            @RequestParam(required = false) Integer pIdEstado*/
    ) {
        return ResponseEntity.ok(this.claseService.getResumeBy(pIdTipoCaso, 177));
    }

    @PutMapping(path = "/file/{idCase}", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Inserta en base datos la información correspondiente a un caso", notes = "ingreso de casos")
    public ResponseEntity<?> updateFilesCase(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int idCase,
            @RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> files, @ApiIgnore UserLogged userLogged) throws IOException {
        return ResponseEntity.ok(claseService.ingresoArchivosRespaldo(
                new Gson().fromJson(data, CasosDto.class), files, userLogged
        ));
    }

    @PutMapping(path = "/massive/assign",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public void getResumeBy(
            @RequestBody MassiveAssignParamsDto pParams,
            @ApiIgnore UserLogged userLogged) {

        this.claseService.assignTopNToResponsible(userLogged, pParams);

    }

    @PutMapping(path = "/decline/{idCaso}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> aproverDeclineCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int idCaso, @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.aproverDeclineCases(idCaso, user));
    }

    @PutMapping(path = "/operator/decline/{idCaso}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> operatorDeclineCases(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int idCaso, @ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(claseService.operatorDeclineCases(idCaso, user, comentario));
    }

    @PutMapping(path = "/not/approver/{idCaso}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> notAprovee(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int idCaso, @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.notAprovee(idCaso, user));
    }

    @PutMapping(path = "/final/rejection/{idCaso}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza el caso", notes = "Se Rechazo definitivo del caso")
    public ResponseEntity<Boolean> finalRejection(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int idCaso, @ApiIgnore UserLogged user, @RequestBody CasosDto caso) {
        return ResponseEntity.ok(claseService.finalRejection(idCaso, caso, user));
    }

    @GetMapping(path = "/customs/request/posterior")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Regresa lista de casos para nombramiento")
    public ResponseEntity<List<SolicitudesAduanasProjections>> getSolitudesAduanas() {
        return ResponseEntity.ok(claseService.getSolitudesAduanas());
    }

    /**
     * OBTIENE LOS CASOS DE PUNTOS FIJOS PARA CREAR UN ALCANCE
     *
     * @return
     */
    @GetMapping(path = "/scope/fixed/point")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos de tipo Puntos Fijos para generar alcance.")
    public ResponseEntity<List<CasosAlcanceProjection>> getCasesForScopesFixedPoints(
            @ApiIgnore UserLogged user
    ) {
        return ResponseEntity.ok(claseService.getCasosForScopesFixedPoints(user.getNit()));
    }

    /**
     * OBTIENE LOS CASODE GABINETE PARA
     *
     * @return
     */
    @GetMapping(path = "/scope/fiscal")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos de tipo Puntos Fijos para generar alcance.")
    public ResponseEntity<List<CasosAlcanceProjection>> getCasesForScopesFiscal(
            @ApiIgnore UserLogged user
    ) {
        return ResponseEntity.ok(claseService.getCasesForScopesFiscal(user.getNit()));
    }

    @PostMapping(path = "/start/process/masive", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea un nuevo proceso de gabinete y sube el archivo a ACS")
    ResponseEntity<?> CreateProcessTax(
            @RequestBody CasesScopeMasiveDto data,
            @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.saveCasesScope(data, user));
    }

    @GetMapping(path = "/proces")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los procesos masivos para generar alcance.")
    public ResponseEntity<List<ProcesosMasivosProjections>> getProcesMasive(
            @ApiIgnore UserLogged user
    ) {
        return ResponseEntity.ok(claseService.getProcesMasive());
    }

    @GetMapping(path = "/wallet/management")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos para su aprobacion gerencial")
    public ResponseEntity<List<CarteraAllCasesProjection>> getAllCases(@ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.casesAll());
    }
    @GetMapping(path="/responsible/data/{idCaso}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los datos del responable del casos")
    public ResponseEntity<List<ResponsableCasosProjection>> getRespnsableCasos(  
            @ApiParam(value = "Identificador del caso")
            @PathVariable Integer idCaso){
     return ResponseEntity.ok(claseService.getResponsability(idCaso));
    }
    
}
