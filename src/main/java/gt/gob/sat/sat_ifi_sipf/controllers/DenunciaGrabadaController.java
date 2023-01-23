/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gt.gob.sat.sat_ifi_sipf.dtos.AsignacionDenunciaParamDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaAlcanceDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaParamDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaDetalleRechazadoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DetalleDenunciaDto;
import gt.gob.sat.sat_ifi_sipf.projections.BandejaDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaAprobadaProjection;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DenunciaGuardadaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DetalleDenunciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GerenciasProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosDenunciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ProcesosMasivosProjections;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.services.ContiendoACSService;
import gt.gob.sat.sat_ifi_sipf.services.DenunciaColaboradorService;
import gt.gob.sat.sat_ifi_sipf.services.DenunciaGrabadaService;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
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
 * @author rabaraho
 */
@Api(tags = {"Denuncias"})
@Slf4j
@Validated
@RestController
@RequestMapping("/complaint")
public class DenunciaGrabadaController {

    @Autowired
    DenunciaGrabadaService denunciaGrabadaService;

    @Autowired
    DenunciaColaboradorService denunciaColaboradorService;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ContiendoACSService contiendoACSService;

    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene lista de insumos filtrado por estado")
    public List<DenunciaGuardadaProjection> getAllIComplaints() {

        return this.denunciaGrabadaService.getWhole();
    }

    @PostMapping(path = "/onpremise",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene las denuncias de on premise por rango de fecha", notes = "")
    @Transactional
    public ResponseEntity<List<DenunciaGuardadaProjection>> getPostOnPremise(@RequestBody DenunciaGrabadaParamDto dto) {
        //log.debug(" " + dto);
        GeneralResponseDto<List<DenunciaGrabadaDto>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/denuncia", GeneralResponseDto.class, HttpMethod.POST);

        // resultado.getData().forEach(item -> item.setEstado("991"));
        List<DenunciaGrabadaDto> denuncias = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<List<DenunciaGrabadaDto>>() {
        });

        // obtengo una lista de denuncias ya guardadas previamente en aws         
        // se transforma en DenunciaGrabadaDto
        List<DenunciaGrabadaDto> denunciasAws = new ObjectMapper().convertValue(
                this.denunciaGrabadaService.getComplaintByDateRange(dto),
                new TypeReference<List<DenunciaGrabadaDto>>() {
        });
        // se obtiene un listado donde estan las repetidas 
        List<DenunciaGrabadaDto> filteredList = denuncias.stream()
                .filter(denunciaOnPremise -> denunciasAws.stream()
                .anyMatch(denunciaAws -> StringUtils.endsWithIgnoreCase(
                denunciaOnPremise.getCorrelativo(),
                denunciaAws.getCorrelativo())
                ))
                .collect(Collectors.toList());
        // se remueven las repetidas para guardar solo las que hacen falta 
        denuncias.removeAll(filteredList);
        // si existen no repetidas se almacenan en aws 
        if (!denuncias.isEmpty()) {
            this.denunciaGrabadaService.saveAll(denuncias);
            //se agregan a la lista por retornar las que se acaban de guardar en aws 
            //denunciasAws.addAll(denuncias);
        }

        // en caso que no existan denuncias por guardar se retorna la lista que viene de aws 
        return ResponseEntity.ok(this.denunciaGrabadaService.getComplaintByDateRangeNotAssigned(dto));
        //return ResponseEntity.ok(filteredList);
    }

    @GetMapping(path = "/approved/complaints",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias que tienen estado APLICA en base a un NIT")
    public List<DenunciaAprobadaProjection> getDenunciasAprobadas(
            @ApiIgnore UserLogged user
    ) {
        return this.denunciaGrabadaService.getApplyComplaints(user.getNit());
    }

    @GetMapping(path = "/scope",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los alcances creados por el opeardor, en base a un NIT")
    public List<AlcanceDenunciaProjection> getScope(
            @ApiIgnore UserLogged user
    ) {
        return this.denunciaGrabadaService.getScope(user.getNit());
    }

    @GetMapping(path = "/approved/complaints/scope",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias en base a su Gerencia y Proceso Masivo")
    public List<DenunciaAprobadaProjection> getDenunciasAprobadasGerenciasProcesos(
            @ApiParam(value = "id Region")
            @RequestParam("idR") Integer idR,
            @RequestParam("idP") Integer idP
    ) {
        return this.denunciaGrabadaService.getApplyComplaintsByGerencyProcess(idR, idP);
    }

    @GetMapping(path = "/approved/complaints/scope/cabinet",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias en base a su Gerencia y Proceso Masivo")
    public List<DenunciaAprobadaProjection> getComplaintsCabinet(
            @ApiParam(value = "id Region")
            @RequestParam("idR") Integer idR
    ) {
        return this.denunciaGrabadaService.getApplyComplaintsCabinet(idR);
    }

    @GetMapping(path = "/rejected/complaints",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias que tienen estado NO APLICA en base a un NIT")
    public List<DenunciaAprobadaProjection> getDenunciasRechazada(
            @ApiIgnore UserLogged user
    ) {
        return this.denunciaGrabadaService.getRejectedComplaints(user.getNit());
    }

    @GetMapping(path = "/cabinet",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias que tienen un proceso masivo Gabinete")
    public List<BandejaDenunciasProjection> getCabinetComplaints() {
        return this.denunciaGrabadaService.getComplaintsGabineteProcess();
    }

    @GetMapping(path = "/catalog/process/complaint/{idCatalogo}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el catalogo de procesos masivos")
    public List<ProcesosDenunciaProjection> getDenunciasRechazada(
            @ApiParam(value = "NIT de operador")
            @PathVariable Integer idCatalogo
    ) {
        return this.denunciaGrabadaService.getCatalogProcessComplaints(idCatalogo);
    }

    @GetMapping(path = "/date",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias que tienen estado NO APLICA en base a un NIT aplicando un flitro de fechas.")
    public List<DenunciaAprobadaProjection> getRejectedComplaintsForDate(
            @ApiIgnore UserLogged user,
            @RequestParam("fecha") String fecha,
            @RequestParam("fechaFin") String fechaFin
    ) {
        return this.denunciaGrabadaService.getRejectedComplaintsForDate(user.getNit(), fecha, fechaFin);
    }

    @GetMapping(path = "/process",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de procesos masivos de la programación Masiva.")
    public List<ProcesosMasivosProjections> getAllProcessMasive() {
        return this.denunciaGrabadaService.getAllProcessMasive();
    }

    @GetMapping(path = "/process/scope",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de procesos masivos de la programación Masiva.")
    public List<ProcesosMasivosProjections> getProcessMasive() {
        return this.denunciaGrabadaService.getProcessMasive();
    }

    @GetMapping(path = "/state",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el listado de estados de una denuncia")
    public List<ProcesosMasivosProjections> getStateComplaints() {
        return this.denunciaGrabadaService.getStateComplaints();
    }
// controlador para aplicar a las denuncias

    @PutMapping(path = "/apply/managements/complaints/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Cambia el estado de una denuncia ha APLICA y cambia direción")
    public void denunciaAP(
            @ApiParam(value = "Correlativo Denuncia.")
            @PathVariable String id,
            @RequestBody DetalleDenunciaDto denuncia
    ) {
        denunciaGrabadaService.setApplyManagementsComplaints(denuncia, id);
    }

    @PutMapping(path = "/rejected/managements/complaints/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Cambia el estado de una denuncia ha NO APLICA")
    public void denunciaNAP(
            @ApiParam(value = "Correlativo Denuncia.")
            @PathVariable String id,
            @RequestBody DenunciaDetalleRechazadoDto denuncia
    ) {
        denunciaGrabadaService.setRejectedManagementsComplaints(denuncia, id);
    }

    @PutMapping(path = "/edit/complaints/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Cambia el estado de una denuncia")
    public void editComplaints(
            @ApiParam(value = "Correlativo Denuncia.")
            @PathVariable String id,
            @RequestBody DenunciaDetalleRechazadoDto denuncia
    ) {
        denunciaGrabadaService.setEditComplaints(denuncia, id);
    }

    @GetMapping(path = "/assignment",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene lista de todas las asignaciones de denuncias")
    public List<?> getAllAssignments() {

        return this.denunciaGrabadaService.getWhole();
    }

    @PostMapping(path = "/start/process", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un proceso una presencia ")
    ResponseEntity<?> CreateProcessTax(
            @RequestPart("data") String data,
            @RequestPart("files") List<MultipartFile> files, @ApiIgnore UserLogged user) {
        
        DenunciaAlcanceDto dto = new Gson().fromJson(data, DenunciaAlcanceDto.class);
        
        return ResponseEntity.ok(denunciaGrabadaService.saveComplaintScopes(dto, files, user));
    }
    @PutMapping(path = "/edit/process/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE,
        MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea el registro de un proceso una presencia ")
    ResponseEntity<?> putProcessTax(
            @ApiParam(value = "id de Alcance")
            @PathVariable Integer id,
            @RequestPart("data") String data,
            @RequestPart("files") List<MultipartFile> files, @ApiIgnore UserLogged user) {
        
        DenunciaAlcanceDto dto = new Gson().fromJson(data, DenunciaAlcanceDto.class);
        
        return ResponseEntity.ok(denunciaGrabadaService.editComplaintScopes(dto, files,id, user));
    }

    @PostMapping(path = "/assignment",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "asigna una lista de denuncias a un colaborador ", notes = "asigna denuncias")

    public ResponseEntity assignment(@RequestBody AsignacionDenunciaParamDto dto, @ApiIgnore UserLogged user) throws BusinessException {

        if (this.denunciaColaboradorService.assignList(dto.getpLista(), dto.getpNitResponsable(), user)) {
            return ResponseEntity.ok().build();
        } else {
            throw BusinessException.unprocessableEntity("Imposible de realizar la asignacion");
        }

    }

    @PostMapping(
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "guarda una nueva denuncia", notes = "crear denuncia")
    @Transactional
    public ResponseEntity saveNew(@RequestPart("file") List<MultipartFile> file,
            @RequestPart("filePay") List<MultipartFile> filePay,
            @RequestPart("data") String data, @ApiIgnore UserLogged user) throws BusinessException {
        DenunciaGrabadaDto dto;

        dto = new Gson().fromJson(data, DenunciaGrabadaDto.class);
        //log.debug("la lista está vacia  " + file.isEmpty());
        //log.debug("esto es del dato " + dto.getCorrelativo());
        SipfDenunciaGrabada complaintSaved = this.denunciaGrabadaService.saveNewComplaint(dto);

        dto.setCorrelativo(complaintSaved.getCorrelativo());
        NodosACSDto nodos;
        if (!file.isEmpty()) {
            Map<String, Object> post = new HashMap<>();
            post.put("denuncia", complaintSaved.getCorrelativo());
            //post.put("carpeta", "Denuncias");
            JSONObject json = new JSONObject(post);
            //log.debug("hola" + json.toString());
            nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.DENUNCIAS, json);
            //log.debug("nodos " + nodos.getId());
            contiendoACSService.uploadFiles(nodos.getId(), file);

        }

        if (!filePay.isEmpty()) {
            Map<String, Object> post = new HashMap<>();
            post.put("denuncia", complaintSaved.getCorrelativo());
            //post.put("carpeta", "Denuncias");
            JSONObject json = new JSONObject(post);
            //log.debug("hola" + json.toString());
            nodos = contiendoACSService.getNodosACSBySiteAndPath(TipoRuta.DENUNCIASPAGO, json);
            //log.debug("nodos " + nodos.getId());
            contiendoACSService.uploadFiles(nodos.getId(), filePay);

        }

        return ResponseEntity.ok(dto);

    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @param logged
     * @return
     */
    @GetMapping(path = "/complaints",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene las denuncias que estan asignadas a un operador en base a su NIT")
    public ResponseEntity<List<BandejaDenunciasProjection>> getComplaints(
            @ApiParam(value = "NIT de operador registrado")
            @ApiIgnore UserLogged logged
    ) {
        return ResponseEntity.ok(denunciaGrabadaService.getComplaints(logged.getNit()));
    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @param id
     * @return
     */
    @GetMapping(path = "/detail/complaints/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene el detalle de una denuncia en base a su correlativo")
    public ResponseEntity<List<DetalleDenunciasProjection>> getDetailComplaint(
            @ApiParam(value = "Correlativo denuncia")
            @PathVariable String id
    ) {
        return ResponseEntity.ok(denunciaGrabadaService.getDetailComplaint(id));
    }

    /**
     * @author lfvillag (Luis Villagrán)
     * @return
     */
    @GetMapping(path = "/managements", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la gerencia de un contribuyente.")
    public ResponseEntity<List<GerenciasProjection>> getManagements() {
        return ResponseEntity.ok(denunciaGrabadaService.getManagements());
    }

}
