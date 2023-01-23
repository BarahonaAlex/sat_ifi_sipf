/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorDetalleDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ColaboradorPerfilDto;
import gt.gob.sat.sat_ifi_sipf.dtos.PerfilDto;
import gt.gob.sat.sat_ifi_sipf.dtos.ProfileDetailDto;
import gt.gob.sat.sat_ifi_sipf.dtos.RolDto;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfil;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfilId;
import gt.gob.sat.sat_ifi_sipf.models.SipfPerfil;
import gt.gob.sat.sat_ifi_sipf.projections.ColaboradoresProjection;
import gt.gob.sat.sat_ifi_sipf.projections.FuncionesProjection;
import gt.gob.sat.sat_ifi_sipf.projections.PerfilProjections;
import gt.gob.sat.sat_ifi_sipf.projections.UrlProjections;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorPerfilRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.PerfilRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ruano
 */
@Transactional
@Service
@Slf4j
public class PerfilService {

    @Autowired
    private PerfilRepository perfilrepository;

    @Autowired
    private ColaboradorPerfilRepository colaboradorperfilrepository;

    @Autowired
    private ConsumosService consumosService;

    @Autowired
    private ColaboradorRepository colaboradorRepository;

    @Transactional(readOnly = true)
    public List<PerfilProjections> getPerfilesByName(String pNombre) {

        return perfilrepository.findByRolName(pNombre);
    }

    @Transactional(readOnly = true)
    public List<PerfilProjections> getperfiles(String pUsuarioSistema) {
        //log.debug(pUsuarioSistema);
        RolDto rol = this.consumosService.consume(null, "sat_rtu/privado/consultas_oid_ws/consultaRoles/" + pUsuarioSistema, RolDto.class, HttpMethod.GET);
        if (rol.isPoseeRol()) {
            //perfilrepository.findByLsitaRol(rol.getOperacion().getListaRolesOID());
            if (!perfilrepository.findByLsitaRol(rol.getOperacion().getListaRolesOID()).isEmpty()) {
                return perfilrepository.findByListRol(rol.getOperacion().getListaRolesOID());
            } else {
                return new ArrayList<>();
            }
        } else {
            return new ArrayList<>();
        }
    }

    @Transactional(readOnly = true)
    public ProfileDetailDto getProfilesByLogin(String pNit, String role, boolean operador) {

        final ColaboradoresProjection collab = colaboradorRepository.getByNit(pNit);
        log.debug("llego aqui" + collab.getnit());
        if (collab == null) {
            throw BusinessException.notFound(Message.COLLABORATOR_NOT_FOUND.getText());
        }

        if (operador) {
            if (!colaboradorperfilrepository.findByIdNitAndEstado(pNit, 170).isEmpty()) {
                throw BusinessException.notFound(Message.OPERATOR_ALREADY_IN_GROUP.getText());
            }
        }

        RolDto rol = this.consumosService.consume(null, "sat_rtu/consultas/rolesOID/" + collab.getlogin() + "/" + role, RolDto.class, HttpMethod.GET);
       log.debug("este es el rol" + rol.isPoseeRol());
        if (rol.isPoseeRol()) {
            List<PerfilProjections> profiles = perfilrepository.findByRolName(role);
            if (profiles.isEmpty()) {
                throw BusinessException.notFound(Message.PROFILES_NOT_FOUND.getText());
            }
            return ProfileDetailDto.builder().user(
                    ColaboradorDetalleDto.builder()
                            .nit(pNit)
                            .nombre(collab.getnombres())
                            .puesto(collab.getpuesto_trabajo())
                            .estado(collab.getnombreEstado())
                            .build()
            ).profiles(profiles).build();
        } else {
            throw BusinessException.notFound(Message.ROLE_NOT_FOUND.getText());
        }
    }

    @Transactional(readOnly = true)
    public List<PerfilProjections> getperfil(String pNit) {
        //log.debug("Obteniendo el listado de perfiles de un colaborador");
        return colaboradorperfilrepository.findByNit(pNit);
    }

    @Transactional
    public SipfPerfil createPerfil(PerfilDto dto) {
        final SipfPerfil perfil = new SipfPerfil();

        perfil.setNombre(dto.getNombre());
        perfil.setIdRol(dto.getIdRol());
        perfil.setEstado(dto.getEstado());

        return perfilrepository.save(perfil);
    }

    @Transactional
    public boolean createColaboradorPerfil(ColaboradorPerfilDto dto) {
        if (colaboradorperfilrepository.findByIdAndEstado(new SipfColaboradorPerfilId(dto.getNit(), dto.getIdPerfil()), 161).isPresent()) {
            return false;
        }
        final SipfColaboradorPerfil perfil = new SipfColaboradorPerfil();
        final SipfColaboradorPerfilId perfil2 = new SipfColaboradorPerfilId();

        perfil2.setNit(dto.getNit());
        perfil2.setIdPerfil(dto.getIdPerfil());
        perfil.setId(perfil2);
        perfil.setEstado(161);
        colaboradorperfilrepository.save(perfil);
        return true;
    }

    @Transactional
    public SipfColaboradorPerfil deleteColaboradorPerfil(ColaboradorPerfilDto dto) {
        final SipfColaboradorPerfil perfil = colaboradorperfilrepository.findById(new SipfColaboradorPerfilId(dto.getNit(), dto.getIdPerfil())).orElse(null);
        if (perfil == null) {
            throw new BusinessException(HttpStatus.NOT_FOUND, "Perfil no encontrado.");
        }
        perfil.setEstado(162);

        return colaboradorperfilrepository.save(perfil);
    }

    @Transactional
    public SipfPerfil modificarPerfil(Integer pId, PerfilDto dto) {
        final Optional<SipfPerfil> perfil = this.perfilrepository.findById(pId);

        perfil.get().setEstado(dto.getEstado());

        return perfilrepository.save(perfil.get());
    }

    @Transactional(readOnly = true)
    public List<FuncionesProjection> getFuncionesLogin(String login) {
        //log.debug("Obteniendo el listado de perfiles por login");
        return perfilrepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public List<UrlProjections> getUrlLogin(String login) {
        //log.debug("Obteniendo el listado de URL por login");
        return perfilrepository.findByLoginUrl(login);
    }

    public void deleteCollaboratorProfile(String pNit, Integer pIdProfile) {
        SipfColaboradorPerfil collaboratorProfile = colaboradorperfilrepository.findByIdAndEstado(new SipfColaboradorPerfilId(pNit, pIdProfile), 161).orElse(null);
        if (collaboratorProfile == null) {

        } else {
            collaboratorProfile.setEstado(162);
            colaboradorperfilrepository.save(collaboratorProfile);
        }
    }

    public Optional<SipfColaboradorPerfil> findProfileCollaboratorById(SipfColaboradorPerfilId id, Integer pStatus) {
        return colaboradorperfilrepository.findByIdAndEstado(id, pStatus);
    }
    
    public Optional<SipfColaboradorPerfil> findProfileById(SipfColaboradorPerfilId id) {
        return colaboradorperfilrepository.findById(id);
    }
}
