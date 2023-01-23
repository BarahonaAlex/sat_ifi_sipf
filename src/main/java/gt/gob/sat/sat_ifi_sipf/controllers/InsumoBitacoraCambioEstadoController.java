/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.SipfInsumoBitacoraCambioEstadoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.services.InsumoBitacoraCambioEstadoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author rabaraho
 */
@Api(tags = {"Insumo bitacora cambio de estado"})
@Validated
@RestController
@Slf4j
@RequestMapping("/input/change/state/logbook")
public class InsumoBitacoraCambioEstadoController {

    @Autowired
    private InsumoBitacoraCambioEstadoService insumoBitacoraCambioEstadoService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un registro de cambio de estado de un insumo", notes = "crea un registro de cambio de estado de un insumo")
    //@Validated(OnCreate.class)
    public ResponseEntity<?> postLogbookRegistri(@RequestBody SipfInsumoBitacoraCambioEstadoDto pData,
            @ApiIgnore UserLogged user) {
        pData.setUsuarioModifica(user.getNit());
        pData.setFechaModifica(new Date());
        return this.insumoBitacoraCambioEstadoService.createRegistriChangeOfState(pData);

    }

}
