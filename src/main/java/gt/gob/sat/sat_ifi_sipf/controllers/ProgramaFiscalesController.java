/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AprobacionProgramasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ProgramaFiscalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfCasos;
import gt.gob.sat.sat_ifi_sipf.projections.ApproveAllCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraAprobacionProgramaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesComentarioProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProgramaFiscalesProjection;
import gt.gob.sat.sat_ifi_sipf.services.ProgramaFiscalesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author ruarcuse
 */
@Api(tags = {"Programas Fiscales"})
@Validated
@RestController
@Slf4j
@RequestMapping("/fiscal/program")
public class ProgramaFiscalesController {

    @Autowired
    private ProgramaFiscalesService fiscalProgramService;

    @GetMapping(path = "/{idFiscalProgram}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los datos de un programa fiscal buscado por su id.")
    public ResponseEntity<ProgramaFiscalesProjection> getFiscalProgramById(
            @ApiParam(value = "Identificador del programa fiscal")
            @PathVariable Integer idFiscalProgram
    ) {
        Optional<ProgramaFiscalesProjection> vOptionalFiscalProgram = this.fiscalProgramService.findFiscalProgramById(idFiscalProgram);
        return vOptionalFiscalProgram.isPresent() ? ResponseEntity.ok(vOptionalFiscalProgram.get()) : ResponseEntity.notFound().build();
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crear programa fiscal", notes = "Crea un nuevo programa fiscal")
    //@Validated(OnCreate.class)
    public ResponseEntity<?> postFiscalProgram(@RequestBody ProgramaFiscalDto pFiscalProgram,
            @ApiIgnore UserLogged user) {
        pFiscalProgram.setUsuarioModifica(user.getLogin());
        pFiscalProgram.setUsuarioAgrega(user.getNit());
        pFiscalProgram.setIpModifica(user.getIp());
        return this.fiscalProgramService.createFiscalProgram(pFiscalProgram);

    }

    @PutMapping(path = "/{idFiscalProgram}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Crear programa fiscal", notes = "Crea un nuevo programa fiscal")
    //@Validated(OnCreate.class)
    public void putFiscalProgram(@PathVariable Integer idFiscalProgram,
            @RequestBody ProgramaFiscalDto pFiscalProgram,
            @ApiIgnore UserLogged user) {
        pFiscalProgram.setUsuarioModifica(user.getNit());
        pFiscalProgram.setIpModifica(user.getIp());
        pFiscalProgram.setFechaModifica(new Date());
        this.fiscalProgramService.updateFiscalProgram(idFiscalProgram, pFiscalProgram,user);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el id y el nombre del programa fiscal filtrados por estado y por gerencia y por tipo de programa y por un rango de fechas")
    public ResponseEntity<List<ProgramaFiscalesProjection>> getFiscalProgramsByStatusAndRegional(
            @ApiParam(value = "id de la gerencia por la que se desea filtrar")
            @RequestParam(required = false) Integer idRegional,
            @ApiParam(value = "id del estado por el que se desa filtrar")
            @RequestParam(required = false) Integer idStatus,
            @ApiParam(value = "id del tipo de programa por el que se desea filtrar")
            @RequestParam(required = false) Integer idProgramType
    ) {

        List<ProgramaFiscalesProjection> getFiscalPrograms = null;
        if (idRegional == null) {
            getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatus(idStatus);
        }

        if (idRegional != null && idStatus != null) {
            getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatusAndRegional(idStatus, idRegional);
        }

        if (idRegional != null && idProgramType != null) {
            getFiscalPrograms = fiscalProgramService.getFiscalProgramsByIdGerenciaAndProgramType(idRegional, idProgramType);
        }

        if (idProgramType != null && idStatus != null) {
            getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatusAndProgramType(idStatus, idProgramType);
        }

        if (idRegional != null && idStatus != null && idProgramType != null) {
            getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatusAndRegionalAndProgramType(idStatus, idRegional, idProgramType);
        }

        return ResponseEntity.ok(getFiscalPrograms);

    }

    @GetMapping(path = "/date/range", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el id y el nombre del programa fiscal filtrados por estado y por gerencia y por tipo de programa y por un rango de fechas")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByListCatalogs(
            @ApiParam(value = "id de la gerencia por la que se desea filtrar")
            @RequestParam(required = true) Integer idRegional,
            @ApiParam(value = "id del estado por el que se desa filtrar")
            @RequestParam(required = true) Integer idStatus,
            @ApiParam(value = "fecha de inicio del rango (fecha de creacion )")
            @RequestParam(required = true) String dateFrom,
            @ApiParam(value = "fecha de fin del rango (fecha de creacion )")
            @RequestParam(required = true) String dateTo) {

        List<ProgramaFiscalesComentarioProjection> getFiscalPrograms = null;

        getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatusAndRegionalAndDateRange(idStatus, idRegional, new Date(dateFrom), new Date(dateTo));

        /*if (getFiscalPrograms == null || getFiscalPrograms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
         */
        return ResponseEntity.ok(getFiscalPrograms);
        //      }
    }
    @GetMapping(path = "/locked", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el id y el nombre del programa fiscal filtrados por estado y por gerencia y por tipo de programa y por un rango de fechas")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsLocked(
            @ApiParam(value = "fecha de inicio del rango (fecha de creacion )")
            @RequestParam(required = true) String dateFrom,
            @ApiParam(value = "fecha de fin del rango (fecha de creacion )")
            @RequestParam(required = true) String dateTo) {

        List<ProgramaFiscalesComentarioProjection> getFiscalPrograms = null;
        getFiscalPrograms = fiscalProgramService.getFiscalProgramsByStatusAndDateRange(new Date(dateFrom), new Date(dateTo));
        return ResponseEntity.ok(getFiscalPrograms);
    }

    @GetMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene programas fiscales creados por el usuario y filtrados por estado")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByUserAndStatus(@ApiIgnore UserLogged user) {

        List<ProgramaFiscalesComentarioProjection> getFiscalProgramsbyUserStatus = null;

        getFiscalProgramsbyUserStatus = fiscalProgramService.getFiscalProgramsByUserAndStatus(Catalog.FiscalPrograms.Status.CORRECTION_REQUEST, user);

        /*if (getFiscalPrograms == null || getFiscalPrograms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
         */
        return ResponseEntity.ok(getFiscalProgramsbyUserStatus);
        //      }
    }

    @GetMapping(path = "/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene programas fiscales filtrados por estado")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByStatus(
            @ApiParam(value = "id del estado por el que se desea filtrar")
            @RequestParam(required = true) Integer idStatus) {

        List<ProgramaFiscalesComentarioProjection> getFiscalProgramsbyStatus = null;

        getFiscalProgramsbyStatus = fiscalProgramService.getFiscalProgramsByStatusProgram(idStatus);

        /*if (getFiscalPrograms == null || getFiscalPrograms.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
         */
        return ResponseEntity.ok(getFiscalProgramsbyStatus);
        //      }
    }
    @GetMapping(path="/authorized/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene programas fiscales filtrados por estado")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByAuthorized() {

        List<ProgramaFiscalesComentarioProjection> getFiscalProgramsbyStatus = null;
        getFiscalProgramsbyStatus = fiscalProgramService.getFiscalProgramsByStatusProgram(Catalog.FiscalPrograms.Status.AUTHORIZED);
        return ResponseEntity.ok(getFiscalProgramsbyStatus);
    }
     @GetMapping(path="/approval/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene programas fiscales filtrados por estado")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByApproval() {

        List<ProgramaFiscalesComentarioProjection> getFiscalProgramsbyStatus = null;
        getFiscalProgramsbyStatus = fiscalProgramService.getFiscalProgramsByStatusProgram(Catalog.FiscalPrograms.Status.APPROVAL);
        return ResponseEntity.ok(getFiscalProgramsbyStatus);
    }
    @GetMapping(path="/revision/status", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene programas fiscales filtrados por estado")
    public ResponseEntity<List<ProgramaFiscalesComentarioProjection>> getFiscalProgramsByrevision() {

        List<ProgramaFiscalesComentarioProjection> getFiscalProgramsbyStatus = null;
        getFiscalProgramsbyStatus = fiscalProgramService.getFiscalProgramsByStatusProgram(Catalog.FiscalPrograms.Status.REVISION);
        return ResponseEntity.ok(getFiscalProgramsbyStatus);
    }
    @GetMapping(path = "/user/created", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el id y el nombre de los programas fiscales creados por usuario logeado")
    public ResponseEntity<List<ProgramaFiscalesProjection>> getFiscalProgramsCreatedByUser(@ApiIgnore UserLogged user) {

        return ResponseEntity.ok(fiscalProgramService.getFiscalProgramsByUser(user));
        //      }
    }

    @GetMapping(path = "/wallet/approve/program",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene una lista de casos con el programa para su aprobacion")
    public ResponseEntity<List<CarteraAprobacionProgramaProjection>> walletApproval() {
        return ResponseEntity.ok(fiscalProgramService.walletApproval());
    }

    @GetMapping(path = "/wallet/approve/management",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene una lista de casos con el programa para su aprobacion por gerencia")
    public ResponseEntity<List<CarteraAprobacionProgramaProjection>> walletDetail(
            @ApiParam(value = "id de la gerencia ")
            @RequestParam Integer idGerencia) {
        return ResponseEntity.ok(fiscalProgramService.walletDetail(idGerencia));
    }

    @PutMapping(path = "/approve",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Aprueba la cantidad de casos para su nombramiento")
    public ResponseEntity<List<AprobacionProgramasDto>> approvalCases(@RequestBody List<AprobacionProgramasDto> programasFiscales, @ApiIgnore UserLogged userLogged) {
        return ResponseEntity.ok(fiscalProgramService.approvalCases(programasFiscales, userLogged));
    }

    @PutMapping(path = "/approve/all",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Aprueba todos los insumos para su nombramiento")
    public ResponseEntity<List<ApproveAllCasesProjection>> approvalCasesAll(@RequestBody AprobacionProgramasDto programasFiscales, @ApiIgnore UserLogged userLogged) {
        return ResponseEntity.ok(fiscalProgramService.approvalAllCase(userLogged));
    }

    @PutMapping(path = "/decline/case",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Rechaza el caso para su nombramiento")
    public void adeclineCases(@RequestBody List<AprobacionProgramasDto> programasFiscales, @ApiIgnore UserLogged userLogged) {
        fiscalProgramService.declineCases(programasFiscales, userLogged);
    }

    @GetMapping(path = "/current/year/{idStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los programas fiscales en base a su estado y del año actual")
    public ResponseEntity<List<ProgramaFiscalesProjection>> getFiscalProgramByStatusAndCurrentYear(
            @ApiParam(value = "Estado de los programas fiscales")
            @PathVariable Integer idStatus) {

        return ResponseEntity.ok(fiscalProgramService.getFiscalProgramsByStatusAndCurrentYear(idStatus));
    }

    @PutMapping(path = "/assign/{idProgram}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Asigan un programa fiscal a un caso")
    public ResponseEntity<SipfCasos> assignFiscalProgram(
            @ApiParam(value = "Identificador del programa fiscal que se asiganrá al caso")
            @PathVariable Integer idProgram,
            @RequestBody Integer idCaso,
            @ApiIgnore UserLogged logged) {

        return ResponseEntity.ok(fiscalProgramService.assignFiscalProgram(idProgram, idCaso, logged));
    }

}
