/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.controllers;

import gt.gob.sat.sat_ifi_sipf.dtos.FolderCreatedDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeChildren;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeDataDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeEntry;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.services.ContiendoACSService;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestPart;

/**
 * Clase que contiene los métodos para consumos del gestor documental
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 07/02/2022
 * @version 1.0
 */
@Api(tags = {"Contenido ACS"})
@Validated
@RestController
@RequestMapping("/content")
@Slf4j
public class ContiendoACSController {

    @Autowired
    private ContiendoACSService contiendoACSService;

    @PostMapping(path = "/base/root/encrypted/{tipo}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Obtiene nodeId encritado de una ruta base", notes = "Obtiene nodeId encritado de una ruta base")
    public ResponseEntity<NodosACSDto> findNodeByPathAndSitio(@PathVariable TipoRuta tipo, @RequestBody(required = false) Map<String, Object> body) {
        NodosACSDto nodos = contiendoACSService.getNodosACSBySiteAndPath(tipo, body);
        if (nodos == null) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(nodos);
    }

    @PostMapping(path = "/create/folder/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "Crear carpeta en un nodo padre (carpeta)", notes = "Crear carpeta en un nodo padre (carpeta)")
    public ResponseEntity<NodosACSDto> crearCarpetaEnNodo(@ApiParam(value = "Identificador de la carpeta en ACS, encriptado")
            @PathVariable String id,
            @ApiParam(value = "Nombre de la carpeta")
            @RequestParam String name,
            @ApiParam(value = "Json con informacion sobre el modelo de datos, propiedades y aspectos")
            @RequestBody(required = false) NodeDataDto[] nodeDataDto) {
        NodosACSDto node = contiendoACSService.createFolderNodeACS(id, name, nodeDataDto);
        if (node == null) {
            ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(node);
    }

    @PostMapping(path = "/upload/multiple/file/{folder}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Crear carpeta en un nodo padre (carpeta)", notes = "Crear carpeta en un nodo padre (carpeta)")
    public List<FolderCreatedDto> uploadFiles(
            @ApiParam(value = "Node id encriptado del forlder")
            @PathVariable String folder,
            @ApiParam(value = "Listado de archivo")
            @RequestPart("files") List<MultipartFile> files) throws IOException {
        return contiendoACSService.uploadFiles(folder, files);
    }

    @GetMapping(path = "/nodes/{id}/nodes-children",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los nodos hijos en base a un nodeId padre", notes = "Retorna los nodos hijos en base a un nodeId padre")
    public NodeChildren getNodosACSByNodeIdFather(
            @ApiParam(value = "Identificador del archivo o carpeta en ACS, encriptado")
            @PathVariable @NotBlank String id,
            @ApiParam(value = "Informacion adicional como properties y/o aspectNames")
            @RequestParam(required = false) String include,
            @ApiParam(value = "Numero de registros a partir del cual se quiere consultar")
            @RequestParam(required = false) String skipCount,
            @ApiParam(value = "Maximo de registros a consultar")
            @RequestParam(required = false) String maxItems) {
        return contiendoACSService.getNodosACSByNodeIdFather(id, include, skipCount, maxItems);
    }

    @GetMapping(path = "sites/files/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public void contentSitesFileById(
            @PathVariable String nodeId,
            HttpServletResponse response) {
        try {
            contiendoACSService.contentSitesFileById(nodeId, response);
        } catch (IOException ex) {
            Logger.getLogger(ContiendoACSController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @GetMapping(path = "sites/files/{nodeId}/versions",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Obtiene los nodos hijos en base a un nodeId padre", notes = "Retorna los nodos hijos en base a un nodeId padre")
    public NodeChildren contentSitesFileByIdVersions(
            @ApiParam(value = "Identificador del archivo o carpeta en ACS, encriptado")
            @PathVariable @NotBlank String nodeId,
            @ApiParam(value = "Informacion adicional como properties y/o aspectNames")
            @RequestParam(required = false) String include,
            @ApiParam(value = "Numero de registros a partir del cual se quiere consultar")
            @RequestParam(required = false) String skipCount,
            @ApiParam(value = "Maximo de registros a consultar")
            @RequestParam(required = false) String maxItems) {
        return contiendoACSService.contentSitesFileByIdVersions(nodeId, include, skipCount, maxItems);
    }

    @GetMapping(path = "sites/nodes/{nodeId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public NodeEntry contentSitesNodeById(
            @PathVariable String nodeId
    ) {
        return contiendoACSService.contentSitesNodeById(nodeId);
    }

    @PutMapping(path = "sites/files/{nodeId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Actualiza un archivo")
    public FolderCreatedDto contentSitesNodeByIdUpdate(
            @PathVariable String nodeId,
            @RequestPart("file") MultipartFile file) {
        return contiendoACSService.contentSitesNodeByIdUpdate(nodeId, file);
    }

    @PutMapping(path = "sites/folders/{nodeId}",
            consumes = { MediaType.APPLICATION_JSON_VALUE,
            MediaType.TEXT_PLAIN_VALUE },
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @ApiOperation(value = "Actualiza el nombre de una carpeta")
    public Boolean contentSitesFoldersSetNameByIdUpdate(
            @PathVariable String nodeId,
            @RequestParam(required = false) String newName,
            @RequestBody(required = false) NodeDataDto metaData) {
        return contiendoACSService.contentSitesFoldersSetNameByIdUpdate(nodeId, newName, metaData);
    }
}
