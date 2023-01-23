/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;


import gt.gob.sat.sat_ifi_sipf.dtos.HallazgosEncontradosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgoDetalleProjection;
import gt.gob.sat.sat_ifi_sipf.projections.HallazgosEncontradosProjection;
import gt.gob.sat.sat_ifi_sipf.services.HallazgosService;
import static gt.gob.sat.sat_ifi_sipf.utils.GeneralUtils.countNull;
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
@Api(tags = {"Hallazgos"})
@Validated
@RestController
@Slf4j
@RequestMapping("findings")
public class HallazgosController {

    @Autowired
    HallazgosService claseService;

    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los hallazgos de un caso")
    public ResponseEntity<List<HallazgoDetalleProjection>> getFindingsDetail(
            @PathVariable Integer id,
            @RequestParam (value = "correlativo", required=false, defaultValue = "") String correlativo,
            @ApiIgnore UserLogged logged) {
        
        if(correlativo == null || correlativo.matches("")){
            //log.debug("ENTRO EN CORRELATIVO NULL " + correlativo);
            return ResponseEntity.ok(claseService.getDetailedFoundings(logged.getNit(), id));
            
        }else{
            //log.debug("ENTRO EN gabinete " + correlativo);
                return ResponseEntity.ok(claseService.getDetailedFoundingsCabinet(logged.getNit(), correlativo));
            }
        
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "crea hallazgo del caso", notes = "Agrega un hallazgo")
    public void createFind(
            @Validated @RequestBody HallazgosEncontradosDto hallazgos,
            @ApiIgnore UserLogged logged) {
        
        if(hallazgos.getCorrelativo() == null){
            
            claseService.createFinding(hallazgos);
        }else{
            claseService.createFindingCabinet(hallazgos, logged);
        }
        
    }

    @PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "actualizar hallazgo del caso", notes = "Actualizar un hallazgo")
    public void updateFinding(
            @PathVariable Integer id, 
            @Validated @RequestBody HallazgosEncontradosDto dto,
            @ApiIgnore UserLogged logged) {
        
        if(dto.getCorrelativo() == null || dto.getCorrelativo().isEmpty()){
            claseService.editFinding(id, dto);
        }else{
            claseService.editFindingCabinet(id, dto, logged);
        }
        
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "eliminar hallazgo del caso", notes = "Eliminar un hallazgo")
    public void deleteFinding(
            @PathVariable Integer id,
            @RequestParam (value = "isGabinet", required=false, defaultValue = "false") boolean isGabinet) {
        if(isGabinet){
            claseService.deleteFindingCabinet(id);
        }else{
            claseService.deleteFinding(id);
        }
        
    }
}
