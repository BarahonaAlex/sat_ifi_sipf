/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.GruposTrabajoDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GruposTrabajoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SolicitudTrasladoColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfGrupoTrabajo;
import gt.gob.sat.sat_ifi_sipf.models.SipfTrasladoColaborador;
import gt.gob.sat.sat_ifi_sipf.projections.GruposTrabajoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.OperatorProjection;
import gt.gob.sat.sat_ifi_sipf.services.GruposTrabajoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import javax.validation.Valid;
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
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudTrasladoColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.services.ContiendoACSService;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.util.HashMap;
import java.util.Map;
import org.json.simple.JSONObject;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author crramosl
 */
@Api(tags = {"Grupos de Trabajo"})
@Validated
@RestController
@Slf4j
@RequestMapping("/workgroups")
public class GruposTrabajoController {

    @Autowired
    GruposTrabajoService groupService;

    @Autowired
    Detector detector;
    
    @Autowired
    ContiendoACSService contiendoACSService;
    
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todos los equipos de trabajo creados.")
    public List<GruposTrabajoProjection> getWorkGroups(
            @ApiParam(value = "Incluye a los equipos eliminados")
            @RequestParam(defaultValue = "false") boolean includeDeleted
    ) {
        return groupService.getWorkGroups(includeDeleted);
    }

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene un equipo de trabajo creados en base a su id.")
    public GruposTrabajoDetalleDto getGroupById(
            @ApiParam(value = "Identificador del grupo de trabajo")
            @PathVariable Integer id
    ) {
        GruposTrabajoDetalleDto detalle = groupService.getGroupById(id);
        if (detalle.getEquipoTrabajo() == null || detalle.getIntegrantes().isEmpty()) {
            throw BusinessException.notFound(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return detalle;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un equipo de trabajo de acuerdo a los datos proveídos.")
    public SipfGrupoTrabajo createWorkGroup(@RequestBody @Valid GruposTrabajoDto group, @ApiIgnore UserLogged userLogged) {
        return groupService.createWorkGroup(group, userLogged);
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Actualiza un equipo de trabajo en base a los datos proveidos.")
    public void updateWorkGroup(@ApiParam(value = "Identificador del equipo de trabajo")
            @PathVariable Long id, @RequestBody GruposTrabajoDto group, @ApiIgnore UserLogged userLogged) {
        groupService.updateWorkGroup(id, group, userLogged);
    }

    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Elimina un equipo de trabajo en base al id.")
    public void deleteWorkGroup(
            @ApiParam(value = "Identificador del equipo de trabajo")
            @PathVariable(required = true) Long id, @ApiIgnore UserLogged userLogged) {
        if (!groupService.deleteGroup(id, userLogged)) {
            throw BusinessException.notFound(detector.getLocaleMessage("usuario.noexiste.message"));
        }
    }

    //Controlador que obtiene solicitud de traslado de colaborador a otro equipo de trabajo
    @GetMapping(path = "/transfer/request/member", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene solicitudes de traslado de colaborador mediante el login")
    public ResponseEntity<List<SolicitudTrasladoColaboradorProjection>> getRequestTransferColaboratorByLoginController(
            @ApiParam(value = "Login de aprobador")
            @ApiIgnore UserLogged userLogged) {
        List<SolicitudTrasladoColaboradorProjection> unit = groupService.getRequestTransferColaboratorByLogin(userLogged);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    //COntrolador para modificar la tabla de integrante grupo al momento de realizar un traslado
    @PutMapping(path = "/approve/transfer/request/{idSoli}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador para aprobar la solicitud de trasnsferencia")
    public Boolean updateTrasferCollaborator(
            @PathVariable Integer idSoli,
            @RequestBody(required = false) Object body,
            @ApiIgnore UserLogged userLogged
    ) {
        try {
            groupService.updateTransferColaborator(idSoli, userLogged);
            return true;
        } catch (Exception e) {
            return false;
        }

    }
    
    //Controlador para rechazar una solicitud de traslado con comentario
    @PutMapping(path = "/decline/transfer/request/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Se rechaza la solicitud", notes = "Se cambia el estado de la solicitud")
    public ResponseEntity<Boolean> approverDeclineRequest(
            @ApiParam(value = "Identificador de la solicitud")
            @PathVariable int id, 
            @ApiIgnore UserLogged user, 
            @RequestBody String comentario) {
        return ResponseEntity.ok(groupService.alterStateRequestWithComentary(id, 1024, 193, user, comentario, 1024));
    }

    //Controlador que obtiene solicitud de traslado de colaborador a otro equipo de trabajo
    @GetMapping(path = "/transfer/request/{idSolicitud}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene solicitud de traslado de colaborador a otro equipo de trabajo")
    public ResponseEntity<List<SolicitudTrasladoColaboradorProjection>> getRequestTransferColaboratorByIdSController(
            @ApiParam(value = "id de la solicitud")
            @PathVariable Integer idSolicitud) {
        List<SolicitudTrasladoColaboradorProjection> unit = groupService.getRequestTransferColaboratorById(idSolicitud);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    //Metodo para  Crear una solicitud de traslado de colaborador a otro equipo de trabajo
    @PostMapping(path = "/transfer/request", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea una solicitud de traslado de colaborador a otro equipo de trabajo")
    public ResponseEntity<?> createTransferCollaborator(@RequestBody SolicitudTrasladoColaboradorDto dto) {
        
        //log.debug(dto.getFechaEfectivaTraslado().toString());
        //log.debug(dto.getFechaEfectivaRetorno().toString());
        //return ResponseEntity.noContent().build();
        return ResponseEntity.ok(groupService.createRequestTransferCollaborator(dto));
    }
    
    @PostMapping(path = "/multipart/transfer/request",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "guarda una nueva denuncia", notes = "crear denuncia")
    @Transactional
    public ResponseEntity saveNew(@RequestPart("file") List<MultipartFile> file,
            @RequestPart("data") String data, @ApiIgnore UserLogged user) {
        SolicitudTrasladoColaboradorDto dto = new Gson().fromJson(data, SolicitudTrasladoColaboradorDto.class);
        log.debug(data);
        log.debug(dto.getFechaEfectivaTraslado().toString());
        log.debug(dto.getFechaEfectivaRetorno().toString());
        
        SipfTrasladoColaborador savedRequest = groupService.createRequestTransferCollaborator(dto);
        
        NodosACSDto nodos;
        Map<String, Object> post = new HashMap<>();
        post.put("nitOperador", savedRequest.getNitProfesional());
        post.put("solicitudTraslado", savedRequest.getIdSolicitud());
        
        JSONObject json = new JSONObject(post);
        
        nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.TRASLADO, json);
        
        contiendoACSService.uploadFiles(nodos.getId(), file);
        
        return ResponseEntity.ok(savedRequest);
    }

    //Controlador que obtiene miembro operador sus equipos y unidades
    @GetMapping(path = "/member", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene miembro operador sus equipos y unidades")
    public ResponseEntity<List<OperatorProjection>> getOperatorTeamsAndUnits() {
        List<OperatorProjection> unit = groupService.getOperatorTeamsAndUnits();
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    @GetMapping(path = "/member/operator/{nitOperator}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene")
    public ResponseEntity<List<OperatorProjection>> getTeamsAndUnitsByNitOperator(
            @ApiParam(value = "nit del operador")
            @PathVariable String nitOperator) {
        List<OperatorProjection> unit = groupService.getTeamsAndUnitsByNitOperator(nitOperator);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }

    //Controlador que obtiene solicitud de traslado de colaborador a otro equipo de trabajo
    @GetMapping(path = "/transfer/request/member/authorizer/{nitAuthorizer}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador que obtiene solicitudes de traslado de colaborador mediante el login")
    public ResponseEntity<List<SolicitudTrasladoColaboradorProjection>> getRequestTransferColaboratorByNitAuthorizer(
            @ApiParam(value = "Login de aprobador")
            @PathVariable String nitAuthorizer) {
        List<SolicitudTrasladoColaboradorProjection> unit = groupService.getRequestTransferColaboratorByNitAuthorizer(nitAuthorizer);
        if (unit == null) {
            throw new ResourceNotFoundException(detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unit);
    }
    
    //Controlador que verifica si el operador cuenta con una solicitud de traslado
    @GetMapping(path = "/transfer/request/exists/{nitOperator}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador para verificar existencia de solicitud de traslado para un operador")
    public Boolean getExistenceTrasferRequest(
            @PathVariable String nitOperator) {
        Integer unit = groupService.getRequestTransferExists(nitOperator);
        System.out.println("Si entra al metodo");
        System.out.println(unit);
        if(unit == null){
            return false;
        }else{
            return true;
        }

    }
}
