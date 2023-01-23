/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.AsignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.BaseColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.EmpleadoFromProsisDto;
import gt.gob.sat.sat_ifi_sipf.dtos.HisotrialEstadoColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ReasignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialEstadoColaborador;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresGerenciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignarCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.EquiposYUnidadesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GrupoColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacioCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SupervidoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.TrasladoColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.services.ColaboradorService;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.services.ContiendoACSService;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author ruarcuse
 */
@Api(tags = {"Colaboradores"})
@Validated
@RestController
@Slf4j
@RequestMapping("/collaborators")
public class ColaboradorController {

    @Autowired
    ColaboradorService colaboradorService;

    @Autowired
    Detector detector;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ContiendoACSService contiendoACSService;

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen todos los colaboradores creados.")
    public ResponseEntity<List<ColaboradoresProjection>> getCollaborators(@ApiIgnore UserLogged logged) {
        return ResponseEntity.ok(colaboradorService.getCollaborators(logged));
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un colaborador en base al id.")
    public ResponseEntity<ColaboradoresProjection> getCollaborator(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String id) {
        ColaboradoresProjection unit = colaboradorService.getCollaboratorId(id);
        if (unit == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    @GetMapping(path = "/management/{idmanagement}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todo los colaboradores en base a IdGerencia.")
    public ResponseEntity<List<ColaboradoresGerenciaProjection>> obtenerColaboradores(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable Integer idmanagement) {
        List<ColaboradoresGerenciaProjection> collaborators = colaboradorService.managementCollaborator(idmanagement);

        return ResponseEntity.ok(collaborators);
    }

    @GetMapping(path = "/assignedcase/{nit}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obteniendo todos los insumos asignado al colaborador.")
    public ResponseEntity<List<ReasignacioCasosProjection>> getAssignedCase(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String nit) {
        List<ReasignacioCasosProjection> assignedCase = colaboradorService.assignedCases(nit);
        return ResponseEntity.ok(assignedCase);
    }

    @GetMapping(path = "/groupscollaborator",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obteniendo la lista de grupo donde pertece el colaborador")
    public ResponseEntity<List<GrupoColaboradoresProjection>> groupsCollaborator(
            @ApiParam(value = "Identificador del colaborador")
            @ApiIgnore UserLogged logged
    ) {
        /*if (logged.getName().equals("localtest")) {
            logged.setNit("103857508");
        } else if (logged.getRoles().contains("AdministrativoSIPFAdministrador")) {
            //log.debug("entro al if");
            List<GrupoColaboradoresProjection> collaboratingGroup = colaboradorService.getMembers();
            return ResponseEntity.ok(collaboratingGroup);
        }
        //log.debug("no entro al if" + logged.getRoles());*/
        List<GrupoColaboradoresProjection> collaboratingGroup = colaboradorService.getPartnerGroup(logged.getNit(), logged.getRoles());
        return ResponseEntity.ok(collaboratingGroup);
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un colaborador de acuerdo a los datos proveídos.")
    public ResponseEntity<?> createCollaborator(@RequestBody ColaboradorDto colaborador,
            @ApiIgnore UserLogged logged) {
        return ResponseEntity.ok(colaboradorService.createCollaborator(colaborador, logged));
    }

    @PostMapping(path = "/history", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un hisotrial cuando se actuliza,elimina o crea un colaborador.")
    public ResponseEntity<?> collaboratorHistory(@RequestBody HisotrialEstadoColaboradorDto history) {
        return ResponseEntity.ok(colaboradorService.saveHistory(history));
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Actualiza un colaborador de acuerdo a los datos proveídos.")
    public void updateCollaborator(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String id,
            @RequestBody BaseColaboradorDto colaborador,
            @ApiIgnore UserLogged logged) {

        colaboradorService.updateCollaborator(id, colaborador, logged);
    }

    @PostMapping(path = "/multipart/modify",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "guarda una nueva denuncia", notes = "crear denuncia")
    @Transactional
    public ResponseEntity saveNew(@RequestPart("file") List<MultipartFile> file,
            @RequestPart("data") String data, @ApiIgnore UserLogged user) {
        BaseColaboradorDto dto = new Gson().fromJson(data, BaseColaboradorDto.class);
        log.debug(data);
        log.debug("" + file.isEmpty());

        SipfHistorialEstadoColaborador modified = colaboradorService.updateCollaborator(dto.getIdColaborador(), dto, user);

        if (!file.isEmpty()) {
            NodosACSDto nodos;
            Map<String, Object> post = new HashMap<>();
            post.put("nitColaborador", modified.getNitColaborador());
            post.put("idModificacion", modified.getId());
            JSONObject json = new JSONObject(post);
            nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.MODIFICA_COLABORADOR, json);
            contiendoACSService.uploadFiles(nodos.getId(), file);
        }
        return ResponseEntity.noContent().build();
    }

    @PutMapping(path = "/reasignacion",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "reasigna el caso a un colaborador.")
    public boolean Reassign(@RequestParam List<Integer> idCaso, @RequestBody List<ReasignacionCasosDto> dto, @ApiIgnore UserLogged userLogged) {
        return colaboradorService.reassignmentCases(dto, idCaso);
    }

    @DeleteMapping(path = "/delete/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Elimina un colaborador en base al id.")
    public void deleteCollaborator(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable(required = true) String id,
            @ApiIgnore UserLogged logged) {
        colaboradorService.deleteCollaborator(id, logged);
    }

    @PostMapping(path = "/assignment/cases",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<?> assignmentCases(
            @ApiParam(value = "Asignacion de casos a los colaboradores ")
            @RequestBody AsignacionCasosDto asignacionDto, @ApiIgnore UserLogged userLogged) {
        return ResponseEntity.ok(colaboradorService.assignmentCases(asignacionDto, userLogged));
    }

    @GetMapping(path = "/supervisor", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen todos los supervidores creados.")
    public ResponseEntity<List<SupervidoresProjection>> supervidorList() {
        return ResponseEntity.ok(colaboradorService.getSupervisor());
    }

    //Controlador que obtiene colaboradores y su respectivo autorizador en base a nit
    @GetMapping(path = "/operator/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene colaboradores y su respectivo autorizador en base a nit")
    public ResponseEntity<List<TrasladoColaboradorProjection>> getOperatorTeams(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String nit, @ApiIgnore UserLogged user) {
        try {
            List<TrasladoColaboradorProjection> unit = colaboradorService.getCollaboratoAndAuthByNit(nit, user);
            return ResponseEntity.ok(unit);
        } catch (Exception err) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
    }

    @GetMapping(path = "/superior/{pPerfilJefe}/{pPerfil}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un colaborador en base al nit y perfil del usuario superior")
    public ResponseEntity<List<ColaboradoresProjection>> getCollaborator(
            @ApiParam(value = "Nit del usuario superior")
            @PathVariable Integer pPerfilJefe,
            @ApiParam(value = "perfil del usuario que necesite")
            @PathVariable Integer pPerfil, @ApiIgnore UserLogged logged) {
        List<ColaboradoresProjection> colaboratores = colaboradorService.getCollaboratorsByNitPerfilJefeandPerfil(logged.getNit(), pPerfilJefe, pPerfil);
        if (colaboratores == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(colaboratores);
    }

    @GetMapping(path = "/superior/general/{pNit}/{pPerfilJefe}/{pPerfil}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un colaborador en base al nit y perfil del usuario superior")
    public ResponseEntity<List<ColaboradoresProjection>> getCollaboratorGeneral(
            @ApiParam(value = "Nit del usuario superior")
            @PathVariable String pNit,
            @ApiParam(value = "perfil del usuario superior")
            @PathVariable Integer pPerfilJefe,
            @ApiParam(value = "perfil del usuario que necesite")
            @PathVariable Integer pPerfil) {
        List<ColaboradoresProjection> colaboratores = colaboradorService.getCollaboratorsByNitAndPefilGeneral(pNit, pPerfilJefe, pPerfil);
        if (colaboratores == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(colaboratores);
    }

    //Controlador que obtiene un aprobador mediante un nit y devuelve equipos y unidades administrativas
    @GetMapping(path = "/authorizer/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene un aprobador mediante un nit y devuelve equipos y unidades administrativas")
    public ResponseEntity<List<EquiposYUnidadesProjection>> geAuthorizertUnitsAndTeams(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String nit
    ) {
        List<EquiposYUnidadesProjection> unit = colaboradorService.getTeamsAndUnitsByNit(nit);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    ////profesionales
    @GetMapping(path = "/professional/{Nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de los profesionales de un atorizador o aprobador")
    public ResponseEntity<List<DesasignarCasosProjection>> getProfessional(
            @ApiParam(value = "Nit del usuario superior")
            @PathVariable String Nit) {
        List<DesasignarCasosProjection> profetional = colaboradorService.getProfetionalByNitCases(Nit);
        return ResponseEntity.ok(profetional);
    }

    ///profesionales desasignar
    @GetMapping(path = "/professional/unassign/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la informacion de un profesional para la desasignacion")
    public ResponseEntity<List<SupervidoresProjection>> professionalUnassign(
            @ApiParam(value = "Nit del usuario profesional")
            @PathVariable String nit) {
        List<SupervidoresProjection> profetionalUnassign = colaboradorService.getProfetionalDesasignar(nit);
        return ResponseEntity.ok(profetionalUnassign);
    }

    //profesionales reasignar
    @GetMapping(path = "/profetional/reasign/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la informacion de un profesional para la desasignacion")
    public ResponseEntity<List<ReasignCasesProjection>> professionalReasign(
            @ApiParam(value = "Nit del usuario profesional")
            @PathVariable String nit,
            @RequestParam(value = "pNitProfesional", required = false) String pNitProfesional,
            @RequestParam(value = "pNitContribuyente", required = false) String pNitContribuyente,
            @RequestParam(value = "idCaso", required = false, defaultValue = "") Integer idCaso,
            @ApiIgnore UserLogged logged) {

        return ResponseEntity.ok(colaboradorService.getCasesForUnassing(pNitProfesional, idCaso, pNitContribuyente, logged));
    }

    @GetMapping(path = "/informacion/prosis/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la informacion de un colaborador desde prosis")
    public ResponseEntity<EmpleadoFromProsisDto> getInfoFromProsis(@ApiParam(value = "Nit del colaborador")
            @PathVariable String pNit,
            @ApiIgnore UserLogged logged) {

        return ResponseEntity.ok(colaboradorService.getInfoFromProsis(pNit));
    }

    @GetMapping(path = "jobPosition/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el puesto de trabajo del colaborador y lo actualiza")
    public ResponseEntity<SipfColaborador> updateJobPosition(@ApiParam(value = "Nit del colaborador")
            @PathVariable String pNit,
            @ApiIgnore UserLogged logged) {

        return ResponseEntity.ok().body(colaboradorService.updateJobPosition(pNit, logged));
    }

    @GetMapping(path = "/subcolaboratores/{pLevel}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un colaborador en base al nit y perfil del usuario superior")
    public ResponseEntity<List<ColaboradorProjection>> getSubcolaboratoresByLevel(
            @ApiParam(value = "número de nivel al que se desea incluir en la lista de sub colaboradores")
            @PathVariable Integer pLevel,
            /*@ApiParam(value = "perfil del usuario que necesite")
            @PathVariable Integer pPerfil, */
            @ApiIgnore UserLogged logged) {
        List<ColaboradorProjection> colaboratores = colaboradorService.getSubColaboratoresBySuperior(logged, pLevel);
        if (colaboratores == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(colaboratores);
    }

    @GetMapping(path = "/getMembers",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obteniendo la lista de grupo donde pertece el colaborador")
    public ResponseEntity<List<GrupoColaboradoresProjection>> getMembers(
            @ApiParam(value = "Identificador del colaborador")
            @ApiIgnore UserLogged logged
    ) {
        List<GrupoColaboradoresProjection> collaboratingGroup = colaboradorService.getMembers();
        return ResponseEntity.ok(collaboratingGroup);
    }

    @GetMapping(path = "/subcolaboratoresgrups", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un colaborador en base al nit y perfil del usuario superior")
    public ResponseEntity<List<ColaboradoresProjection>> getColaboradorGrups(
            @ApiParam(value = "número de nivel al que se desea incluir en la lista de sub colaboradores")
            @ApiIgnore UserLogged logged) {
        return ResponseEntity.ok(colaboradorService.getOperadorGrups(logged));
    }

    @GetMapping(path = "/prosis/login/{login}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la información general del colaborador en base a su login desde prosis")
    public ResponseEntity<EmpleadoFromProsisDto> getGeneralInfoByLogin(
            @PathVariable String login) {
        return ResponseEntity.ok(colaboradorService.getGeneralInfoByLogin(login));
    }

    @GetMapping(path = "/hasWorkload/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Retorna verdadero si el colaborador tiene carga de trabajo")
    public boolean hasWorkload(@PathVariable String nit) {
        return colaboradorService.hasWorkload(nit);
    }
}
