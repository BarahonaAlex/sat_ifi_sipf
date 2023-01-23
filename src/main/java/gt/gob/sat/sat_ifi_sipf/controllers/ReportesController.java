/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIsrOsDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIsrRcDto;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceMasivoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.AlcanceProjection;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.services.ReportesService;
import gt.gob.sat.sat_ifi_sipf.utils.ReportesUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author ruarcuse
 */
@Api(tags = {"Reportes"})
@Validated
@RestController
@Slf4j
@RequestMapping("/reports")
public class ReportesController {

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ReportesService reportesService;

    @Autowired
    Detector detector;

    @GetMapping(path = "/scopes/{nit}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un alcance en base al nit.")
    public ResponseEntity<List<AlcanceProjection>> obtenerAlcance(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable String nit) {

        List<AlcanceProjection> unidad = reportesService.getAlcanceId(nit);
        if (unidad == null) {
            throw new ResourceNotFoundException(
                    detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unidad);
    }

    @GetMapping(path = "/scopes/massive/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen un alcance masivo en base al id.")
    public ResponseEntity<List<AlcanceMasivoProjection>> obtenerAlcanceMasivoId(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable Integer id) {

        List<AlcanceMasivoProjection> unidad = reportesService.getAlcanceMasivoXId(id);
        if (unidad == null) {
            throw new ResourceNotFoundException(
                    detector.getLocaleMessage("usuario.noexiste.message"));
        }
        return ResponseEntity.ok(unidad);
    }

    @GetMapping(path = "/documentos/{nit}/{tipo}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen todos los colaboradores creados.")
    public void generarReporteAlcanceSelectivo(
            @PathVariable Integer tipo,
            @PathVariable String nit,
            HttpServletResponse response) {
        switch (tipo) {
            case 117:
                ReportesUtils.crearReporte(reportesService.getAlcanceSelectivoId(nit), "ReporteAlcanceSelectivo.jrxml", response, ReportesUtils.WORD);
                break;
            case 118:
                ReportesUtils.crearReporte(reportesService.getAlcanceSolicitudId(nit), "ReporteAlcanceSolicitud.jrxml", response, ReportesUtils.WORD);
                break;
        }
    }

    @GetMapping(path = "/documentos/masivos/{id}/{tipo}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtienen todos los colaboradores creados.")
    public void generarReporteAlcanceMasivo(
            @PathVariable Integer tipo,
            @PathVariable Integer id,
            HttpServletResponse response) {
        switch (tipo) {
            case 120:
                ReportesUtils.crearReporte(reportesService.getAlcanceMasivoId(id), "ReporteAlcanceMasivo.jrxml", response, ReportesUtils.WORD);
                break;
        }
    }

    @GetMapping(path = "/constancia/isr/{id}/{tipo}/{estado}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Genera constancia isr ", notes = "")
    public void getListIsr2(
            @ApiParam(value = "numero del fromulario")
            @PathVariable String id,
            @PathVariable String tipo,
            @PathVariable String estado,
            HttpServletResponse response) {

        if (tipo.equals("os")) {
            GeneralResponseDto<List<RetenIsrOsDto>> resultado = consumosService.consumeCompleteUrlOracle(id, "/constancia/isr/os/" + id, GeneralResponseDto.class, HttpMethod.GET);
            ArrayList lista = (ArrayList) resultado.getData();
            System.out.println(lista);
            if (estado.equals("Anuladas")) {
                ReportesUtils.crearReporte(lista, "ConstanciaRO_1Anulada.jrxml", response, ReportesUtils.PDF);
            }
            if (estado.equals("Activas")) {
                ReportesUtils.crearReporte(lista, "ConstanciaRO_1.jrxml", response, ReportesUtils.PDF);
            }
        }
        if (tipo.equals("rc")) {
            GeneralResponseDto<List<RetenIsrRcDto>> resultado = consumosService.consumeCompleteUrlOracle(id, "/constancia/isr/rc/" + id, GeneralResponseDto.class, HttpMethod.GET);
            ArrayList lista = (ArrayList) resultado.getData();
            System.out.println(lista);
            if (estado.equals("Anuladas")) {
                ReportesUtils.crearReporte(lista, "ConstanciaRC_1Anulada.jrxml", response, ReportesUtils.PDF);
            }
            if (estado.equals("Activas")) {
                ReportesUtils.crearReporte(lista, "ConstanciaRC_1.jrxml", response, ReportesUtils.PDF);
            }
        }
    }
    
    @GetMapping(path = "/cedula/verificacion/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Genera la cedula de verificacion")
    public void generarCedulaVerificacion(
            @PathVariable Integer id,
            HttpServletResponse response) {
                ReportesUtils.crearReporte(reportesService.getCedulaVerificacion(id), "cedulaVerificacion.jrxml", response, ReportesUtils.PDF);
    }
}
