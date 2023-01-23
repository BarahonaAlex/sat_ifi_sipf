/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.projections;

import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 *
 * @author crramosl
 */
public interface OpcionMenuProjection {

    String getId();

    String getTitle();

    String getIcon();

    String getRoute();

    @JsonRawValue
    String getChildren();
}
