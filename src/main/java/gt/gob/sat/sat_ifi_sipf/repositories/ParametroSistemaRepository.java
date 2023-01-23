/*
 * Superintendencia de Administracion Tributaria (SAT)
 * Gerencia de Informatica
 * Departamento de Desarrollo de Sistemas
 */
package gt.gob.sat.sat_ifi_sipf.repositories;

import gt.gob.sat.sat_ifi_sipf.models.SipfParametroSistema;
import java.util.List;
import org.springframework.data.repository.CrudRepository;

/**
 *
 * @author adftopvar
 */
public interface ParametroSistemaRepository extends CrudRepository<SipfParametroSistema, Integer> {
    
    List<SipfParametroSistema> findAllByOrderByIdAsc();
}
