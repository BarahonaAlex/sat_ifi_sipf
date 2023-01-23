/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.components;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import gt.gob.sat.arquitectura.microservices.config.request.Detector;
import gt.gob.sat.sat_ifi_sipf.Config;
import gt.gob.sat.sat_ifi_sipf.exceptions.BusinessException;
import gt.gob.sat.sat_ifi_sipf.dtos.UserLogged;
import gt.gob.sat.sat_ifi_sipf.constants.Constants;
import gt.gob.sat.sat_ifi_sipf.constants.Message;
import gt.gob.sat.sat_ifi_sipf.constants.Role;
import gt.gob.sat.sat_ifi_sipf.dtos.AllowedUriDto;
import gt.gob.sat.sat_ifi_sipf.dtos.EmpleadoFromProsisDto;
import gt.gob.sat.sat_ifi_sipf.dtos.GeneralResponseDto;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfil;
import gt.gob.sat.sat_ifi_sipf.models.SipfColaboradorPerfilId;
import gt.gob.sat.sat_ifi_sipf.projections.UrlProjections;
import gt.gob.sat.sat_ifi_sipf.repositories.ColaboradorPerfilRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.OpcionesMenuRepository;
import gt.gob.sat.sat_ifi_sipf.repositories.PerfilRepository;
import gt.gob.sat.sat_ifi_sipf.utils.HeaderUtils;
import gt.gob.sat.sat_ifi_sipf.utils.UriIgnore;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

/**
 *
 * @author crramosl
 */
@Component
@Slf4j
public class UserInterceptor extends HandlerInterceptorAdapter {

    /**
     * detector que tiene la informacion del usuario loggeado {@link Detector}.
     */
    @Autowired
    private Detector detector;

    /**
     * detector que tiene la informacion del usuario loggeado
     * {@link PerfilRepository}.
     */
    @Autowired
    private PerfilRepository profile;

    /**
     * detector que tiene la informacion del usuario loggeado
     * {@link PerfilRepository}.
     */
    @Autowired
    private ColaboradorPerfilRepository collabProfile;

    /**
     * detector que tiene la informacion de las opciones del menu
     * {@link OpcionesMenuRepository}.
     */
    @Autowired
    private OpcionesMenuRepository optionsMenu;

    /**
     * configuraciones del sistema {@link Config}.
     */
    @Autowired
    private Config config;

    /**
     * Metodo que se ejecuta previo a la ejecucion de los metodos de los
     * diferentes controladores REST. El metodo se encarga de verificar si el
     * usuario que accede al servicio REST si es un usuario autenticado.
     *
     * @param request Peticion hecha por el usuario
     * @param response Respuesta para el usuario
     * @param handler Manejador del metodo que responde al servicio REST
     * @return boolean {@code true} si el usuario puede acceder al servicio REST
     * @throws Exception Si ocurre algun error
     */
    @Override
    public boolean preHandle(HttpServletRequest request,
            HttpServletResponse response, Object handler) throws Exception {

        // las peticiones preflight utilizan OPTIONS para verificar CORS
        if (RequestMethod.OPTIONS.name().equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        UserLogged user = this.getAuthenticatedUser();
        this.verifySpecificPermission(request, user, handler);
        request.setAttribute(Constants.USER_ATTRIBUTE, user);
        return true;
    }

    /**
     * Este metodo obtiene el login, los roles y la direccion IP del usuario que
     * realiza la peticion, con esos datos crea el bean de usuario autenticado.
     *
     * Si la peticion no incluye el login o los roles se lanza una excepcion del
     * tipo {@link ExcepcionNegocio} con estado http 401.
     *
     * @return UserLogged Bean con los datos basicos del usuario
     */
    private UserLogged getAuthenticatedUser() {
        if (config.getTesting()) {
            final UserLogged user = UserLogged.createDefault();

            user.setOptions(optionsMenu.getAllowMenuOption()
                    .stream()
                    .filter(item -> item.getChildren() != null)
                    .collect(Collectors.toList())
            );

            return user;
        }

        final UserLogged user = new UserLogged();
        try {
            user.setLogin(detector.getLogin().replace("user:", ""));
            user.setIp(detector.getIp());
            user.setRoles(UserLogged.matchedRoles(detector.getRoles()));

            try {
                String ruta = "sat_rtu/contribuyentes/datos/general/".concat(user.getLogin());
                JsonObject res = new Gson().fromJson(consume(null, ruta, String.class, HttpMethod.GET), JsonObject.class);

                user.setNit(user.getLogin());
                user.setName(getTaxPayerName(res));
            } catch (JsonSyntaxException | HttpClientErrorException e) {
                GeneralResponseDto<EmpleadoFromProsisDto> resultado = consumeCompleteUrlSqlServer(null, "employee/" + user.getLogin(), GeneralResponseDto.class, HttpMethod.GET);
                if (resultado.getCode() == 1001) {
                    throw new BusinessException(HttpStatus.NOT_FOUND, "El colaborador actualmente no se encontra registrado en la base de datos del Sistema Prosis");
                }
                EmpleadoFromProsisDto collab = new ObjectMapper().convertValue(resultado.getData(), new TypeReference<EmpleadoFromProsisDto>() {
                });

                user.setNit(collab.getNit());
                user.setName(collab.getNombre());
            }
            user.setLoggedSince(new Date());
        } catch (BusinessException | IllegalArgumentException | NullPointerException e) {
            final String message = e instanceof BusinessException ? e.getMessage() : Message.UNKNOWN_USER.getText();
            throw BusinessException.unauthorized(message);
        }

        if (user.getLogin().equals(Constants.USER_UNKNOWN)) {
            throw BusinessException.unauthorized(Message.UNKNOWN_USER.getText());
        }

        this.verifyUserProfiles(user.getNit(), user.getRoles());

        List<String> fuctions = profile.findByLogin(user.getNit())
                .stream()
                .map(function -> function.getCodigo())
                .collect(Collectors.toList());

        Map<String, List<UrlProjections>> urls = profile
                .findByLoginUrl(user.getNit())
                .stream()
                .collect(Collectors.groupingBy(UrlProjections::getUrl));

        List<AllowedUriDto> allowedUrls = urls.keySet()
                .stream()
                .map(url -> new AllowedUriDto(url, urls.get(url).stream().map(method -> method.getMetodo()).collect(Collectors.toList())))
                .collect(Collectors.toList());

        user.setAllowedUris(allowedUrls);

        user.setOptions(optionsMenu.getAllowMenuOption(fuctions)
                .stream()
                .filter(item -> item.getChildren() != null)
                .collect(Collectors.toList())
        );

        return user;
    }

    /**
     * Este metodo se encarga de verificar si el servicio al que trata de
     * acceder el usuario requiere de algun permiso especifico, y si lo requiere
     * entonces verifica si el usuario cuenta con ese permiso.
     *
     * Si no se pasan las verificaciones se lanza una excepcion del tipo
     * {@link ExcepcionNegocio} con estado http 403.
     *
     * @param request Peticion hecha por el usuario
     * @param handler Manejador del metodo que responde al servicio REST
     * @param user Bean con los datos del usuario autenticado
     * @throws BusinessException cuando alguna validación de acceso no se
     * cumpla.
     */
    private void verifySpecificPermission(HttpServletRequest request,
            UserLogged user, Object handler) throws BusinessException {
        if (handler instanceof HandlerMethod) {
            UriIgnore ignore = ((HandlerMethod) handler).getMethodAnnotation(UriIgnore.class);
            if (ignore != null && ignore.value()) {
                return;
            }
        }

        if (user.getRoles().isEmpty()) {
            throw BusinessException.unauthorized(Message.NO_ROLE_USER.getText());
        }

        if (!user.getAllowedUris().isEmpty()
                && user.getAllowedUris().get(0).getUri().equals("*")
                && (user.getAllowedUris().get(0).getHttpsMethods().contains("*")
                || user.getAllowedUris().get(0).getHttpsMethods().contains(request.getMethod()))) {
            return;
        }

        List<AllowedUriDto> uris = user.getAllowedUris()
                .stream()
                .filter(
                        uri
                        -> request.getRequestURI().replace(request.getContextPath(), "").contains(uri.getUri().split("\\/\\?")[0])
                        && uri.getHttpsMethods().contains(request.getMethod())
                ).collect(Collectors.toList());

        if (uris.isEmpty()) {
            throw BusinessException.unauthorized(Message.NO_PROFILE_USER.getText());
        }
    }

    private void verifyUserProfiles(String nit, List<String> roles) {
        addOrRemoveProfile(roles, nit, Role.ADMINISTRADOR, 1);
        addOrRemoveProfile(roles, nit, Role.SOLICITANTE_ADUANAS, 7);
        addOrRemoveProfile(roles, nit, Role.SOLICITANTE_OPERATIVOS, 8);
        addOrRemoveProfile(roles, nit, Role.SOLICITANTE_CREDITO_FISCAL, 22);
        addOrRemoveProfile(roles, nit, Role.AUTORIZADOR_GERENCIAL_INTENDENTE, 23);
    }

    private void addOrRemoveProfile(List<String> roles, String nit, String role, Integer profile) {
        final Optional<SipfColaboradorPerfil> prof = collabProfile.findById(new SipfColaboradorPerfilId(nit, profile));

        if (roles.contains(role) && !prof.isPresent()) {
            final SipfColaboradorPerfil cp = new SipfColaboradorPerfil();
            final SipfColaboradorPerfilId cpi = new SipfColaboradorPerfilId();

            cpi.setNit(nit);
            cpi.setIdPerfil(profile);
            cp.setId(cpi);
            cp.setEstado(161);

            collabProfile.save(cp);
        } else if (!roles.contains(role) && prof.isPresent() && prof.get().getEstado() == 161) {
            prof.get().setEstado(162);
            collabProfile.save(prof.get());
        } else if (roles.contains(role) && prof.isPresent()) {
            prof.get().setEstado(161);
            collabProfile.save(prof.get());
        }
    }

    private <T> T consumeCompleteUrlSqlServer(@Nullable Object body, @NotNull @NotBlank String url, @NotNull Class<T> klass, @NotNull HttpMethod type) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = HeaderUtils.createHeaders(config.getUsrWebServiceProsis(), config.getPassWebServiceProsis());
        String ruta = config.getPingUrlWsSqlServer() + url;
        log.debug(ruta);
        HttpEntity<?> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(ruta, type, requestBody, klass).getBody();
    }

    private <T> T consume(@Nullable Object body, @NotNull @NotBlank String url, @NotNull Class<T> klass, @NotNull HttpMethod type) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = HeaderUtils.createHeaders();
        String ruta = config.getPingUrlInterna() + url;
        log.debug(ruta);
        HttpEntity<?> requestBody = new HttpEntity<>(body, headers);
        return restTemplate.exchange(ruta, type, requestBody, klass).getBody();
    }

    private String getTaxPayerName(JsonObject data) {
        JsonObject info = data.getAsJsonObject("data").getAsJsonObject("attributes").getAsJsonObject("datos");
        JsonObject taxPayer = info.get("empresa") instanceof JsonNull ? info.getAsJsonObject("contribuyente").getAsJsonObject("persona") : info.getAsJsonObject("empresa");
        JsonElement name = taxPayer.get("razonSocial");
        if (name == null) {
            return (orElse(taxPayer.get("primer_Nombre")) + " "
                    + orElse(taxPayer.get("segundo_Nombre")) + " "
                    + orElse(taxPayer.get("primer_Apellido")) + " "
                    + orElse(taxPayer.get("segundo_Apellido")) + " "
                    + orElse(taxPayer.get("apellido_Casada")) + " ")
                    .replaceAll("/\\s+/g", " ").trim();
        }
        return name.getAsString();
    }

    private String orElse(JsonElement element) {
        if (element instanceof JsonNull) {
            return "";
        }
        return element.getAsString();
    }
}
