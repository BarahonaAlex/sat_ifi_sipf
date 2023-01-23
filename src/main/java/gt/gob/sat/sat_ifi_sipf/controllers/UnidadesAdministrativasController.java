/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.UnidadesAdministrativasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfUnidadAdministrativa;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.UnidadAdministrativaProjection;
import gt.gob.sat.sat_ifi_sipf.services.UnidadesAdministrativasService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 * Clase que contiene los métodos para consumo de unidades administrativas.
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 06/01/2022
 * @version 1.0
 */
@Api(tags = {"Unidades Administrativas"})
@Validated
@RestController
@RequestMapping("/administrative/units")
@Slf4j
public class UnidadesAdministrativasController {

    @Autowired
    UnidadesAdministrativasService unidadesAdministrativasService;

    @Autowired
    Detector detector;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea una unidad administrativa", notes = "Retorna la unidad administrativa creada")
    public ResponseEntity<SipfUnidadAdministrativa> createAdministrativeUnit(@RequestBody UnidadesAdministrativasDto pUnit, @ApiIgnore UserLogged user) {
        log.debug(""+pUnit.getIdTipoProgramacion());
        //return ResponseEntity.ok().build();
        return ResponseEntity.ok(unidadesAdministrativasService.createAdministrativeUnit(new SipfUnidadAdministrativa(pUnit),user));
    }

    @GetMapping(path = "/fathers",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las unidas administrativas padres registradas", notes = "Retorna todas las unidas administrativas padres registradas")
    public ResponseEntity<List<UnidadAdministrativaProjection>> getAdministrativeUnitsFather(
            @ApiParam(value = "Estados de las unidades administrativas")
            @RequestParam List<Integer> pStatus) {
        List<UnidadAdministrativaProjection> units = unidadesAdministrativasService.getAdministrativeUnitsFather(pStatus);
        if (units == null || units.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(units);
    }

    @GetMapping(path = "/children/father/prosis/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las unidas administrativas en base al id del padre de prosis", notes = "Retorna todas las unidas administrativas padres registradas")
    public ResponseEntity<List<CatalogoHijoProjection>> getUnitsByFatherProsis(
            @ApiParam(value = "Estados de las unidades administrativas")
            @PathVariable Integer id) {
        List<CatalogoHijoProjection> units = unidadesAdministrativasService.getUnitByParentProsis(id);
        if (units == null || units.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(units);
    }

    @GetMapping(path = "/children/father/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las unidades administrativas hijas en base a su id padre", notes = "Retorna todas las unidades administrativas hijas en base a su id padre")
    public ResponseEntity<List<UnidadAdministrativaProjection>> getAdministrativeUnitsByIdFather(
            @ApiParam(value = "Identificador de unidad administrativa padre")
            @PathVariable long id,
            @ApiParam(value = "Estados de las unidades administrativas")
            @RequestParam List<Integer> pStatus) {
        List<UnidadAdministrativaProjection> units = unidadesAdministrativasService.getAdministrativeUnitsByIdFather(id, pStatus);
        if (units == null || units.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(units);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las unidas administrativas registradas", notes = "Obtiene todas las unidas administrativas registradas")
    public ResponseEntity<List<UnidadAdministrativaProjection>> getAllAdministrativeUnits(
            @ApiParam(value = "Estados de las unidades administrativas")
            @RequestParam List<Integer> pStatus) {
        List<UnidadAdministrativaProjection> units = unidadesAdministrativasService.getAllAdministrativeUnits(pStatus);
        if (units == null || units.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(units);
    }

    @PutMapping(path = "/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Actualiza una unidad administrativa", notes = "Retorna la unidad administrativa actualizada")
    public void alterAdministrativeUnit(@RequestBody UnidadesAdministrativasDto pUnit,
            @ApiParam(value = "Identificador de unidad administrativa padre")
            @PathVariable long id,
            @ApiIgnore UserLogged user) {
        unidadesAdministrativasService.alterAdministrativeUnit(pUnit, id,user);
    }

    //Controlador que obtiene un aprobador mediante un nit y devuelve equipos y unidades administrativas
    @GetMapping(path = "/rol/{pIdRol}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene las unidades hijas de las unidades en las que se encuentra el colaborador con un rol especifico")
    public ResponseEntity<List<UnidadAdministrativaProjection>> getUnitsByNitAndRol(
            @ApiParam(value = "Identificador del rol que se desea filtrar")
            @PathVariable Integer pIdRol,
            @ApiIgnore UserLogged logged
    ) {
        List<UnidadAdministrativaProjection> unit = unidadesAdministrativasService.getUnitsByNitAndRol(logged.getNit(), pIdRol);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    @GetMapping(path = "/prosis", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las unidades administrativas registradas en prosis")
    public ResponseEntity<Boolean> getUnitFromProsis(@ApiIgnore UserLogged user) {
        return ResponseEntity.ok().body(unidadesAdministrativasService.mergeInfoUnitFromProsis(user));
    }

}
