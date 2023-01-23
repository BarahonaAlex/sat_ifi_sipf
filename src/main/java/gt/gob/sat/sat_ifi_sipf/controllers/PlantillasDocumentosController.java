/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.PlantillasDocumentosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfPlantillasDocumentos;
import gt.gob.sat.sat_ifi_sipf.services.PlantillasDocumentosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

/**
 *
 * @author ajabarrer
 */
@Api(tags = {"Plantilla Documentos"})
@Validated
@RestController
@RequestMapping("/template/documents")
@Slf4j
public class PlantillasDocumentosController {
    
    @Autowired
    private PlantillasDocumentosService plantillasDocumentosService;
    
    @GetMapping(path = "/findAllTemplates",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las plantillas de documentos ")
    public List<SipfPlantillasDocumentos> findAllTemplates(){
        return plantillasDocumentosService.findAllPlantillasDocumentos();
    }
    
    @GetMapping(path = "/findTemplates/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene todas las plantillas de documentos ")
    public SipfPlantillasDocumentos findTemplatesById(
            @ApiParam(value = "Id de la plantilla") 
            @PathVariable Integer id){
        
        return plantillasDocumentosService.findPlantillasDocumentosById(id);
    }
    
    @PostMapping(path = "/create/template",produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "crea una nueva plantilla de documentos")
    public SipfPlantillasDocumentos createTemplate(@RequestBody PlantillasDocumentosDto dto){
        return plantillasDocumentosService.createPlantillasDocumentos(dto);
    }
    
    @PutMapping(path = "/update/template/{id}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Actualiza los datos de una plantilla.")
    public void updateTemplate(
            @ApiParam(value = "Id de la plantilla")
            @PathVariable Integer id,
            @RequestBody PlantillasDocumentosDto plantilla,
             @ApiIgnore UserLogged user) {

        plantillasDocumentosService.updatePlantilla(id, plantilla, user);
    }

}
