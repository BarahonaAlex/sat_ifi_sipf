/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.google.gson.Gson;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PresenciasFiscalesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.PresenciasFiscalesProjetion;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.services.PresenciasFiscalesService;
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
 * @author jdaldana
 */
@Api(tags = {"Presencias Fiscales"})
@Validated
@RestController
@Slf4j
@RequestMapping("/tax/presences")
public class PresenciasFiscalesController {

    @Autowired
    PresenciasFiscalesService claseService;

    @Autowired
    ConsumosService consumosService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los formularios", notes = "Retorna una lista de formularios")
    ResponseEntity<List<PresenciasFiscalesProjetion>> FindTaxAll(@ApiIgnore UserLogged logged) {
        return ResponseEntity.ok(claseService.FindPresenciasAll(logged.getNit()));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de una presencia ")
    ResponseEntity<PresenciasFiscalesDto> CreateTax(@RequestBody PresenciasFiscalesDto data, @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.CreateFormulario(data, user));
    }

    @GetMapping(path = "/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene solo una presencia")
    ResponseEntity<PresenciasFiscalesProjetion> FindTaxById(
            @ApiParam(value = "Identificador de la presencia")
            @PathVariable int id) {
        return ResponseEntity.ok(claseService.FindIdPresencia(id));
    }

    @PostMapping(path = "/start/process", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un proceso una presencia ")
    ResponseEntity<PresenciasFiscalesDto> CreateProcessTax(
            @RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> file,
            @ApiIgnore UserLogged user) {
        log.debug("entra?");
        return ResponseEntity.ok(claseService.startProcessPresencias(new Gson().fromJson(data, PresenciasFiscalesDto.class),file, user));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Modifica el registro de un proceso una presencia ")
    ResponseEntity<?> updateTax(
             @ApiParam(value = "Identificador de la presencia")
            @PathVariable Integer id,
            @RequestBody PresenciasFiscalesDto data,
            @ApiIgnore UserLogged user) {
        return ResponseEntity.ok(claseService.updatePresencias(id,data, user));
    }
     @PutMapping(path = "process/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Modifica el registro de un proceso una presencia ")
    ResponseEntity<?> updateProcessTax(
             @ApiParam(value = "Identificador de la presencia")
            @PathVariable Integer id,
          @RequestPart("data") String data,
            @RequestPart("file") List<MultipartFile> file,
            @ApiIgnore UserLogged user) {
        
        return ResponseEntity.ok(claseService.updateProcessPresencias(id,new Gson().fromJson(data, PresenciasFiscalesDto.class),file, user));
    }

}
