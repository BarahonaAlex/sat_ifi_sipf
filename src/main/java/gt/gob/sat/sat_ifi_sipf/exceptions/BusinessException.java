/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gt.gob.sat.sat_ifi_sipf.exceptions;

import com.google.gson.Gson;
import gt.gob.sat.arquitectura.microservices.exceptions.ResourceNotFoundException;
import gt.gob.sat.sat_ifi_sipf.dtos.ErrorDto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

/**
 *
 * @author crramosl
 */
public class BusinessException extends ResponseStatusException {

    private final transient List<ErrorDto> errors;

    /**
     * Crea una excepcion del tipo sin autenticacion (401).
     *
     * @param message Mensaje que describre la excepcion
     * @return BusinessException
     */
    public static BusinessException unauthorized(String message) {
        return new BusinessException(HttpStatus.UNAUTHORIZED, message);
    }

    /**
     * Crea una excepcion del tipo sin autorizacion (403).
     *
     * @param message Mensaje que describre la excepcion
     * @return BusinessException
     */
    public static BusinessException forbidden(String message) {
        return new BusinessException(HttpStatus.FORBIDDEN, message);
    }

    /**
     * Crea una excepcion del tipo no encontrado (404).
     *
     * @param message Mensaje que describre la excepcion
     * @return BusinessException
     */
    public static BusinessException notFound(String message) {
        return new BusinessException(HttpStatus.NOT_FOUND, message);
    }

    /**
     * Crea una excepcion del tipo entidad no procesable (422).
     *
     * @param message Mensaje que describre la excepcion
     * @return BusinessException
     */
    public static BusinessException unprocessableEntity(String message) {
        return new BusinessException(HttpStatus.UNPROCESSABLE_ENTITY, message);
    }

    /**
     * Crea una excepcion del tipo error interno del servidor (500).
     *
     * @param message Mensaje que describre la excepcion
     * @return BusinessException
     */
    public static BusinessException internalServerError(String message) {
        return new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR, message);
    }

    /**
     * Crea una excepcion del tipo mala estructura de la petición (400).
     *
     * @param message Mensaje que describre la excepcion
     * @param errors Lista de errores a mostrar al usuario.
     * @return BusinessException
     */
    public static BusinessException badRequestError(String message, List<ErrorDto> errors) {
        return new BusinessException(HttpStatus.BAD_REQUEST, message, errors);
    }

    /**
     * Constructor, establece el estado HTTP y el message de la excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     */
    public BusinessException(HttpStatus httpStatus, String message) {

        super(httpStatus, message);
        this.errors = Collections.emptyList();
    }

    /**
     * Constructor, establece el estado HTTP, message y cause de la excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     * @param cause Excepcion previa ocurrida
     */
    public BusinessException(HttpStatus httpStatus, String message,
            Throwable cause) {

        super(httpStatus, message, cause);
        this.errors = Collections.emptyList();
    }

    /**
     * Constructor, establece el estado HTTP, message y error de la excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     * @param error Bean con el error que provoco la excepcion
     */
    public BusinessException(HttpStatus httpStatus, String message,
            ErrorDto error) {

        super(httpStatus, message);
        this.errors = Collections.unmodifiableList(Arrays.asList(error));
    }

    /**
     * Constructor, establece el estado HTTP, message y errores de la excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     * @param errores Listado de errores que provocaron la excepcion
     */
    public BusinessException(HttpStatus httpStatus, String message,
            List<ErrorDto> errores) {

        super(httpStatus, message);
        this.errors = Collections.unmodifiableList(errores);
    }

    /**
     * Constructor, establece el estado HTTP, message, cause y error de la
     * excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     * @param cause Excepcion previa ocurrida
     * @param error Bean con el error que provoco la excepcion
     */
    public BusinessException(HttpStatus httpStatus, String message,
            Throwable cause, ErrorDto error) {

        super(httpStatus, message, cause);
        this.errors = Collections.unmodifiableList(Arrays.asList(error));
    }

    /**
     * Constructor, establece el estado HTTP, message, cause y errores de la
     * excepcion.
     *
     * @param httpStatus Estado HTTP
     * @param message Mensaje que describre la excepcion
     * @param cause Excepcion previa ocurrida
     * @param errores Listado de errores que provocaron la excepcion
     */
    public BusinessException(HttpStatus httpStatus, String message,
            Throwable cause, List<ErrorDto> errores) {

        super(httpStatus, message, cause);
        this.errors = Collections.unmodifiableList(errores);
    }

    /**
     * Devuelve el listado de errores asociados a la excepcion, tomar en cuenta
     * que puede devolver {@code null} o un listado vacio.
     *
     * @return List Listado de errores asociados a la excepcion
     */
    public List<ErrorDto> getErrors() {
        return this.errors;
    }

    /**
     * Indica si la excepcion tiene errores asociados o no.
     *
     * @return boolean {@code true} si y solo si hay errores
     */
    public boolean hasErrors() {
        return !CollectionUtils.isEmpty(this.errors);
    }

    public static BusinessException createFromException(HttpClientErrorException e) {
        switch (e.getStatusCode()) {
            case FORBIDDEN:
                return BusinessException.forbidden(e.getMessage());
            default: return BusinessException.unprocessableEntity("No se pudo procesar la excepción");
        }
    }
}
