/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.CatDatoAdminDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoPadreProjection;
import gt.gob.sat.sat_ifi_sipf.projections.RubroProjection;
import gt.gob.sat.sat_ifi_sipf.services.CatalogosService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
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
 *
 * @author ruarcuse
 */
@Api(tags = {"Catalogos"})
@Validated
@RestController
@Slf4j
@RequestMapping("/catalog")
public class CatalogosController {

    @Autowired
    CatalogosService catalogService;

    @GetMapping(path = "/data", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los datos de un catalogo filtrado por id del catalogo y por estado del dato.")
    public ResponseEntity<List<?>> getCatalogDataByIdCatalogAndStatus(
            @ApiParam(value = "Identificador catalogo")
            @RequestParam(required = false) Integer idCatalog,
            @ApiParam(value = "nombre de la condicion especial")
            @RequestParam(required = false) String nombre,
            @ApiParam(value = "Identificador del estado del dato")
            @RequestParam Integer idStatus,
            @ApiParam(value = "Identificador del dato padre ")
            @RequestParam(required = false) Integer idDatoPadre,
            @ApiParam(value = "lista de indentificadores de catalogo")
            @RequestParam(required = false) List<Integer> listIdCatalog
    ) {
        if (idCatalog != null) {

            if (nombre != null && !StringUtils.isEmpty(nombre)) {
                //log.debug("si trae gerencia " + StringUtils.isEmpty(nombre));
                return ResponseEntity.ok(catalogService.getData(idCatalog, idStatus, nombre));
            } else {

                return ResponseEntity.ok(catalogService.findCatalogDataByIdCatalogAndStatus(idCatalog, idStatus));
            }
        } else if (listIdCatalog != null) {

            return ResponseEntity.ok(catalogService.findCatalogDataByIdCatalogAndStatus(listIdCatalog, idStatus));

        } else {
            return ResponseEntity.ok(catalogService.findCatalogDataByDatoPadreAndStatus(idDatoPadre, idStatus));
        }

    }

    @GetMapping(path = "/data/item", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene los datos de un item de algun catalogo filtrado por id del item.")
    public ResponseEntity<CatalogDataProjection> getSingleDatoById(
            @ApiParam(value = "Identificador del item a buscar ")
            @RequestParam(required = false) Integer idDato
    ) {
        return ResponseEntity.ok(catalogService.findCatalogDataByIdData(idDato));
    }

    //Controlador para obtener los catalogos padres administrable
    @GetMapping(path = "/manageable", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obteniendo la lista de catalogos padres que sean administrables")
    public ResponseEntity<List<CatalogoPadreProjection>> getManageableCatalog() {
        return ResponseEntity.ok(catalogService.getManageableCatalog());
    }

    //Controlador para obtener los catalogos hijos en base a id padre
    @GetMapping(path = "/item/{idCatalog}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obteniendo la lista de catalogos hijos en base a id de padre")
    public ResponseEntity<List<CatalogoHijoProjection>> getItemById(
            @ApiParam(value = "Identificador del colaborador")
            @PathVariable Integer idCatalog) {
        List<CatalogoHijoProjection> catSon = catalogService.getItemById(idCatalog);
        return ResponseEntity.ok(catSon);
    }

    //Metodo para crear un nuevo registro de catalogo hijo
    @PostMapping(path = "/item", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crea un nuevo registro de catalogo")
    public ResponseEntity<?> createItem(@RequestBody CatDatoAdminDto dto, @ApiIgnore UserLogged userLogged) {
        return ResponseEntity.ok(catalogService.createItem(dto, userLogged));
    }

    //COntrolador para modificar el estado de un catalogo
    @PutMapping(path = "/item/{idCat}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador para modificar el estado inactivo de un catalogo")
    public Boolean removeItem(
            @PathVariable Integer idCat,
            @RequestBody(required = false) Object body,
            @ApiIgnore UserLogged userLogged
    ) {
        try {
            catalogService.removeItem(idCat, userLogged);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    //COntrolador para modificar nombre y descripcion de un catalogo
    @PutMapping(path = "/item/description/{idCatSon}", consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Controlador para modificar nombre y descripcion un catalogo")
    public Boolean editItem(
            @PathVariable Integer idCatSon,
            @RequestBody CatDatoAdminDto dto,
            @ApiIgnore UserLogged userLogged
    ) {
        try {
            catalogService.editItem(idCatSon, dto, userLogged);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @GetMapping(path = "/findings/items", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "obtiene los rubros ingresados.")
    public List<RubroProjection> getFindingsItems() {
        return catalogService.getFindingsItem();
    }
}
