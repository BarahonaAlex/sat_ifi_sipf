/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 *
 * @author crramosl
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class Role {

    public static final String ADMINISTRADOR = "AdministrativoSIPFAdministrador";
    public static final String AUTORIZADOR = "AdministrativoSIPFAutorizador";
    public static final String AUTORIZADOR_GERENCIAL = "AdministrativoSIPFAutorizadorGerencial";
    public static final String APROBADOR = "AdministrativoSIPFAprobador";
    public static final String VERIFICADOR = "AdministrativoSIPFVerificador";
    public static final String OPERADOR = "AdministrativoSIPFOperador";
    public static final String SOLICITANTE = "AdministrativoSIPFSolicitante";
    public static final String SOLICITANTE_ADUANAS = "AdministrativoSIPFSolicitanteAduanas";
    public static final String SOLICITANTE_OPERATIVOS = "AdministrativoSIPFSolicitanteOperativos";  
    public static final String SOLICITANTE_CREDITO_FISCAL = "AdministrativoSIPFSolicitanteCreditoFiscal";
    public static final String AUTORIZADOR_GERENCIAL_INTENDENTE = "AdministrativoSIPFAutorizadorGerencialIntendente";

    public static final List<String> LIST;

    static {
        String[] roles = {
            ADMINISTRADOR, AUTORIZADOR_GERENCIAL, AUTORIZADOR,
            APROBADOR, VERIFICADOR, OPERADOR, SOLICITANTE, SOLICITANTE_ADUANAS,
            SOLICITANTE_OPERATIVOS, SOLICITANTE_CREDITO_FISCAL,AUTORIZADOR_GERENCIAL_INTENDENTE
        };
        LIST = Collections.unmodifiableList(Arrays.asList(roles));
    }
}
