/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.InsumoManualDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParametroAccionInsumoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.InsumoProjecionStatus;
import gt.gob.sat.sat_ifi_sipf.services.CasosService;
import gt.gob.sat.sat_ifi_sipf.services.InsumoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author rabaraho
 */
@Api(tags
        = {
            "Insumos"
        })
@Slf4j
@Validated
@RestController
@RequestMapping("/input")
public class InsumoController {

    @Autowired
    private InsumoService insumoService;

    @Autowired
    private CasosService casosService;

    @Autowired
    Detector detector;

    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un insumo ")
    public void createInsumo(@RequestBody(required = true) InsumoManualDto pInsumo, @ApiIgnore UserLogged user) {
        this.insumoService.createInput(pInsumo, user);
    }

    @PostMapping(path = "/solicitud/externa",
            consumes
            = {
                MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE
            },
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un insumo ")
    public void createInsumo(@RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> files, @ApiIgnore UserLogged userLogged) {
        InsumoManualDto prueba = new Gson().fromJson(data, InsumoManualDto.class);
        prueba.setIdTipoCaso(1041);
        this.insumoService.createInputExterna(prueba, files, userLogged);
    }

    //Servicio para obtener insumos por estado y filtrado por perfil autorizador
    @GetMapping(path = "authorizer/status/{pIdStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene lista de insumos filtrado por estado")
    public ResponseEntity<List<InsumoProjecionStatus>> getAllInputAuthorizer(@ApiParam(value = "Identificador del estado del insumo por el que se desa filtrar")
            @PathVariable List<Integer> pIdStatus,
            @ApiIgnore UserLogged userLogged) {
        String pNit = userLogged.getNit();
        List<InsumoProjecionStatus> insumos = insumoService.findAllAuthorizer(pIdStatus, pNit);
        if (insumos == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(insumos);
    }

    //Servicio para obtener insumos por estado y filtrado por perfil aprobador
    @GetMapping(path = "/approver/status/{pIdStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene lista de insumos filtrado por estado")
    public ResponseEntity<List<InsumoProjecionStatus>> getAllInputApprover(@ApiParam(value = "Identificador del estado del insumo por el que se desa filtrar")
            @PathVariable List<Integer> pIdStatus,
            @ApiIgnore UserLogged userLogged) {
        String pNit = userLogged.getNit();
        List<InsumoProjecionStatus> insumos = insumoService.findAllApprover(pIdStatus, pNit);
        if (insumos == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(insumos);
    }

    //Servicio para obtener insumo por estado
    @GetMapping(path = "/status/{pIdStatus}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene lista de insumos filtrado por estado")
    public ResponseEntity<List<InsumoProjecionStatus>> getAllInput(@ApiParam(value = "Identificador del estado del insumo por el que se desa filtrar")
            @PathVariable List<Integer> pIdStatus) {
        List<InsumoProjecionStatus> insumos = insumoService.findAll(pIdStatus);
        if (insumos == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(insumos);
    }

    @GetMapping(path = "/{idInput}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene un insumo")
    public ResponseEntity<InsumoComentarioDto> getInput(
            @ApiParam(value = "Identificador del insumo ")
            @PathVariable Integer idInput,
            @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(this.insumoService.findByIdInsumoComentario(idInput, user));
    }

    @PutMapping(path = "/{idInput}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "actualiza un insumo", notes = "actualiza insumo")
    //@Validated(OnCreate.class)
    public ResponseEntity<InsumoDto> putFiscalProgram(@PathVariable Integer idInput, @RequestBody InsumoDto pInputDto, @ApiIgnore UserLogged user) {
        return this.insumoService.modifyInput(pInputDto, idInput, user);
    }

    @PutMapping(path = "/definitive/rejection/{idInput}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<InsumoDto> definitiveRejectionInput(
            @PathVariable Integer idInput,
            @RequestBody InsumoDto pInputDto,
            @ApiIgnore UserLogged user) {
        return this.insumoService.declineInput(pInputDto, idInput, user);
    }//correct

    @PutMapping(path = "/fix/{idInput}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<InsumoDto> correctInput(
            @PathVariable Integer idInput,
            @RequestBody InsumoDto pInputDto,
            @ApiIgnore UserLogged user) {
        return this.insumoService.correctInput(pInputDto, idInput, user);
    }

    @PutMapping(path = "/assignment/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Asigna un insumo ", notes = "actualiza insumo")
    public ResponseEntity<InsumoDto> asignaInput(@PathVariable Integer id, @RequestBody InsumoDto pParamDto, @ApiIgnore UserLogged user) {
        //return this.insumoService.modifyInput(pInputDto, idInput);
        //log.error("parametros " + pParamDto);
        pParamDto.setIdEstado(180);// estado de asignado a jefe de unidad 
        this.insumoService.assignmentInsumo(pParamDto, id, user);
        return ResponseEntity.ok(pParamDto);
    }

    @PutMapping(path = "/assignment/case/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Asigna casos a los operadores", notes = "actualiza insumo")
    public ResponseEntity<ParametroAccionInsumoDto> asignaCases(@PathVariable Integer id, @RequestBody ParametroAccionInsumoDto pParamDto, @ApiIgnore UserLogged user) {
        //return this.insumoService.modifyInput(pInputDto, idInput);
        //log.error("parametros " + pParamDto);

        this.casosService.assignmentCases(pParamDto, user, id);

        return ResponseEntity.ok(pParamDto);
    }

    @PutMapping(path = "/rejection/definitive/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "rechazo definitivo", notes = "rechazo definitivo de un insumo ")

    public ResponseEntity<InsumoDto> definitiveRejection(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        return this.insumoService.changeStatus(id, 440, 189, pBody, user);// estado de rechazo definitivo para un insumo
    }

    @PutMapping(path = "/rejection/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "rechazo de insumo", notes = "rechazo de un insumo ")

    public ResponseEntity<InsumoDto> rejection(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        return this.insumoService.changeStatus(id, 179, 190, pBody, user);// estado de rechazo para un insumo
    }

    @PutMapping(path = "/suspend/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "suspension de insumo", notes = "suspende un insumo")

    public ResponseEntity<InsumoDto> suspend(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        return this.insumoService.changeStatus(id, 439, 191, pBody, user);// estado de rechazo para un insumo
    }

    @PutMapping(path = "/correct/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "correcion de insumo", notes = "solicita corregir un insumo")

    public ResponseEntity<InsumoDto> correct(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        return this.insumoService.changeStatus(id, 179, 192, pBody, user);// estado de rechazo para un insumo
    }

    @PutMapping(path = "/publish/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "correcion de insumo", notes = "solicita corregir un insumo")

    public ResponseEntity publish(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        this.insumoService.publish(id, user);
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/delete/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "correcion de insumo", notes = "solicita corregir un insumo")
    public ResponseEntity<InsumoDto> delete(@PathVariable Integer id, @RequestBody(required = false) ParametroAccionInsumoDto pBody, @ApiIgnore UserLogged user) {
        return this.insumoService.deleteInput(id, Catalog.Input.Status.DELETED, 188, pBody, user);// 188 comentario de eliminado
    }

}
