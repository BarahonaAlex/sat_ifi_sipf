/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorPerfilDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ProfileDetailDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.PerfilProjections;
import gt.gob.sat.sat_ifi_sipf.services.PerfilService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author ruano
 */
@Api(tags = {"Perfiles"})
@Validated
@RestController
@Slf4j
@RequestMapping("/perfil")
public class PerfilController {

    @Autowired
    private PerfilService perfilservice;

    @Autowired
    Detector detector;

    @GetMapping(path = "/role/{pNombreRol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de perfiles filtrador por el nombre del rol del OID.")
    public ResponseEntity<List<PerfilProjections>> getPerfilByRole(@PathVariable String pNombreRol) {
        return ResponseEntity.ok(perfilservice.getPerfilesByName(pNombreRol));
    }

    @GetMapping(path = "/{pUser}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de perfiles filtrador por el nombre de usuario.")
    public ResponseEntity<List<PerfilProjections>> getperfil(@PathVariable String pUser,
            @ApiIgnore UserLogged user
    ) {
        return ResponseEntity.ok(perfilservice.getperfiles(pUser));
    }

    // GET para el repository de Colaborador pefil
    @GetMapping(path = "/nit/{pNitColaborador}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen listado de perfiles que tiene asignado en el momento el colaborador.")
    public ResponseEntity<List<PerfilProjections>> getperfilnit(@PathVariable String pNitColaborador) {
        return ResponseEntity.ok(perfilservice.getperfil(pNitColaborador));
    }

    // GET para el repository de Colaborador pefil
    @GetMapping(path = "/logged/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen listado de perfiles que tiene asignado en el momento el usuario loggeado.")
    public ResponseEntity<List<PerfilProjections>> getperfilnit(@ApiIgnore UserLogged user) {
        return ResponseEntity.ok(perfilservice.getperfil(user.getLogin()));
    }

    @PostMapping(path = "/colaborador", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Asigna un perfil a un colaborador especifico.")
    public ResponseEntity<Boolean> crateColaboradorPerfil(@RequestBody ColaboradorPerfilDto perfil) {
        return ResponseEntity.ok(perfilservice.createColaboradorPerfil(perfil));
    }

    @PutMapping(path = "/colaborador", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Asigna un perfil a un colaborador especifico.")
    public ResponseEntity<?> deleteColaboradorPerfil(@RequestBody ColaboradorPerfilDto perfil) {
        return ResponseEntity.ok(perfilservice.deleteColaboradorPerfil(perfil));
    }

    @GetMapping(path = "/login/role", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Obtiene el listado de perfiles en base al rol de oid")
    public ResponseEntity<List<PerfilProjections>> getProfileByLoginAndRole(@RequestParam String login, @RequestParam String role) throws Exception {
        return ResponseEntity.ok(perfilservice.getProfilesByLogin(login, role, false).getProfiles());
    }

    @GetMapping(path = "/login/role/detail", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Obtiene el listado de perfiles en base al rol de oid")
    public ResponseEntity<ProfileDetailDto> getProfileByLoginAndRoleDetail(
            @RequestParam String login, @RequestParam String role,
            @RequestParam(defaultValue = "false") boolean operator
    ) throws Exception {
        return ResponseEntity.ok(perfilservice.getProfilesByLogin(login, role, operator));
    }
}
