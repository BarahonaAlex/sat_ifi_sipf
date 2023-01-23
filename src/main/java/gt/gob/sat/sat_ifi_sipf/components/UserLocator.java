/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.components;

import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.constants.Constants;
import java.util.Optional;
import org.springframework.core.MethodParameter;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 *
 * @author crramosl
 */
@Component
public class UserLocator
        implements HandlerMethodArgumentResolver, AuditorAware<String> {

    /**
     * Verifica si el parametro de metodo a resolver es soportado por este
     * resolver.
     *
     * @param parametro Parametro de metodo a verificar
     * @return {@code true} Si, y solo si, el parametro es soportado
     */
    @Override
    public boolean supportsParameter(MethodParameter parametro) {

        return parametro.getParameterType().equals(UserLogged.class);
    }

    /**
     * Este metodo se encarga de resolver los parametros del tipo
     * {@link UserLogged} para los metodos que pertenecen a los
     * controladores. Tomar en cuenta que si no es posible localizar el objeto
     * entonces se devuelve un objeto vacio.
     *
     * @param parameter Parametro a resolver, este se tuvo que haber pasado
     * previamente a {@link #supportsParameter(MethodParameter)} que tuvo que
     * devolver {@code true}
     * @param container Proporciona acceso al modelo de la peticion
     * @param request Peticion actual
     * @param factory Permite realizar dataBinding y conversion de datos
     * @return Object Argumento resuelto
     * @throws Exception En caso de error al preparar/obtener el parametro
     */
    @Override
    public Object resolveArgument(MethodParameter parameter,
            ModelAndViewContainer container, NativeWebRequest request,
            WebDataBinderFactory factory) throws Exception {

        return this.extractUser();
    }

    /**
     * Metodo que permite obtener el login del usuario que esta realizando la
     * operacion {@code DML} en la base de datos. La informacion se obtiene del
     * objeto {@link UserLogged} que fue colocado en el {@code request}
     * por el interceptor de usuario.
     *
     * @return Optional con el login del usuario, o vacio de no existir
     */
    @Override
    public Optional<String> getCurrentAuditor() {

        return Optional.of(this.extractUser().getLogin());
    }

    /**
     * Metodo que se encarga de obtener el bean con los datos del usuario que
     * fue colocado por el interceptor de seguridad en la peticion.
     *
     * @return Bean del tipo {@link CollaboratorLogged.Autenticado}
     */
    private UserLogged extractUser() {

        return Optional
                .ofNullable(RequestContextHolder.getRequestAttributes())
                .map(r -> (UserLogged) r.getAttribute(Constants.USER_ATTRIBUTE, RequestAttributes.SCOPE_REQUEST))
                .orElseGet(UserLogged::createDefault);
    }
}
