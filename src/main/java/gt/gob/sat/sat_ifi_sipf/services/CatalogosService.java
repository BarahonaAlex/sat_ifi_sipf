/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.services;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.dtos.CatDatoAdminDto;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.models.SipfCatDatoAdmin;
import gt.gob.sat.sat_ifi_sipf.models.SipfHistoricoOperaciones;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataSpecialConditionProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoPadreProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoAdminRepository;
import gt.gob.sat.sat_ifi_sipf.projections.RubroProjection;
import gt.gob.sat.sat_ifi_sipf.repositories.CatDatoRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.HistorialOperacionesRepository;
import java.util.Date;
import gt.gob.sat.sat_ifi_sipf.repositories.RubroHallazgoRepository;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author ruarcuse
 */
@Transactional
@Service
@Slf4j
public class CatalogosService {

    @Autowired
    private CatDatoRepository catalogDataRepository;

    @Autowired
    private RubroHallazgoRepository findingItemRepository;

    @Autowired
    private CatDatoAdminRepository CatDatoAdminRepository;

    @Autowired
    private Detector detector;

    @Autowired
    private HistorialOperacionesRepository historialOperacionesRepository;

    public List<CatalogDataProjection> findCatalogDataByIdCatalogAndStatus(Integer pIdCatalog, Integer pStatus) {
        return this.catalogDataRepository.findByCodigoCatalogoAndEstado(pIdCatalog, pStatus);
    }

    public List<CatalogDataProjection> findCatalogDataByIdCatalogAndStatus(List<Integer> pIdCatalog, Integer pStatus) {
        return this.catalogDataRepository.findByCodigoCatalogoInAndEstado(pIdCatalog, pStatus);
    }

    public List<CatalogDataProjection> findCatalogDataByDatoPadreAndStatus(Integer pCodigoDato, Integer pStatus) {
        return this.catalogDataRepository.findByCodigoDatoPadreAndEstado(pCodigoDato, pStatus);
    }

    public CatalogDataProjection findCatalogDataByIdData(Integer pCodigoDato) {
        return this.catalogDataRepository.findByCodigo(pCodigoDato);
    }

    /**
     * Metodo obtener los catalogos padres que sean administrables
     *
     * @author Jamier Batz (ajsbatzmo)
     *
     * @since 18/02/2022
     * @return nit
     */
    @Transactional(readOnly = true)
    public List<CatalogoPadreProjection> getManageableCatalog() {
        //log.debug("Obteniendo la lista de catalogos padres que sean administrables");
        return catalogDataRepository.getManageableCatalog();
    }

    /**
     * Metodo obtener los catalogos hijo en base a id del padre en estado activo
     *
     * @author Gabriel runao (agaruanom)
     * @param id indentificador hijo de catalogos administrables
     * @since 18/02/2022
     * @return nit
     */
    @Transactional(readOnly = true)
    public List<CatalogoHijoProjection> getItemById(Integer idCatalog) {
        //log.debug("Obteniendo la lista de catalogos hijos en base a id de padres que sean administrable");
        return catalogDataRepository.getItemById(idCatalog);
    }

    /**
     * Metodo para crear registro de nueva insercion de catalogo hijo
     *
     * @author Jamier Batz (ajsbatzmo)
     * @param dto CatDatoAdminDto dto
     * @since 15/06/2022
     * @return collaborator
     */
    @Transactional
    public SipfCatDatoAdmin createItem(CatDatoAdminDto dto, UserLogged userLogged) {
        final SipfCatDatoAdmin catDatoAdmin = new SipfCatDatoAdmin();

        catDatoAdmin.setCodigoCatalogo(dto.getCodigoCatalogo());
        catDatoAdmin.setNombre(dto.getNombre());
        catDatoAdmin.setDescripcion(dto.getDescripcion());
        catDatoAdmin.setEstado(1);
        catDatoAdmin.setUsuarioAgrega(userLogged.getLogin());
        catDatoAdmin.setFechaAgrega(new Date());

        return CatDatoAdminRepository.save(catDatoAdmin);
    }

    /**
     * Metodo para realizar cambio de estado desactivado en base a id de
     * catalogo
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param id identificador unico de codigo de catalogo
     * @since 15/06/2022
     * @return pId
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean removeItem(Integer id, UserLogged userLogged) {
        final SipfCatDatoAdmin catDatoAdmin = CatDatoAdminRepository.findById(id).orElse(null);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_cat_dato_admin")
                        .idCambioRegistro("2")
                        .idTipoOperacion(2)
                        .data(new Gson().toJson(catDatoAdmin))
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .build()
        );

        catDatoAdmin.setEstado(2);
        CatDatoAdminRepository.save(catDatoAdmin);
        return true;
    }

    /**
     * Metodo para realizar actualizar catalogo
     *
     * @author Jamier Batz(ajsbatzmo)
     * @param id identificador unico de codigo de catalogo
     * @param dto cuerpo de la actualizacion
     * @since 15/06/2022
     * @return true
     */
    @Transactional(rollbackFor = Exception.class)
    public Boolean editItem(Integer id, CatDatoAdminDto dto, UserLogged userLogged) {

        final SipfCatDatoAdmin catDatoAdmin = CatDatoAdminRepository.findById(id).orElse(null);

        historialOperacionesRepository.save(
                SipfHistoricoOperaciones.builder()
                        .nombreTabla("sipf_cat_dato_admin")
                        .idCambioRegistro("2")
                        .idTipoOperacion(2)
                        .data(new Gson().toJson(catDatoAdmin))
                        .fechaModifica(new Date())
                        .usuarioModifica(userLogged.getLogin())
                        .ipModifica(userLogged.getIp())
                        .build()
        );

        catDatoAdmin.setNombre(dto.getNombre());
        catDatoAdmin.setDescripcion(dto.getDescripcion());
        catDatoAdmin.setEstado(dto.getIsChangeState() ? catDatoAdmin.getEstado().equals(1) ? 2 : 1 : catDatoAdmin.getEstado());

        CatDatoAdminRepository.save(catDatoAdmin);
        return true;
    }

    public List<RubroProjection> getFindingsItem() {
        return findingItemRepository.findAllItems();
    }

    public List<CatalogDataSpecialConditionProjection> getData(Integer pIdCatalog, Integer pStatus, String pNombre) {
        return this.catalogDataRepository.getDataByCatalogStatus(pIdCatalog, pStatus, pNombre);
    }
}
