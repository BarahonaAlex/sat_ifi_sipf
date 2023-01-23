/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfCatDato;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogDataSpecialConditionProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoPadreProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 *
 * @author rabaraho
 */
public interface CatDatoRepository extends CrudRepository<SipfCatDato, Integer> {

    List<CatalogDataProjection> findByCodigoCatalogoAndEstado(Integer pIdCatalog, Integer pStatus);

    List<CatalogDataProjection> findByCodigoDatoPadreAndEstado(Integer pCodigoDatoPadre, Integer pStatus);

    List<CatalogDataProjection> findByCodigoCatalogoInAndEstado(List<Integer> pIdCatalog, Integer pStatus);

    CatalogDataProjection findByCodigo(Integer pCodigo);

    CatalogDataProjection findByCodigoIngresado(String departamento);

    /**
     * Metodo para obtener catalogos padres administrables (idFhater = 1)
     *
     * @author ajsbatzmo
     */
    @Query(value = "select \n"
            + "	scc.codigo as id,\n"
            + "	scc.nombre as nombre,\n"
            + "	scc.descripcion as descripcion\n"
            + "from sat_ifi_sipf.sipf_cat_catalogo scc  \n"
            + "where scc.codigo_cat_padre = 1", nativeQuery = true)
    List<CatalogoPadreProjection> getManageableCatalog();

    /**
     * Metodo para obtener catalogos hijos en base a id padre que esten en
     * estado activo
     *
     * @author ajsbatzmo
     */
    @Query(value = "select \n"
            + "	scc.nombre as nombrePadre,\n"
            + "	scd.codigo as codigo,\n"
            + "	scd.nombre as nombre,\n"
            + "	scd.descripcion as descripcion,\n"
            + "	scd.codigo_ingresado as codigoIngresado,\n"
            + "	scd2.nombre as estado\n"
            + "from sat_ifi_sipf.sipf_cat_dato_admin scd \n"
            + "inner join sat_ifi_sipf.sipf_cat_estado_dato scd2 on scd.estado = scd2.codigo \n"
            + "inner join sat_ifi_sipf.sipf_cat_catalogo scc \n"
            + "on scc.codigo = scd.codigo_catalogo \n"
            + "where scd.codigo_catalogo =:idCatalog and (scd.estado between 1 and 2)", nativeQuery = true)
    List<CatalogoHijoProjection> getItemById(@Param("idCatalog") Integer idCatalog);

    @Query(value = "select\n"
            + "	scd.codigo \n"
            + "from sat_ifi_sipf.sipf_cat_dato scd \n"
            + "inner join sat_ifi_sipf.sipf_cat_catalogo scc on scc.codigo  = scd.codigo_catalogo \n"
            + "where scc.codigo= :catalogo and scd.codigo_ingresado = :codigoIngresado",
            nativeQuery = true)
    Integer getCodigoByCatalogoAndCodIngresado(@Param("codigoIngresado") String codigoIngresado, @Param("catalogo") Integer catalogo);

    /*
    int getCodigo();

    Integer getCodigoCatalogo();

    Integer getCodigoDatoPadre();

    String getCodigoIngresado();

    String getNombre();

    String getDescripcion();*/
    @Query("select t.codigo as codigo, "
            + "t.codigoCatalogo as codigoCatalogo, "
            + "t.codigoDatoPadre as codigoDatoPadre,"
            + "t.codigoIngresado as codigoIngresado,"
            + "t.nombre as nombre,"
            + "t.descripcion as descripcion,"
            + "(select p.valor as valor from SipfCatDatoCondicionEspecial p "
            + "where p.codigoDato=t.codigo and p.nombre =:pNombre ) as valor "
            + "from SipfCatDato t "
            + "where t.codigoCatalogo = :pIdCatalog "
            + "and t.estado = :pStatus ")
    List<CatalogDataSpecialConditionProjection> getDataByCatalogStatus(@Param("pIdCatalog") Integer pIdCatalog, @Param("pStatus") Integer pStatus, @Param("pNombre") String pNombre);

}
