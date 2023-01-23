/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosComprasParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosReporteDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AsisteLibrosVentasParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.AuditCommentDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ConsultaFelDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ConvenioPagosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DeclaracionParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DetalleConvenioPagoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.DuasDucasReponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.EfaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.EfaParametrosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ExcelMasivoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ImportacionExportacionSiveDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ImportacionExportacionSivepaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.LineasDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParamsDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParamsDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ParamsLineasDuasDucasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIVAResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIVATotalDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIVAUserDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RetenIsrParametrosDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.services.ComentarioHistoricoNombramientoService;
import gt.gob.sat.sat_ifi_sipf.services.ConsumosService;
import gt.gob.sat.sat_ifi_sipf.services.ContribuyenteService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * Clase que contiene los métodos para consumo de información de contribuyentes
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 10/06/2022
 * @version 1.0
 */
@Api(tags = {"Contribuyente"})
@Validated
@RestController
@RequestMapping("/tax/payer")
@Slf4j
public class ContribuyenteController {

    @Autowired
    ContribuyenteService contribuyenteService;

    @Autowired
    ConsumosService consumosService;

    @Autowired
    ComentarioHistoricoNombramientoService comentarioHistoricoNombramientoService;

    @GetMapping(path = "/{nit}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonRawValue
    @ApiOperation(value = "Obtiene la información general del contribuyente en base al NIT", notes = "Retorna la información general del contribuyente en base al NIT")
    public ResponseEntity<String> getGeneralTaxpayerInformation(
            @ApiParam(value = "Número de Identificación Tributaria")
            @PathVariable String nit) {
        String taxPayer = contribuyenteService.getGeneralTaxpayerInformation(nit);
        if (taxPayer.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(taxPayer);
    }

    @GetMapping(path = "/legal/service/{nit}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonRawValue
    @ApiOperation(value = "Obtiene la información de un contribuyente Empresa/Negocio", notes = "Retorna la información de un contribuyente Empresa/Negocio")
    public ResponseEntity<String> getTypeLegalService(
            @ApiParam(value = "Número de Identificación Tributaria (Empresa/Negocio)")
            @PathVariable String nit) {
        String taxPayer = contribuyenteService.getTypeLegalService(nit);
        if (taxPayer.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(taxPayer);
    }

    @GetMapping(path = "/audit/historial/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el historial de auditorias de un contribuyente ", notes = "")
    public ResponseEntity<List<AuditCommentDto>> obtainAuditHistorial(
            @ApiParam(value = "Id del insumo")
            @PathVariable Integer pNit) {

        HttpHeaders headers;

        GeneralResponseDto<List<AuditCommentDto>> resultado = consumosService.consumeCompleteUrlOracle(pNit, "nit/" + pNit, GeneralResponseDto.class, HttpMethod.GET);

        ArrayList lista = (ArrayList) resultado.getData();

        lista.forEach((obj) -> {
            ((LinkedHashMap) obj).put("comentario", this.comentarioHistoricoNombramientoService.getCommentByNombramientoExpediente(
                    ((LinkedHashMap) obj).get("nombramiento").toString(),
                    ((LinkedHashMap) obj).get("numeroExpediente").toString()));

            //log.debug("hizo lo del comentario");
        });

        return ResponseEntity.ok(resultado.getData());
    }

    @GetMapping(path = "/vehiculos/{nit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el historial de auditorias de un contribuyente ", notes = "")
    public ResponseEntity<List<Object>> getVehiclesConsultation(@PathVariable String nit) {
        return ResponseEntity.ok(contribuyenteService.getVehiclesConsultation(nit));
    }

    ///////////////////////
    /**
     * Controlador que recibe la data de importaciones del sistema SIVEPA
     *
     * @author Luis Villagran (lfvillag)
     * @param importacionSivepa
     * @return
     * @since 21/07/2022
     */
    @PostMapping(path = "/importacion/exportacion", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la informacion de importacion del sistema Sivepa", notes = "")
    public ResponseEntity<List<Object>> getImportacionSivepa(@RequestBody ImportacionExportacionSivepaDto importacionSivepa) {
        return ResponseEntity.ok(contribuyenteService.getImportacionSivepa(importacionSivepa));
    }
    ///////////////////////

    ///////////////////////
    /**
     * Controlador que recibe la data de importaciones del sistema SIVEPA
     *
     * @author Luis Villagran (lfvillag)
     * @param importacionSivepa
     * @return
     * @since 21/07/2022
     */
    @PostMapping(path = "/importacion/exportacion/detalle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la informacion de importacion del sistema Sivepa", notes = "")
    public ResponseEntity<List<Object>> getImportacionSivepaDetalle(@RequestBody ImportacionExportacionSiveDetalleDto importacionSivepaDetalle) {
        return ResponseEntity.ok(contribuyenteService.getImportacionSivepaDetalle(importacionSivepaDetalle));
    }
    ///////////////////////

    //Obtiene la data para el reporte de compras del contribuyente/empresa
    @PostMapping(path = "/asistelibros/purchases", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonRawValue
    @ApiOperation(value = "Obtiene un reporte de las compras de un contribuyente ", notes = "")
    public ResponseEntity<String> getReporteCompras(
            @RequestBody AsisteLibrosComprasParametrosDto dto
    ) {
        String resultado = consumosService.consume(dto, "sat-ale-reportes/reporteCompras", String.class, HttpMethod.POST);
        return ResponseEntity.ok(resultado);
    }

    //Obtiene la data más detalladamente de las compras para generar un excel 
    @PostMapping(path = "/asistelibros/purchases/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @JsonRawValue
    @ApiOperation(value = "Obtiene un reporte de las compras detalladamente por medio de un excel", notes = "")
    public ArrayList<AsisteLibrosReporteDto> getDetalleComprasReporte(
            @RequestBody AsisteLibrosComprasParametrosDto dto
    ) {
        return contribuyenteService.getDetalleComprasDescarga(dto);
    }

    //Obtiene la data para el reporte de ventas del contribuyente/empresa
    @PostMapping(path = "/asistelibros/sales", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @JsonRawValue
    @ApiOperation(value = "Obtiene un reporte de las ventas de un contribuyente", notes = "")
    public ResponseEntity<String> getReporteVentas(
            @RequestBody AsisteLibrosVentasParametrosDto dto
    ) {
        String resultado = consumosService.consume(dto, "sat-ale-reportes/reporteVentas", String.class, HttpMethod.POST);
        return ResponseEntity.ok(resultado);
    }

    //Metodo para traer detalladamente el reporte de ventas
    @PostMapping(path = "/asistelibros/sales/report", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Obtiene un reporte de las ventas detalladamente por medio de un excel", notes = "")
    @JsonRawValue
    public ArrayList<AsisteLibrosReporteDto> getDetalleVentasReporte(
            @RequestBody AsisteLibrosVentasParametrosDto dto
    ) {

        return contribuyenteService.getDetalleVentasDescarga(dto);
    }

    @PostMapping(path = "/declaraciones", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el historial de auditorias de un contribuyente ", notes = "")
    public ResponseEntity<List<Object>> getDeclaracionesConsult(
            @RequestBody DeclaracionParametrosDto dto
    ) {
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/declaracionConsolidada", GeneralResponseDto.class, HttpMethod.POST);

        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "/resumen/declaracion", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el historial de auditorias de un contribuyente ", notes = "")
    public ResponseEntity<List<Object>> getResumenDeclaraciones(
            @RequestBody DeclaracionParametrosDto dto
    ) {
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/resumen/declaracion", GeneralResponseDto.class, HttpMethod.POST);

        return ResponseEntity.ok().body(resultado.getData());
    }

//cocntrolador de convenios de pago contribuyente
    @GetMapping(path = "/convenio/{pNit}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene los convenios de pago del contribuyente", notes = "")
    public ResponseEntity<List<ConvenioPagosDto>> getPaymentAgreement(@PathVariable String pNit) {
        return ResponseEntity.ok(contribuyenteService.getPaymentAgreement(pNit));
    }

    //METODO PARA OBTENER EL DETALLE DE CONVENIOS DE PAGO MEDIANTE EL NIT, FORMULARIO Y EXPEDIENTE
    @GetMapping(path = "/convenio/detalle", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene los convenios de pago del contribuyente", notes = "")
    public ResponseEntity<List<DetalleConvenioPagoDto>> getPaymentAgreementDetail(
            @RequestParam String pNit,
            @RequestParam String pForm,
            @RequestParam String pDoc) {
        return ResponseEntity.ok(contribuyenteService.getPaymentAgreementDetail(pNit, pForm, pDoc));
    }

    //Metodo para obtener data de registro de retenciones
    @PostMapping(path = "/reten/IVA", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la data de las retenciones ", notes = "")
    public ResponseEntity<String> getRetenIVA(
            @RequestBody RetenIVAResponseDto dto
    ) {
        String resultado = consumosService.consume(dto, "ret-isr-consultas/constancias", String.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado);
    }

    //metodo para obtener total de registro de retenciones
    @PostMapping(path = "/reten/IVA/total", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene el total de registros de retenciones ", notes = "")
    public ResponseEntity<String> getRetenIVATotal(
            @RequestBody RetenIVATotalDto dto
    ) {
        String resultado = consumosService.consume(dto, "ret-isr-consultas/constanciasTotal", String.class, HttpMethod.POST);

        return ResponseEntity.ok().body(resultado);
    }

    //metodo para generar excel masivo de retenciones 
    @PostMapping(path = "/reten/excel/masivo", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "genera excel masivo y obtiene respuesta del resultado", notes = "")
    public ResponseEntity<String> geExcelMasivo(
            @RequestBody ExcelMasivoDto dto
    ) {
        String resultado = consumosService.consume(dto, "sat-reteniva3-cargaMasiva/generarExcelMasivo", String.class, HttpMethod.POST);

        return ResponseEntity.ok().body(resultado);
    }

    //Metodo para obtener data USUARIO LOGUEADO POR NIT
    @PostMapping(path = "/reten/IVA/user", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la data del usuario por nit retenedor ", notes = "")
    public ResponseEntity<String> getUserIVA(
            @RequestBody RetenIVAUserDto dto
    ) {
        String resultado = consumosService.consume(dto, "ret-isr-consultas/consultaUsuariosNit", String.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado);
    }

    @PostMapping(path = "/reten/isr/rc", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene infromacion de retenciones ISR rentas capital", notes = "")
    public ResponseEntity<List<Object>> getRetenIsrRc(@RequestBody RetenIsrParametrosDto dto) {
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/reten/isr/rc", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "/reten/isr/os", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene infromacion de retenciones ISR opcional simplificado", notes = "")
    public ResponseEntity<List<Object>> getRetenIsrOs(@RequestBody RetenIsrParametrosDto dto) {
        GeneralResponseDto<List<Object>> resultado = consumosService.consumeCompleteUrlOracle(dto, "/reten/isr/os", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    @PostMapping(path = "/consulta/fel", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la data de las facturas electrónicas ", notes = "")
    public ResponseEntity<String> getFel(
            @RequestBody ConsultaFelDto dto
    ) {
        String resultado = consumosService.consume(dto, "ret-isr-consultas/consultaFelNit", String.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado);
    }

    @GetMapping(path = "/gerency", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene la gerencia de contribuyente")
    public ResponseEntity<CatalogDataProjection> getGerency(@RequestParam Integer clasificacion, @RequestParam Integer departamento) {

        if (departamento == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El Contribuyente no cuenta con un departamento valido");
        }
        return ResponseEntity.ok(contribuyenteService.getGerency(clasificacion, departamento));
    }

    @PostMapping(path = "/efa", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la data requerida para la consulta", notes = "")
    public ResponseEntity<List<EfaDto>> getEfa(
            @RequestBody EfaParametrosDto dto
    ) {
        GeneralResponseDto<List<EfaDto>> resultado = consumosService.consumeCompleteUrlOracle(dto, "efa", GeneralResponseDto.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado.getData());
    }

    //controlador para consumir webservices que optiene ducas y duas en base a nit, fecha 
    @PostMapping(path = "/consulta/dua/duca", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene la data de las ducas y duas ", notes = "")
    public List<DuasDucasReponseDto> getDuasAndDucas(
            @RequestParam(required = false, defaultValue = "false") Boolean export,
            @RequestBody ParamsDuasDucasDto dto, HttpServletResponse response
    ) {
        return contribuyenteService.getDuasAndDucas(dto, response, export);
    }

    @PostMapping(path = "/request/ducas", produces = MediaType.APPLICATION_JSON_VALUE)
    @JsonRawValue
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "obtiene la data de las ducas", notes = "")
    public ResponseEntity<String> getDucasByNoOrden(
            @RequestBody ParamsDucasDto dto
    ) {
        String resultado = consumosService.consumeCompleteUrlOracle(dto, "/request/ducas", String.class, HttpMethod.POST);
        return ResponseEntity.ok().body(resultado);
    }

}
