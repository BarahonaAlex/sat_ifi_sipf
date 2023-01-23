/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import java.math.BigInteger;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

/**
 *
 * @author josed
 */
@Repository
@Slf4j
public class UtilsRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public int nextValSequence(String pSequence) {
        Query vQuery = entityManager.createNativeQuery("select nextval(:sequence) as idSequencia").setParameter("sequence", pSequence);
        return ((BigInteger) vQuery.getResultList().get(0)).intValue();
    }
    public int idCatDato(String nombre){
        Query vQuery = entityManager.createNativeQuery("select codigo from sat_ifi_sipf.sipf_cat_dato where nombre =:nombre").setParameter("nombre", nombre);
        return ((Integer) vQuery.getResultList().get(0));
    }
}
