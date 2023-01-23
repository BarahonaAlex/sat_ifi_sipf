/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.utils.UriIgnore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author crramosl
 */
@Api(tags = {"Usuarios"})
@Validated
@RestController
@Slf4j
@RequestMapping("/users")
public class UserController {

    @Autowired
    Detector detector;

    @Autowired
    ConsumosService consumesService;

    @GetMapping(path = "/logged", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene información general del usuario con la sesión activa.")
    @UriIgnore
    public UserLogged getUserLogged(@ApiIgnore UserLogged logged) {
        logged.setAllowedUris(null);
        logged.setRoles(null);
        return logged;
    }

    @GetMapping(path = "/logged/image", produces = MediaType.IMAGE_JPEG_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene información general del usuario con la sesión activa.")
    public ResponseEntity<byte[]> getUserImage(@ApiIgnore UserLogged logged) {
        try {
            HttpHeaders headers = consumesService.getProsisHeaders();
            headers.setAccept(Arrays.asList(MediaType.IMAGE_JPEG));
            String ruta = consumesService.getConfig().getPingUrlWsSqlServer();
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(consumesService.consume(
                            null,
                            ruta + "user/image/" + logged.getNit(),
                            byte[].class,
                            HttpMethod.GET,
                            headers
                    ));
        } catch (Exception ignored) {
            return null;
        }
    }
}
