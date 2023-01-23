/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.constants.Catalog;
import gt.gob.sat.sat_ifi_sipf.dtos.AsignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.BaseColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.EmpleadoFromProsisDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.HisotrialEstadoColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ReasignacionCasosDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasos;
import gt.gob.sat.sat_ifi_sipf.models.SipfAsignacionCasosId;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfGrupoTrabajo;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialEstadoColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfIntegranteGrupo;
import gt.gob.sat.sat_ifi_sipf.models.SipfUnidadAdministrativa;
import gt.gob.sat.sat_ifi_sipf.projections.AsignacionCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresGerenciaProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignarCasosDetalleProjection;
import gt.gob.sat.sat_ifi_sipf.projections.DesasignarCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.EquiposYUnidadesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.GrupoColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignCasesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.ReasignacioCasosProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SupervidoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.TrasladoColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.AsignacionCasosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.GruposTrabajoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialEstadoColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.IntegranteEquipoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.SolicitudTrasladoColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.UnidadesAdministrativaRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author ruarcuse
 */
@Transactional
@Service
@Slf4j
public class ColaboradorService {

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CatDatoRepository catDatoRpository;

    @Autowired
    private AsignacionCasosRepository asignacionInsumosRepository;

    @Autowired
    private HistorialEstadoColaboradorRepository hisotiralEstadosRepository;

    @Autowired
    private IntegranteEquipoRepository integrantesEquipoRepository;

    @Autowired
    private GruposTrabajoRepository gruposTrabajoRepository;

    @Autowired
    private AsignacionCasosRepository asignacionCasoRepository;

    @Autowired
    private CasosService casosService;

    @Autowired
    private GeneralServices generalService;

    @Autowired
    private Detector detector;

    @Autowired
    private SolicitudTrasladoColaboradorRepository solicitudTrasladoColaboradorRepository;

    @Autowired
    private ConsumosService consumosService;

    @Autowired
    private UnidadesAdministrativaRepository unidadesAdministrativaRepository;

    @Autowired
    private SolicitudTrasladoColaboradorRepository trasladoRepository;

    @Autowired
    ContribuyenteService contribuyenteService;

    @Autowired
    private CorreoService correoService;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    /**
     * Metodo para obtener todos los colaboradores
     *
     * @author Gabriel runao (agaruanom)
     * @param logged
     * @since 18/02/2022
     * @return getall
     */
    @Transactional(readOnly = true)
    public List<ColaboradoresProjection> getCollaborators(UserLogged logged) {
        //log.debug("Obteniendo todos los colaboradores");
        //retorna a los colaboradores activos 
        return colaboradorRepository.getAll(Catalog.Management.Collaborators.Status.ACTIVE,
                Catalog.Management.EmployeeProfileAssignmen.Status.ACTIVE,
                Catalog.Management.Collaborators.Status.REMOVED,
                logged.getNit());
    }

    /**
     * Metodo para crear un coloborador
     *
     * @author Gabriel runao (agaruanom)
     * @param dto colaborador dto
     * @since 18/02/2022
     * @return collaborator
     */
    @Transactional
    public SipfColaborador createCollaborator(ColaboradorDto dto, UserLogged logged) {
        final SipfColaborador checkCollaborator = colaboradorRepository.findById(dto.getIdColaborador()).orElse(null);

        if (checkCollaborator != null) {
            if (checkCollaborator.getIdEstado() != 407) {
                throw new BusinessException(HttpStatus.FOUND, "El colaborador que intenta agregar ya se encuentra registrado.");
            } else if (checkCollaborator.getIdEstado() == 407) {
                checkCollaborator.setIdEstado(4);
                checkCollaborator.setUsuarioModifica(logged.getLogin());
                checkCollaborator.setIpModifica(logged.getIp());
                checkCollaborator.setFechaModifica(new Date());

                final SipfHistorialEstadoColaborador collaboratorHistory = new SipfHistorialEstadoColaborador();

                collaboratorHistory.setNitColaborador(checkCollaborator.getNit());
                collaboratorHistory.setIdEstado((int) checkCollaborator.getIdEstado());
                collaboratorHistory.setFechaInicio(new Date());
                collaboratorHistory.setFechaFin(new Date());
                collaboratorHistory.setUsuarioCrea(logged.getLogin());
                collaboratorHistory.setIpCrea(logged.getIp());
                collaboratorHistory.setFechaCrea(new Date());
                hisotiralEstadosRepository.save(collaboratorHistory);

                return colaboradorRepository.save(checkCollaborator);
            }
        }

        final SipfColaborador collaborator = colaboradorRepository.save(
                SipfColaborador.builder().nit(dto.getIdColaborador())
                        .nombres(dto.getNombresColaborador())
                        .puestoTrabajo(dto.getPuestoTrabajo())
                        .idEstado(dto.getIdEstado().longValue())
                        .login(dto.getLoginColaborador())
                        .idGerencia(unidadesAdministrativaRepository.findByIdUnidadProsis(dto.getIdGerencia()).getId())
                        .correo(dto.getCorreo())
                        .usuarioModifica(logged.getLogin())
                        .fechaModifica(new Date())
                        .ipModifica(logged.getIp())
                        .build()
        );
        final SipfHistorialEstadoColaborador collaboratorHistory = new SipfHistorialEstadoColaborador();

        collaboratorHistory.setNitColaborador(collaborator.getNit());
        collaboratorHistory.setIdEstado(dto.getIdEstado());
        collaboratorHistory.setFechaInicio(new Date());
        collaboratorHistory.setFechaFin(new Date());
        collaboratorHistory.setUsuarioCrea(logged.getLogin());
        collaboratorHistory.setIpCrea(logged.getIp());
        collaboratorHistory.setFechaCrea(new Date());
        hisotiralEstadosRepository.save(collaboratorHistory);

        return collaborator;
    }

    /**
     * Metodo para crear historial cuando se crea,actuliza o elimina un
     * colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param dto historial de colaborador
     * @since 18/02/2022
     * @return collaboratorHistory
     */
    @Transactional
    public SipfHistorialEstadoColaborador saveHistory(HisotrialEstadoColaboradorDto dto) {

        final SipfHistorialEstadoColaborador collaboratorHistory = new SipfHistorialEstadoColaborador();

        collaboratorHistory.setNitColaborador(dto.getNitColaborador());
        collaboratorHistory.setIdEstado(dto.getIdEstado());
        collaboratorHistory.setFechaInicio(dto.getFechaInicio());
        collaboratorHistory.setFechaFin(dto.getFechaFin());
        collaboratorHistory.setUsuarioCrea(detector.getLogin());
        collaboratorHistory.setIpCrea(detector.getIp());
        collaboratorHistory.setFechaCrea(new Date());

        return hisotiralEstadosRepository.save(collaboratorHistory);
    }

    /**
     * Metodo para actulizar un colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param nit indentificador tributario
     * @param dto base del colaborador
     * @since 18/02/2022
     * @return collaborator
     */
    @Transactional
    public SipfHistorialEstadoColaborador updateCollaborator(String nit, BaseColaboradorDto dto, UserLogged logged) {
        final SipfColaborador collaborator = colaboradorRepository.findById(nit).orElse(null);
        final CatalogDataProjection estado = catDatoRpository.findByCodigo(dto.getIdEstado());
        final SipfHistorialEstadoColaborador collaboratorHistory = new SipfHistorialEstadoColaborador();
        String correo = "Se le informa que se le cambio de estado a " + estado.getNombre();

        collaborator.setNombres(dto.getNombresColaborador());
        collaborator.setPuestoTrabajo(dto.getPuestoTrabajo());
        collaborator.setIdEstado(dto.getIdEstado());
        collaborator.setLogin(dto.getLoginColaborador());
        collaborator.setCorreo(dto.getCorreo());
        collaborator.setUsuarioModifica(logged.getLogin());
        collaborator.setFechaModifica(new Date());
        collaborator.setIpModifica(logged.getIp());

        colaboradorRepository.save(collaborator);

        collaboratorHistory.setNitColaborador(collaborator.getNit());
        collaboratorHistory.setIdEstado(dto.getIdEstado());
        collaboratorHistory.setFechaInicio(dto.getFechaInicio());
        collaboratorHistory.setFechaFin(dto.getFechaFin());
        collaboratorHistory.setUsuarioCrea(logged.getLogin());
        collaboratorHistory.setIpCrea(logged.getIp());
        collaboratorHistory.setFechaCrea(new Date());
        hisotiralEstadosRepository.save(collaboratorHistory);

        correoService.envioCorreo(collaborator.getCorreo(), correo, "Cambio de estado de colaborador");
        return collaboratorHistory;
    }

    /**
     * Metodo para eliminacion logica para un colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param id indentificador tributario NIT
     * @param dto HisotrialEstadoColaboradorDto
     * @since 18/02/2022
     * @return collaborator
     */
    @Transactional
    public boolean deleteCollaborator(String id, UserLogged logged) {
        final SipfColaborador collaborator = colaboradorRepository.findById(id).orElse(null);
        final SipfHistorialEstadoColaborador collaboratorHistory = new SipfHistorialEstadoColaborador();
        final List<SipfIntegranteGrupo> integranteCollaborator = integrantesEquipoRepository.findByNitAndIdEstado(id, 170);
        final SipfGrupoTrabajo groupCollaborator = integranteCollaborator.isEmpty() ? new SipfGrupoTrabajo()
                : gruposTrabajoRepository.findById(integranteCollaborator.get(0).getIdGrupo()).orElse(new SipfGrupoTrabajo());

        if (!integranteCollaborator.isEmpty() && groupCollaborator.getIdEstado() != 164) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador que desea eliminar pertenece a un grupo de trabajo, por favor validar.");
        } else if (!trasladoRepository.findByNitProfesionalAndIdEstado(id, 193).isEmpty()) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador que desea eliminar tiene una solicitud de traslado en proceso, por favor validar.");
        }

        collaborator.setIdEstado(407);
        collaborator.setUsuarioModifica(logged.getLogin());
        collaborator.setIpModifica(logged.getIp());
        collaborator.setFechaModifica(new Date());

        collaboratorHistory.setNitColaborador(id);
        collaboratorHistory.setIdEstado(5);
        collaboratorHistory.setFechaInicio(collaborator.getFechaModifica());
        collaboratorHistory.setFechaFin(new Date());
        collaboratorHistory.setUsuarioCrea(logged.getLogin());
        collaboratorHistory.setIpCrea(logged.getIp());
        collaboratorHistory.setFechaCrea(new Date());

        colaboradorRepository.save(collaborator);

        hisotiralEstadosRepository.save(collaboratorHistory);

        return true;
    }

    /**
     * Metodo para traer una lista de colaboradores por medio de la gerencia
     *
     * @author Gabriel runao (agaruanom)
     * @param idGerencia identificador unico de la gerencia
     * @since 18/02/2022
     * @return Collaborator
     */
    @Transactional(readOnly = true)
    public List<ColaboradoresGerenciaProjection> managementCollaborator(Integer idGerencia) {
        //log.debug("Obteniendo todo los caloboradores y en que gerencia estan en base a su id_gerencia");
        final List<ColaboradoresGerenciaProjection> Collaborator = colaboradorRepository.managementCollaborator(idGerencia);

        return Collaborator;
    }

    /**
     * Metodo obtener un colaborador por medio de su id (NIT)
     *
     * @author Gabriel runao (agaruanom)
     * @param pId identificador unico NIT
     * @since 18/02/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public ColaboradoresProjection getCollaboratorId(String pId) {
        //log.debug("Obteniendola dato del colaborador");

        ColaboradoresProjection respuesta = colaboradorRepository.getByNit(pId);
        if (respuesta == null) {
            return null;
        }
        return respuesta;

    }

    /**
     * Metodo obtener un colaborador por medio de su id (LOGIN)
     *
     * @author crramosl
     * @param login identificador unico login
     * @since 18/02/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public Optional<SipfColaborador> getCollaboratorLogin(String login) {
        //log.debug("Obteniendola dato del colaborador");
        return colaboradorRepository.findByLogin(login);
    }

    /**
     * Metodo obtener casos asignado a un colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param nit indentificador tributario NIT
     * @since 18/02/2022
     * @return nit
     */
    @Transactional(readOnly = true)
    public List<ReasignacioCasosProjection> assignedCases(String nit) {
        //log.debug("Obteniendo datos asignados");
        return colaboradorRepository.annexOne(nit);
    }

    /**
     * Metodo obtener los grupo donde pertenece un colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param nit indentificador tributario NIT
     * @param idRol indentificador tributario NIT
     * @since 18/02/2022
     * @return nit
     */
    @Transactional(readOnly = true)
    public List<GrupoColaboradoresProjection> getPartnerGroup(String nit, List<String> idRol) {
        List<GrupoColaboradoresProjection> getMember = new ArrayList();
        //log.debug("Obteniendo la lista dr grupo que pertenece el colaborador");
        List<Integer> listRoles = new ArrayList<>();
        if (idRol.contains("AdministrativoSIPFAdministrador")) {
            getMember = this.integrantesEquipoRepository.getAllCollaboratorsToReassign();
        } else {
            idRol.forEach(action -> {
                switch (action) {
                    case "AdministrativoSIPFAprobador":
                        listRoles.add(2);
                        break;
                    case "AdministrativoSIPFAutorizador":
                        listRoles.add(1);
                        break;
                }
            });
            getMember = this.integrantesEquipoRepository.getCollaboratorsToReassign(listRoles, nit);
        }
        return getMember;
    }

    public List<ReasignCasesProjection> getCasesForUnassing(String pNitProfesional, Integer pIdCaso, String pNitContribuyente, UserLogged user) {
        List<ReasignCasesProjection> getCases = new ArrayList();
        List<Integer> listRoles = new ArrayList<>();
        pNitProfesional = pNitProfesional == null ? "" : (String) pNitProfesional;
        pNitContribuyente = pNitContribuyente == null ? "" : (String) pNitContribuyente;
        pIdCaso = pIdCaso == null ? 0 : pIdCaso;

        if (user.getRoles().contains("AdministrativoSIPFAdministrador")) {
            getCases = this.integrantesEquipoRepository.getAllCasesForUnassign(pNitProfesional, pIdCaso, pNitContribuyente);
        } else {
            user.getRoles().forEach(action -> {
                switch (action) {
                    case "AdministrativoSIPFAprobador":
                        listRoles.add(2);
                        break;
                    case "AdministrativoSIPFAutorizador":
                        listRoles.add(1);
                        break;
                }
            });
            getCases = this.integrantesEquipoRepository.getCasesForUnassign(pNitProfesional, pIdCaso, pNitContribuyente, listRoles, user.getNit());
        }

        return getCases;
    }

    /**
     * Metodo obtener los grupo donde pertenece un colaborador
     *
     * @author Gabriel runao (agaruanom)
     * @param idCaso indentificador unico de idcaso
     * @param dto ReasignacionCasosDto
     * @since 18/02/2022
     * @return true
     */
    @Transactional(rollbackFor = {Exception.class})
    public boolean reassignmentCases(List<ReasignacionCasosDto> dto, List<Integer> idCaso) {
        //log.debug("Se reasigna el caso al nuevo colaborador.");
        try {
            for (int i = 0; i < idCaso.size(); i++) {
                SipfAsignacionCasos casos = new SipfAsignacionCasos();
                SipfAsignacionCasos cases = asignacionCasoRepository.findById(new SipfAsignacionCasosId(idCaso.get(i), dto.get(i).getNitAnterior())).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
                if (cases != null) {
                    cases.setIdEstado(139);

                    //log.debug("paso por esta parte");
                    SipfHistoricoOperaciones history = historialOperacionesRepository.save(SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_asignacion_casos")
                            .idCambioRegistro(cases.getId().toString())
                            .idTipoOperacion(406)
                            .usuarioModifica(detector.getLogin())
                            .ipModifica(detector.getIp())
                            .fechaModifica(new Date())
                            .data(new Gson().toJson(cases.toString()))
                            .build());
                    //log.debug("paso por esta parte2" + history.getIdHistoricoOperacion());

                    asignacionCasoRepository.delete(cases);

                    casos.setId(new SipfAsignacionCasosId(idCaso.get(i), dto.get(0).getNit()));
                    casos.setIdEstado(138);
                    casos.setIdOrigen(cases.getIdOrigen());
                    casos.setIdGrupoAsignacion(cases.getIdGrupoAsignacion());
                    casos.setFechaModifica(new Date());
                    casos.setUsuarioModifica(detector.getLogin());
                    casos.setIpModifica(detector.getIp());
                    asignacionCasoRepository.save(casos);

                }
            }
        } catch (Exception e) {
            log.error("Error al consumir el servicio reasignar: " + e);
            return false;
        }
        return true;
    }

    public ResponseEntity<?> assignmentCases(AsignacionCasosDto pCasoDto, UserLogged user) {

        List<AsignacionCasosProjection> vListaCasosAsignar = casosService.findAssignmentCases(pCasoDto.getCantidadCasos(), pCasoDto.getIdImpuesto(), pCasoDto.getNombreCaso(), pCasoDto.getIdGerencia());

        Integer vIdGrupo = generalService.nextValSequence("sat_ifi_sipf.sipf_seq_id_grupo_asignacion");

        List<SipfAsignacionCasos> vAsignacionCasos = new ArrayList<>();

        vListaCasosAsignar.forEach(caso -> {
            SipfAsignacionCasosId vIdAsignacionCasos = new SipfAsignacionCasosId();
            SipfAsignacionCasos vAsignacion = new SipfAsignacionCasos();
            vAsignacion.setIdEstado(15);
            vAsignacion.setIdOrigen(140);
            vAsignacion.setIpModifica(detector.getIp());
            vAsignacion.setFechaModifica(new Date());
            vAsignacion.setIdGrupoAsignacion(vIdGrupo);
            vAsignacion.setUsuarioModifica(detector.getLogin());

            vIdAsignacionCasos.setIdCaso(caso.getIdCaso());
            vIdAsignacionCasos.setNitColaborador(pCasoDto.getNitColaborador());
            vAsignacion.setId(vIdAsignacionCasos);

            vAsignacionCasos.add(vAsignacion);

            casosService.alterCase(caso.getIdCaso(), CasosDto.builder().estado(16).build(), user);
        });
        asignacionInsumosRepository.saveAll(vAsignacionCasos);
        return ResponseEntity.ok().build();
    }

    /**
     * Metodo obtener un grupo de supervisores en estado activo
     *
     * @author Alex Estrada / Debora Top (abaestrad)
     * @since 03/03/2022
     * @return supervisorList
     */
    @Transactional(readOnly = true)
    public List<SupervidoresProjection> getSupervisor() {
        //log.debug("Obteniendo todos los supervidores");
        return colaboradorRepository.supervisorList();
    }

    /**
     * Metodo obtener un colaborador y sus autorizadores con equipos y unidades
     * por medio de su id (NIT)
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param pId identificador unico NIT OPERADOR
     * @param Prol identificador rol Aprobador
     * @param Prol1 identificador rol Operador
     * @since 23/05/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public List<TrasladoColaboradorProjection> getCollaboratoAndAuthByNit(String pId, UserLogged user) {
        //log.debug("Obteniendola dato del colaborador y su autorizador en base al nit");
        if (user.getRoles().contains("AdministrativoSIPFAdministrador")) {
            return colaboradorRepository.getColaboratorByTraslateAdministrator(pId, 1, 3);
        } else {
            return colaboradorRepository.getColaboratorByTraslate(pId, 1, 3, user.getNit());
        }

    }

    //Sobrecarga del metodo getCollaboratoAndAuthByNit
    @Transactional(readOnly = true)
    public List<TrasladoColaboradorProjection> getCollaboratoAndAuthByNit(String pId, Integer prol, Integer prol1, UserLogged user) {
        //log.debug("Obteniendola dato del colaborador y su autorizador en base al nit");
        return colaboradorRepository.getColaboratorByTraslate(pId, prol, prol1, user.getNit());
    }

    /**
     * metodo para obtener los colaboradores que esten bajo la supervision de un
     * nit y perfil dados
     *
     * @param pNit
     * @param pPerfilJefe
     * @param pPerfil
     * @return
     */
    @Transactional(readOnly = true)
    public List<ColaboradoresProjection> getCollaboratorsByNitPerfilJefeandPerfil(String pNit, Integer pPerfilJefe, Integer pPerfil) {
        //log.debug("Obteniendo todos los colaboradores");
        return colaboradorRepository.findByNitPerfilJefeandPerfil(pNit, pPerfilJefe, pPerfil);
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresProjection> getCollaboratorsByNitAndPefilGeneral(String pNit, Integer pPerfilJefe, Integer pPerfil) {
        //log.debug("Obteniendo todos los colaboradores");
        return colaboradorRepository.findByNitAndRolGeneral(pNit, pPerfilJefe, pPerfil);
    }

    /**
     *
     * /**
     * Metodo obtener un autorizador con equipos y unidades por medio de su id
     * (NIT)
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param pId identificador unico NIT Autorizador
     * @since 23/05/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public List<EquiposYUnidadesProjection> getTeamsAndUnitsByNit(String pId) {
        //log.debug("un autorizador con equipos y unidades por medio de su id (NIT)");
        return colaboradorRepository.getTeamsAndUnits(pId, 1, 163);
    }

    /**
     * Servicio que trae la informacion de un profesional en base a un
     * autorizador o aprobador.
     *
     * @since 6/14/2022
     * @author Luis Villagran (lfvillag)
     * @param nit
     * @return
     */
    @Transactional(readOnly = true)
    public List<DesasignarCasosProjection> getProfetionalByNitCases(String nit) {
        //log.debug("Obtiene los profesionales que pertenecen a un Autorizador o Aprobador.");
        return colaboradorRepository.fingByNitProfetional(nit);
    }

    /**
     * Servicio que trae la informacion de un profesional para desasignar.
     *
     * @since 6/14/2022
     * @author Luis Villagran (lfvillag)
     * @param nit
     * @return
     */
    @Transactional(readOnly = true)
    public List<SupervidoresProjection> getProfetionalDesasignar(String Nit) {
        //log.debug("Obtiene informacion del profesional para desasignar");
        return colaboradorRepository.getProfetionalDesasignar(Nit);
    }

    /**
     * Servicio que trae la informacion de un profesional para reasignar.
     *
     * @since 6/15/2022
     * @author Luis Villagran (lfvillag)
     * @param nit
     * @return
     */
    @Transactional(readOnly = true)
    public List<DesasignarCasosDetalleProjection> getProfetionalReasignar(String nit) {
        //log.debug("Obtiene informacion del profesional para reasignar");
        return colaboradorRepository.getPrfoetionalReasignar(nit);
    }

    public EmpleadoFromProsisDto getInfoFromProsis(String pNit) {

//        try {
//            this.contribuyenteService.getGeneralTaxpayerInformation(pNit);
//        } catch (Exception e) {
//            throw new BusinessException(HttpStatus.PRECONDITION_REQUIRED, "El NIT ingresado es inválido.");
//        }
        GeneralResponseDto<EmpleadoFromProsisDto> resultado = consumosService.consumeCompleteUrlSqlServer(null, "employee/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        //log.debug("el objecto es:" + resultado);
        if (resultado.getCode() == 1001) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador actualmente no sé encuentra registrado en el Sistema Prosis");
        }

        EmpleadoFromProsisDto list = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<EmpleadoFromProsisDto>() {
        });

        Optional<SipfUnidadAdministrativa> unit = Optional.ofNullable(unidadesAdministrativaRepository.findByIdUnidadProsis(list.getCodigoUnidad()));

        if (!unit.isPresent()) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El usuario no pertenece actualmente a la Intendencia de Fiscalizacion");
        }

        return list;
    }

    @Transactional
    public SipfColaborador updateJobPosition(String pNit, UserLogged logged) {
        GeneralResponseDto<EmpleadoFromProsisDto> resultado = consumosService.consumeCompleteUrlSqlServer(null, "employee/" + pNit, GeneralResponseDto.class, HttpMethod.GET);
        if (resultado.getCode() == 1001) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador actualmente no sé encuentra registrado en el Sistema Prosis");
        }
        EmpleadoFromProsisDto list = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<EmpleadoFromProsisDto>() {
        });

        System.out.println("nit encontrado:" + pNit + "Colaborador a buscar:" + list.getNit());
        if (!colaboradorRepository.findById(pNit).isPresent()) {
            throw new BusinessException(HttpStatus.NOT_FOUND, detector.getLocaleMessage("No se pudo actualizar la información del colaborador desde Prosis"));
        } else if (colaboradorRepository.findById(pNit).get().getIdEstado() == 407) {
            throw new BusinessException(HttpStatus.NOT_FOUND, detector.getLocaleMessage("No se pudo actualizar la información del colaborador desde Prosis"));
        }

        final SipfColaborador collaborator = colaboradorRepository.findById(pNit).orElse(null);
        Optional<SipfUnidadAdministrativa> unit = Optional.ofNullable(unidadesAdministrativaRepository.findByIdUnidadProsis(list.getCodigoUnidad()));
        if (unit.isPresent()) {
            collaborator.setIdGerencia(unidadesAdministrativaRepository.findByIdUnidadProsis(list.getCodigoUnidad()).getId());
        }

        if (!collaborator.getCorreo().equals(list.getCorreo())) {
            collaborator.setCorreo(list.getCorreo());
        }

        if (!collaborator.getLogin().equals(list.getLogin())) {
            collaborator.setLogin(list.getLogin());
        }

        collaborator.setPuestoTrabajo(list.getNombrePuesto());

        return colaboradorRepository.save(collaborator);

    }

    public List<ColaboradorProjection> getSubColaboratoresBySuperior(UserLogged logged, Integer pLevel) {
        return this.colaboradorRepository.findSubColaboratoresBySuperior(logged.getNit(), pLevel);
    }

    @Transactional(readOnly = true)
    public List<GrupoColaboradoresProjection> getMembers() {
        //log.debug("Obteniendo datos asignados");
        return integrantesEquipoRepository.getMembers();
    }

    @Transactional(readOnly = true)
    public List<ColaboradoresProjection> getOperadorGrups(UserLogged logged) {
        List<Integer> grupo = colaboradorRepository.getIdGrups(logged.getNit());

        return this.colaboradorRepository.getOperadorGrups(grupo);
    }
    
    @Transactional
    public EmpleadoFromProsisDto getGeneralInfoByLogin(String login){
         GeneralResponseDto<EmpleadoFromProsisDto> resultado = consumosService.consumeCompleteUrlSqlServer(null, "employee/" + login, GeneralResponseDto.class, HttpMethod.GET);
        //log.debug("el objecto es:" + resultado);
        if (resultado.getCode() == 1001) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador actualmente no sé encuentra registrado en el Sistema Prosis");
        }

        EmpleadoFromProsisDto collaborator = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<EmpleadoFromProsisDto>() {
        });

        return collaborator;
    }
    
    @Transactional(readOnly = true)
    public boolean hasWorkload(String nit){
        return colaboradorRepository.hasWorkload(nit);
    }
}
