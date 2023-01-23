/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorPerfilDto;
import gt.gob.sat.sat_ifi_sipf.dtos.CollaboratorProfileDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ErrorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GruposTrabajoDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GruposTrabajoDto;
import gt.gob.sat.sat_ifi_sipf.dtos.SolicitudTrasladoColaboradorDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfil;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfilId;
import gt.gob.sat.sat_ifi_sipf.models.SipfGrupoTrabajo;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistorialComentarios;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.models.SipfIntegranteGrupo;
import gt.gob.sat.sat_ifi_sipf.models.SipfIntegranteGrupoId;
import gt.gob.sat.sat_ifi_sipf.models.SipfTrasladoColaborador;
import gt.gob.sat.sat_ifi_sipf.models.SipfUnidadAdministrativa;
import gt.gob.sat.sat_ifi_sipf.projections.GruposTrabajoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.OperatorProjection;
import gt.gob.sat.sat_ifi_sipf.projections.SolicitudTrasladoColaboradorProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorPerfilRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.GruposTrabajoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialComentariosRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.IntegranteEquipoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.UnidadesAdministrativaRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toSet;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import gt.gob.sat.sat_ifi_sipf.repositories.SolicitudTrasladoColaboradorRepository;
import java.util.Objects;
import java.util.Optional;
import org.springframework.http.HttpStatus;

/**
 * @author crramosl
 */
@Service
@Transactional
@Slf4j
public class GruposTrabajoService {

    @Autowired
    private GruposTrabajoRepository workGroupRepository;

    @Autowired
    private IntegranteEquipoRepository memberGroupRepository;

    @Autowired
    private UnidadesAdministrativaRepository unidadesAdministrativasRepository;

    @Autowired
    private PerfilService profile;

    @Autowired
    private ColaboradorPerfilRepository colaboradorPerfilRepository;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    @Autowired
    private HistorialComentariosRepository historialComentariosRepository;

    @Autowired
    private Detector detector;

    @Autowired
    private SolicitudTrasladoColaboradorRepository solicitudTrasladoColaboradorRepository;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Autowired
    private CorreoService correoService;

    @Transactional(readOnly = true)
    public List<GruposTrabajoProjection> getWorkGroups(boolean incluirInactivos) {
        final List<Integer> status = new ArrayList<>(Arrays.asList(163));
        if (incluirInactivos) {
            status.add(164);
        }

        return workGroupRepository.getWorkGroups(status);
    }

    @Transactional(readOnly = true)
    public GruposTrabajoDetalleDto getGroupById(Integer groupId) {
        GruposTrabajoProjection group = workGroupRepository.getWorkGroup(groupId);
        return new GruposTrabajoDetalleDto(
                group,
                memberGroupRepository.getMemberAndProfileByGroupId(groupId),
                unidadesAdministrativasRepository.findFatherUnitsByChildrenId(group.getUnidad())
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public SipfGrupoTrabajo createWorkGroup(GruposTrabajoDto dto, UserLogged userLogged) {

        List<ErrorDto> errors = memberGroupRepository.getRepeatedMembers(dto.getIntegrantes().stream().map(CollaboratorProfileDto::getNit).collect(Collectors.toList())).stream().map(item -> {
            return ErrorDto.builder()
                    .param("Integrante - " + item.getNit())
                    .value(item.getNombre())
                    .description(Message.OPERATOR_ONCE_PER_GROUP.getText())
                    .build();
        }).collect(Collectors.toList());

        if (errors.size() > 0) {
            throw BusinessException.badRequestError(Message.OPERATORS_ALREADY_IN_GROUP.getText(), errors);
        }

        final SipfGrupoTrabajo group = workGroupRepository.save(
                SipfGrupoTrabajo
                        .builder()
                        .nombre(dto.getNombre())
                        .descripcion(dto.getDescripcion())
                        .idEstado(163)
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .idGerencia(dto.getIdUnidadAdministrativa())
                        .build()
        );

        final List<SipfIntegranteGrupo> integrantes = dto.getIntegrantes()
                .stream()
                .filter(integrante -> integrante.getNit() != null || !StringUtils.isEmpty(integrante.getNit()))
                .map(item -> new SipfIntegranteGrupo(
                group.getId(),
                item.getNit(),
                userLogged.getLogin(),
                new Date(),
                userLogged.getIp(),
                item.getProfile(),
                item.getRol(),
                170
        )).collect(Collectors.toList());

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_grupo_trabajo")
                        .idCambioRegistro(String.valueOf(group.getId()))
                        .idTipoOperacion(404)
                        .data(new Gson().toJson(group))
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .build()
        );

        integrantes.forEach(integrante -> {
            final SipfColaboradorPerfil perfil = profile.findProfileCollaboratorById(new SipfColaboradorPerfilId(integrante.getNit(), integrante.getIdPerfil()), 162).orElse(null);
            if (perfil != null) {
                perfil.setEstado(161);
                colaboradorPerfilRepository.save(perfil);
            } else {
                profile.createColaboradorPerfil(new ColaboradorPerfilDto(integrante.getNit(), integrante.getIdPerfil(), 161));
            }
            memberGroupRepository.save(integrante);
            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_integrante_grupo")
                            .idCambioRegistro(integrante.getNit())
                            .idTipoOperacion(404)
                            .data(new Gson().toJson(integrante))
                            .fechaModifica(new Date())
                            .usuarioModifica(userLogged.getLogin())
                            .ipModifica(userLogged.getIp())
                            .build()
            );
        });

        return group;
    }

    @Transactional(rollbackFor = Exception.class)
    public SipfGrupoTrabajo updateWorkGroup(Long id, GruposTrabajoDto dto, UserLogged userLogged) {
        SipfGrupoTrabajo group = workGroupRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.GROUP_NOT_FOUND.getText(id))
        );

        JsonObject dataGroup = new JsonObject();
        dataGroup.add("valorAnterior", new Gson().toJsonTree(group));

        List<SipfIntegranteGrupo> members = memberGroupRepository.findByIdGrupo(id);

        group.setNombre(dto.getNombre());
        group.setDescripcion(dto.getDescripcion());
        group.setIdEstado(dto.getEstado());
        group.setFechaModifica(new Date());
        group.setUsuarioModifica(userLogged.getLogin());
        group.setIpModifica(userLogged.getIp());
        group.setIdGerencia(dto.getIdUnidadAdministrativa());

        List<SipfIntegranteGrupo> deleted = members.stream()
                .filter((t) -> !dto.getIntegrantes().stream().map(CollaboratorProfileDto::getNit).collect(toSet()).contains(t.getNit()))
                .filter((t) -> !dto.getIntegrantes().stream().map(CollaboratorProfileDto::getProfile).collect(toSet()).contains(t.getIdPerfil()))
                .collect(Collectors.toList());
        List<SipfIntegranteGrupo> old = members.stream()
                .filter((t) -> dto.getIntegrantes().stream().map(CollaboratorProfileDto::getNit).collect(toSet()).contains(t.getNit()))
                .collect(Collectors.toList());
        deleted.forEach(member -> {
            member.setIdEstado(171);
            if (memberGroupRepository.canDeleteProfiles(member.getNit(), member.getIdPerfil())) {
                final SipfColaboradorPerfil cProfile = profile.findProfileById(new SipfColaboradorPerfilId(member.getNit(), member.getIdPerfil())).orElse(null);
                if (cProfile != null) {
                    cProfile.setEstado(162);
                }
            }
            memberGroupRepository.deleteById(new SipfIntegranteGrupoId(group.getId(), member.getNit(), member.getIdPerfil()));
            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_integrante_grupo")
                            .idCambioRegistro(member.getNit())
                            .idTipoOperacion(406)
                            .data(new Gson().toJson(member))
                            .fechaModifica(new Date())
                            .usuarioModifica(userLogged.getLogin())
                            .ipModifica(userLogged.getIp())
                            .build()
            );
        });
        dto.getIntegrantes()
                .stream()
                .filter(item -> !deleted.stream().map(SipfIntegranteGrupo::getNit).collect(toSet()).contains(item.getNit()))
                .forEach(integrante -> {

                    final JsonObject dataMember = new JsonObject();
                    List<SipfIntegranteGrupo> oldMember = old.stream().filter(item
                            -> item.getNit().equals(integrante.getNit()) && item.getIdPerfil().equals(integrante.getProfile())).collect(Collectors.toList());
                    SipfIntegranteGrupo member = new SipfIntegranteGrupo(group.getId(), integrante.getNit(), userLogged.getLogin(), new Date(), userLogged.getIp(), integrante.getProfile(), integrante.getRol(), 170);

                    if (oldMember.isEmpty()) {
                        if (members.stream().filter(item -> item.getNit().equals(integrante.getNit())).findFirst().isPresent()) {
                            memberGroupRepository.deleteById(new SipfIntegranteGrupoId(group.getId(), members.stream().filter(item -> item.getNit().equals(integrante.getNit())).findFirst().get().getNit(), members.stream().filter(item -> item.getNit().equals(integrante.getNit())).findFirst().get().getIdPerfil()));
                        } else {
                            List<CollaboratorProfileDto> listNewMember = new ArrayList<>();
                            CollaboratorProfileDto newMember = new CollaboratorProfileDto();
                            newMember.setNit(integrante.getNit());
                            newMember.setProfile(integrante.getProfile());
                            newMember.setRol(integrante.getRol());
                            listNewMember.add(newMember);
                            List<ErrorDto> errors = memberGroupRepository.getRepeatedMembers(listNewMember.stream().map(CollaboratorProfileDto::getNit).collect(Collectors.toList())).stream().map(item -> {
                                return ErrorDto.builder()
                                        .param("Integrante - " + item.getNit())
                                        .value(item.getNombre())
                                        .description(Message.OPERATOR_ONCE_PER_GROUP.getText())
                                        .build();
                            }).collect(Collectors.toList());

                            if (errors.size() > 0) {
                                throw BusinessException.badRequestError(Message.OPERATORS_ALREADY_IN_GROUP.getText(), errors);
                            }
                        }
                    } else {
                        return;
                    }
                    if (oldMember.size() > 0) {
                        member = oldMember.get(0);
                        dataMember.add("valorAnterior", new Gson().toJsonTree(member));

                        if (!Objects.equals(integrante.getProfile(), member.getIdPerfil())) {
                            if (memberGroupRepository.canDeleteProfiles(member.getNit(), member.getIdPerfil())) {
                                final SipfColaboradorPerfil cProfile = profile.findProfileById(new SipfColaboradorPerfilId(member.getNit(), member.getIdPerfil())).orElse(null);
                                if (cProfile != null) {
                                    cProfile.setEstado(162);
                                }
                            }
                        }
                    }

                    member.setIdPerfil(integrante.getProfile());
                    member.setIdRol(integrante.getRol());
                    dataMember.add("valorNuevo", new Gson().toJsonTree(member));

                    final SipfColaboradorPerfil perfil = profile.findProfileById(new SipfColaboradorPerfilId(integrante.getNit(), integrante.getProfile())).orElse(null);
                    final int status = dto.getEstado() == 164 ? 162 : 161;
                    if (perfil != null) {
                        perfil.setEstado(status);
                        colaboradorPerfilRepository.save(perfil);
                    } else {
                        profile.createColaboradorPerfil(new ColaboradorPerfilDto(integrante.getNit(), integrante.getProfile(), status));
                    }

                    memberGroupRepository.save(member);
                    historialOperacionesRepository.save(
                            SipfHistoricoOperaciones.builder()
                                    .nombreTabla("sipf_integrante_grupo")
                                    .idCambioRegistro(integrante.getNit())
                                    .idTipoOperacion(404)
                                    .data(dataMember.toString())
                                    .fechaModifica(new Date())
                                    .usuarioModifica(userLogged.getLogin())
                                    .ipModifica(userLogged.getIp())
                                    .build()
                    );
                });

        dataGroup.add("valorNuevo", new Gson().toJsonTree(group));

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_grupo_trabajo")
                        .idCambioRegistro(String.valueOf(group.getId()))
                        .idTipoOperacion(405)
                        .data(dataGroup.toString())
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .build()
        );
        return group;
    }

    @Transactional(rollbackFor = Exception.class)
    public boolean deleteGroup(Long id, UserLogged userLogged) {
        final SipfGrupoTrabajo group = workGroupRepository.findById(id).orElseThrow(
                () -> new BusinessException(HttpStatus.NOT_FOUND, Message.GROUP_NOT_FOUND.getText(id))
        );
        final List<GruposTrabajoProjection> solicitud = workGroupRepository.getSolictud((int) (long) id);
        final JsonObject dataGroup = new JsonObject();
        dataGroup.add("valorAnterior", new Gson().toJsonTree(group));

        final List<SipfIntegranteGrupo> members = memberGroupRepository.findByIdGrupo(id);

        members.forEach(member -> {
            final JsonObject data = new JsonObject();
            data.add("valorAnterior", new Gson().toJsonTree(member));

            member.setFechaModifica(new Date());
            member.setUsuarioModifica(userLogged.getLogin());
            member.setIpModifica(userLogged.getIp());

            member.setIdEstado(171);
            if (memberGroupRepository.canDeleteProfiles(member.getNit(), member.getIdPerfil())) {
                final SipfColaboradorPerfil cProfile = profile.findProfileCollaboratorById(new SipfColaboradorPerfilId(member.getNit(), member.getIdPerfil()), 161).orElse(null);
                if (cProfile != null) {
                    cProfile.setEstado(162);
                } else {
                    profile.createColaboradorPerfil(new ColaboradorPerfilDto(member.getNit(), member.getIdPerfil(), 162));
                }
            }
            data.add("valorNuevo", new Gson().toJsonTree(member));
            historialOperacionesRepository.save(
                    SipfHistoricoOperaciones.builder()
                            .nombreTabla("sipf_integrante_grupo")
                            .idCambioRegistro(member.getNit())
                            .idTipoOperacion(405)
                            .data(data.toString())
                            .fechaModifica(new Date())
                            .usuarioModifica(userLogged.getLogin())
                            .ipModifica(userLogged.getIp())
                            .build()
            );
        });
        List<SipfTrasladoColaborador> solicitudTraslado = new ArrayList<>();
        for (int a = 0; a < solicitud.size(); a++) {
            SipfTrasladoColaborador solicitudCambio;
            solicitudCambio = solicitudTrasladoColaboradorRepository.findById(solicitud.get(a).getId()).orElseThrow(()
                    -> new BusinessException(HttpStatus.NOT_FOUND, Message.GROUP_NOT_FOUND.getText(id))
            );
            solicitudCambio.setIdEstado(1024);
            solicitudCambio.setFechaModifica(new Date());
            solicitudCambio.setUsuarioModifica(userLogged.getLogin());
            solicitudCambio.setIpModifica(userLogged.getIp());
            solicitudTraslado.add(solicitudCambio);
        }
        solicitudTrasladoColaboradorRepository.saveAll(solicitudTraslado);
        group.setIdEstado(164);
        dataGroup.add("nuevoValor", new Gson().toJsonTree(group));
        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_grupo_trabajo")
                        .idCambioRegistro(String.valueOf(group.getId()))
                        .idTipoOperacion(405)
                        .data(dataGroup.toString())
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .build()
        );
        return true;
    }

    /**
     * Metodo obtener una solicitud de traslado de colaborador en base a un
     * login
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param userLogged identificador unico
     * @since 24/05/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorByLogin(UserLogged userLogged) {
        if (userLogged.getRoles().contains("AdministrativoSIPFAdministrador")) {
            return memberGroupRepository.getRequestTransferColaboratorAdministrator();
        } else {
            return memberGroupRepository.getRequestTransferColaborator(userLogged.getLogin());
        }
    }

    /**
     * Metodo para realizar cambio en grupo un integrante de acuerdo a solicitud
     * de traslado
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param id identificador unico de solicitud de traslado de colaborador
     * @param userLogged
     * @since 24/05/2022
     * @return pId
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean updateTransferColaborator(Integer id, UserLogged userLogged) {

        final SipfTrasladoColaborador transferCollaborator = solicitudTrasladoColaboradorRepository.findById(id).orElse(null);
        final SipfIntegranteGrupo members = memberGroupRepository.findByIdGrupoAndNitAndIdEstadoAndIdRol(transferCollaborator.getIdGrupoAnterior(), transferCollaborator.getNitProfesional(), 170, 3).orElse(null);
        Optional<SipfColaborador> colaborator = null;
        Optional<SipfGrupoTrabajo> workGroup = null;
        Optional<SipfUnidadAdministrativa> administrativeUnit = null;

        if (members != null) {
            try {
                //log.debug("entre al if");
                colaborator = this.colaboradorRepository.findById(transferCollaborator.getNitProfesional());
                workGroup = this.workGroupRepository.findById(transferCollaborator.getIdGrupoNuevo());
                administrativeUnit = this.unidadesAdministrativasRepository.findById(new Long(workGroup.get().getIdGerencia()));

                //Insercion de un nuevo registro de integrante de grupo
                final SipfIntegranteGrupo newMember = new SipfIntegranteGrupo();
                newMember.setIdGrupo(transferCollaborator.getIdGrupoNuevo());
                newMember.setNit(transferCollaborator.getNitProfesional());
                newMember.setUsuarioModifica(userLogged.getLogin());
                newMember.setFechaModifica(new Date());
                newMember.setIpModifica(userLogged.getIp());
                newMember.setIdPerfil(members.getIdPerfil());
                newMember.setIdRol(members.getIdRol());
                newMember.setIdEstado(170);
                memberGroupRepository.save(newMember);
                historialOperacionesRepository.save(
                        SipfHistoricoOperaciones.builder()
                                .nombreTabla("sipf_integrante_grupo")
                                .idCambioRegistro(members.getNit())
                                .idTipoOperacion(404)
                                .data(new Gson().toJson(newMember))
                                .fechaModifica(new Date())
                                .usuarioModifica(userLogged.getLogin())
                                .ipModifica(userLogged.getIp())
                                .build()
                );

                //eliminacion del integrante del grupo anterior
                memberGroupRepository.deleteById(new SipfIntegranteGrupoId(members.getIdGrupo(), members.getNit(), members.getIdPerfil()));
                historialOperacionesRepository.save(
                        SipfHistoricoOperaciones.builder()
                                .nombreTabla("sipf_integrante_grupo")
                                .idCambioRegistro(members.getNit())
                                .idTipoOperacion(406)
                                .data(new Gson().toJson(members))
                                .fechaModifica(new Date())
                                .usuarioModifica(userLogged.getLogin())
                                .ipModifica(userLogged.getIp())
                                .build()
                );

                //cambio de estado aprobada de solicitud de transferencia 
                transferCollaborator.setIdEstado(194);
                solicitudTrasladoColaboradorRepository.save(transferCollaborator);
                historialOperacionesRepository.save(
                        SipfHistoricoOperaciones.builder()
                                .nombreTabla("sipf_integrante_grupo")
                                .idCambioRegistro(String.valueOf(transferCollaborator.getIdSolicitud()))
                                .idTipoOperacion(405)
                                .data(new Gson().toJson(transferCollaborator))
                                .fechaModifica(new Date())
                                .usuarioModifica(userLogged.getLogin())
                                .ipModifica(userLogged.getIp())
                                .build()
                );

                String cuerpo = "Se le informa que se trasladó a la siguiente Unidad Administrativa " + administrativeUnit.get().getNombre();
                cuerpo += " y Equipo de Trabajo " + workGroup.get().getNombre() + ".";

                this.correoService.envioCorreo(colaborator.get().getCorreo(), cuerpo, "Notificación de traslado");
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            //log.debug("no entre al if");
            return null;
        }
    }

    /**
     * Metodo para rechazar una solicitud de traslado
     *
     * @author Anderson Suruy (aalsuruyq)
     * @param idSolicitud identificador unico de solicitud de traslado de
     * colaborador
     * @param idState estado nuevo
     * @param idStateValid estado anterior
     * @param user datos del usuario
     * @param comentary Comentario del usuario
     * @param typeComentary tipo de comentario a registar
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean alterStateRequestWithComentary(int idSolicitud, int idState, Integer idStateValid, UserLogged user, String comentary, Integer typeComentary) {
        SipfTrasladoColaborador solicitud = solicitudTrasladoColaboradorRepository.findById(idSolicitud).orElse(null);
        if (idStateValid != null && solicitud.getIdEstado() != idStateValid) {
            throw new BusinessException(HttpStatus.BAD_REQUEST, "La fase a la que se desea cambiar la solicitud no es la correcta");
        }

        final SipfColaborador collaborator = colaboradorRepository.findById(user.getNit()).orElse(null);
        final SipfTrasladoColaborador collaborator1 = solicitudTrasladoColaboradorRepository.findById(idSolicitud).orElse(null);
        final SipfColaborador collaborator2 = colaboradorRepository.findById(collaborator1.getIdAutorizadorAcepta()).orElse(null);

        solicitud.setIdEstado(idState);
        solicitud.setUsuarioModifica(user.getLogin());
        solicitud.setIpModifica(user.getIp());
        solicitud.setFechaModifica(new Date());
        solicitudTrasladoColaboradorRepository.save(solicitud);

        SipfHistorialComentarios comentaryF = new SipfHistorialComentarios();
        comentaryF.setComentarios(comentary);
        comentaryF.setIdRegistro(String.valueOf(idSolicitud));
        comentaryF.setIdTipoComentario(typeComentary);
        comentaryF.setFechaModifica(new Date());
        comentaryF.setUsuarioModifica(user.getLogin());
        comentaryF.setIpModifica(user.getIp());
        historialComentariosRepository.save(comentaryF);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_traslado_colaborador")
                        .idCambioRegistro(String.valueOf(idSolicitud))
                        .idTipoOperacion(405)
                        .data(new Gson().toJson(solicitud))
                        .fechaModifica(new Date())
                        .usuarioModifica(user.getLogin())
                        .ipModifica(user.getIp())
                        .build()
        );

        String cuerpo = "Se le informa que el profesional " + collaborator2.getNombres() + " mando a rechazar la solicitud traslado de integrantes correspondiente a la solicitud  "
                + solicitud.getIdSolicitud() + " por el motivo de " + comentary;

        this.correoService.envioCorreo(collaborator.getCorreo(), cuerpo, "Rechazo solicitud traslado de integrante");
        return true;
    }

    /**
     * Metodo obtener una solicitud de traslado de colaborador en base a un id
     * de solicitud
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param id identificador unico
     * @since 24/05/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorById(Integer idSolicitud) {
        //log.debug("Metodo obtener una solicitud de traslado de colaborador en base al idSolicitud");
        return memberGroupRepository.getRequestTransferColaboratorbyIdS(idSolicitud);
    }

    /**
     * Metodo para crear solicitud de trasladar un coloborador a otro equipo de
     * trabajo
     *
     * @author Jamier Batz (ajsbatzmo)
     * @param dto colaborador dto
     * @since 26/05/2022
     * @return collaborator
     */
    @Transactional
    public SipfTrasladoColaborador createRequestTransferCollaborator(SolicitudTrasladoColaboradorDto dto) {
        final SipfTrasladoColaborador transferCollaborator = new SipfTrasladoColaborador();

        transferCollaborator.setNitProfesional(dto.getNitColaborador());
        transferCollaborator.setIdAutorizadorSolicitante(dto.getIdAprobadorSolicitante());
        transferCollaborator.setIdGrupoAnterior(dto.getIdGrupoAnterior());
        transferCollaborator.setIdAutorizadorAcepta(dto.getIdAprobadorAcepta());
        transferCollaborator.setIdGrupoNuevo(dto.getIdGrupoNuevo());
        transferCollaborator.setIdEstado(193);
        transferCollaborator.setFechaEfectivaTraslado(dto.getFechaEfectivaTraslado());
        transferCollaborator.setFechaNotificacionTraslado(dto.getFechaNotificacionTraslado());
        transferCollaborator.setMotivo(dto.getMotivo());
        transferCollaborator.setUsuarioModifica(detector.getLogin());
        transferCollaborator.setFechaModifica(new Date());
        transferCollaborator.setIpModifica(detector.getIp());
        transferCollaborator.setFechaEfectivaRetorno(dto.getFechaEfectivaRetorno());
        transferCollaborator.setIdTipoTraslado(dto.getIdTipoTraslado());

        return solicitudTrasladoColaboradorRepository.save(transferCollaborator);
    }

    /**
     * Metodo obtener equipos de trabajo y unidades de un integrante
     *
     * @author Jamier Batz(ajsbatzmo)
     * @since 24/05/2022
     */
    @Transactional(readOnly = true)
    public List<OperatorProjection> getOperatorTeamsAndUnits() {
        //log.debug("Metodo obtener equipos de trabajo y unidades de un integranted");
        return memberGroupRepository.getOperatorTeamsAndUnits();
    }

    /**
     * Metodo obtener equipos de trabajo y unidades de un integrante por nit
     * operador
     *
     * @author Jamier Batz(ajsbatzmo)
     * @since 24/05/2022
     */
    @Transactional(readOnly = true)
    public List<OperatorProjection> getTeamsAndUnitsByNitOperator(String nit) {
        //log.debug("Metodo obtener equipos de trabajo y unidades de un integranted");
        return memberGroupRepository.getTeamsAndUnitsByNitOperator(nit);
    }

    /**
     * Metodo obtener una solicitud de traslado de colaborador en base a un nit
     * autorizador
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param nit identificador unico
     * @since 24/05/2022
     * @return pId
     */
    @Transactional(readOnly = true)
    public List<SolicitudTrasladoColaboradorProjection> getRequestTransferColaboratorByNitAuthorizer(String nit) {
        //log.debug("Metodo obtener una solicitud de traslado de colaborador");
        return memberGroupRepository.getRequestTransferColaboratorByNitAuthorizer(nit);
    }

    @Transactional(readOnly = true)
    public Integer getRequestTransferExists(String nit) {
        //log.debug("Metodo obtener si el operador ya tiene una solicitud de traslado");
        return memberGroupRepository.transferRequestExist(nit);
    }
}
