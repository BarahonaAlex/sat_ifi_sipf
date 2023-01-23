/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UnidadesAdministrativasDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UnidadesAdministrativasWsDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfUnidadAdministrativa;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.UnidadAdministrativaProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.UnidadesAdministrativaRepository;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 06/01/2022
 * @version 1.0
 */
@Transactional
@Service
@Slf4j
public class UnidadesAdministrativasService {

    @Autowired
    private UnidadesAdministrativaRepository unidadesAdministrativaRepository;

    @Autowired
    Detector detector;

    @Autowired
    private ConsumosService consumosService;

    /**
     * Metodo para crear una unidad administrativa
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pUnit datos de la unidad
     * @since 06/01/2022
     * @return unidad administrativa creada
     */
    @Transactional(rollbackFor = Exception.class)
    public SipfUnidadAdministrativa createAdministrativeUnit(SipfUnidadAdministrativa pUnit,UserLogged user) {
       
        pUnit.setIdEstado(136);
        pUnit.setUsuarioModifica(user.getLogin());
        pUnit.setIpModifica(user.getIp());
        pUnit.setFechaModifica(new Date());
        pUnit.setFechaCreacion(new Date());
        log.debug("id de programacion"+pUnit.getIdTipoProgramacion());
        return unidadesAdministrativaRepository.save(pUnit);
    }

    /**
     * Se obtienen todas las unidades administrativas hijas en base al id Padre
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pId id de la unidad padre
     * @param pStatus Lista de estados
     * @since 06/01/2022
     * @return unidades administrativas
     */
    @Transactional(readOnly = true)
    public List<UnidadAdministrativaProjection> getAdministrativeUnitsByIdFather(long pId, List<Integer> pStatus) {
        final List<UnidadAdministrativaProjection> units = unidadesAdministrativaRepository.findAdministrativeUnitsByIdFather(pId, pStatus);
        return units;
    }

    /**
     * Se obtienen todas las unidades administrativas padres
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pStatus Lista de estados
     * @since 06/01/2022
     * @return unidades administrativas
     */
    @Transactional(readOnly = true)
    public List<UnidadAdministrativaProjection> getAdministrativeUnitsFather(List<Integer> pStatus) {
        List<UnidadAdministrativaProjection> unidades = unidadesAdministrativaRepository.findAdministraveUnitsFather(pStatus);
        return unidades;
    }

    /**
     * Se obtienen todas las unidades administrativas en base al estado
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pStatus Lista de estado
     * @since 06/01/2022
     * @return unidades administrativas
     */
    @Transactional(readOnly = true)
    public List<UnidadAdministrativaProjection> getAllAdministrativeUnits(List<Integer> pStatus) {
        List<UnidadAdministrativaProjection> units = unidadesAdministrativaRepository.findAllAdministrativeUnitsByStatus(pStatus);
        return units;
    }

    /**
     * Metodo para actualizar una unidad administrativa
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pUnit datos de la unidad
     * @param id id de la unidad administrativa
     * @since 06/01/2022
     * @return unidad administrativa actualizada
     */
    @Transactional(rollbackFor = Exception.class)
    public SipfUnidadAdministrativa alterAdministrativeUnit(UnidadesAdministrativasDto pUnit, long id, UserLogged user) {
        SipfUnidadAdministrativa res = unidadesAdministrativaRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado."));
        if (res.getIdUnidadProsis() != null) {
            throw new ResourceNotFoundException("Las Unidades Administrativas provenientes del Sistemas Prosis no se pueden modificar");
        }
        res.setNombre(pUnit.getNombre());
        res.setDescripcion(pUnit.getDescripcion());
        res.setIdEstado(pUnit.getIdEstado());
        res.setUsuarioModifica(user.getLogin());
        res.setIpModifica(user.getIp());
        res.setIdTipoProgramacion(pUnit.getIdTipoProgramacion());
        res.setFechaModifica(new Date());
        return res;
    }

    @Transactional(readOnly = true)
    public List<UnidadAdministrativaProjection> getUnitsByNitAndRol(String pNit, Integer pIdRol) {
        //log.debug("unidades donde el nit es un jefe ");
        return unidadesAdministrativaRepository.getUniteByUserAndRol(pNit, pIdRol);
    }

    /**
     * Metodo para actualizar las unidades registradas desde el sistema prosis y
     * añadir las nuevas
     *
     * @author Rudy Culajay (ruarcuse)
     * @param user datos de usuario que inicio sesion
     * @since 11/07/2022
     * @return unidad administrativa
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean mergeInfoUnitFromProsis(UserLogged user) {
        try {
            GeneralResponseDto<List<UnidadesAdministrativasWsDto>> resultado = consumosService.consumeCompleteUrlSqlServer(null, "administrative/units/", GeneralResponseDto.class, HttpMethod.GET);
            List<UnidadesAdministrativasWsDto> wsUnits = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<List<UnidadesAdministrativasWsDto>>() {
            });

            List<SipfUnidadAdministrativa> currenUnits = unidadesAdministrativaRepository.findByIdUnidadProsisIsNotNull();
            wsUnits.forEach(wsUnit -> {
                SipfUnidadAdministrativa findUnit = currenUnits.stream()
                        .filter(unit -> wsUnit.getIdUnitOrg()
                        .equals(unit.getIdUnidadProsis())).findFirst().orElse(null);

                // Se valida si la unidad administrativa es hijo de otra para hacer la relación
                Long idFather = null;
                if (!wsUnit.getIdPadre().equals(wsUnit.getIdUnitOrg())) {
                    SipfUnidadAdministrativa newFatherUnit = unidadesAdministrativaRepository.findByIdUnidadProsis(wsUnit.getIdPadre());
                    if (newFatherUnit != null) {
                        idFather = newFatherUnit.getId();
                    }
                }

                if (findUnit == null) {
                    unidadesAdministrativaRepository.save(new SipfUnidadAdministrativa(wsUnit.getUnitName(), wsUnit.getShortUnitName(), user.getLogin(), new Date(), idFather, 136, user.getIp(),
                            wsUnit.getIdUnitOrg().intValue(), new Date()
                    ));

                } else {
                    findUnit.setNombre(wsUnit.getUnitName());
                    findUnit.setDescripcion(wsUnit.getShortUnitName());
                    findUnit.setIpModifica(user.getIp());
                    findUnit.setUsuarioModifica(user.getLogin());
                    findUnit.setIdPadre(idFather);
                }
            });
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    @Transactional(readOnly = true)
    public List<CatalogoHijoProjection> getUnitByParentProsis(Integer parent) {
        return unidadesAdministrativaRepository.getUnitByParentProsis(parent);
    }
}
