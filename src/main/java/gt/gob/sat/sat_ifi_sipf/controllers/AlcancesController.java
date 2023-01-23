package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.AlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcancesMasivosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AlcancesMasivosVersionesDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosMasivosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciasPuntosFijosPresenciasComentarioDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GenerationPdfDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PresenciasComentariosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SeccionesCasoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcancesMasivosVersiones;
import gt.gob.sat.sat_ifi_sipf.models.SipfSeccionesCaso;
import gt.gob.sat.sat_ifi_sipf.projections.AlcancesMasivosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CarteraCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CasosAndAlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SeccionesCasoProjection;
import gt.gob.sat.sat_ifi_sipf.services.AlcancesService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author ruarcuse
 */
@Api(tags = {"Alcances"})
@Slf4j
@Validated
@RestController
@RequestMapping("/scope/massive")
public class AlcancesController {

    @Autowired
    private AlcancesService alcancesService;

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un nuevo alcance masivo")
    public ResponseEntity<SipfAlcancesMasivosVersiones> createScope(
            @ApiParam(value = "Datos del alcance masivo segun Item de alcance masivo")
            @RequestBody AlcancesMasivosDto dto
    ) {
        return ResponseEntity.ok(alcancesService.createScope(dto));
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Modifica un alcance masivo, y posteriormente crea una nueva version del mismo")
    public void modifyScope(
            @ApiParam(value = "Datos del alcance masivo segun Item de alcance masivo")
            @RequestBody(required = true) AlcancesMasivosVersionesDto dto
    ) {
        alcancesService.modifyScopeVersion(dto);
    }

    @PutMapping(path = "/version",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Cambia el estado en el que se encuentra una version del alcance masivo")
    public void changeMassiveVersionStatus(
            @ApiParam(value = "version del alcance")
            @RequestParam Integer ver,
            @ApiParam(value = "Id del alcance masivo")
            @RequestParam Integer idMassiveScope
    ) {
        alcancesService.changeVersionStatus(ver, idMassiveScope);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todos los alcances masivos en general")
    public ResponseEntity<List<AlcancesMasivosProjection>> getMassiveScope() {
        return ResponseEntity.ok(alcancesService.getMassiveScope());
    }

    @GetMapping(path = "/version/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las versiones en funcion del alcance masivo seleccionado")
    public ResponseEntity<List<SipfAlcancesMasivosVersiones>> getVersionsByMassiveScope(
            @ApiParam(value = "Id del alcance masivo")
            @PathVariable Integer id
    ) {
        return ResponseEntity.ok(alcancesService.getVersionsByMassiveScope(id));
    }

    @PutMapping(path = "/request",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Asigna, modifica y elimina un alcance masivo a una solicitud masiva")
    public void assignModifyAndDeleteMassiveScope(
            @ApiParam(value = "Cuerpo de la peticion")
            @RequestBody CasosMasivosDto dto,
            @ApiParam(value = "Tipo de transaccion")
            @RequestParam Integer op
    ) {
        alcancesService.assignModifyAndDeleteMassiveScope(dto, op);
    }

    @GetMapping(path = "/reaches/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todos los casos de tipo masivo, con su alcance asignado si lo tiene")
    public ResponseEntity<List<CasosAndAlcanceProjection>> getReachAndMassiveScope(
            @PathVariable Integer nit
    ) {
        return ResponseEntity.ok(alcancesService.findReachAndMassiveScope(nit));
    }

    @PostMapping(path = "/section/case",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea una sección para un caso")
    public ResponseEntity<SipfSeccionesCaso> createSectionCase(
            @RequestBody(required = true) SeccionesCasoDto section
    ) {
        return ResponseEntity.ok(alcancesService.createSectionCase(new SipfSeccionesCaso(section)));
    }

    /*
    @GetMapping("/section/{idCaso}")
    @ApiOperation(value = "Obtiene la sección que ya esta creada o no creada.")
    public ResponseEntity<SipfSeccionesCaso> getSectionCase(
            @PathVariable Integer idCaso) {
        return ResponseEntity.ok(alcancesService.getSectionCase(idCaso));
    }
     */
    @GetMapping("/section/{idCaso}")
    @ApiOperation(value = "Obtiene la sección que ya esta creada o no creada y su comentario.")
    public ResponseEntity<List<SeccionesCasoProjection>> getSection(
            @PathVariable Integer idCaso) {
        return ResponseEntity.ok(alcancesService.getSectionAndComentary(idCaso));
    }

    @PostMapping(path = "/generation/file",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_PDF_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Crear archivo pdf")
    public ResponseEntity<  byte[]> createPdf(
            @RequestBody(required = true) GenerationPdfDto data, @ApiIgnore UserLogged user) throws IOException {

        return ResponseEntity.ok().contentType(MediaType.APPLICATION_PDF).body(alcancesService.generacionPdf(data, user));
    }

    @DeleteMapping(path = "delete/generation/file")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Elimina archivo pdf")
    public ResponseEntity<?> deletePdf() throws IOException {
        alcancesService.deleteDocument();
        return ResponseEntity.ok().build();
    }

    @PutMapping(path = "/authorizer/file/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del caso", notes = "Se cambia el estado del caso")
    public ResponseEntity<Boolean> autorizerFile(
            @ApiParam(value = "Identificador del caso")
            @PathVariable int id, @ApiIgnore UserLogged user, @RequestBody GenerationPdfDto data) throws IOException {
        return ResponseEntity.ok(alcancesService.changeState(id, data, user));
    }

    @PostMapping(path = "/general", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un nuevo alcance masivo")
    public ResponseEntity<AlcanceDto> createAlcance(
            @ApiParam(value = "Datos del alcance")
            @RequestBody AlcanceDto dto, @ApiIgnore UserLogged userLogged) {
        return ResponseEntity.ok(alcancesService.createAlcance(dto, userLogged));
    }

    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/points/presence/cabinet/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getPointsPresenceCabinet(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getPointsPresenceCabinet(idalcance));
    }
    
    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/presence/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getPresence(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getPresence(idalcance));
    }
    
    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/cabinet/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getCabinet(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getGabinet(idalcance));
    }
    
    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/points/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getPoint(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getPoint(idalcance));
    }

    @GetMapping(path = "/scopes/presence/{idAlcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances Presencia")
    public ResponseEntity<PresenciasComentariosDto> getScopesPresence(@PathVariable Integer idAlcance) {
        return ResponseEntity.ok(alcancesService.getAlcancesbyType(idAlcance,973));
    }
    
    @GetMapping(path = "/scopes/selectiva/{idAlcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances Presencia")
    public ResponseEntity<PresenciasComentariosDto> getScopesSelectiva(@PathVariable Integer idAlcance) {
        return ResponseEntity.ok(alcancesService.getAlcancesbyType(idAlcance,117));
    }
    
    @PutMapping(path = "/rechazar/alcance/{idalcance}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del alcance", notes = "Se cambia el estado del alcance")
    public ResponseEntity<Boolean> rechazarAlcance(
            @ApiParam(value = "Identificador del caso")
            @PathVariable Integer idalcance,@ApiIgnore UserLogged user, @RequestBody String comentario) {
        return ResponseEntity.ok(alcancesService.rechazarAlcance(idalcance,user,comentario));
    }
    
    @PutMapping(path = "/autorizar/ju/{idalcance}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del alcance", notes = "Se cambia el estado del alcance")
    public ResponseEntity<Boolean> autorizarrAlcanceJU(
            @ApiParam(value = "Identificador del idalcance")
            @PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.autorizarrAlcanceJU(idalcance));
    }
    
     @PutMapping(path = "/autorizar/jd/{idalcance}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se solicitan correcciones del alcance", notes = "Se cambia el estado del alcance")
    public ResponseEntity<Boolean> autorizarrAlcanceJD(
            @ApiParam(value = "Identificador del idalcance")
            @PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.autorizarrAlcanceJD(idalcance));
    }
    
    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/cabinetjd/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getCabinetJd(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getGabinetJd(idalcance));
    }
    
    /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/pointsjd/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getPointJd(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getPointJd(idalcance));
    }
    
     /**
     * @author Gabriel ruano (garuanom)
     * @return
     */
    @GetMapping(path = "/presencejd/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de Gabinete, puntos fijo o presencia")
    public ResponseEntity<List<BandejaAlcanceProjection>> getPresenceJd(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getPresenceJd(idalcance));
    }
     /**
     * @author José Aldana (jdaldana)
     * @return
     */
    @GetMapping(path = "/presencias/puntosFijos/{idalcance}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances de denuncias de puntos fijo y presencia")
    public ResponseEntity<DenunciasPuntosFijosPresenciasComentarioDto> getAlcanceDenunciaPuntosFijos(@PathVariable Integer idalcance) {
        return ResponseEntity.ok(alcancesService.getAlcancePuntosPresencias(idalcance));
    }
    
    @GetMapping(path = "/states",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los casos en base a estados", notes = "Retorna los casos en base a estado")
    public ResponseEntity<List<CarteraCasosProjection>> getSelectScope (
            @ApiParam(value = "Estado de los casos")
            @RequestParam List<Integer> states
    ) {
        List<CarteraCasosProjection> selectScope = alcancesService.getSelectScope(states);
        return ResponseEntity.ok(selectScope);
    }
}
