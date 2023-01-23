/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfParametroSistema;
import gt.gob.sat.sat_ifi_sipf.services.ParametroSistemaService;
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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author adftopvar
 */
@Api(tags = {"Parametros del Sistema"})
@Validated
@RestController
@Slf4j
@RequestMapping("/system/params")
public class ParametroSistemaController {

    @Autowired
    ParametroSistemaService parametroSistemaService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen todos los parametros del sistema.")
    public ResponseEntity<List<SipfParametroSistema>> getSistemParams() {
        return ResponseEntity.ok(parametroSistemaService.getParams());
    }

    @PutMapping(path = "/{id}", consumes = MediaType.TEXT_PLAIN_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Actualiza un parametro del sistema de acuerdo a los datos proveídos.")
    public void updateSistemParam(
            @ApiParam(value = "Identificador del parametro del sistema")
            @PathVariable Integer id,
            @RequestBody String value,
            @ApiIgnore UserLogged logged) {
        parametroSistemaService.updateParam(id, value, logged);
    }
}
