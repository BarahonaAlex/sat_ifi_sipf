/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.constants;

/**
 *
 * @author crramosl
 */
public enum Message {
    UNKNOWN_USER("No se permite el acceso de usuarios desconocidos"),
    NO_ROLE_USER("No tiene el rol requerido para realizar la operación"),
    NO_PROFILE_USER("No tiene el perfil requerido para realizar la operación"),
    INVALID_PARAMS("La petición tiene parámetros inválidos"),
    UNKNOWN_ERROR("Error interno del sistema"),
    GROUP_NOT_FOUND("El grupo \"%d\" no existe"),
    COLLABORATOR_NOT_FOUND("El NIT no pertenece a un colaborador registrado"),
    OPERATOR_ALREADY_IN_GROUP("El operador ya existe en otro grupo de trabajo. Por favor, verifique."),
    OPERATORS_ALREADY_IN_GROUP("Los siguientes operadores ya existe en otro grupo de trabajo. Por favor, verifique."),
    PROFILES_NOT_FOUND("No se encontraron perfiles para NIT ingresados"),
    ROLE_NOT_FOUND("No posee el rol requerido"),
    OPERATOR_ONCE_PER_GROUP("El operador únicamente puede pertenecer a un grupo de trabajo"),
    VARIATIONS_FOUND("Los valores declarados como crédito fiscal y débito fiscal, no coinciden con los valores registrados en el Libro de Compras y Servicios Recibidos y Libro de Ventas"),
    PARAM_NOT_FOUND("No se encuentra el parámetro del sistema"),
    YEARLY_PLAN_ALREADY_CREATED("No se pudo crear el Plan Anual de Fiscalizacion, porque ya existe un registro para el año \"%d\"."),
    YEARLY_PLAN_ALREADY_UPDATED("No se pudo actualizar el año del Plan Anual de Fiscalizacion a \"%d\", porque ya existe un registro con este año."),
    MONTH_ALREADY_CREATED("No se pudo crear la meta para el mes de \"%s\", porque y existe un registro con este mes."),
    NO_MONTH_EXISTS("No existe la meta para el mes de \"%s\"."),
    NO_YEALY_PLAN_EXISTS("No existe el Plan Anual de Fiscalizacion para el año \"%d\"."),
    NO_YEALY_PLAN_EXISTS_ID("No existe el Plan Anual de Fiscalizacion.");

    private final String text;

    private Message(String text) {
        this.text = text;
    }

    public String getText(Object... params) {
        return String.format(this.text, params);
    }
}
