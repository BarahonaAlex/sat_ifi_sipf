/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfUnidadAdministrativa;
import gt.gob.sat.sat_ifi_sipf.projections.CatalogoHijoProjection;
import gt.gob.sat.sat_ifi_sipf.projections.UnidadAdministrativaProjection;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * Es un contrato que define el comportamiento que alguna clase debe implementar
 * mediante un conjunto de metodos relacionados.
 * <p>
 * Este repositorio utiliza un {@code CrudRepository} para acceder a la ENTITY y
 * poder ejecutar los querys necesarios. Debe seguir el estandar de Spring JPA
 * para el nombrado de las consultas y envio de parametros.
 *
 * @author Rudy Culajay (ruarcuse)
 * @since 06/01/2022
 * @version 1.0
 */
public interface UnidadesAdministrativaRepository extends CrudRepository<SipfUnidadAdministrativa, Long> {

    /**
     * Se obtienen todas las unidades administrativas padres
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pEstados Estados separados por , de las unidades (A, I)
     * @since 06/01/2022
     * @return unidades administrativas
     */
    @Query(value = "select\n"
            + "	fua.id,\n"
            + "	fua.nombre,\n"
            + "	fua.descripcion,\n"
            + "	fua.id_padre \"idPadre\",\n"
            + "	fua.id_estado \"idEstado\",\n"
            + "	scd.nombre \"nombreEstado\",\n"
            + "	fua.id_unidad_prosis \"idUnidadProsis\",\n"
            + " fua.fecha_creacion \"fechaCreacion\",\n"
            + "	(\n"
            + "	select\n"
            + "		count(*)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_unidad_administrativa t1\n"
            + "	where\n"
            + "		t1.id_padre = fua.id 		\n"
            + "		) hijos,\n"
            + "		(\n"
            + "	select\n"
            + "		count(*)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_unidad_administrativa t1\n"
            + "	where\n"
            + "		t1.id_padre = fua.id\n"
            + "		and t1.id_estado = 137\n"
            + "		) hijosInactivos,\n"
            + "		(\n"
            + "	select\n"
            + "		count(*)\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_unidad_administrativa t1\n"
            + "	where\n"
            + "		t1.id_padre = fua.id\n"
            + "		and t1.id_estado = 136 \n"
            + "		) hijosActivos\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_unidad_administrativa fua\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on\n"
            + "	fua.id_estado = scd.codigo\n"
            + "where\n"
            + "	fua.id_padre is null\n"
            + "	and fua.id_estado in (:pEstados)\n"
            + "order by\n"
            + "	fua.id", nativeQuery = true)
    List<UnidadAdministrativaProjection> findAdministraveUnitsFather(@Param("pEstados") List<Integer> pEstados);

    /**
     * Se obtienen todas las unidades administrativas de un padre
     *
     * @author Rudy Culajay (ruarcuse)
     * @param ids id de la unidad padre
     * @param pEstados Estados separados por , de las unidades (A, I)
     * @since 06/01/2022
     * @return unidades administrativas
     */
    @Query(value = "select fua.id, fua.nombre, fua.descripcion, fua.id_padre \"idPadre\", fua.id_estado \"idEstado\", scd.nombre \"nombreEstado\",\n"
            + "fua.fecha_creacion \"fechaCreacion\", fua.id_tipo_programacion \"idTipoProgramacion\", scd1.nombre \"nombreTipoProgramacion\",\n"
            + "fua.id_unidad_prosis \"idUnidadProsis\", (select count(*) from  sat_ifi_sipf.sipf_unidad_administrativa where id_padre = fua.id) hijos\n"
            + "from sat_ifi_sipf.sipf_unidad_administrativa fua\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on fua.id_estado = scd.codigo\n"
            + "left join sat_ifi_sipf.sipf_cat_dato scd1 on fua.id_tipo_programacion = scd1.codigo\n"
            + "where fua.id_padre =:pId And fua.id_estado in (:pEstados)",
            nativeQuery = true)
    List<UnidadAdministrativaProjection> findAdministrativeUnitsByIdFather(@Param("pId") long ids, @Param("pEstados") List<Integer> pEstados);

    /**
     * Se obtienen todas las unidades administrativas en base a estados
     *
     * @author Rudy Culajay (ruarcuse)
     * @param pEstados Estados de las unidades
     * @since 12/01/2022
     * @return unidades administrativas
     */
    @Query(value = "select fua.id, fua.nombre, fua.descripcion, fua.id_padre \"idPadre\", fua.id_estado \"idEstado\", scd.nombre \"nombreEstado\", \n"
            + "fua.id_unidad_prosis \"idUnidadProsis\", (select count(*) from  sat_ifi_sipf.sipf_unidad_administrativa where id_padre = fua.id) hijos, \n"
            + "sgt.id \"idGrupo\"\n"
            + "from sat_ifi_sipf.sipf_unidad_administrativa fua\n"
            + "inner join sat_ifi_sipf.sipf_cat_dato scd on fua.id_estado = scd.codigo\n"
            + "left join sat_ifi_sipf.sipf_grupo_trabajo sgt on fua.id = sgt.id_gerencia \n"
            + "where  fua.id_estado in (:pEstados)", nativeQuery = true)
    List<UnidadAdministrativaProjection> findAllAdministrativeUnitsByStatus(@Param("pEstados") List<Integer> pEstados);

    /**
     * Se obtienen todas las unidades administrativas padres en base al hijo de
     * manera recursiva
     *
     * @author Sergio Cano (asacanoes)
     * @param idUnidad codigo de la unidad seleccionada
     * @since 01/04/2022
     * @return unidades administrativas
     */
    @Query(value = "with recursive c as (\n"
            + "\n"
            + "  --start with the \"anchor\" row\n"
            + "  select\n"
            + "    *\n"
            + "  from sat_ifi_sipf.sipf_unidad_administrativa sua \n"
            + "  where\n"
            + "    id= :id\n"
            + "\n"
            + "  union all\n"
            + "\n"
            + "  select\n"
            + "    sat_ifi_sipf.sipf_unidad_administrativa.*\n"
            + "  from sat_ifi_sipf.sipf_unidad_administrativa \n"
            + "  join c on c.id_padre = sat_ifi_sipf.sipf_unidad_administrativa.id\n"
            + ")\n"
            + "select\n"
            + "  c.id as id, c.nombre as nombre\n"
            + "from c where id_estado = 136\n"
            + "order by\n"
            + "  id_padre nulls first;", nativeQuery = true)
    List<UnidadAdministrativaProjection> findFatherUnitsByChildrenId(@Param("id") Integer idUnit);

    @Query(value = "select\n"
            + "	suap.id ,\n"
            + "	suap.nombre\n"
            + "from\n"
            + "	sat_ifi_sipf.sipf_unidad_administrativa suap\n"
            + "where\n"
            + "	suap.id_padre in (\n"
            + "	select\n"
            + "		suap.id_padre\n"
            + "	from\n"
            + "		sat_ifi_sipf.sipf_unidad_administrativa suap\n"
            + "	where\n"
            + "		suap.id in (\n"
            + "		select\n"
            + "			sua.id\n"
            + "		from\n"
            + "			sat_ifi_sipf.sipf_grupo_trabajo sgt\n"
            + "		inner join\n"
            + "                sat_ifi_sipf.sipf_integrante_grupo sig \n"
            + "                    on\n"
            + "			sig.id_grupo = sgt.id\n"
            + "		inner join\n"
            + "                sat_ifi_sipf.sipf_unidad_administrativa sua \n"
            + "                    on\n"
            + "			sua.id = sgt.id_gerencia\n"
            + "		where\n"
            + "		sig.nit = :pNit \n "
            + "		and sig.id_rol = :pRol ))", nativeQuery = true
    )
    List<UnidadAdministrativaProjection> getUniteByUserAndRol(@Param("pNit") String nit, @Param("pRol") Integer rol);

    List<SipfUnidadAdministrativa> findByIdUnidadProsisIsNotNull();

    SipfUnidadAdministrativa findByIdUnidadProsis(Integer id);

    @Query(value = "select sua.id as codigo, sua.nombre from sat_ifi_sipf.sipf_unidad_administrativa sua \n"
            + "where sua.id_padre = (select sua.id from sat_ifi_sipf.sipf_unidad_administrativa sua\n"
            + "where sua.id_unidad_prosis = :id)", nativeQuery = true)
    List<CatalogoHijoProjection> getUnitByParentProsis(@Param("id") Integer id);
}
