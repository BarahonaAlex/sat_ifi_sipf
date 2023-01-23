/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.Config;
import gt.gob.sat.sat_ifi_sipf.dtos.FolderCreatedDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeChildren;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeDataDto;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeEntries;
import gt.gob.sat.sat_ifi_sipf.dtos.NodeEntry;
import gt.gob.sat.sat_ifi_sipf.dtos.NodosACSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.StartProcessDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskCompleteAPSDto;
import gt.gob.sat.sat_ifi_sipf.dtos.TaskDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfAlcance;
import gt.gob.sat.sat_ifi_sipf.models.SipfDetalleCaso;
import gt.gob.sat.sat_ifi_sipf.repositories.AlcanceRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.DetalleCasoRepository;
import static gt.gob.sat.sat_ifi_sipf.services.EncryptService.APS;
import gt.gob.sat.sat_ifi_sipf.utils.HeaderUtils;
import gt.gob.sat.sat_ifi_sipf.utils.TipoRuta;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 04/02/2022
 * @version 1.0
 */
@Service
@Slf4j
public class ContiendoACSService {

    @Autowired
    private EncryptService encryptService;

    @Autowired
    private ConsumosService consumosService;

    private final Config config;

    @Autowired
    private AlcanceRepository alcanceRepository;

    @Autowired
    private DetalleCasoRepository detalleCasoRepository;

    public ContiendoACSService(Config config) {
        this.config = config;
    }

    /**
     * Metodo para obtener la ruta inicial para consulta en ACS
     *
     * @param tipo Se detalla el tipo de la ruta a resolver.
     * @param body estructura de consulta para path base
     * @since 3/12/2021
     * @return ruta para consulta en ACS
     */
    public String getBaseRootByParams(TipoRuta tipo, Map<String, Object> body) {
        JSONObject consulta = body != null ? new JSONObject(body) : null;

        switch (tipo) {
            case RUTA_BASE:
                if (consulta.getAsString("rutaInicial").contains("/")) {
                    throw BusinessException.internalServerError("Los carácteres \"/\" no son permitidos en la ruta inicial");
                }
                return consulta.getAsString("rutaInicial");
            case CASOS:
                return "Programacion".concat("/Casos/")
                        .concat(consulta.getAsString("caso"))
                        .concat("/")
                        .concat(consulta.getAsString("carpeta"));
            case ADMINISTRACION_COLABORADOR:
                return "Administracion/Colaboradores/".concat(consulta.getAsString("colaborador"));
            case ADMINISTRACION_DOCUMENTOS:
                return "Administracion/Documentos";
            case CARGA_MASIVAS:
                return "Programacion/Cargas Masivas";
            case RUTA_BASE_CASO:
                return "Programacion".concat("/Casos/")
                        .concat(consulta.getAsString("caso"));
            case DENUNCIAS:
                return "Programacion".concat("/Denuncias/")
                        .concat(consulta.getAsString("denuncia"));
            case TRASLADO:
                return "Administracion/Colaboradores/".concat(consulta.getAsString("nitOperador")).concat("/Traslados/")
                        .concat(consulta.getAsString("solicitudTraslado"));

            case MODIFICA_COLABORADOR:
                return "Administracion/Colaboradores/".concat(consulta.getAsString("nitColaborador")).concat("/Cambios de Estado/")
                        .concat(consulta.getAsString("idModificacion"));
            case ARCHIVOS_RESPALDO_CREDITO_FISCAL:
                return "Programacion".concat("/Solicitudes Credito Fiscal/").concat("/Periodos/")
                        .concat(consulta.getAsString("nit")).concat("/").concat(consulta.getAsString("periodo"));
            case CREDITO_FISCAL:
                return "Programacion/Solicitudes Credito Fiscal/".concat(consulta.getAsString("idSolicitud"));
            case DENUNCIASPAGO:
                return "Programacion".concat("/Denuncias/")
                        .concat(consulta.getAsString("denuncia")).concat("/Pago");
            case POSTERIORI:
                return "Programacion".concat("/Solicitudes Posteriori/").concat(consulta.getAsString("carpeta"));
            case PRESENCIAS:
                return "Programacion/Masivos/Presencias Fiscales/".concat(consulta.getAsString("presencia"));
            case ALCANCE_DENUNCIA:
                return "Programacion/Masivos/Alcances/".concat(consulta.getAsString("denuncia"));
            case GABINETE:
                return "Programacion/Masivos/Gabinete/".concat(consulta.getAsString("gabinete"));
            case PUNTO_FIJO:
                return "Programacion/Masivos/Puntos Fijos/".concat(consulta.getAsString("puntoFijo"));
            case CEDULA_CREDITO:
                return "Programacion".concat("/Solicitudes Credito Fiscal/")
                        .concat(consulta.getAsString("idSolicitud")
                                .concat("/").concat(consulta.getAsString("carpeta")));
            case ALCANCEMASIVO:
                SipfAlcance alcance = alcanceRepository.findById(Integer.parseInt(consulta.getAsString("id"))).orElseThrow(()
                        -> new BusinessException(HttpStatus.NOT_FOUND, "Alcance no encontrado."));
                if (alcance.getIdTipoAlcance() == 117) {
                    List<SipfDetalleCaso> detalleCaso = detalleCasoRepository.findByIdAlcance(alcance.getIdAlcance());
                    return "Programacion".concat("/Casos/").concat(String.valueOf(detalleCaso.get(0).getIdCaso()));
                }
                return "Programacion/Masivos/Alcances/".concat(consulta.getAsString("id"));

            default:
                return null;
        }
    }

    /**
     * Metodo para obtener los nodos (carpetas,documentos) en base un sitio y
     * path
     *
     * @param tipo Detalle de los tipos de rutas permitidos.
     * @param body estrutura de consulta para path base
     * @since 2/12/2021
     * @return lista de nodos
     */
    public NodosACSDto getNodosACSBySiteAndPath(TipoRuta tipo, Map<String, Object> body) {
        String path = getBaseRootByParams(tipo, body);
        log.debug(path);
        if (path == null) {
            throw new ResourceNotFoundException("Error al construir la ruta");
        }
        return getNodeIdEncrypted(path);
    }

    /**
     * Metodo para crear una carpeta en un nodo padre
     *
     * @param nodoId indentificador del nodo padre
     * @param nombre nombre de la carpeta a crear
     * @param propiedades propiedades de la carpeta
     * @since 2/12/2021
     * @return lista de nodos
     */
    public NodosACSDto createFolderNodeACS(String nodoId, String nombre, NodeDataDto[] propiedades) {
        try {
            String nombreEncript = encryptService.encrypt(EncryptService.ACS, nombre);
            //log.debug(" nombre en el nodos asc " + nombre);
            String ruta = "sat-gestor-app/content/sites/folders/".concat(nodoId).concat("/folders?path=").concat(nombreEncript);
            return consumosService.consume(propiedades, ruta, NodosACSDto.class, HttpMethod.POST);
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    @JsonRawValue
    public List<FolderCreatedDto> replaceFile(String id, MultipartFile file) {
        //final String path = config.getPingUrlInterna() + "sat-gestor-app/content/sites/folders/" + id + "/multiple-files";
        final String updatePath = config.getPingUrlInterna() + "sat-gestor-app/content/sites/files/";
        //final NodeChildren children = getNodosACSByNodeIdFather(id, "", "", "");
        //log.debug("node children " + children);

        //final MultiValueMap<String, Object> newFiles = new LinkedMultiValueMap<>();
        final List<FolderCreatedDto> response = new ArrayList<>();

        final MultiValueMap<String, Object> oldFiles = new LinkedMultiValueMap<>();

        oldFiles.add("file", file.getResource());
        //oldFiles.add("fileName", file.getOriginalFilename());

        response.add(consumosService.consume(
                oldFiles,
                updatePath + id,
                new ParameterizedTypeReference<FolderCreatedDto>() {
        },
                HttpMethod.PUT,
                new HttpHeaders() {
            {
                setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                setContentType(MediaType.MULTIPART_FORM_DATA);
            }
        }));
        return response;
    }

    @JsonRawValue
    public List<FolderCreatedDto> uploadFiles(String folder, List<MultipartFile> files) {
        final String path = config.getPingUrlInterna() + "sat-gestor-app/content/sites/folders/" + folder + "/multiple-files";
        final String updatePath = config.getPingUrlInterna() + "sat-gestor-app/content/sites/files/";
        final NodeChildren children = getNodosACSByNodeIdFather(folder, "", "", "");
        //log.debug("node children " + children);

        final MultiValueMap<String, Object> newFiles = new LinkedMultiValueMap<>();
        final List<FolderCreatedDto> response = new ArrayList<>();

        files.forEach((file) -> {

            if (children != null) {
                List<NodeEntries> entries = children.getList().getEntries()
                        .stream()
                        .filter(
                                item -> item.getEntry()
                                        .getName()
                                        .equals(file.getResource().getFilename())
                        ).collect(Collectors.toList());

                if (entries.size() > 0) {
                    final MultiValueMap<String, Object> oldFiles = new LinkedMultiValueMap<>();
                    oldFiles.add("file", file.getResource());
                    response.add(consumosService.consume(
                            oldFiles,
                            updatePath + entries.get(0).getEntry().getId(),
                            new ParameterizedTypeReference<FolderCreatedDto>() {
                    },
                            HttpMethod.PUT,
                            new HttpHeaders() {
                        {
                            setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                            setContentType(MediaType.MULTIPART_FORM_DATA);
                        }
                    }));
                }
            }
            newFiles.add("file", file.getResource());

        });

        response.addAll(consumosService.consume(
                newFiles,
                path,
                new ParameterizedTypeReference<List<FolderCreatedDto>>() {
        },
                HttpMethod.POST,
                new HttpHeaders() {
            {
                setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                setContentType(MediaType.MULTIPART_FORM_DATA);
            }
        }
        ));
        return response;
    }

    /**
     * Metodo para obtener el nodeId encriptado de una folder, archivo, o crear
     * la ruta si no se encuenta
     *
     * @param path ruta a consultar
     * @since 07/01/2022
     * @return lista de nodos
     */
    private NodosACSDto getNodeIdEncrypted(String path) {
        NodosACSDto node = null;
        try {
            //log.debug("justo antes de llamar al consume ");
            String ruta = "sat-gestor-app/content/sites/".concat(encryptService.encrypt(EncryptService.ACS, config.getSiteACS())).concat("/nodes?path=").concat(encryptService.encrypt(EncryptService.ACS, path));
            node = consumosService.consume(null, ruta, NodosACSDto.class, HttpMethod.GET);
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                List<String> parts = new ArrayList(Arrays.asList(path.split("/")));
                String parent = parts.remove(0);
                final NodosACSDto parentNode = getNodeIdEncrypted(parent);
                node = createFolderNodeACS(parentNode.getId(), String.join("/", parts), new NodeDataDto[]{});

            }
        }
        return node;
    }

    /* Metodo para obtener la tarea del proceso
     *
     * @param intanceId indentificador de la instancia
     * @since 13/07/2022
     * @return identificadores de la tarea
     */
    private TaskDto getTaskByIntanceId(String intanceId) {
        try {
            String ruta = "sat-gestor-app/tasks/next/".concat(encryptService.encrypt(EncryptService.APS, intanceId));
            return consumosService.consume(null, ruta, TaskDto.class, HttpMethod.GET);
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /* Metodo para Cancelar una tarea del proceso
     *
     * @param intanceId indentificador de la instancia
     * @since 28/10/2022
     * @return resultado de la tarea
     */
    public void cancelTaskByInstanceId(String intanceId) {
        try {
            String ruta = "sat-gestor-app/process-instances/".concat(encryptService.encrypt(EncryptService.APS, intanceId));
            consumosService.consume(null, ruta, Object.class, HttpMethod.DELETE);
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /* Metodo para completar una tarea del proceso
     *
     * @param intanceId indentificador de la instancia
     * @since 13/07/2022
     * @return resultado de la tarea
     */
    public String completaTaksByIntanceId(String intanceId, TaskCompleteAPSDto bodyTaks) {

        try {
            //log.debug("=>" + bodyTaks);
            TaskDto taskInformationAPS = this.getTaskByIntanceId(intanceId);
            String ruta = "sat-gestor-app/tasks/complete/".concat(taskInformationAPS.getTaskIdEnc());
            return consumosService.consume(bodyTaks, ruta, String.class, HttpMethod.POST);
        } catch (RestClientException e) {
            throw e;
        }
    }

    public StartProcessDto startProces(Integer id, Object data) {
        StartProcessDto response = consumosService.consume(
                data,
                "sat-gestor-app/process-instances/app-definition/" + consumosService.getAppId(id),
                StartProcessDto.class,
                HttpMethod.POST
        );

        response.setId(encryptService.decrypt(APS, response.getId()));

        return response;
    }

    /**
     * Metodo para obtener los nodos hijos en base a nodeId padre
     *
     * @param nodeId Identificador del nodo padre
     * @param include
     * @param skipCount
     * @param maxItems
     * @since 25/07/2022
     * @return lista de nodos
     */
    public NodeChildren getNodosACSByNodeIdFather(String nodeId, String include, String skipCount, String maxItems) {
        try {
            //log.debug(" este es en el node children 286 ");
            String ruta = "sat-gestor-app/content/sites/folders/".concat(nodeId).concat("/nodes-children").concat(applyQueryFilter(null, include, skipCount, maxItems));
            //log.debug(ruta);
            return consumosService.consume(null, ruta, NodeChildren.class, HttpMethod.GET);
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /**
     * Construye los parametros adicionales o query de una peticion
     *
     * @param relativePath
     * @param include
     * @param skipCount
     * @param maxItems
     * @return
     */
    private String applyQueryFilter(String relativePath, String include, String skipCount, String maxItems) {
        StringBuilder ret = new StringBuilder();
        if (!StringUtils.isEmpty(relativePath)) {
            ret.append(StringUtils.isEmpty(ret) ? "?" : "&").append("relativePath=").append(relativePath);
        }
        if (!StringUtils.isEmpty(include)) {
            ret.append(StringUtils.isEmpty(ret) ? "?" : "&").append("include=").append(include);
        }
        if (skipCount != null) {
            ret.append(StringUtils.isEmpty(ret) ? "?" : "&").append("skipCount=").append(skipCount);

        }
        if (maxItems != null) {
            ret.append(StringUtils.isEmpty(ret) ? "?" : "&").append("maxItems=").append(maxItems);
        }
        return ret.toString();
    }

    /* Metodo para obtener un archivo por medio del nodeId
     *
     * @param nodeId indentificador del nodo
     * @since 17/01/2023
     * @return archivo
     */
    public void contentSitesFileById(String nodeId, HttpServletResponse response) throws IOException {
        try {
            String ruta = "sat-gestor-app/content/sites/files/".concat(nodeId);
            byte[] object = consumosService.consume(null, ruta, byte[].class, HttpMethod.GET);

            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment");
            response.setContentLength(object.length);

            response.getOutputStream().write(object);
            response.getOutputStream().flush();

        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /* Metodo para obtener informacion de las versiones de un archivo en base a su Id
     *
     * @param nodeId indentificador del nodo
     * @since 18/01/2023
     * @return archivo
     */
    public NodeChildren contentSitesFileByIdVersions(String nodeId, String include, String skipCount, String maxItems) {
        try {
            String ruta = "sat-gestor-app/content/sites/files/".concat(nodeId).concat("/versions" + applyQueryFilter(null, include, skipCount, maxItems));
            NodeChildren object = consumosService.consume(null, ruta, NodeChildren.class, HttpMethod.GET);

            return object;
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    /* Metodo para obtener la informacion de un nodo en base a su ID
     * @param nodeId indentificador del nodo
     * @param file archivo a reemplazar
     * @since 19/01/2023
     * @return FolderCreatedDto
     */
    public NodeEntry contentSitesNodeById(String nodeId) {
        try {
            String ruta = "sat-gestor-app/content/sites/nodes/".concat(nodeId);
            NodeEntry object = consumosService.consume(null, ruta, NodeEntry.class, HttpMethod.GET);

            return object;
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

    }

    /* Metodo para actualizar archivo en base a su ID
     *
     * @param nodeId indentificador del nodo
     * @param file archivo a actualizar
     * @since 19/01/2023
     * @return FolderCreatedDto
     */
    public FolderCreatedDto contentSitesNodeByIdUpdate(String nodeId, MultipartFile file) {
        try {
            final MultiValueMap<String, Object> updateFiles = new LinkedMultiValueMap<>();

            updateFiles.add("files", file.getResource());
            updateFiles.add("nodeDataDto", null);

            String ruta = config.getPingUrlInterna() + "sat-gestor-app/content/sites/files/".concat(nodeId);

            return consumosService.consume(
                    updateFiles,
                    ruta,
                    new ParameterizedTypeReference<FolderCreatedDto>() {
            },
                    HttpMethod.PUT,
                    new HttpHeaders() {
                {
                    setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                    setContentType(MediaType.MULTIPART_FORM_DATA);
                }
            });

        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }
    }

    public Boolean contentSitesFoldersSetNameByIdUpdate(String nodeId, String newName, NodeDataDto metaData) {

        try {
            String param = newName != null ? !StringUtils.isEmpty(newName) ? "?newName=".concat(newName) : "" : "";
            String ruta = "sat-gestor-app/content/sites/folders/".concat(nodeId) + param;

            consumosService.consume(metaData, ruta, Object.class, HttpMethod.PUT);

            return true;
        } catch (RestClientException e) {
            throw new ResourceNotFoundException(e.getMessage());
        }

    }
}
