/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.Gson;
import gt.gob.sat.sat_ifi_sipf.dtos.DenunciaGrabadaDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GenerationPdfDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfDenunciaGrabada;
import gt.gob.sat.sat_ifi_sipf.models.SipfPlantillasDocumentos;
import gt.gob.sat.sat_ifi_sipf.services.AlcancesService;
import gt.gob.sat.sat_ifi_sipf.services.CatalogosService;
import gt.gob.sat.sat_ifi_sipf.services.ContiendoACSService;
import gt.gob.sat.sat_ifi_sipf.services.ContribuyenteService;
import gt.gob.sat.sat_ifi_sipf.services.DenunciaGrabadaService;
import gt.gob.sat.sat_ifi_sipf.services.PlantillasDocumentosService;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author rabaraho
 */
@Api(tags = {"Servicios publicos"})
@Slf4j
@Validated
@RestController
@RequestMapping("/public")
public class PublicController {

    @Autowired
    CatalogosService catalogService;

    @Autowired
    ContribuyenteService contribuyenteService;

    @Autowired
    ContiendoACSService contiendoACSService;

    @Autowired
    DenunciaGrabadaService denunciaGrabadaService;

    @Autowired
    private AlcancesService alcancesService;

    @Autowired
    private PlantillasDocumentosService plantillasDocumentosService;

    @GetMapping(path = "/complaint/catalog", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los datos de un catalogo filtrado por id del catalogo y por estado del dato.")
    public ResponseEntity<List<?>> getCatalogDataByIdCatalogAndStatus() {
        List<Integer> vListIdCatalog = new ArrayList<>();

        vListIdCatalog.add(74);
        vListIdCatalog.add(75);
        vListIdCatalog.add(76);
        //vListIdCatalog.add(77);
        vListIdCatalog.add(78);
        vListIdCatalog.add(79);
        vListIdCatalog.add(80);
        vListIdCatalog.add(81);
        vListIdCatalog.add(9);

        Integer vIdStatus = 1;

        return ResponseEntity.ok(Stream.of(catalogService.getData(77, vIdStatus, "gerencia"), catalogService.findCatalogDataByIdCatalogAndStatus(vListIdCatalog, vIdStatus))
                .flatMap(x -> x.stream())
                .collect(Collectors.toList()));

    }

    @PostMapping(path = "/complaint",
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

    @GetMapping(path = "/tax/payer/{nit}",
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

    @GetMapping(path = "/template/documents/findTemplates/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las plantillas de documentos ")
    public SipfPlantillasDocumentos findTemplatesById(
            @ApiParam(value = "Id de la plantilla")
            @PathVariable Integer id) {

        return plantillasDocumentosService.findPlantillasDocumentosById(id);
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

    @DeleteMapping(path = "/delete/generation/file")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Elimina archivo pdf")
    public ResponseEntity<?> deletePdf() throws IOException {
        alcancesService.deleteDocument();
        return ResponseEntity.ok().build();
    }

}
