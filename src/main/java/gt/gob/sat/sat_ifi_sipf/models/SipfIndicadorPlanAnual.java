/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.models;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author crramosl
 */
@Entity
@Table(name = "sipf_indicador_plan_anual", schema = "sat_ifi_sipf")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@IdClass(SipfIndicadorPlanAnualId.class)
public class SipfIndicadorPlanAnual implements Serializable {

    @Id
    @Column(name = "id_plan")
    private Integer idPlan;
    @Id
    @Column(name = "id_indicador")
    private Integer idIndicador;
    @Column(name = "valor")
    private Integer valor;
    @Column(name = "usuario", length = 16)
    private String usuario;
    @Column(name = "ip", length = 15)
    private String ip;
    @Temporal(TemporalType.DATE)
    @Column(name = "fecha", length = 19)
    private Date fecha;
}